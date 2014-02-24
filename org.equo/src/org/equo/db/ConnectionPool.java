package org.equo.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.equo.DatasourceInfo;

public interface ConnectionPool {
	
	public static final String DRIVER_CLASS_NAME = "driver";
	public static final String DATABASE_URL = "url";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	
	public boolean accepts(DatasourceInfo info);

	public void initialize(DatasourceInfo info);
	
	public Connection getConnection() throws SQLException;
	
	public void close() throws SQLException;

}
