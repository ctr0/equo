package org.equo.test.model.peer;

import javax.annotation.Generated;

import org.equo.Entity;
import org.equo.Table;
import org.equo.TableColumn;
import org.equo.test.model.Profession;

@Generated("org.equo.gen.Generator")
public class PROFESSIONS<E extends Entity<E>> extends Table<E, Profession> {
	
	public TableColumn<E, String> CPROF = null;
	
	public PROFESSIONS(Table <E, ?> parent, String alias) {
		super(parent, alias);
		CPROF = new TableColumn<E, String>(this, "CPROF", Domains.CCODE, 0);
		setColumns(CPROF);
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
