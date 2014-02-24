package org.equo.test;

import static org.junit.Assert.assertEquals;

import org.equo.Column;
import org.equo.Datasource;
import org.equo.DatasourceException;
import org.equo.IColumn;
import org.equo.Peer;
import org.equo.PeerBuilder;
import org.equo.Record;
import org.equo.Select;
import org.equo.Types;
import org.equo.test.AllTestSuite.BaseTest;
import org.equo.test.model.WeaponsProf;
import org.equo.util.Iterator;
import org.junit.Test;

public class TestPeerBuilder extends BaseTest {

	@Test
	public void test() throws IllegalStateException, DatasourceException {
		
		Datasource ds = getDatasource();
		
		// Select with dynamically created peers
		PeerBuilder<Record> builder = PeerBuilder.createPeer(ds, "GENER_MAT");
		Column<Record, String> mtnr = builder.addColumn("MTNR");
		Column<Record, Integer> size = builder.addColumn("SIZE", Types.INTEGER, 8);
		
		Peer<Record> peer = builder.getPeer();
		IColumn c = peer.getColumn("MTNR");
		assertEquals(mtnr, c);
		
		Select<Record> select = Select.selectFrom(peer, mtnr).where(mtnr.EQ("1234"));
		Peer<Record> p = select.getPeer();
		assertEquals(p, peer);
		
		try (Iterator<Record> it = select.iterator()) {
			while (it.hasNext()) {
				Record record = (Record) it.next();
				String string = record.getObject(mtnr);
				string = record.getObject(c);
				string = record.getObject(WeaponsProf.WEAPONSPROF().CPROF); // ok (rt error)
			}
		}
	}

}
