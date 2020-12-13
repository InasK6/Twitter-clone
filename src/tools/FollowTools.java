package tools;

/*import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import bd.DBStatic;
import bd.Database;

public class FollowTools {
	

    public static JSONObject getFollowed(int id_user) throws SQLException, JSONException{
	String query = "select followed from "+ DBStatic.FOLLOW_TABLE+ " where follower = "+id_user+" ;" ;
	Connection conn = Database.getMySQLConnection();
	Statement st = conn.createStatement();
	ResultSet res = st.executeQuery(query);
	
	List<String> friends =new ArrayList<String>();
	while(res.next()) {
		String s=res.getString("login");
		friends.add(s);
	}
	
	JSONObject j=new JSONObject();
	j.put("liste utilisateurs", friends);
	st.close();
	conn.close();
	return j;
	
    }*/

    /**
     * 
     * @param id_user	identifiant de l'utilisateur qui follow
     * @param id_user_delete identifiant de l'utilisateur suivi
     * @return JSONObject
     * @throws SQLException
     * @throws JSONException
     */
   /* public static JSONObject deleteFollowed(int id_user, int id_user_delete) throws SQLException, JSONException{
	String query = "delete from  "+ DBStatic.FOLLOW_TABLE+ " where follower = "+id_user+" and followed = "+id_user_delete+" ;";
	Connection conn = Database.getMySQLConnection();
	Statement st = conn.createStatement();
	st.executeUpdate(query);
	st.close();
	return ErrorJSON.serviceAccepted();
    }

    public static JSONObject addFollowed(int id_user, int id_user_add) throws SQLException, JSONException{	
	String query = "insert  into "+DBStatic.FOLLOW_TABLE+"  values ("+id_user+","+id_user_add+", NOW() ) ; ";
	Connection conn = Database.getMySQLConnection();
	Statement st = conn.createStatement();
	st.executeUpdate(query);
	st.close();
	conn.close();
	return ErrorJSON.serviceAccepted();
    }
}*/

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import bd.DBStatic;
import bd.Database;

public class FollowTools {
	

    public static JSONObject getFollowed(String nom_user) throws SQLException, JSONException{
    	int id1 = UserTools.getId(nom_user);
    	// récupère les identifiants des personnes followed
    	String query = "select followed from "+ DBStatic.FOLLOW_TABLE+ " where follower = "+id1+" ;" ;
    	System.out.println(query);
    	Connection conn = Database.getMySQLConnection();
    	Statement st = conn.createStatement();
    	ResultSet res = st.executeQuery(query);
	
    	List<Integer> friends =new ArrayList<Integer>();
    	while(res.next()) {
    		int tmp = res.getInt("followed");
    		friends.add(tmp);
    	}
	
    	JSONObject j=new JSONObject();
    	j.put("liste utilisateurs", friends);
    	st.close();
    	conn.close();
    	return j;
    }

    public static JSONObject deleteFollowed(String nom_user, String nom_user_delete) throws SQLException, JSONException{
    	int id1 = UserTools.getId(nom_user);
		int id2 = UserTools.getId(nom_user_delete);
    	String query = "delete from  "+ DBStatic.FOLLOW_TABLE+ " where follower = "+id1+" and followed = "+id2+" ;";
    	Connection conn = Database.getMySQLConnection();
    	Statement st = conn.createStatement();
    	st.executeUpdate(query);
    	st.close();
    	return ErrorJSON.serviceAccepted();
    }

    public static JSONObject addFollowed(String nom_user, String nom_user_add) throws SQLException, JSONException{	
    	int id1 = UserTools.getId(nom_user);
		int id2 = UserTools.getId(nom_user_add);
		String query = "insert  into "+DBStatic.FOLLOW_TABLE+"  values ("+id1+","+id2+", NOW() ) ; ";
		Connection conn = Database.getMySQLConnection();
		Statement st = conn.createStatement();
		st.executeUpdate(query);
		st.close();
		conn.close();
		return ErrorJSON.serviceAccepted();
    }
    /**
     * 
     * @param id
     * @return liste des followers abonnés à l'utilisateur d'identifiant id
     * @throws SQLException
     * @throws JSONException
     */
    public static JSONObject getFollowers(int id) throws SQLException, JSONException {
    	String query = "select follower from "+ DBStatic.FOLLOW_TABLE+ " where followed = "+id+" ;" ;
    	//"select followed from "+ DBStatic.FOLLOW_TABLE+ " where follower = "+id1+" ;" ;
    	
    	System.out.println(query);
    	Connection conn = Database.getMySQLConnection();
    	Statement st = conn.createStatement();
    	ResultSet res = st.executeQuery(query);
	
    	List<Integer> friends =new ArrayList<Integer>();
    	while(res.next()) {
    		int tmp = res.getInt("follower");
    		friends.add(tmp);
    	}
	
    	JSONObject j=new JSONObject();
    	j.put("liste utilisateurs", friends);
    	st.close();
    	conn.close();
    	return j;
    }
    
    public static boolean followExists(String nom_user_1, String nom_user_2) throws SQLException {
		Connection conn=Database.getMySQLConnection();
		Statement st=conn.createStatement();
		int id1 = UserTools.getId(nom_user_1);
		int id2 = UserTools.getId(nom_user_2);
		String Query="Select * from "+DBStatic.FOLLOW_TABLE+" where follower="+id1+" AND followed="+id2+" ;";
		System.out.println(Query);
		ResultSet res=st.executeQuery(Query);
		boolean b=res.next();
		
		st.close();
		conn.close();	
    	
    	return b;
    }
}
