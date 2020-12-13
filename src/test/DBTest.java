package test;

import javax.naming.*;
import javax.sql.*;
import java.sql.*;


public class DBTest {

	public void init() {
		try {
			Context ctx = new InitialContext();
			if(ctx==null)
				throw new Exception("Boom");
			DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/db");
			if(ds != null) {
				Connection conn = ds.getConnection();
				if(conn != null) {
					Statement stmt = conn.createStatement();
					ResultSet rst = stmt.executeQuery("SELECT * FROM test");
					if(rst.next()) {
						int id = rst.getInt("id");
						System.out.println("ID : "+id);
					}
					rst.close();
					conn.close();
				}
			}
		} catch(Exception e) { e.printStackTrace(); }
	}
}
