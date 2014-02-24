package org.equo.test.model;

import javax.annotation.Generated;

import org.equo.APeerTable;
import org.equo.Entity;
import org.equo.PeerTable;
import org.equo.Session;
import org.equo.test.model.peer.WEAPONSPROF;

@Generated("org.equo.gen.ModelGenerator")
public class WeaponsProf extends Entity<WeaponsProf> {
	
	@SuppressWarnings("unchecked")
	@APeerTable
	public static final WEAPONSPROF<WeaponsProf> WEAPONSPROF() {
		return (WEAPONSPROF<WeaponsProf>) Session.getTable(WEAPONSPROF.class);
	}
	
	public WeaponsProf(PeerTable<WeaponsProf> peer) {
		super(peer);
	}
}
