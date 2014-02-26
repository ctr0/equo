package org.equo;


public class Delete<E extends IEntity> implements ICommand {
	
	public static final <E extends IEntity> Delete<E> deleteFrom(Peer<E> master) {
		return new Delete<E>(master);
	}
	
	private Peer<E> peer;
	Segment<E> where = new Segment<E>();
	
	public Delete(Peer<E> peer) {
		this.peer = peer;
	}
	
	@SafeVarargs
	public final Delete<E> where(Criteria<E>... criterias) {
		where.add(criterias);
		return this;
	}
	
	public int execute() throws DatasourceException {
		return peer.getDatasource().execute(this);
	}
	
	@Override
	public void accept(CommandVisitor visitor) throws DatasourceException {
		visitor.visitDelete();
		visitor.visitFrom(peer);
		visitor.visitWhere(where);
		// visitor.visitEnd(); TODO CommandVisitor.visitEnd ?
	}
	
	public static final <E extends IEntity> Delete<E> wrap(E e) {
		@SuppressWarnings("unchecked")
		Peer<E> peer = (Peer<E>) e.getPeer();
		Delete<E> delete = new Delete<E>(peer);
		Segment<E> where = delete.where;
		for (IColumn key : peer.getKeys()) {
			where.add(new Condition<E>(0, key, e.getObject(key.getIndex())));
		}
		return delete;
	}

}
