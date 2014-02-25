package org.equo;

import java.lang.reflect.Constructor;
import java.util.Collection;

public class PeerTable<E extends Entity<E>> extends Peer<E> {
	
	private Session session;
	private Datasource ds;
	private Table<E, ?> table;
	private Constructor<E> factory;

	// accessed only by session
	public PeerTable(Session session, Datasource ds, Table<E, ?> table, Class<E> entity) {
		this.session = session;
		this.ds = ds;
		this.table = table;
		this.factory = getEntityConstructor(entity);
	}
	
	public Session getSession() {
		return session;
	}
	
	@Override
	public Datasource getDatasource() {
		return ds;
	}
	
	Table<E, ?> getTable() {
		return table;
	}
	
	@Override
	public String getName() {
		return table.getAlias();
	}
	
	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IColumn[] getKeys() {
		// TODO Auto-generated method stub
		return table.getKeys();
	}
	
	@Override
	public TableColumn<E, ?> getColumn(String alias) {
		return table.getColumn(alias);
	}
	
	@Override
	public int getColumnsCount() {
		return table.getColumnsCount();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public TableColumn<E, ?>[] getColumns() {
		Collection<TableColumn<E, ?>> columns = table.getColumns0();
		return columns.toArray(new TableColumn[columns.size()]);
	}

	public Table<E, ?> getForeign(String alias) {
		return table.getForeign(alias);
	}
	
	@SuppressWarnings("unchecked")
	public Table<E, ?>[] getForeigns() {
		Collection<Table<E, ?>> foreigns = table.getForeigns0();
		return foreigns.toArray(new Table[foreigns.size()]);
	}
	
	@Override
	public E createEntity() {
		try {
			return getEntityFactory().newInstance(this);
		} catch (Exception e) {
			throw new RuntimeException("Cannot instantiate entity");
		}
	}

	@Override
	public Constructor<E> getEntityFactory() {
		return factory;
	}
	
	@SafeVarargs
	public final Query<E> select(TableColumn<E, ?>... columns) {
		return new Query<E>(this, columns);
	}
	
	@SafeVarargs
	public final Query<E> select(Table<E, ?>... tables) {
		return new Query<E>(this, tables);
	}

	public final Query<E> select(Criteria<E> criteria) {
		return new Query<E>(this).where(criteria);
	}

}
