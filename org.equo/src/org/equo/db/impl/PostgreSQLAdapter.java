package org.equo.db.impl;

import org.equo.db.DefaultCommandAdapter;

public class PostgreSQLAdapter extends DefaultCommandAdapter {
	
	@Override
	public PostgreSQLAdapter newInstance() {
		return new PostgreSQLAdapter();
	}

	protected void visitIdentifier(String id) {
		builder.append('\"');
		builder.append(id);
		builder.append('\"');
	}

}
