package org.equo;



public class Field {
	
	String table;
	String name;
//	int index;
	Domain domain;
	
	Field(Field field) {
		this.table = field.table;
		this.name = field.name;
//		this.index = field.index;
		this.domain = field.domain;
	}
	
	Field(Domain domain) {
		this(null, domain.getName(), domain);
	}
	
	public Field(String table, Domain domain) {
		this(table, domain.getName(), domain);
	}
	
	public Field(String table, String name, Domain domain) {
		this.table = table;
		this.name = name;
		this.domain = domain;
	}

//	public final int getIndex() {
//		return index;
//	}
	
	public final String getName() {
		return name;
	}

	public final String getTable() {
		return table;
	}

	public final Domain getDomain() {
		return domain;
	}
	
	@Override
	public int hashCode() {
		// TODO hash with table.name & field.name
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj instanceof Field) {
			Field field = (Field) obj;
			return field.table.equals(table) && field.name.equals(name);
		}
		return false;
	}

	@Override
	public String toString() {
		return table + '.' + name;
	}
}
