package org.equo;




public abstract class AbstractEntity<E extends AbstractEntity<E>> extends IEntity {

	protected AbstractEntity(Peer<E> peer) {
		super(peer);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getObject(Column<E, T> column) {
		if (peer == column.getTable()) {
			return (T) getObject(column.getIndex());
		}
		throw new IllegalArgumentException("Illegal column " + column);
	}
	
	public <T> void setObject(Column<? extends IEntity, T> column, T value) {
		if (peer == column.getTable()) {
			setObject(column.getIndex(), checkType(column, value));
		}
		throw new IllegalArgumentException("Illegal column " + column);
	}
	
}
