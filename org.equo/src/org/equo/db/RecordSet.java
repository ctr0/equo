package org.equo.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.equo.Column;
import org.equo.DatasourceException;
import org.equo.Peer;
import org.equo.IColumn;
import org.equo.IRecord;

public class RecordSet<E extends IRecord> implements AutoCloseable {

	private ResultContext context;
	
	private ResultSet rs;
	private Peer<E> peer;
	private IColumn[] columns;
	private SQLException e;
	
	RecordSet(ResultContext context, Peer<E> peer, IColumn[] columns) {
		this.context = context;
		this.rs = context.getResultSet();
		this.peer = peer;
		this.columns = columns;
	}

	public boolean next() throws IllegalStateException {
		try {
			return rs.next();
		} catch (SQLException e) {
			throw new IllegalStateException("Illegal data source state", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getObject(Column<? extends IRecord, T> column) {
		try {
			return (T) rs.getObject(checkColumn(column) + 1);
		} catch (SQLException e) {
			throw new IllegalStateException("Illegal data source state", e);
		}
	}

	public E getRecord() throws IllegalStateException {
		E record = peer.createEntity();
		IColumn[] columns = this.columns;
		ResultSet rs2 = rs;
		try {
			for (int i = 0; i < columns.length; i++) {	
				Database.initialize(record, columns[i], rs2.getObject(i + 1));
			}
		} catch (SQLException e) {
			setError(e);
			return null;
		}
		return record;
	}

	@Override
	public void close() throws IllegalStateException {
		try {
			context.close();
		} catch (SQLException e) {
			setError(e);
		}
	}

	public void remove() throws UnsupportedOperationException, IllegalStateException {
		throw new UnsupportedOperationException();
	}
	
	private void setError(SQLException e) throws IllegalStateException {
		if (this.e == null) {
			this.e = e;
		}
		throw new IllegalStateException("Illegal data source state");
	}

	public DatasourceException getError() {
		return new DatasourceException(e);
	}
	
	private int checkColumn(IColumn column) {
		IColumn c = peer.getColumn(column.getAlias());
		if (c == null || c != column) 
			throw new IllegalArgumentException("Illegal column " + column);
		return c.getIndex();
	}
}
