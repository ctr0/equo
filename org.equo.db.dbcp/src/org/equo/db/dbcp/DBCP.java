package org.equo.db.dbcp;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.equo.DatasourceInfo;
import org.equo.db.ConnectionPool;

public class DBCP implements ConnectionPool {
	
	private BasicDataSource ds;
	

	
	@Override
	public boolean accepts(DatasourceInfo info) {
		return true;
	}

	@Override
	public void initialize(DatasourceInfo info) {
		ds = new BasicDataSource();
		ds.setDriverClassName(info.getRequiredProperty(DRIVER_CLASS_NAME));
		ds.setUrl(info.getRequiredProperty(DATABASE_URL));
		ds.setUsername(info.getRequiredProperty(USERNAME));
		ds.setPassword(info.getRequiredProperty(PASSWORD));
	}

	@Override
	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	@Override
	public void close() throws SQLException {
		ds.close();
	}
}
