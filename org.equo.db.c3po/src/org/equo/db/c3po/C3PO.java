package org.equo.db.c3po;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import org.equo.DatasourceInfo;
import org.equo.db.ConnectionPool;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

public class C3PO implements ConnectionPool {
	
	private ComboPooledDataSource cpds;

	@Override
	public boolean accepts(DatasourceInfo info) {
		return true;
	}

	@Override
	public void initialize(DatasourceInfo info) {
		cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass(info.getRequiredProperty(DRIVER_CLASS_NAME));
		} catch (PropertyVetoException e) {
			throw new IllegalStateException("Cannot initialize driver");
		}
		cpds.setJdbcUrl(info.getRequiredProperty(DATABASE_URL));
		cpds.setUser(info.getRequiredProperty(USERNAME));
		cpds.setPassword(info.getRequiredProperty(PASSWORD));
	}

	@Override
	public Connection getConnection() throws SQLException {
		return cpds.getConnection();
	}

	@Override
	public void close() throws SQLException {
		DataSources.destroy(cpds);
	}

}
