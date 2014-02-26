package org.equo;

import org.equo.Condition.Operator;

public class Column<E extends IEntity, T> implements IColumn {
	
	public static <E extends IEntity, T> Column<E, T> createColumn(ITable table, Field field, int index) {
		return new Column<>(table, field, index);
	}
	
	public static <E extends Entity<E>, T> Column<E, T> createColumn(Table<E, ?> table, String name, Domain domain, int index) {
		return new Column<E, T>(table, new Field(table.getName(), name, domain), index);
	}
	
	ITable table;
	Field field;
	int index;
	
	Column(ITable peer, String name) {
		this(peer, new Field(peer.getName(), name, Domain.VARCHAR20), -1);
	}
	
	Column(IColumn column) {
		this.table = column.getTable();
		this.field = column.getField();
		this.index = column.getIndex();
	}
	
	Column(ITable table, Field field, int index) {
		this.table = table;
		this.field = field;
		this.index = index;
	}

	@Override
	public String getName() {
		return field.getName();
	}
	
	@Override
	public String getAlias() {
		return getName();
	}
	
	@Override
	public ITable getTable() {
		return table;
	}

	@Override
	public Field getField() {
		return field;
	}

	@Override
	public final int getIndex() {
		return index;
	}
	
	boolean isForeign() {
		return table.getParent() != null;
	}

	@Override
	public Column<E, T> as(String alias) {
		return new Column<E, T>(this) {
			
			private String alias;
			
			@Override
			public String getAlias() {
				return alias;
			}
		};
	}
	
	public Criteria<E> EQ(T value) {
		return new Condition<E>(Operator.EQ, this, value);
	}
	
	public Criteria<E> EQ(Column<E, T> column) {
		return new Condition<E>(Operator.EQ, this, column);
	}
	
	public Criteria<Record> EQ(IColumn column) {
		return new Condition<Record>(Operator.EQ, this, column);
	}
	
	public Condition<E> NE(T value) {
		return new Condition<E>(Operator.NE, this, value);
	}
	
	public Condition<E> NE(Column<E, T> column) {
		return new Condition<E>(Operator.NE, this, column);
	}
	
	public Condition<Record> NE(IColumn column) {
		return new Condition<Record>(Operator.NE, this, column);
	}
	
	public Condition<E> GT(T value) {
		return new Condition<E>(Operator.GT, this, value);
	}
	
	public Condition<E> GT(Column<E, T> column) {
		return new Condition<E>(Operator.GT, this, column);
	}
	
	public Condition<Record> GT(IColumn column) {
		return new Condition<Record>(Operator.GT, this, column);
	}
	
	public Condition<E> GE(T value) {
		return new Condition<E>(Operator.GE, this, value);
	}
	
	public Condition<E> GE(Column<E, T> column) {
		return new Condition<E>(Operator.GE, this, column);
	}
	
	public Condition<Record> GE(IColumn column) {
		return new Condition<Record>(Operator.GE, this, column);
	}
	
	public Condition<E> LT(T value) {
		return new Condition<E>(Operator.LT, this, value);
	}
	
	public Condition<E> LT(Column<E, T> column) {
		return new Condition<E>(Operator.LT, this, column);
	}
	
	public Condition<Record> LT(IColumn column) {
		return new Condition<Record>(Operator.LT, this, column);
	}
	
	public Condition<E> LE(T value) {
		return new Condition<E>(Operator.LE, this, value);
	}
	
	public Condition<E> LE(Column<E, T> column) {
		return new Condition<E>(Operator.LT, this, column);
	}
	
	public Condition<Record> LE(IColumn column) {
		return new Condition<Record>(Operator.LE, this, column);
	}
	
//	public OrderByField<E, T> ASC() {
//		return null;
//	}
//	
//	public SetField<E, T> SET(T value) {
//		return new SetField<E, T>(this, value);
//	}
	
	public Function<E, T> MAX() {
		return new Function<E, T>(this, Function.FUNC_MAX);
	}
	
	public Function<E, T> MIN() {
		return new Function<E, T>(this, Function.FUNC_MIN);
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder(getTable().getName());
		b.append('.');
		b.append(field);
		return b.toString();
	}
	
}
