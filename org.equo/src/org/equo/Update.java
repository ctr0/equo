package org.equo;

import java.util.ArrayList;
import java.util.List;

public class Update<E extends IEntity> implements ICommand {
	
	public static final <E extends IEntity> Update<E> selectFrom(Peer<E> master) {
		return new Update<E>(master);
	}
	
	public static class Set {
		
		private IColumn column;
		private IColumn value;
		private Object object;
		
		private Set(IColumn column, IColumn value) {
			this.column = column;
			this.value = value;
		}
		
		private Set(IColumn column, Object value) {
			this.column = column;
			this.object = value;
		}
		
		public IColumn getColumn() {
			return column;
		}
		
		public IColumn getColumnValue() {
			return value;
		}
		
		public Object getObjectValue() {
			return object;
		}
	}
	
	private Peer<E> peer;
	private Segment<E> where = new Segment<E>();
	private List<Set> sets = new ArrayList<>();
	
	public Update(Peer<E> peer) {
		this.peer = peer;
	}
	
	public <T> Update<E> set(Column<E, T> column, T value) {
		sets.add(new Set(column, value));
		return this;
	}
	
	public <T> Update<E> set(Column<E, T> column, Column<E, T> value) {
		sets.add(new Set(column, value));
		return this;
	}
	
	@SafeVarargs
	public final Update<E> where(Criteria<E>... criterias) {
		where.add(criterias);
		return this;
	}
	
	public int execute() throws DatasourceException {
		return peer.getDatasource().execute(this);
	}
	
	@Override
	public void accept(CommandVisitor visitor) throws DatasourceException {
		visitor.visitUpdate(peer);
		visitor.visitSets(sets.toArray(new Set[sets.size()]));
		visitor.visitWhere(where);
	}
	
	public static final <E extends IEntity> Update<E> wrap(E e) {
		if (!e.isDirty()) return null;
		@SuppressWarnings("unchecked")
		Peer<E> peer = (Peer<E>) e.getPeer();
		Update<E> update = new Update<E>(peer);
		List<Set> sets = update.sets;
		IColumn[] columns = peer.getColumns();
		for (int i = 0; i < peer.getColumnsCount(); i++) {
			if (e.isDirty(i)) {
				sets.add(new Set(columns[i], e.getObject(i)));
			}
		}
		Segment<E> where = update.where;
		for (IColumn key : peer.getKeys()) {
			where.add(new Condition<E>(0, key, e.getObject(key.getIndex())));
		}
		return update;
	}

}
