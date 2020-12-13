package services;

import java.sql.SQLException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import tools.AuthentificationTools;
import tools.ErrorJSON;
import tools.MessageTools;
import tools.UserTools;
//je considère qu'il faut être connecté pour accèder à la plateforme 
public class Messages {
	/*
	 * On veut que la session ne soit plus active au bout d'un temps d'inactivité
	 * donc après avoir effectué une quelconque action il faut le notifier en mettant
	 * à jour la date associé dans la table session à la date courante
	 */
	public static JSONObject addMessage(String key, String login, String message){
		try{	if(login==null || login.equals("") || key==null || key.equals("") || message==null || message.equals("") ) {
				return ErrorJSON.serviceRefused("informations manquantes", ErrorJSON.ERROR_ARG);
			}
			if(!UserTools.userExists(login)) {
			
				return ErrorJSON.serviceRefused("login incorrect ", ErrorJSON.ERROR_SQL);
		
			}
			//tester que l'utilisateur est connecté et que le login associé au message correspond à celui de la connexion
			if(!AuthentificationTools.keyExists(key) || !login.equals(UserTools.getLogin(AuthentificationTools.getID(key)))) {
				
				return ErrorJSON.serviceRefused("Connectez-vous au compte "+login+" pour pouvoir rédiger ce message", ErrorJSON.ERROR_SQL);
			}
			if(!AuthentificationTools.checkKey(key)) {
				Authentification.deleteConnexion(key);
				return ErrorJSON.serviceRefused("Session expirée", ErrorJSON.ERROR_SQL);
			}
			else {
				MessageTools.addMessage(login, message);
				AuthentificationTools.updateDate(key);
				return ErrorJSON.serviceAccepted();
			}
		}catch(SQLException s) {
			return ErrorJSON.serviceRefused("can't add message", ErrorJSON.ERROR_SQL);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return ErrorJSON.serviceRefused("can't add message,Sorry", ErrorJSON.ERROR_SQL);
		}
	}
	
	
	 /**
	  * 
	  * @param login
	  * @return tous les messages  
	  * @throws JSONException
	  * @throws SQLException
	  */
	public static JSONObject getMessages(String key,String login, boolean followed) {
		try{
			if(key==null || key.contentEquals("")) {
				return ErrorJSON.serviceRefused("informations manquantes", ErrorJSON.ERROR_ARG);
			}
			if(login==null || login.equals("") ) {
				AuthentificationTools.updateDate(key);
				return MessageTools.getMessages();
			}
			
			//tester que l'utilisateur existe
			int id=UserTools.getId(login);
			if(!UserTools.userExists(id)) {
				
					return ErrorJSON.serviceRefused("login incorrect ", ErrorJSON.ERROR_SQL);
				
			}
			
			if(!AuthentificationTools.keyExists(key)) {
				return ErrorJSON.serviceRefused("Connectez-vous", ErrorJSON.ERROR_SQL);
			}
			if(!AuthentificationTools.checkKey(key)) {
				Authentification.deleteConnexion(key);
				return ErrorJSON.serviceRefused("Session expirée", ErrorJSON.ERROR_SQL);
			}
			else {
				AuthentificationTools.updateDate(key);
				if(followed) {
					return MessageTools.getMessagesFollowed(login);
				}
				return MessageTools.getMessage(login);
				
			}
		}catch (SQLException j) {
			return ErrorJSON.serviceRefused("Error can't get message", ErrorJSON.ERROR_SQL);
		}
		catch (JSONException e) {
			return ErrorJSON.serviceRefused("problem sorry", ErrorJSON.ERROR_JSON);
		}
	}
	
	public static JSONObject deleteMessage(String key,  String id_M )  {
		/*
		 * Un utilisateur a le droit de supprimer ses messages seulement s'il n'est pas admin
		 */
		try{
			if(key==null || key.contentEquals("")) {
				return ErrorJSON.serviceRefused("informations manquantes", ErrorJSON.ERROR_ARG);
			}
			int id=AuthentificationTools.getID(key);
		/* Pas nécessaire car utilisateur est une clé étrangère de session, si l'utilisateur n'existe pas alors la clès n'existe forcèment pas 
		if(!UserTools.userExists(id)) {
			return ErrorJSON.serviceRefused("login incorrect ", ErrorJSON.ERROR_SQL);
		}*/
		//on vérifie si la session existe
		if(!AuthentificationTools.keyExists(key)) {
			Authentification.deleteConnexion(key);
			return ErrorJSON.serviceRefused("connectez-vous", ErrorJSON.ERROR_SQL);
		}
		if(!AuthentificationTools.checkKey(key)) {
			return ErrorJSON.serviceRefused("Session expirée", ErrorJSON.ERROR_SQL);
		}
		// on vérifie si la session est active
		else {
			 MessageTools.deleteMessage(id, id_M);
			 AuthentificationTools.updateDate(key);
			 return ErrorJSON.serviceAccepted();
		}
		}catch( SQLException e) {
			return ErrorJSON.serviceRefused("Operation refused", ErrorJSON.ERROR_SQL);
		}catch(JSONException e) {
			return ErrorJSON.serviceRefused("Error, Sorry!!", ErrorJSON.ERROR_JSON);
		}
		
	}
	//ne pas oublier l'update
	public static JSONObject like(String key, String idM) {
		try{
			if(key==null || key.contentEquals("")) {
				return ErrorJSON.serviceRefused("informations manquantes", ErrorJSON.ERROR_ARG);
			}
			String login=UserTools.getLogin(AuthentificationTools.getID(key));

		//on vérifie si la session existe
		if(!AuthentificationTools.keyExists(key)) {
			Authentification.deleteConnexion(key);
			return ErrorJSON.serviceRefused("connectez-vous", ErrorJSON.ERROR_SQL);
		}
		if(!AuthentificationTools.checkKey(key)) {
			return ErrorJSON.serviceRefused("Session expirée", ErrorJSON.ERROR_SQL);
		}
		AuthentificationTools.updateDate(key);
		return MessageTools.addLike(idM, login);
		}catch( SQLException e) {
			return ErrorJSON.serviceRefused("Operation refused", ErrorJSON.ERROR_SQL);
		}catch(JSONException e) {
			return ErrorJSON.serviceRefused("Error, Sorry!!", ErrorJSON.ERROR_JSON);
		}
	}
	
	public static JSONObject dislike(String key, String idM) {
		try{
			if(key==null || key.contentEquals("")) {
				return ErrorJSON.serviceRefused("informations manquantes", ErrorJSON.ERROR_ARG);
			}
			String login=UserTools.getLogin(AuthentificationTools.getID(key));

		//on vérifie si la session existe
		if(!AuthentificationTools.keyExists(key)) {
			Authentification.deleteConnexion(key);
			return ErrorJSON.serviceRefused("connectez-vous", ErrorJSON.ERROR_SQL);
		}
		if(!AuthentificationTools.checkKey(key)) {
			return ErrorJSON.serviceRefused("Session expirée", ErrorJSON.ERROR_SQL);
		}
		AuthentificationTools.updateDate(key);
		return MessageTools.removeLike(idM, login);
		}catch( SQLException e) {
			return ErrorJSON.serviceRefused("Operation refused", ErrorJSON.ERROR_SQL);
		}catch(JSONException e) {
			return ErrorJSON.serviceRefused("Error, Sorry!!", ErrorJSON.ERROR_JSON);
		}
	}
	
	public static JSONObject comment (String key, String idM, String content) {
		try{
			if(key==null || key.contentEquals("")) {
				return ErrorJSON.serviceRefused("informations manquantes", ErrorJSON.ERROR_ARG);
			}
			String login=UserTools.getLogin(AuthentificationTools.getID(key));

		//on vérifie si la session existe
		if(!AuthentificationTools.keyExists(key)) {
			Authentification.deleteConnexion(key);
			return ErrorJSON.serviceRefused("connectez-vous", ErrorJSON.ERROR_SQL);
		}
		if(!AuthentificationTools.checkKey(key)) {
			return ErrorJSON.serviceRefused("Session expirée", ErrorJSON.ERROR_SQL);
		}
		AuthentificationTools.updateDate(key);
		//here
		if(!MessageTools.addComment(idM, content, login)) {
			return ErrorJSON.serviceRefused("message non trouvé!!", ErrorJSON.ERROR_MONGO);
		}
		return ErrorJSON.serviceAccepted();
	}catch( SQLException e) {
		return ErrorJSON.serviceRefused("Operation refused", ErrorJSON.ERROR_SQL);
	}catch(JSONException e) {
		return ErrorJSON.serviceRefused("Error, Sorry!!", ErrorJSON.ERROR_JSON);
	}
	}
	
	// On n'a pas besoin de remove comment car c'est la même chose que delete message dans la conception de ma base de données sur mongoDB
}
