package org.equo.db;

import java.util.ArrayList;
import java.util.List;

import org.equo.Condition;
import org.equo.CriteriaVisitor;
import org.equo.DatasourceException;
import org.equo.Domain;
import org.equo.Field;
import org.equo.IColumn;
import org.equo.ICriteria;
import org.equo.IPeer;
import org.equo.Order;
import org.equo.Segment;
import org.equo.Types;
import org.equo.Condition.Operator;
import org.equo.ICommand.CommandVisitor;
import org.equo.Update.Set;

public class DefaultCommandAdapter implements CommandBuilder, CommandVisitor, CriteriaVisitor {
	
	protected final StringBuilder builder = new StringBuilder();
	protected final ArrayList<Object> values = new ArrayList<Object>();
	
	@Override
	public DefaultCommandAdapter newInstance() {
		return new DefaultCommandAdapter();
	}

	@Override
	public void visitSelect(IColumn[] columns) throws DatasourceException {
		StringBuilder b = builder;
		b.append("SELECT ");
		if (columns == null || columns.length == 0) {
			b.append('*'); // Unsafe
		} else {
			visitField(columns[0]);
			for (int i = 1; i < columns.length; i++) {
				b.append(", ");
				visitField(columns[i]);
			}
		}
	}

	
	public void visitField(IColumn column) {
		visitIdentifier(column);
	}

//	/* (non-Javadoc)
//	 * @see org.ctro.jsql.FieldVisitor#visitField(org.ctro.jsql.ViewField)
//	 */
//	@Override
//	public void visitField(ViewField field) {
//		StringBuilder b = builder;
//		switch(field.getFunction()) {
//		case ViewField.COUNT: b.append("COUNT("); break;
//		case ViewField.SUM: b.append("SUM("); break;
//		case ViewField.MIN: b.append("MIN("); break;
//		case ViewField.MAX: b.append("MAX("); break;
//		}
//		visitIdentifier(field);
//		b.append(')');
//	}

	@Override
	public void visitUpdate(IPeer peer) throws DatasourceException {
		StringBuilder b = builder;
		b.append("UPDATE ");
		visitIdentifier(peer);
	}

	@Override
	public void visitDelete() throws DatasourceException {
		builder.append("DELETE");
	}

	@Override
	public void visitInsert(IPeer peer, IColumn[] columns) throws DatasourceException {
		StringBuilder b = builder;
		b.append("INSERT INTO ");
		visitIdentifier(peer);
		b.append(" (");
		if (columns != null && columns.length > 0) {
			for (int i = 0; i < columns.length; i++) {
				if (i != 0) b.append(", ");
				visitIdentifier(columns[i]);
			}
		}
		b.append(')');
	}

	@Override
	public void visitFrom(IPeer peer) throws DatasourceException {
		StringBuilder b = builder;
		b.append(" FROM ");
		visitIdentifier(peer);
	}

	@Override
	public void visitJoin(boolean inner, IPeer left, IPeer right, ICriteria criteria) throws DatasourceException {
		StringBuilder b = builder;
		b.append(inner ? " INNER" : " OUTTER");
		b.append(" JOIN ");
		visitIdentifier(right);
		b.append(" ON ");
		criteria.accept(this);
	}

	@Override
	public void visitWhere(ICriteria criteria) throws DatasourceException {
		if (!criteria.isEmpty()) {
			builder.append(" WHERE");
			criteria.accept(this);
		}
	}

	@Override
	public void visitSegment(Segment<?> segment) {
		StringBuilder b = builder;
		b.append(" (");
		String junction = segment.isAnd() ? " AND" : " OR";
		List<ICriteria> conditions = segment.getConditions();
		boolean first = true;
		for (ICriteria criteria : conditions) {
			if (!first) b.append(junction);
			else first = false;
			criteria.accept(this);
		}
		b.append(')');
	}

	@Override
	public void visitCondition(Condition<?> condition) {
		IColumn vf = condition.getValueField();
		Object[] values = condition.getValues();
		if (vf == null) {
			for (Object value : values) {
				this.values.add(value);
			}
		}
		StringBuilder b = builder;
		b.append(' ');
		visitIdentifier(condition.getField());
		switch (condition.getOp()) {
		case Operator.EQ: 
			b.append(" = ");
			if (vf == null) b.append('?');
			else visitIdentifier(vf);
			break;
		case Operator.NE: 
			b.append(" != ");
			if (vf == null) b.append('?');
			else visitIdentifier(vf);
			break;
		case Operator.GT: 
			b.append(" > ");
			if (vf == null) b.append('?');
			else visitIdentifier(vf);
			break;
		case Operator.LT: 
			b.append(" < ");
			if (vf == null) b.append('?');
			else visitIdentifier(vf);
			break;
		case Operator.NU: b.append(" IS NULL"); break;
		case Operator.NN: b.append(" IS NOT NULL"); break;
		case Operator.NL:
			b.append(" NOT");
			//$FALL-THROUGH$
		case Operator.IL: 
			b.append(" IN (?");
			for (int j = 1; j < values.length; j++) {
				b.append(", ?");
			}
			b.append(')');
			break;			
		}
	}

	@Override
	public void visitSets(Set[] sets) throws DatasourceException {
		StringBuilder b = builder;
		b.append(" SET ");
		for (int i = 0; i < sets.length; i++) {
			if (i != 0) b.append(", ");
			Set set = sets[i];
			visitIdentifier(set.getColumn());
			b.append(" = ");
			IColumn cv = set.getColumnValue();
			if (cv != null) {
				visitIdentifier(cv);
			} else {
				b.append('?');
				values.add(set.getObjectValue());
			}
		}
	}

	@Override
	public void visitValues(Object[][] values) throws DatasourceException {
		StringBuilder b = builder;
		b.append(" VALUES ");
		for (int i = 0; i < values.length; i++) {
			if (i != 0) b.append(", ");
			b.append('(');
			for (int j = 0; j < values[i].length; j++) {
				if (j != 0) b.append(", ");
				b.append('?');
				this.values.add(values[i][j]);
			}
			b.append(')');
		}
	}

	// FIXME Aliases
	@Override
	public void visitOrderBy(Order order) throws DatasourceException {
		StringBuilder b = builder;
		b.append(" ORDER BY ");
		int i = 0;
		for (Order.Segment segment: order.getSegments()) {
			if (i != 0) b.append(", ");
			visitIdentifier(segment.getColumn());
			b.append(segment.isAsc() ? " ASC" : " DESC");
		}
	}

	// FIXME Aliases
	@Override
	public void visitGroupBy(IColumn[] columns) throws DatasourceException {
		StringBuilder b = builder;
		b.append(" GROUP BY ");
		for (int i = 0; i < columns.length; i++) {
			if (i != 0) b.append(", ");
			visitIdentifier(columns[i]);
		}
	}
	
	protected void visitIdentifier(IPeer peer) {
//		builder.append('\"');
		builder.append(peer.getName());
//		builder.append('\"');
	}
	
	protected void visitIdentifier(IColumn column) {
		Field field = column.getField();
		//builder.append('\"');
		builder.append(field.getTable());
		//builder.append("\".\"");
			builder.append('.');
		builder.append(field.getName());
		//builder.append('\"');
	}

	@Override
	public String toSql() {
		return builder.toString();
	}
	
	@Override
	public String toString() {
		return toSql();
	}

	@Override
	public void visitCreateTable(IPeer peer) throws DatasourceException {
		StringBuilder b = builder;
		b.append("CREATE TABLE \"" + peer.getName() + "\" ");
		b.append("(\n");
		boolean first = true;
		for (IColumn column : peer.getColumns()) {
			if (!first) b.append(", \n");
			else first = false;
			b.append("\"" + column.getName() + "\" ");
			Domain domain = column.getField().getDomain();
			String type = Types.toString(domain.getType());
			b.append(type);
			if (!type.equals("INTEGER") && !type.equals("SMALLINT")) {
				b.append(" (" + domain.getLength() + ")");
			}
			
//			if (column.isPrimaryKey()) {
//				b.append(" NOT NULL");
//			}
		}
		b.append(", \n");
		b.append("CONSTRAINT \"" + peer.getName() + "_PK\" PRIMARY KEY (");
		first = true;
		for (IColumn column : peer.getKeys()) {
			if (!first) b.append(", ");
			else first = false;
			b.append("\"" + column.getName() + "\"");
		}
		b.append(")\n)");
	}

	@Override
	public List<Object> getValues() {
		return values;
	}

}
