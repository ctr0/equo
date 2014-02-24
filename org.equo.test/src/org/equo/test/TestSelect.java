package org.equo.test;

import java.util.List;

import org.equo.DatasourceException;
import org.equo.PeerTable;
import org.equo.Query;
import org.equo.test.AllTestSuite.BaseTest;
import org.equo.test.model.WeaponsProf;
import org.junit.Test;

public class TestSelect extends BaseTest {

	@Test
	public void test() throws IllegalStateException, DatasourceException {

		PeerTable<WeaponsProf> peerWp = getSession().getPeer(WeaponsProf.class);
		
		peerWp.getDatasource().synchronize(peerWp);
		
		Query<WeaponsProf> select = peerWp.select(WeaponsProf.WEAPONSPROF().PROFESSIONS());
		List<WeaponsProf> list = select.list();
		for (WeaponsProf wp : list) {
			String cprof = wp.getObject(WeaponsProf.WEAPONSPROF().CPROF);
			System.out.println("CPROF: " + cprof);
		}
		System.out.println("END");
	}

}
