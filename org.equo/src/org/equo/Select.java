package org.equo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.equo.util.Iterator;

public final class Select<E extends IRecord> implements ICommand {
	
	public static final <E extends IRecord> Select<E> selectFrom(Peer<E> master) {
		return new Select<E>(master, master.getColumns());
	}
	
	@SafeVarargs
	public static final <E extends IRecord> Select<E> selectFrom(Peer<E> master, Column<E, ?>... columns) {
		return new Select<E>(master, columns);
	}
	
	public static final Select<Record> selectFrom(Peer<? extends IRecord> master, IColumn... columns) {
		return new Select<Record>(
			new View<Record>(master.getDatasource(), master.getName(), Record.class, columns),
			columns
		);
	}
	
	// TODO select without peers
//	public static final Select<Record> selectFrom(Datasource ds, String master, String... columns) {
//		View<Record> view = new View<Record>(ds, master, Record.class, Parser.parseColumn(columns));
//		return new Select<Record>(view).columns(columns);
//	}
	
	Peer<E> peer;
	IColumn[] columns;
	Collection<Join> joins = new ArrayList<Join>(4);
	Segment<E> where = new Segment<E>(); // FIXME Segment can be abstract
	List<IColumn> groupBy = new ArrayList<IColumn>(8);
	Order orderBy = new Order();
	
	// TODO select without peers
//	protected Select(Datasource ds, String peer) {
//		this(null); // FIXME
//	}
	
	public Select(Peer<E> peer, IColumn... columns) {
		this.peer = peer;
		this.columns = columns;
	}
	
//	protected Select(View<E> view, IColumn... columns) {
//		this.view = view;
//		this.columns = columns;
//	}
	
//	@SafeVarargs
//	protected final Select<E> columns(Column<E, ?>... columns) {
//		for (IColumn column : columns) {
//			this.columns.add(column);
//		}
//		return this;
//	}
//	
//	@SuppressWarnings("unchecked")
//	protected Select<Record> columns(IColumn... columns) {
//		for (IColumn column : columns) {
//			this.columns.add(column);
//		}
//		return (Select<Record>) this;
//	}
//	
//	@SafeVarargs
//	@SuppressWarnings("unchecked")
//	public final Select<Record> columns(Function<E, ?>... functions) {
//		for (IColumn function : functions) {
//			this.columns.add(function);
//		}
//		return (Select<Record>) this;
//	}
//	
//	private Select<E> columns(String... columns) {
//		for (String column : columns) {
//			this.columns.add(Parser.parseColumnAndAlias(column));
//		}
//		return this;
//	}
	
	protected static void checkDuplicatedColumns(IColumn[] columns) {
		Map<Field, IColumn> map = new HashMap<Field, IColumn>(columns.length);
		for (IColumn column : columns) {
			if (map.put(column.getField(), null) != null) {
				throw new IllegalArgumentException("Duplicated column " + column);
			}
		}
	}
	
	void setJoins(Collection<Join> joins) {
		this.joins = joins;
	}
	
	public Select<E> innerJoinOn(ITable right, ICriteria criteria) {
		this.joins.add(new Join(true, null, right, criteria));
		return this;
	}
	
	// TODO select without peers
//	public Select<E> innerJoinOn(String right, ICriteria criteria) {
//		this.joins.add(new Join(true, null, Parser.parsePeer(right), criteria));
//		return this;
//	}
	
	public Select<E> innerJoinOn(ITable left, ITable right, ICriteria criteria) {
		this.joins.add(new Join(true, left, right, criteria));
		return this;
	}
	
	// TODO select without peers
//	public Select<E> innerJoinOn(String left, String right, ICriteria criteria) {
//		this.joins.add(new Join(true, Parser.parsePeer(left), Parser.parsePeer(right), criteria));
//		return this;
//	}
	
	public Select<E> outterJoinOn(ITable right, ICriteria criteria) {
		this.joins.add(new Join(true, peer, right, criteria));
		return this;
	}
	
	// TODO select without peers
//	public Select<E> outterJoinOn(String right, ICriteria criteria) {
//		this.joins.add(new Join(false, peer, Parser.parsePeer(right), criteria));
//		return this;
//	}
	
	public Select<E> outterJoinOn(ITable left, ITable right, ICriteria criteria) {
		this.joins.add(new Join(false, left, right, criteria));
		return this;
	}
	
	// TODO select without peers
//	public Select<E> outterJoinOn(String left, String right, ICriteria criteria) {
//		this.joins.add(new Join(false, Parser.parsePeer(right), Parser.parsePeer(right), criteria));
//		return this;
//	}
	
	@SafeVarargs
	public final Select<E> where(ICriteria... criterias) {
		where.add(criterias);
		return this;
	}
	
	// TODO select without peers
//	public Select<E> where(String string) {
//		// TODO Auto-generated method stub
//		return this;
//	}
	
	// TOOD Having
//	@SafeVarargs
//	public final Select<E> having(ICriteria... criterias) {
//		// TODO having
//		return this;
//	}
	
	public Select<E> groupBy(IColumn... columns) {
		for (IColumn column : columns) {
			groupBy.add(column);
		}
		return this;
	}
	
	// TODO select without peers
//	public Select<E> groupBy(String... columns) {
//		for (String column : columns) {
//			groupBy.add(Parser.parseColumn(column));
//		}
//		return this;
//	}

	public Select<E> orderBy(IColumn... columns) {
		orderBy.addSegments(true, columns);
		return this;
	}
	
	public Select<E> orderBy(boolean asc, IColumn... columns) {
		orderBy.addSegments(asc, columns);
		return this;
	}
	
	public Select<E> orderBy(Order order) {
		orderBy = order;
		return this;
	}
	
	public Peer<E> getPeer() { // TODO hide?
		return peer;
	}
	
	public IColumn[] getColumns() { // TODO hide?
		return columns;
	}
	
	public List<E> list() throws DatasourceException {
		return peer.getDatasource().select(this);
	}
	
	public E first() throws DatasourceException {
		try (Iterator<E> iterator = iterator()) {
			if (iterator.hasNext()) {
				return iterator.next();
			}
		}
		return null;
	}
	
	public Iterator<E> iterator() throws DatasourceException {
		return peer.getDatasource().iterator(this);
	}
	
	public Iterator<E> cursor() throws DatasourceException {
		return peer.getDatasource().iterator(this);
	}

	@Override
	public void accept(CommandVisitor visitor) throws DatasourceException {
		visitor.visitSelect(columns);
		visitor.visitFrom(peer);
		for (Join join : joins) {
			visitor.visitJoin(join.inner, join.left, join.right, join.criteria);
		}
		visitor.visitWhere(where);
		int groupBySize = groupBy.size();
		if (groupBySize > 0) {
			visitor.visitGroupBy(groupBy.toArray(new Column[groupBySize]));
		}
		visitor.visitOrderBy(orderBy);
	}
	
	public static final <E extends IRecord> Select<E> wrap(E e) {
		@SuppressWarnings("unchecked")
		Peer<E> peer = (Peer<E>) e.getPeer();
		Select<E> select = new Select<E>(peer, peer.getKeys());
		Segment<E> where = select.where;
		for (IColumn key : peer.getKeys()) {
			where.add(new Condition<E>(0, key, e.getObject(key.getIndex())));
		}
		return select;
	}

}
