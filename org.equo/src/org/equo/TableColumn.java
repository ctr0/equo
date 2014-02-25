package org.equo;


public class TableColumn<E extends Entity<E>, T> extends Column<E, T> {
	
	Table<E, ?> table;
	
	public TableColumn(Table<E, ?> table, Domain domain, int index) {
		super(table.getName(), new Field(table.getName(), domain), index);
		this.table = table;
	}
	
	public TableColumn(Table<E, ?> table, String name, Domain domain, int index) {
		super(table.getName(), new Field(table.getName(), name, domain), index);
		this.table = table;
	}
	
	public TableColumn(Table<E, ?> table, Field field, int index) {
		super(table.getName(), field, index);
		this.table = table;
	}
	
	public Table<E, ?> getTable() {
		return table;
	}

	@Override
	public String getPeer() {
		return table.getName();
	}
	
	public boolean isForeign() {
		return table.getParent() != null;
	}

	public Table<E, ?> getParent() {
		return table.getParent();
	}
	
}
