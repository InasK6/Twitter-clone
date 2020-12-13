package tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import bd.DBStatic;
import bd.Database;

public class AuthentificationTools {
	/**
	 * générateur de valeurs aléatoires de String 
	 * @return chaîne de caractères aléatoire
	 * @throws SQLException
	 */
	public static String keyGenerator() throws SQLException {
		String s="";
		// tant qu'une clé semblable existe dans la table, en générer une autre
		
		do {
			s=UUID.randomUUID().toString().replace("-", "");
		}while(keyExists(s));
		return s;
	}
	
	public static JSONObject insertConnexion(int id) throws SQLException, JSONException {
		String query = "insert  into "+DBStatic.SESSION_TABLE+"  values ("+"'"+keyGenerator()+"' , "+id+", NOW() ) ; ";
		Connection conn = Database.getMySQLConnection();
		Statement st = conn.createStatement();
		st.executeUpdate(query);
		st.close();
		conn.close();
		return ErrorJSON.serviceAccepted();
	}
	
	public static JSONObject deleteConnexion(String key) throws SQLException, JSONException {
		
		
		
		String query = "delete  from "+DBStatic.SESSION_TABLE+" where cle= '"+key+"'  ; ";
		Connection conn = Database.getMySQLConnection();
		Statement st = conn.createStatement();
		st.executeUpdate(query);
		st.close();
		conn.close();
		return ErrorJSON.serviceAccepted();
	}
	
	public static boolean checkKey(String key) throws SQLException {
		String query = "Select  * from "+DBStatic.SESSION_TABLE+" where cle='"+key+"' and time_to_sec(TIMEDIFF(  NOW(),timestamp))<"+DBStatic.time_limit+" ;";
		Connection conn = Database.getMySQLConnection();
		Statement st = conn.createStatement();
		ResultSet res = st.executeQuery(query);
		boolean b=res.next();
		st.close();
		conn.close();
		
		return b;
	}
	
	public static boolean keyExists(String key) throws SQLException {
		String query = "Select * from "+DBStatic.SESSION_TABLE+" where cle='"+key+"';";
		Connection conn = Database.getMySQLConnection();
		Statement st = conn.createStatement();
		ResultSet res = st.executeQuery(query);
		boolean b=res.next();
		st.close();
		conn.close();
		return b;
	}
	public static boolean alreadyConnected(int id) throws SQLException  {
		String query = "Select * from "+DBStatic.SESSION_TABLE+" where id="+id+";";
		Connection conn = Database.getMySQLConnection();
		Statement st = conn.createStatement();
		ResultSet res = st.executeQuery(query);
		boolean b=res.next();
		st.close();
		conn.close();
		return b;
	}
	public static boolean updateDate(String key) throws SQLException{
		
		Connection conn = Database.getMySQLConnection();
		Statement st = conn.createStatement();
		String Query="update "+DBStatic.SESSION_TABLE+" set timestamp= NOW() where cle= '"+key+"' ;";
		System.out.println(Query);
		int res=st.executeUpdate(Query);
		st.close();
		conn.close();
		return res>0;
		
	}
	/**
	 * fonction utilisée pour les tests 
	 * @param id identifiant de l'utilisateur
	 * @return première occuprence d'une connexion d'un utilisateur d'identifiant id, donc la connexion la plus récente
	 * 
	 */
	public static String getKey(int id) throws SQLException {
		String query = "Select cle from "+DBStatic.SESSION_TABLE+" where id="+id+";";
		Connection conn = Database.getMySQLConnection();
		Statement st = conn.createStatement();
		ResultSet res = st.executeQuery(query);
		String s="";
		if(res.next()) {
			s=res.getString("cle");
		}
		st.close();
		conn.close();
		return s;
	}
	public static int getID(String key) throws SQLException {
		String query = "Select id from "+DBStatic.SESSION_TABLE+" where cle='"+key+"';";
		Connection conn = Database.getMySQLConnection();
		Statement st = conn.createStatement();
		ResultSet res = st.executeQuery(query);
		int s=-1;
		if(res.next()) {
			s=res.getInt("id");
		}
		st.close();
		conn.close();
		return s;
		
		
		
	}
	
}