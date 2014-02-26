package org.equo.db.impl;

import org.equo.Field;
import org.equo.IColumn;
import org.equo.ITable;
import org.equo.db.DefaultCommandAdapter;

public class PostgreSQLAdapter extends DefaultCommandAdapter {
	
	@Override
	public PostgreSQLAdapter newInstance() {
		return new PostgreSQLAdapter();
	}

	protected void visitIdentifier(ITable peer) {
		builder.append('\"');
		builder.append(peer.getName());
		builder.append('\"');
	}
	
	protected void visitIdentifier(IColumn column) {
		Field field = column.getField();
		builder.append('\"');
		builder.append(field.getTable());
		builder.append("\".\"");
		builder.append('.');
		builder.append(field.getName());
		builder.append('\"');
	}

}
