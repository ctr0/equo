package org.equo;


/**
 * Internal implementation of Record for View type peers.
 * 
 * @author j0rd1
 *
 */
public final class Record extends IEntity {
	
	public Record(Peer<Record> peer) {
		super(peer);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getObject(IColumn column) {
		return (T) getObject(checkColumn(column));
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getObject(Column<? extends IEntity, T> column) {
		return (T) getObject(checkColumn(column));
	}
	
	public <T> void setObject(Column<? extends IEntity, T> column, T value) {
		setObject(checkColumn(column), checkType(column, value));
	}
	
	private int checkColumn(IColumn column) {
		IColumn c = peer.getColumn(column.getAlias());
		if (c == null || c.getField() != column.getField()) 
			throw new IllegalArgumentException("Illegal column " + column);
		return c.getIndex();
	}
}
