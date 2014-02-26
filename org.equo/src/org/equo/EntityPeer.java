package org.equo;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;

public class EntityPeer<E extends IEntity> extends Peer<E> {
	
	private String name;
	private Datasource ds;
	Map<String, IColumn> columns;
	private IColumn[] key;
	private Constructor<E> entityConstructor;

	protected EntityPeer(Datasource ds, String name, Class<E> c) {
		this.name = name;
		this.ds = ds;
		try {
			if (c == Record.class) entityConstructor = c.getConstructor(Peer.class);
			else entityConstructor = c.getConstructor(getClass());
		} catch (Exception e) {
			throw new IllegalStateException("Cannot retrieve entity constructor", e);
		}
	}
	
	/**
	 * Created without columns
	 * @param peer
	 */
	protected EntityPeer(Peer<E> peer) {
		this.name = peer.getName();
		this.ds = peer.getDatasource();
		this.entityConstructor = peer.getEntityFactory();
	}
	
	protected void setColumns(IColumn... columns) {
		if (columns == null) throw new IllegalArgumentException();
		Map<String, IColumn> map = new LinkedHashMap<>(8);
		for (IColumn column : columns) {
			map.put(column.getAlias(), column);
		}
		this.columns = map;
	}
	
	protected void setKey(IColumn... columns) {
		if (columns == null || columns.length == 0) throw new IllegalArgumentException();
		// TODO validate columns?
		this.key = columns;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getAlias() {
		return name;
	}

	@Override
	public IColumn[] getColumns() {
		return columns.values().toArray(new IColumn[columns.size()]);
	}
	
	@Override
	public int getColumnsCount() {
		return columns.size();
	}

	@Override
	public IColumn[] getKeys() {
		// FIXME unmutable iterator
		return key;
	}
	
	public IColumn getColumn(int index) {
		int i = 0;
		for (IColumn column : columns.values()) {
			if (index == i++) return column;
		}
		throw new ArrayIndexOutOfBoundsException(index);
	}

	@Override
	public IColumn getColumn(String alias) {
		return columns.get(alias);
	}

	public E createEntity() {
		try {
			return getEntityFactory().newInstance(this);
		} catch (Exception e) {
			throw new IllegalStateException("Cannot instantiate entity", e);
		}
	}

	@Override
	public Constructor<E> getEntityFactory() {
		return entityConstructor;
	}

	public Datasource getDatasource() {
		return ds;
	}

	@Override
	public int getIndex() {
		return -1;
	}

	@Override
	public ITable getParent() {
		return null;
	}

	@Override
	String[] getForeignPath() {
		return null;
	}

	@Override
	public String getDatasourceName() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
