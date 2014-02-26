package org.equo.db.bonecp;

import java.sql.Connection;
import java.sql.SQLException;

import org.equo.DatasourceInfo;
import org.equo.db.ConnectionPool;

import com.jolbox.bonecp.BoneCPConfig;

public class BoneCP implements ConnectionPool {
	
	private com.jolbox.bonecp.BoneCP cpds;
	
	@Override
	public boolean accepts(DatasourceInfo info) {
		return true;
	}

	@Override
	public void initialize(DatasourceInfo info) {
		String driverClassName = info.getRequiredProperty(DRIVER_CLASS_NAME);
		try {
			Class.forName(driverClassName);
		} catch (Exception e) {
			throw new IllegalStateException("Cannot load driver " + driverClassName);
		}
		// setup the connection pool
		BoneCPConfig config = new BoneCPConfig();
		// jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
		config.setJdbcUrl(info.getRequiredProperty(DATABASE_URL)); 
		config.setUsername(info.getRequiredProperty(USERNAME)); 
		config.setPassword(info.getRequiredProperty(PASSWORD));
		config.setMinConnectionsPerPartition(5);
		config.setMaxConnectionsPerPartition(10);
		config.setPartitionCount(1);
		try {
			cpds = new com.jolbox.bonecp.BoneCP(config);
		} catch (SQLException e) {
			throw new IllegalStateException("Error crating BoneCP ", e);
		}
	}

	@Override
	public Connection getConnection() throws SQLException {
		return cpds.getConnection();
	}

	@Override
	public void close() throws SQLException {
		cpds.close();
	}

}
