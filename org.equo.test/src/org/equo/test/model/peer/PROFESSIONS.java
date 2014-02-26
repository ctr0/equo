package org.equo.test.model.peer;

import javax.annotation.Generated;

import org.equo.Column;
import org.equo.Entity;
import org.equo.Table;
import org.equo.test.model.Profession;

@Generated("org.equo.gen.Generator")
public class PROFESSIONS<E extends Entity<E>> extends Table<E, Profession> {
	
	public Column<E, String> CPROF = null;
	public Column<E, String> NINT = null;
	
	public PROFESSIONS(Table <E, ?> parent, String alias) {
		super(parent, alias);
		CPROF = Column.createColumn(this, "CPROF", Domains.CCODE, 0);
		NINT = Column.createColumn(this, "NINT", Domains.NINT8, 1);
		setColumns(CPROF, NINT);
		setKeys(CPROF);
	}

	@Override
	public String getName() {
		return "PROFESSIONS";
	}

	@Override
	public String getDatasourceName() {
		return "MASTER";
	}

}
