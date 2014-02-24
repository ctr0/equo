package org.equo.test.model;

import javax.annotation.Generated;

import org.equo.APeerTable;
import org.equo.Entity;
import org.equo.PeerTable;
import org.equo.Session;
import org.equo.test.model.peer.PROFESSIONS;

@Generated("org.equo.gen.Generator")
public class Profession extends Entity<Profession> {
	
	@APeerTable
	@SuppressWarnings("unchecked")
	public static PROFESSIONS<Profession> PROFESSIONS() {
		return (PROFESSIONS<Profession>) Session.getTable(PROFESSIONS.class);
	}

	public Profession(PeerTable<Profession> peer) {
		super(peer);
	}
}
