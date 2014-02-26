package org.equo.db;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import org.equo.CreateTable;
import org.equo.Datasource;
import org.equo.DatasourceException;
import org.equo.DatasourceInfo;
import org.equo.IColumn;
import org.equo.ICommand;
import org.equo.ITable;
import org.equo.IRecord;
import org.equo.Select;
import org.equo.util.Iterator;

public class Database extends Datasource {
	
	private boolean debug = true;
	
	private ConnectionPool pool;
	private CommandBuilder adapter;
	
	public Database(DatasourceInfo info) {
		this(info, null, new DefaultCommandAdapter());
	}
	
	public Database(DatasourceInfo info, ConnectionPool pool) {
		this(info, pool, new DefaultCommandAdapter());
	}
	
	public Database(DatasourceInfo info, CommandBuilder adapter) {
		this(info, loadConnectionPool(info), adapter);
	}
	
	public Database(DatasourceInfo info, ConnectionPool pool, CommandBuilder adapter) {
		super(info);
		this.pool = pool;
		this.adapter = adapter;
		this.pool.initialize(info);
	}

	private static final ConnectionPool loadConnectionPool(DatasourceInfo info) {
		java.util.Iterator<ConnectionPool> providers = 
				ServiceLoader.load(ConnectionPool.class).iterator();
		while (providers.hasNext()) {
			try {
				ConnectionPool provider = providers.next();
				if (provider.accepts(info)) {
					return provider;
				}
			} catch (ServiceConfigurationError e) {
				// For now just ignore the exceptions
			}
		}
		return null;
	}

	protected ConnectionPool getConnectionPool() {
		return pool;
	}
	
	protected CommandBuilder getCommandBuilder() {
		return adapter.newInstance();
	}
	
	private Connection getConnection() throws DatasourceException {
		try {
			return pool.getConnection();
		} catch (SQLException e) {
			throw new DatasourceException(e.getMessage(), e.getCause());
		}
	}
	
	@Override
	public int execute(ICommand command) throws DatasourceException {
		ResultContext result = execute0(command);
		int updateCount = result.getUpdateCount();
		result.kill();
		return updateCount;
	}
	
	@Override
	public final <E extends IRecord> Iterator<E> iterator(Select<E> select) 
			throws DatasourceException {
		
		return new ResultSetIterator<E>(
			execute0(select), 
			select.getPeer(),
			select.getColumns()
		);
	}
	
	private ResultContext execute0(ICommand command) throws DatasourceException {
		CommandBuilder adapter = getCommandBuilder();
		command.accept(adapter);
		String sql = adapter.toSql();
		Connection cn = null;
		PreparedStatement ps = null;
		boolean isResultSet = false;
		try {
			cn = getConnection();
			ps = cn.prepareStatement(sql);
			List<Object> values = adapter.getValues();
			if (values != null) {
				for (int j = 0; j < values.size(); j++) {
					ps.setObject(j + 1, values.get(j));
				}
			}
			
			if (debug) {
				System.out.println(sql + " " + values);
//				System.out.println(values);
			}
			isResultSet = ps.execute();
			ResultSet rs = null;
			int updateCount = -1;
			if (isResultSet) rs = ps.getResultSet();
			else updateCount = ps.getUpdateCount();
			return new ResultContext(cn, ps, rs, updateCount);
		} catch (SQLException e) {
			throw new DatasourceException(e.getMessage() + " SQL: "  + sql, e.getCause());
		} finally {
			if (!isResultSet) {
				if (ps != null) { try { ps.close(); } catch (SQLException e) {}}
				if (cn != null) { try { cn.close(); } catch (SQLException e) {}}
			}
		}
	}

	@Override
	public void close() throws DatasourceException {
		try {
			pool.close();
		} catch (SQLException e) {
			throw new DatasourceException(e.getMessage(), e.getCause());
		}
	}

	protected static void initialize(IRecord record, IColumn column, Object value) {
		// TODO Auto-generated method stub
		Datasource.initialize(record, column, value);
	}

	@Override
	public void synchronize(ITable peer) throws DatasourceException {
//		Connection cn = getConnection();
//		String schema = null;
//		try (ResultSet rs = cn.getMetaData().getTables(null, schema, peer.getName(), null)) {
//			if (rs.next()) {
//				//syncTable(commands, peer);
//			} else {
				execute(new CreateTable(peer));
//			}
//		}
	}
	
	
}
