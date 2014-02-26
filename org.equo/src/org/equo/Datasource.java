package org.equo;

import java.util.ArrayList;
import java.util.List;

import org.equo.util.Iterator;

public abstract class Datasource {
	
	private static final int ARRAY_INITIAL_CAPACITY = 16;
	
	private DatasourceInfo info;
	
	public Datasource(DatasourceInfo info) {
		this.info = info;
	}

	/**
	 * @return the connectionInfo
	 */
	public DatasourceInfo getDataSourceInfo() {
		return info;
	}
	
	public abstract void close() throws DatasourceException;
	
	public abstract int execute(ICommand command) throws DatasourceException;
	
	public abstract void synchronize(ITable peer) throws DatasourceException;
	
	public abstract <E extends IEntity> 
			Iterator<E> iterator(Select<E> select) throws DatasourceException;
	
	public final <E extends IEntity>
			List<E> select(Select<E> select) throws DatasourceException {
		
		return asList(iterator(select));
	}

	public static <E extends IEntity> List<E> asList(Iterator<E> iterator) throws DatasourceException {
		List<E> list = new ArrayList<E>(ARRAY_INITIAL_CAPACITY);
		try {
			while (iterator.hasNext()) {
				list.add(iterator.next());
			}
		} catch (IllegalStateException e) {
			throw new DatasourceException(e.getCause());
		} finally {
			iterator.close();
		}
		return list;
	}

	// TODO Transactions
//	public ITransaction getTransaction() {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	protected static void initialize(IEntity record, IColumn column, Object value) {
		record.initialize(column, value);
	}
	
	protected static void reset(IEntity record, int column, Object value) {
		record.initialize(column, value);
	}
}
