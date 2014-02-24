package org.equo;




public abstract class AbstractEntity<E extends AbstractEntity<E>> extends IRecord {

	protected AbstractEntity(Peer<E> peer) {
		super(peer);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getObject(Column<E, T> column) {
		if (peer.getName() == column.getPeer()) {
			return (T) getObject(column.getIndex());
		}
		throw new IllegalArgumentException("Illegal column " + column);
	}
	
	public <T> void setObject(Column<? extends IRecord, T> column, T value) {
		if (peer.getName() == column.getPeer()) {
			setObject(column.getIndex(), checkType(column, value));
		}
		throw new IllegalArgumentException("Illegal column " + column);
	}
	
}
