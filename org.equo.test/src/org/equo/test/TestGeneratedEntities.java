package org.equo.test;

import org.equo.AbstractEntity;
import org.equo.Column;
import org.equo.Criteria;
import org.equo.Datasource;
import org.equo.DatasourceException;
import org.equo.Peer;
import org.equo.PeerTable;
import org.equo.Query;
import org.equo.Record;
import org.equo.Select;
import org.equo.Session;
import org.equo.test.AllTestSuite.BaseTest;
import org.equo.test.model.Profession;
import org.equo.test.model.WeaponsProf;
import org.equo.test.model.peer.PROFESSIONS;
import org.equo.test.model.peer.WEAPONSPROF;
import org.junit.Test;

public class TestGeneratedEntities extends BaseTest {

	private static class Person extends AbstractEntity<Person> {

		public static Column<Person, String> colp;

		protected Person(Peer<Person> peer) {
			super(peer);
		}
	}

	private static class Color extends AbstractEntity<Color> {

		public static Column<Color, String> colc;

		protected Color(Peer<Color> peer) {
			super(peer);
		}
	}

	@Test
	public void test() throws IllegalStateException, DatasourceException {

		Datasource ds = getDatasource();

		// B1. Select with generated tables

		Session session = getSession();
//		session.getPeer("");

		WEAPONSPROF<WeaponsProf> WP = WeaponsProf.WEAPONSPROF();
		PROFESSIONS<Profession> PR = Profession.PROFESSIONS();
		PeerTable<WeaponsProf> peer1 = session.getPeer(WeaponsProf.class);
		PeerTable<Profession> peer2 = session.getPeer(Profession.class);

		Select<WeaponsProf> sw = Select.selectFrom(peer1, WeaponsProf.WEAPONSPROF().ALL(), WeaponsProf.WEAPONSPROF().PROFESSIONS().CPROF);
		Criteria<WeaponsProf> c1 = WeaponsProf.WEAPONSPROF().PROFESSIONS().CPROF.EQ("W");
		Criteria<Record> c2 = WeaponsProf.WEAPONSPROF().PROFESSIONS().CPROF.EQ(PR.CPROF);
		sw.where(c1);
		sw.where(c2); // ???

		// B2. Select view

		Select<Record> s = Select.selectFrom(peer2, PR.ALL(), WP.CPROF);
		s.innerJoinOn(PR, WP.CPROF.EQ(PR.CPROF));
		s.where(WP.CPROF.EQ("PICATECLAS"));

		Query<WeaponsProf> q = peer1.select(c1);
		q.where(c1);
//		q.where(c2); ERROR

	}

}
