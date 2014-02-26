package org.equo.test;

import java.util.HashMap;

import org.equo.Datasource;
import org.equo.DatasourceException;
import org.equo.DatasourceInfo;
import org.equo.Session;
import org.equo.db.Database;
import org.equo.db.bonecp.BoneCP;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
//	TestPeerBuilder.class, 
//	TestCustomEntities.class,
//	TestGeneratedEntities.class,
	TestSelect.class,
//	TestFunctions.class
})
public class AllTestSuite {
	
	public static Session session;

	@BeforeClass
	public static void start() {
		// Create the DB session
		session = new Session() {
			@Override
			protected Datasource createDatasource0(DatasourceInfo info)
					throws DatasourceException {
				return new Database(info, new BoneCP());
			}
		};

		HashMap<String, String> props = new HashMap<String, String>();
		props.put("driver", "com.mysql.jdbc.Driver");
		props.put("url", "jdbc:mysql://localhost:3306/equo");
		props.put("username", "root");
		props.put("password", "root");

		try {
			session.createDatasource(new DatasourceInfo("MASTER", props));
		} catch (DatasourceException e) {
			e.printStackTrace();
			session.close();
		}
	}
	
	@AfterClass
	public static void end() {
		session.close();
	}

	public static class BaseTest {
		
		public Session getSession() {
			return AllTestSuite.session;
		}
		
		public Datasource getDatasource() {
			return Session.getDatasource("MASTER");
		}
	}
}
