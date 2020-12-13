package services;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import tools.AuthentificationTools;
import tools.ErrorJSON;
import tools.UserTools;

public class Authentification {
	/* l'ajout d'une session correspond à un login
	 * il peut y avoir plusieurs connexions pour un même utilisateurs sur différentes machines 
	 * il faut vérifier que le mot de passe est correct avant de créer une nouvelle connexion
	 */
	
	public static JSONObject insertConnexion(String login, String mdp) {
		try{if(!UserTools.userExists(login) || !UserTools.checkPassword(login, mdp)) {
			return ErrorJSON.serviceRefused("Erreur d'authentification", ErrorJSON.ERROR_SQL);
		}
		
		int id = UserTools.getId(login);
		/* inutile car il peut y avoir plusieurs connexions
		
		*if(AuthentificationTools.alreadyConnected(id)) {
			return ErrorJSON.serviceRefused("déjà connecté!!", ErrorJSON.ERROR_SQL)
		}*/
		JSONObject j = AuthentificationTools.insertConnexion(id);
		return j ;
		}catch(SQLException e) {
			return ErrorJSON.serviceRefused("problem occured ", ErrorJSON.ERROR_SQL);
		}catch(JSONException e) {
			return ErrorJSON.serviceRefused("can't add connexion", ErrorJSON.ERROR_JSON);
		}
	}
	
	public static JSONObject deleteConnexion(String key)  {
		try{if(!AuthentificationTools.keyExists(key)) {
			return ErrorJSON.serviceRefused("key non existante", ErrorJSON.ERROR_SQL);
		}
		JSONObject j = AuthentificationTools.deleteConnexion(key);
		return j ;
		}catch(SQLException e) {
			return ErrorJSON.serviceRefused("problem occured ", ErrorJSON.ERROR_SQL);
		}catch(JSONException e) {
			return ErrorJSON.serviceRefused("can't delete connexion", ErrorJSON.ERROR_JSON);
		}
	}
	
}