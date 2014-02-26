package org.equo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.equo.ICommand.CommandVisitor;
import org.equo.util.Iterator;

public class Query<E extends Entity<E>> {
	
	private Select<E> select;
	
	public Query(PeerTable<E> peer) {
		List<IColumn> columns = new ArrayList<>(8);
		for (IColumn column : peer.getColumns()) {
			columns.add(column);
		}
		select = new Select<E>(peer, columns.toArray(new IColumn[columns.size()]));
	}

	public Query(PeerTable<E> peer, Column<E, ?>[] columns) {
		Map<Field, IColumn> map = new HashMap<Field, IColumn>(columns.length);
		Map<String, Join> joins = new LinkedHashMap<>(8);
		for (Column<E, ?> column : columns) {
			if (map.put(column.getField(), null) != null) {
				throw new IllegalArgumentException("Duplicated column " + column);
			}
			if (column.isForeign()) {
				ITable table = column.getTable();
				Table<E, ?> parent = peer.getTable();
				String[] foreignPath = table.getForeignPath();
				for (int i = 1; i < foreignPath.length; i++) {
					String alias = foreignPath[i];
					Table<E, ?> foreign = parent.getOrCreateForeign(alias);
					Join join = new Join(true, parent, foreign, foreign.getCriteria());
					joins.put(table.getAlias(), join); // alias != table.getAlias()
					parent = foreign;
				}
			}
		}
		select = new Select<E>(peer, map.values().toArray(new IColumn[map.size()]));
		select.setJoins(joins.values());
	}

	public Query(PeerTable<E> peer, Table<E, ?>[] tables) {
		int size = tables != null ? tables.length + 1 : 1;
		List<IColumn> columns = new ArrayList<>(size * 8);
		Map<String, Join> joins = new LinkedHashMap<>(8);
		for (IColumn column : peer.getColumns()) {
			columns.add(column);
		}
		for (Table<E, ?> table : tables) {
			if (table != peer.getTable()) {
				for (IColumn column : table.getColumns()) {
					columns.add(column);
				}
				Table<E, ?> parent = peer.getTable();
				String[] foreignPath = table.getForeignPath();
				for (int i = 1; i < foreignPath.length; i++) {
					String alias = foreignPath[i];
					Table<E, ?> foreign = parent.getOrCreateForeign(alias);
					Join join = new Join(true, parent, foreign, foreign.getCriteria());
					joins.put(table.getAlias(), join); // alias != table.getAlias()
					parent = foreign;
				}
			}
		}
		select = new Select<E>(peer, columns.toArray(new IColumn[columns.size()]));
		select.setJoins(joins.values());
	}

	@SafeVarargs
	public final Query<E> where(Criteria<E>... criterias) {
		select.where(criterias);
		return this;
	}
	
	// TODO Having
//	@SafeVarargs
//	public final Query<E> having(Criteria<E>... criterias) {
//		select.having(criterias);
//		return this;
//	}

	@SafeVarargs
	public final Query<E> groupBy(Column<E, ?>... columns) {
		select.groupBy(columns);
		return this;
	}

	@SafeVarargs
	public final Query<E> orderBy(Column<E, ?>... columns) {
		select.orderBy(columns);
		return this;
	}

	@SafeVarargs
	public final Query<E> orderBy(boolean asc, Column<E, ?>... columns) {
		select.orderBy(asc, columns);
		return this;
	}

	public Query<E> orderBy(Order order) {
		select.orderBy(order);
		return this;
	}

	public List<E> list() throws DatasourceException {
		return select.list();
	}

	public E first() throws DatasourceException {
		return select.first();
	}

	public Iterator<E> iterator() throws DatasourceException {
		return select.iterator();
	}

	public Iterator<E> cursor() throws DatasourceException {
		return select.cursor();
	}

	public void accept(CommandVisitor visitor) throws DatasourceException {
		select.accept(visitor);
	}

	public String toString() {
		return select.toString();
	}

}
