package org.equo.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.equo.IColumn;
import org.equo.IRecord;
import org.equo.Peer;
import org.equo.util.Iterator;



public class ResultSetIterator<E extends IRecord> implements Iterator<E> {

	private ResultContext context;
	
	private ResultSet rs;
	private Peer<E> peer;
	private IColumn[] columns;
	
	ResultSetIterator(ResultContext context, Peer<E> peer, IColumn[] columns) {
		this.context = context;
		this.rs = context.getResultSet();
		this.peer = peer;
		this.columns = columns;
	}

	@Override
	public boolean hasNext() throws IllegalStateException {
		try {
			return rs.next();
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public E next() throws IllegalStateException {
		E record = peer.createEntity();
		IColumn[] columns = this.columns;
		ResultSet rs2 = rs;
		try {
			for (int i = 0; i < columns.length; i++) {	
				Database.initialize(record, columns[i], rs2.getObject(i + 1));
			}
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
		return record;
	}

	@Override
	public void close() throws IllegalStateException {
		try {
			context.close();
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void remove() throws UnsupportedOperationException, IllegalStateException {
		throw new UnsupportedOperationException();
	}

}
