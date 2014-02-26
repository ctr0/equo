package org.equo.test.model.peer;

import javax.annotation.Generated;

import org.equo.Column;
import org.equo.Table;
import org.equo.Entity;
import org.equo.test.model.WeaponsProf;

@Generated("org.equo.gen.ModelGenerator")
public class WEAPONSPROF<E extends Entity<E>> extends Table<E, WeaponsProf> {

	public final Column<E, String> CPROF;
	public final Column<E, String> CWEAPON;

	public WEAPONSPROF(Table<E, ?> parent, String alias) {
		super(parent, alias);
		CPROF = Column.createColumn(this, "CPROF", Domains.CCODE, 0);
		CWEAPON = Column.createColumn(this, "CWEAPON", Domains.CCODE, 1);
		setColumns(CPROF, CWEAPON);
	}

	@Override
	public String getName() {
		return "WEAPONSPROF";
	}

	@Override
	public String getDatasourceName() {
		return "MASTER";
	}

	public PROFESSIONS<E> PROFESSIONS() {
		PROFESSIONS<E> f = (PROFESSIONS<E>) getForeign("PROFESSIONS");
		if (f == null) setForeign(f = new PROFESSIONS<E>(this, "PROFESSIONS"), 
				CPROF.EQ(f.CPROF));
		return f;
	}
}