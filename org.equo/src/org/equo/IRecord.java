package org.equo;

import java.util.Arrays;

import org.equo.util.BitArray;



public abstract class IRecord {

	protected static final Object UNBOUND = new Object();
	
	final Peer<? extends IRecord> peer;
	Object[] values;
	final BitArray dirty;
	
	protected IRecord(Peer<? extends IRecord> peer) {
		this.peer = peer;
		int length = peer.getColumnsCount();
		this.dirty = BitArray.create(length);
		Arrays.fill(this.values = new Object[length], UNBOUND);
	}
	
	public Peer<?> getPeer() {
		return peer;
	}
	
	public Object[] getObjects() {
		return values;
	}
	
	public Object getObject(String alias) {
		IColumn column = peer.getColumn(alias);
		if (column == null) throw new IllegalArgumentException("Illegal column " + alias);
		return getObject(column.getIndex());
	}
	
	public void setObject(String alias, Object value) {
		IColumn column = peer.getColumn(alias);
		if (column == null) throw new IllegalArgumentException("Illegal column " + alias);
		setObject(column.getIndex(), value);
	}
	
	public final Object getObject(int index) {
		Object object = values[index];
		if (object == UNBOUND) {
			throw new IllegalArgumentException("Unbound column " + peer.getColumns()[index]);
		}
		return object;
	}
	
	public final void setObject(int index, Object value) {
		values[index] = value;
		dirty.set(index);
	}
	
	public Object[] getObjects(IColumn... columns) {
		Object[] values = this.values;
		Object[] objects = new Object[columns.length];
		for (int i = 0; i < columns.length; i++) {
			objects[i] = values[columns[i].getIndex()];
		}
		return objects;
	}
	
	public boolean isDirty() {
		return dirty.any();
	}
	
	public boolean isDirty(int index) {
		return dirty.get(index);
	}

	protected void initialize(IColumn column, Object value) {
		initialize(column.getIndex(), value);
	}
	
	protected final void initialize(int index, Object value) {
		values[index] = value;
	}
	
	protected final void reset(int index, Object value) {
		values[index] = value;
		dirty.unset(index);
	}
	
	protected int getColumnIndex(IColumn column) {
		IColumn c = peer.getColumn(column.getAlias());
		if (c == null || c != column) 
			throw new IllegalArgumentException("Illegal column " + column);
		return c.getIndex();
	}
	
	protected static void checkColumn(IPeer peer, IColumn column) {
		if (peer.getName() != column.getPeer()) {
			throw new IllegalArgumentException("Illegal column " + column);
		}
	}
	
	protected static Object checkType(IColumn column, Object value) {
		return Types.check(value, column.getField().domain.type);
	}
	
	public boolean refresh() throws DatasourceException {
		return peer.refresh(this);
	}

	public boolean exists() throws DatasourceException {
		return peer.exists(this);
	}
	
	public boolean delete() throws DatasourceException {
		return peer.delete(this);
	}
	
	public void insert() throws DatasourceException {
		peer.insert(this);
	}
	
	public boolean update() throws DatasourceException {
		return peer.update(this);
	}
	
	// TODO See Peer.save
//	public boolean save() throws DatasourceException {
//		return peer.save(this);
//	}

}
