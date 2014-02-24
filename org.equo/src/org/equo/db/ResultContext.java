package org.equo.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class ResultContext {
	
	private Connection cn;
	private Statement st;
	private ResultSet rs;
	private int uc;

	ResultContext(Connection cn, Statement st, ResultSet rs, int uc) {
		this.cn = cn;
		this.st = st;
		this.rs = rs;
		this.uc = uc;
	}
	
	public ResultSet getResultSet() {
		return rs;
	}

	public int getUpdateCount() {
		return uc;
	}
	
	public void kill() {
		try { if (rs != null) rs.close(); } catch (Exception e) {}
		try { if (st != null) st.close(); } catch (Exception e) {}
		try { if (cn != null) cn.close(); } catch (Exception e) {}
	}
	
	public void close() throws SQLException {
		// FIXME throw exceptions no close ?
		kill();
	}	
	
}
