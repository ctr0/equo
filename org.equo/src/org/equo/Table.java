package org.equo;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;



public abstract class Table<E extends Entity<E>, F extends Entity<F>> extends ITable {

	Table<E, ?> parent;
	int order = -1;
	int index = -1;
	String alias;
	Criteria<E> criteria;
	
	private IColumn[] keys;
	
	private Map<String, Column<E, ?>> columns;
	private Map<String, Table<E, ?>> foreigns;
	
	public Table(Table<E, ?> parent) {
		this.parent = parent;
		this.alias = getName();
		this.foreigns = new LinkedHashMap<>(4);
	}
	
	public Table(Table<E, ?> parent, String alias) {
		this.parent = parent;
		this.alias = createAlias(alias);
		this.foreigns = new LinkedHashMap<>(4);
	}

	public Table(Table<E, ?> parent, Criteria<E> criteria) {
		this.parent = parent;
		this.criteria = criteria;
		this.foreigns = new LinkedHashMap<>(4);
	}
	
	public Table(Table<E, ?> parent, String alias, Criteria<E> criteria) {
		this.parent = parent;
		this.alias = createAlias(alias);
		this.criteria = criteria;
		this.foreigns = new LinkedHashMap<>(4);
	}
	
	Table<E, ?> getParent() {
		return parent;
	}
	
	int getIndex() {
		return index;
	}
	
	protected Criteria<E> getCriteria() {
		return criteria;
	}
	
	protected void setCriteria(Table<E, ?> foreign, Criteria<E> criteria) {
		foreign.criteria = criteria;
	}
	
	public abstract String getName();
	
	public String getAlias() {
		if (alias == null) return getName();
		return alias;
	}
	
	public abstract String getDatasourceName();
	
	@Override
	public int getColumnsCount() {
		return columns.size();
	}

	@Override
	public IColumn[] getKeys() {
		return keys;
	}

	@Override
	public Column<E, ?> getColumn(String alias) {
		return columns.get(alias);
	}
	
	@Override
	public IColumn[] getColumns() {
		return columns.values().toArray(new Column[columns.size()]);
	}

	//@SuppressWarnings("unchecked")
	Collection<Column<E, ?>> getColumns0() {
		//return columns.values().toArray(new TableColumn[columns.size()]);
		return columns.values();
	}
	
	@SafeVarargs
	protected final void setColumns(Column<E, ?>... columns) {
		Map<String, Column<E, ?>> map = new LinkedHashMap<>(columns.length);
		for (Column<E, ?> column : columns) {
			map.put(column.getName(), column);
		}
		this.columns = map;
	}
	
	protected final Table<E, ?> getForeign(String alias) {
		return foreigns.get(alias);
	}
	
	Collection<Table<E, ?>> getForeigns0() {
		return foreigns.values();
	}
	
	@SuppressWarnings("unchecked")
	final Table<E, ?> getOrCreateForeign(String alias) {
		Table<E, ?> foreign = foreigns.get(alias);
		if (foreign == null) {
			try {
				Method method = getClass().getMethod(alias);
				try {
					foreign = (Table<E, ?>) method.invoke(this, (Object[]) null);
					foreigns.put(alias, foreign);
				} catch (Exception e) {
					throw new RuntimeException("Error invoking foreign table accesor " + method);
				}
			} catch (NoSuchMethodException e) {
				return null; // XXX exception ?
			} catch (SecurityException e) {
				throw new RuntimeException("Cannot getting accesor for foreign table " + alias);
			}
		}
		return foreign;
	}
	
	protected void setForeign(Table<E, ?> foreign, Criteria<E> criteria) {
		foreign.criteria = criteria;
		foreigns.put(foreign.getAlias(), foreign);
	}
	
	String[] getForeignPath() {
		String[] path = getAlias().split("\\.");
		if (path.length == 0) path = new String[] {alias};
		return path;
	}

	public Column<E, ?> ALL() {
		// All columns
		return null;
	}

	private String createAlias(String alias) {
		if (getParent() == null) return alias;
		StringBuilder b = new StringBuilder(parent.getAlias());
		b.append('.');
		b.append(alias);
		return b.toString();
	}
	
	
//	private void initialize()
//			throws IllegalArgumentException, IllegalAccessException {
//		
//		List<IColumn> columns = new ArrayList<>(16);
//		Class<?> c = getClass();
//		do initialize(columns, c);
//		while ((c = c.getSuperclass()) != Table.class);
//		//columns.toArray(new IColumn[columns.size()]);
//		//setColumns(columns.toArray(new IColumn[columns.size()]));
//	}
//
//	@SuppressWarnings("unchecked")
//	private void initialize(List<IColumn> columns, Class<?> c)
//			throws IllegalArgumentException, IllegalAccessException {
//		
//		for (Field field : c.getFields()) {
//			if (TableColumn.class.isAssignableFrom(field.getType())) {
//				TableColumn<E, ?> column = (TableColumn<E, ?>) field.get(this);
//				this.columns.put(column.getName(), column);
//				//columns.add((IColumn) field.get(table));
//			} else if (Table.class.isAssignableFrom(field.getType())) {
//				//addForeign(field.getName(), (ITable) field.get(table));
//			}
//		}
//	}
	
}
