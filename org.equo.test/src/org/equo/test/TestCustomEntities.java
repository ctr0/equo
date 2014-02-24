package org.equo.test;

import static org.junit.Assert.assertEquals;

import org.equo.AbstractEntity;
import org.equo.Column;
import org.equo.Criteria;
import org.equo.Datasource;
import org.equo.DatasourceException;
import org.equo.Peer;
import org.equo.PeerBuilder;
import org.equo.Record;
import org.equo.Select;
import org.equo.test.AllTestSuite.BaseTest;
import org.junit.Test;

public class TestCustomEntities extends BaseTest {

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

		// A3. Select with dynamically created entities

		PeerBuilder<Person> bp = PeerBuilder.createPeer(ds, "PERSONS", Person.class);
		Column<Person, String> colp = bp.addColumn("NAME");
		Peer<Person> peerp = bp.getPeer();
		assertEquals(colp, peerp.getColumn("NAME"));

		Select<Person> selectp = Select.selectFrom(peerp);
		Person firstp = selectp.first();

		String strp = firstp.getObject(colp);

		PeerBuilder<Color> bc = PeerBuilder.createPeer(ds, "COLORS", Color.class);
		Column<Color, String> colc = bc.addColumn("NAME");
		Peer<Color> peerc = bc.getPeer();
		assertEquals(colc, peerc.getColumn("NAME"));
	
		Select<Color> selectc = null;
		Select<Color> s1 = Select.selectFrom(peerc).where(colc.EQ(colp));
		Select<Color> s2 = Select.selectFrom(peerc)
				.innerJoinOn(peerp, colc.EQ(colp)).where(colc.EQ(colp));
//		Select<Record> s3 = Select.selectFrom(peerc, peerp.ALL())
//				.innerJoinOn(peerp, colc.EQ(colp)).where(colc.EQ(colp)); // ok
//		Select<Record> s4 = Select.selectFrom(peerc, peerp.ALL())
//				.innerJoinOn(peerp, colc.EQ(colp)).where(colc.EQ("")); // ok

		Color firstc = selectc.first();

//		firstc.getObject(colp); // error
//		firstp.getObject(colc); // error
		firstc.getObject(colc); // ok
		firstp.getObject(colp); // ok

		Criteria<Person> eq = colp.EQ(colp);
		Criteria<Record> eq2 = colp.EQ(colc);

		Select<Person> columns = Select.selectFrom(peerp, colp, colp);
		Select<Record> columns2 = Select.selectFrom(peerp, colp, colc);

		Select<Person> innerJoinOn = Select.selectFrom(peerp, colp)
				.innerJoinOn(peerc, colc.EQ(colp)).groupBy(colp);

		Select<Record> groupBy = Select.selectFrom(peerp, colc.MAX()).groupBy(
				colp);
		String max = groupBy.first().getObject(colc.MAX());


	}

}
