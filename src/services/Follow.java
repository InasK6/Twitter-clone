package services;

/*import java.sql.*;

import org.json.JSONException;
import org.json.JSONObject;

import bd.DBStatic;
import bd.Database;
import tools.ErrorJSON;
import tools.FollowTools;
import tools.UserTools;

public class Follow {



    public static JSONObject getListFollow(int nom_user)throws JSONException, SQLException{
		if(!UserTools.userExists(nom_user)) {
			return ErrorJSON.serviceRefused("login incorrect ", ErrorJSON.ERROR_SQL);
		}

    	JSONObject res = FollowTools.getFollowed(nom_user);
	
    	return res;
	
    }

    public static JSONObject deleteFollow(int nom_user, int nom_user_delete) throws JSONException, SQLException{
		if(!UserTools.userExists(nom_user) || !UserTools.userExists(nom_user_delete)) {
			return ErrorJSON.serviceRefused("login incorrect ", ErrorJSON.ERROR_SQL);
		}
    	if(!followExists(nom_user,nom_user_delete)) {
			return ErrorJSON.serviceRefused("Follow non exists ", ErrorJSON.ERROR_SQL); 
    	}
    	JSONObject res = FollowTools.deleteFollowed(nom_user, nom_user_delete);
	
    	return res;   
    }

    public static JSONObject addFollow(int nom_user, int nom_user_add) throws SQLException, JSONException{
    	if(!UserTools.userExists(nom_user) || !UserTools.userExists(nom_user_add)) {
			return ErrorJSON.serviceRefused("login non existant ", ErrorJSON.ERROR_SQL);
		}
    	if(followExists(nom_user,nom_user_add)) {
			return ErrorJSON.serviceRefused("Follow already exists ", ErrorJSON.ERROR_SQL); 
    	}
    	JSONObject res = FollowTools.addFollowed( nom_user, nom_user_add);
    	
    	return res;   
    }
    
    public static boolean followExists(int id1, int id2) throws SQLException {
		Connection conn=Database.getMySQLConnection();
		Statement st=conn.createStatement();
		System.out.println("entrée");
		//int id1 = UserTools.getId(nom_user_1);
		//int id2 = UserTools.getId(nom_user_2);
		String Query="Select * from "+DBStatic.FOLLOW_TABLE+" where follower="+id1+" and followed="+id2+" ;";
		System.out.println(Query);
		ResultSet res=st.executeQuery(Query);
		boolean b=res.next();
		System.out.println(b);
		st.close();
		conn.close();	
    	
    	return b;
    }
    
}*/

import java.sql.*;

import org.json.JSONException;
import org.json.JSONObject;

import bd.DBStatic;
import bd.Database;
import tools.AuthentificationTools;
import tools.ErrorJSON;
import tools.FollowTools;
import tools.UserTools;

public class Follow {

	/**
	 *
	 * @param nom_user login de l'utilisateur 
	 * @return liste des personnes auxquelles nom_user est abonné
	 * @throws JSONException
	 * @throws SQLException
	 */

    public static JSONObject getListFollow(String key,int id){
    	
    	try{
    		String nom_user=UserTools.getLogin(id);
    		if(key==null || key.equals("")) {
			
			return ErrorJSON.serviceRefused("informations manquantes ", ErrorJSON.ERROR_SQL);
	
		}
		//tester que l'utilisateur est connecté 
		if(!AuthentificationTools.keyExists(key)) {
			return ErrorJSON.serviceRefused("Connectez-vous à votre compte pour pouvoir effectuer cette opération", ErrorJSON.ERROR_SQL);
		}
		if(!AuthentificationTools.checkKey(key)) {
			Authentification.deleteConnexion(key);
			return ErrorJSON.serviceRefused("Session expirée", ErrorJSON.ERROR_SQL);
		}
    	
    	if(!UserTools.userExists(id)) {
			return ErrorJSON.serviceRefused("login incorrect ", ErrorJSON.ERROR_SQL);
		}

    	JSONObject res = FollowTools.getFollowed(nom_user);
    	AuthentificationTools.updateDate(key);
    	return res;
    	}catch(SQLException e) {
			return ErrorJSON.serviceRefused(" Problem detected", ErrorJSON.ERROR_SQL);
		}catch (JSONException e) {
			return ErrorJSON.serviceRefused(" Error", ErrorJSON.ERROR_JSON);
			
		}
	
    }
    // un follow peut être supprimé seulement par les deux utilisateurs qui le composent 
    public static JSONObject deleteFollow(String key, String nom_user, String nom_user_delete) {
    	try{if(key==null || key.equals("")) {
			
			return ErrorJSON.serviceRefused("informations manquantes ", ErrorJSON.ERROR_SQL);
	
		}
		//tester que l'utilisateur est connecté et que le login associé au message correspond à celui de la connexion
		if(!AuthentificationTools.keyExists(key) || (!nom_user.equals(UserTools.getLogin(AuthentificationTools.getID(key))) && !nom_user_delete.equals(UserTools.getLogin(AuthentificationTools.getID(key))))) {
			return ErrorJSON.serviceRefused("Connectez-vous au compte "+nom_user+" pour pouvoir effectuer cette opération", ErrorJSON.ERROR_SQL);
		}
		if(!AuthentificationTools.checkKey(key)) {
			Authentification.deleteConnexion(key);
			return ErrorJSON.serviceRefused("Session expirée", ErrorJSON.ERROR_SQL);
		}
    	
    	if(!UserTools.userExists(nom_user) || !UserTools.userExists(nom_user_delete)) {
			return ErrorJSON.serviceRefused("login incorrect ", ErrorJSON.ERROR_SQL);
		}
    	if(!FollowTools.followExists(nom_user,nom_user_delete)) {
			return ErrorJSON.serviceRefused("Follow non exists ", ErrorJSON.ERROR_SQL); 
    	}
    	JSONObject res = FollowTools.deleteFollowed(nom_user, nom_user_delete);
    	AuthentificationTools.updateDate(key);
    	return res;  
    	}catch(SQLException e) {
			return ErrorJSON.serviceRefused(" Problem detected", ErrorJSON.ERROR_SQL);
		}catch (JSONException e) {
			return ErrorJSON.serviceRefused(" Error", ErrorJSON.ERROR_JSON);
			
		}
    }

    public static JSONObject addFollow(String key, String nom_user, String nom_user_add) {
    	try{if(key==null || key.equals("")) {
			
			return ErrorJSON.serviceRefused("informations manquantes ", ErrorJSON.ERROR_SQL);
	
		}
		//tester que l'utilisateur est connecté et que le login associé au message correspond à celui de la connexion
		if(!AuthentificationTools.keyExists(key) || !nom_user.equals(UserTools.getLogin(AuthentificationTools.getID(key)))) {
			return ErrorJSON.serviceRefused("Connectez-vous au compte "+nom_user+" pour pouvoir effectuer cette opération", ErrorJSON.ERROR_SQL);
		}
		if(!AuthentificationTools.checkKey(key)) {
			Authentification.deleteConnexion(key);
			return ErrorJSON.serviceRefused("Session expirée", ErrorJSON.ERROR_SQL);
		}
    	
    	if(!UserTools.userExists(nom_user) || !UserTools.userExists(nom_user_add)) {
			return ErrorJSON.serviceRefused("login incorrect ", ErrorJSON.ERROR_SQL);
		}
    	if(FollowTools.followExists(nom_user,nom_user_add)) {
			return ErrorJSON.serviceRefused("Follow already exists ", ErrorJSON.ERROR_SQL); 
    	}
    	
    	JSONObject res = FollowTools.addFollowed( nom_user, nom_user_add);
    	AuthentificationTools.updateDate(key);
    	return res;
    }catch(SQLException e) {
		return ErrorJSON.serviceRefused(" Problem detected", ErrorJSON.ERROR_SQL);
	}catch (JSONException e) {
		return ErrorJSON.serviceRefused(" Error", ErrorJSON.ERROR_JSON);
		
	}
    }
    
    public static JSONObject getFollowers(String key, int id) {
    	if(key==null || key.equals("")) {
			
			return ErrorJSON.serviceRefused("informations manquantes ", ErrorJSON.ERROR_SQL);
	
		}
		//tester que l'utilisateur est connecté 
		try{if(!AuthentificationTools.keyExists(key)) {
			return ErrorJSON.serviceRefused("Connectez-vous à votre compte pour pouvoir effectuer cette opération", ErrorJSON.ERROR_SQL);
		}
		if(!AuthentificationTools.checkKey(key)) {
			Authentification.deleteConnexion(key);
			return ErrorJSON.serviceRefused("Session expirée", ErrorJSON.ERROR_SQL);
		}
    	
    	if(!UserTools.userExists(id)) {
			return ErrorJSON.serviceRefused("login incorrect ", ErrorJSON.ERROR_SQL);
		}

    	JSONObject res = FollowTools.getFollowers(id);
    	AuthentificationTools.updateDate(key);
    	return res;
		}catch(SQLException e) {
			e.printStackTrace();
			return ErrorJSON.serviceRefused(" Problem detected "+e.getMessage(), ErrorJSON.ERROR_SQL);
		}catch (JSONException e) {
			return ErrorJSON.serviceRefused(" Error", ErrorJSON.ERROR_JSON);
			
		}
    }

    
}