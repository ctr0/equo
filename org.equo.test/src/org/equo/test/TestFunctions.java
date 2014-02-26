package org.equo.test;

import org.equo.DatasourceException;
import org.equo.PeerTable;
import org.equo.Query;
import org.equo.test.AllTestSuite.BaseTest;
import org.equo.test.model.Profession;
import org.junit.Test;

public class TestFunctions extends BaseTest {

	@Test
	public void test() throws IllegalStateException, DatasourceException {

		PeerTable<Profession> peer = getSession().getPeer(Profession.class);
		
		//peer.getDatasource().synchronize(peer);
		
		Query<Profession> select = peer.select(Profession.PROFESSIONS().CPROF.MIN());
		Profession first = select.first();
		System.out.println("MinProf: " + first);
	}

}
