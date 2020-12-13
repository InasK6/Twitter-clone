package services;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import tools.AuthentificationTools;
import tools.ErrorJSON;
import tools.MessageTools;
import tools.UserTools;

public class Search {
	public static JSONObject  UserKeyWords(String key, String keyWords) {
		try{if(key==null || key.equals("") || keyWords==null || keyWords.equals("")) {
			return ErrorJSON.serviceRefused("Informations manquantes", ErrorJSON.ERROR_ARG);
		}
		if(!AuthentificationTools.keyExists(key)) {
			return ErrorJSON.serviceRefused("Connectez-vous", ErrorJSON.ERROR_SQL);
		}
		if(!AuthentificationTools.checkKey(key)) {
			Authentification.deleteConnexion(key);
			return ErrorJSON.serviceRefused("Session expirée", ErrorJSON.ERROR_SQL);
		}
		AuthentificationTools.updateDate(key);
		return UserTools.getByKeyWords(keyWords);
				
		}catch(JSONException j) {
			return ErrorJSON.serviceRefused("error on getting user! Sorry", ErrorJSON.ERROR_JSON);
		}catch( SQLException e) {
			return ErrorJSON.serviceRefused("can't get user!", ErrorJSON.ERROR_SQL);
		}
	}
	
	public static JSONObject MessageKeyWords(String key, String keyWords) {
		try{if(key==null || key.equals("") || keyWords==null || keyWords.equals("")) {
			return ErrorJSON.serviceRefused("Informations manquantes", ErrorJSON.ERROR_ARG);
		}
		if(!AuthentificationTools.keyExists(key)) {
			return ErrorJSON.serviceRefused("Connectez-vous", ErrorJSON.ERROR_SQL);
		}
		if(!AuthentificationTools.checkKey(key)) {
			Authentification.deleteConnexion(key);
			return ErrorJSON.serviceRefused("Session expirée", ErrorJSON.ERROR_SQL);
		}
		
		return MessageTools.searchMessages(keyWords);
		
		}catch(JSONException j) {
			return ErrorJSON.serviceRefused("error on getting user! Sorry", ErrorJSON.ERROR_JSON);
		}catch( SQLException e) {
			return ErrorJSON.serviceRefused("can't get user!", ErrorJSON.ERROR_SQL);
		}
	}
}
