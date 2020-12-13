package services;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import Exceptions.MailException;
import tools.AuthentificationTools;
import tools.ErrorJSON;
import tools.UserTools;

/*
 * —  Code -1 erreur d’arguments passé au Web service 
 * (argument manquant, mauvais format, ...)
 * —  Code 100 erreur de JSON
 * —  Code 1000 erreur de SQL
 * —  Code 10000 erreur de JAVA
 */

public class User {
	public static JSONObject createUser(String login, String nom, String prenom, String mdp, String date, String mail )  {
		//verification des paramètres
		try{
			if( login==null || login.equals("") || nom==null || nom.equals("") || prenom==null || prenom.equals("") || mdp==null || mdp.equals("") ||date==null || date.equals("")  || mail==null  || mail.equals("") ) {
		
				return ErrorJSON.serviceRefused("informations incomplètes, veuillez indiquer les informations: login, nom, prenom, mot de passe, date de naissance et mail ", ErrorJSON.ERROR_ARG);
			}
			//Verifier si l'utilisateur existe déja
			
			int id=UserTools.getId(login); // retourne -1 s'il n'y a pas d'id correspondant 
			if(id!=-1) {
				return ErrorJSON.serviceRefused("Utilisateur déja existant", ErrorJSON.ERROR_ARG);
			}
			if(UserTools.mailExists(mail)) {
				return ErrorJSON.serviceRefused("Compte existant avec ce mail", ErrorJSON.ERROR_ARG);
			}
			UserTools.createUser(login, nom,prenom, mdp, date, mail);
			
			return ErrorJSON.serviceAccepted();
		}catch (SQLException e) {
			e.printStackTrace();
			return ErrorJSON.serviceRefused("can't create user! mail already exists", ErrorJSON.ERROR_SQL);
		}catch(JSONException j) {
			return ErrorJSON.serviceRefused("Error create user! Sorry", ErrorJSON.ERROR_JSON);
		}
	}
	
	
	
	// dans les fonctions suivantes, il faut verifié que l'utilisateur est bien connecté à chaque fois
	/**
	 * Avant de supprimer un utilisateur on demande à ce que l'utilisateur communique son mot de passe une autre fois comme confirmation
	 * L'utilisateur peut être supprimé par l'admin, soit par lui même quand il est déjà connecté
	 * @param login
	 * @param mdp
	 * @return
	 */
	
	public static JSONObject deleteUser(String key,String login, String mdp)  {
		//Vérification des paramètres
		//vérification que le mot de passe est correct
		try{
			if(key==null || key.equals("")) {
				
				return ErrorJSON.serviceRefused("informations manquantes ", ErrorJSON.ERROR_SQL);
		
			}
			if(login==null || login=="" || mdp==null || mdp=="" ) {
				
				return ErrorJSON.serviceRefused("login ou mot de passe incorrect", ErrorJSON.ERROR_ARG);
			}
			//tester que l'utilisateur est connecté 
			if(!AuthentificationTools.keyExists(key)) {
				return ErrorJSON.serviceRefused("Connectez-vous à votre compte pour pouvoir effectuer cette opération", ErrorJSON.ERROR_SQL);
			}
			if(!AuthentificationTools.checkKey(key)) {
				Authentification.deleteConnexion(key);
				return ErrorJSON.serviceRefused("Session expirée", ErrorJSON.ERROR_SQL);
			}
	
			int id=UserTools.getId(login);
			if(!UserTools.userExists(id)) {
				return ErrorJSON.serviceRefused("utilisateur non existant", ErrorJSON.ERROR_SQL);

			}
			if(!UserTools.checkPassword(login, mdp) ) {
				return ErrorJSON.serviceRefused("login ou mot de passe incorrect", ErrorJSON.ERROR_ARG);
			}
			int asker=AuthentificationTools.getID(key);
			if(UserTools.isAdmin(asker) || asker==id ) {
				UserTools.deleteUser(login);
			}
			else {
				return ErrorJSON.serviceRefused("Action non autorisée", ErrorJSON.ERROR_SQL);
			}
			AuthentificationTools.updateDate(key);
			return ErrorJSON.serviceAccepted();
		}catch (SQLException e) {
			return ErrorJSON.serviceRefused("can't delete user!", ErrorJSON.ERROR_SQL);
		}catch(JSONException j) {
			return ErrorJSON.serviceRefused("Error delete user! Sorry", ErrorJSON.ERROR_JSON);
		}

	}
	// pas besoin de confirmer son mot de passe lors de la modification d'informations sur l'utilisateur
	/**
	 * hypothése: l'utilisateur doit rentrer au moins un login et un mdp correcte
	 * @param login
	 * @param nom
	 * @param prenom
	 * @param mdp
	 * @param date
	 * @param mail
	 * @param newlogin nouveau login si on veut qu'il soit modifier
	 * @param newmdp  nouveau mdp si on veut qu'il soit modifié                                              
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	public static JSONObject updateUser(String key, String login, String nom, String prenom, String mdp, String date, String mail,String newlogin, String newmdp ) {
		//vérification qu'il s'est bien authentifié
		try{
    		if(key==null || key.equals("")) {
    			
			return ErrorJSON.serviceRefused("informations manquantes ", ErrorJSON.ERROR_SQL);
	
		}
			if(login==null || login=="" || mdp==null || mdp==""  ) {
		
				return ErrorJSON.serviceRefused("login ou mot de passe indisponible", ErrorJSON.ERROR_ARG);
			}

		//tester que l'utilisateur est connecté 
		if(!AuthentificationTools.keyExists(key)) {
			return ErrorJSON.serviceRefused("Connectez-vous à votre compte pour pouvoir effectuer cette opération", ErrorJSON.ERROR_SQL);
		}
		if(!AuthentificationTools.checkKey(key)) {
			Authentification.deleteConnexion(key);
			return ErrorJSON.serviceRefused("Session expirée", ErrorJSON.ERROR_SQL);
		}
		
			int id=UserTools.getId(login);
			if(!UserTools.userExists(id)) {
			
				return ErrorJSON.serviceRefused("L'utilisateur n'existe pas!", ErrorJSON.ERROR_SQL);
			}
			if(AuthentificationTools.getID(key) !=id) {
				return ErrorJSON.serviceRefused("Action non autorisée", ErrorJSON.ERROR_ARG);
			}
			try{
				UserTools.updateUser(login, nom, prenom, newmdp, date, mail, newlogin);
			}catch(MailException e) {
				return ErrorJSON.serviceRefused("mail already exists", ErrorJSON.ERROR_ARG);
			}
			AuthentificationTools.updateDate(key);
			return ErrorJSON.serviceAccepted();
		}catch (SQLException e) {
			return ErrorJSON.serviceRefused("can't update user!", ErrorJSON.ERROR_SQL);
		}catch(JSONException j) {
			return ErrorJSON.serviceRefused("Error update user! Sorry", ErrorJSON.ERROR_JSON);
		}
	}
	/**
	 * 
	 * @param login
	 * @return erreur si le service est refusé, la liste des utilisateurs de Birdy si login n'est pas spécifié, sinon le le nom de l'utilisateur login, la liste de ses poste
	 * et la liste de ses amis 
	 * 
	 */
	public static JSONObject getUser(String key, String login)  {
		//Si l'id n'est pas spécifié, on affiche tous les users présents sur Birdy
		try{
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
			if(login==null || login=="") {
				AuthentificationTools.updateDate(key);
				return UserTools.getUsers();
				
			}
			else {
				int id=UserTools.getId(login);
				if(!UserTools.userExists(id)) {
			
					return ErrorJSON.serviceRefused("L'utilisateur n'existe pas!", ErrorJSON.ERROR_SQL);
				}
				AuthentificationTools.updateDate(key);
				return UserTools.getUser(login);
			
			}
		}catch(JSONException j) {
			return ErrorJSON.serviceRefused("error on getting user! Sorry", ErrorJSON.ERROR_JSON);
		}catch( SQLException e) {
			return ErrorJSON.serviceRefused("can't get user!", ErrorJSON.ERROR_SQL);
		}
	}
	// Pas obligé d'être connecté pour faire l'upgrade du compte 
	public static JSONObject upgradeAdmin(String login, String code) {
		try{if(login ==null || login.equals("") || code==null || code.equals("")) {
			return ErrorJSON.serviceRefused(" Entrées incorrects", ErrorJSON.ERROR_ARG);
		}
		int id=UserTools.getId(login);
		UserTools.UpgradeAdmin(id, code);
		return ErrorJSON.serviceAccepted();
		}catch(SQLException e) {
			return ErrorJSON.serviceRefused("Error!", ErrorJSON.ERROR_SQL);
		}catch(JSONException e) {
			return ErrorJSON.serviceRefused("Sorry!", ErrorJSON.ERROR_JSON);
		}
	}
}