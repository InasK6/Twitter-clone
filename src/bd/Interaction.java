package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Interaction {
	
	//Insertion
	
	public static void insert( String query) throws Exception{
		String url="jdbc:mysql://localhost/Bracchi_KACI";
		Connection conn=DriverManager.getConnection(url,"root","root");
		//String query="INSERT INTO test VALUES (1);";
		Statement st = conn.createStatement();
		int res=st.executeUpdate(query);
		st.close();
		//System.out.println(res);
		
		st.close();
	}
	
	//Interrogation
	public static ResultSet interrogate(String query) throws Exception{
		String url="jdbc:mysql://localhost/Bracchi_Kaci";
		Connection conn=DriverManager.getConnection(url,"root","root");
		Statement st = conn.createStatement();
		ResultSet res=st.executeQuery(query);
		
		System.out.println(res);
		return res;
	}
	
}
