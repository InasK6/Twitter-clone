package tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import Exceptions.MailException;
import bd.DBStatic;
import bd.Database;

public class UserTools {
	/**
	 * 
	 * @param id identifiant de l'utilisateur 
	 * @return
	 * @throws SQLException
	 */
	public static boolean userExists(int id) throws SQLException{
		Connection conn=Database.getMySQLConnection();
		Statement st=conn.createStatement();
		
		String Query="Select * from "+DBStatic.USER_TABLE+" where idU="+id;
		ResultSet res=st.executeQuery(Query);
		boolean b=res.next();
		
		st.close();
		conn.close();
		return b;
		
	}
	/**
	 * 
	 * @param login nom de l'utilisateur 
	 * @return
	 * @throws SQLException
	 */
	public static boolean userExists(String login) throws SQLException{
		Connection conn=Database.getMySQLConnection();
		Statement st=conn.createStatement();
		
		String Query="Select * from "+DBStatic.USER_TABLE+" where login='"+login+"'";
		ResultSet res=st.executeQuery(Query);
		boolean b=res.next();
		
		st.close();
		conn.close();
		return b;
		
	}
	
	public static void createUser(String login, String nom, String prenom, String mdp, String date, String mail) throws SQLException {
		Connection conn=Database.getMySQLConnection();
		Statement st=conn.createStatement();
		
		String Query="insert into "+DBStatic.USER_TABLE+" values ( NULL, "+"'"+login+"'"+" , PASSWORD('"+mdp+"') ,'"+nom+"' , '"+prenom+"' ,TIMESTAMP('"+date+"') ,'"+mail+"' , "+DBStatic.USER_ROOT+" )";
		int res=st.executeUpdate(Query);
		
		st.close();
		conn.close();
	}
	
	/**
	 * delete all messsages created by login used as a tool when deleting a user, never called by the client 
	 * @param login
	 * @return true if the action succeeds
	 */
	private static boolean deleteAllUserMessages(String login) {
		JSONObject j=new JSONObject();
		MongoDatabase db = Database.getMongoDBConnection();
		MongoCollection<Document> mess = db.getCollection(DBStatic.MONGO_MESSAGE);
		Document query = new Document();
		
		query.append("login", login);
		MongoCursor<Document> curs = mess.find(query).iterator();
		
		// si le message existe bien 
		while(curs.hasNext()) {
			Document message=curs.next();
			mess.deleteOne(query);
				
		}
		return true;
	}
	public static void deleteUser(String login) throws SQLException {
		Connection conn=Database.getMySQLConnection();
		Statement st=conn.createStatement();

		// on doit supprimer tous les messages de login
		deleteAllUserMessages(login);
		
		String Query="delete from "+DBStatic.USER_TABLE+" where login= '"+login+"' ";
		int res=st.executeUpdate(Query);
		
		st.close();
		conn.close();
	}
	/**
	 * retourne la liste des logins de tous les utilisateur sous forme de JSONObject
	 * @throws JSONException 
	 * @throws SQLException 
	 */
	public static JSONObject getUsers() throws JSONException, SQLException {
		Connection conn=Database.getMySQLConnection();
		Statement st=conn.createStatement();
		
		String Query="select login from "+DBStatic.USER_TABLE;
		ResultSet res=st.executeQuery(Query);
		List<String > listeUtilisateurs=new ArrayList<String>();
		
		List<String> utilisateurs=new ArrayList<String>();
		while(res.next()) {
			String s=res.getString("login");
			utilisateurs.add(s);
		}
		
		JSONObject j=new JSONObject();
		j.put("liste utilisateurs", utilisateurs);
		
		st.close();
		conn.close();
		
		
		return j;
		
	}
	/**
	 * 
	 * @return JSONObject avec l'id du user, son nom, son prénom,  ses messages postés et sa liste d'amis 
	 * @throws JSONException 
	 * @throws SQLException 
	 */
	// Je ne peux pas encore faire la partie message car elle est en mangoDB
	// je suppose qu'il y a une méthode dans FollowTools qui retourne la liste d'amis d'un utilisateur en JSONObject
	public static JSONObject getUser(String login) throws JSONException, SQLException {
		JSONObject j=new JSONObject();
		
		j.put("login", login);
		//récuperation des noms et prénoms
		Connection conn=Database.getMySQLConnection();
		Statement st=conn.createStatement();
		
		String Query="select nomU, prenomU from "+DBStatic.USER_TABLE+" where login='"+login+"'";
		ResultSet res=st.executeQuery(Query);
		
		while(res.next()) {
			String s=res.getString("nomU");
			j.put("Nom", s);
			s=res.getString("prenomU");
			j.put("Prenom", s);
			
		}
		JSONObject j2=FollowTools.getFollowed(login);
		j.put("followed ", j2);
		
		JSONObject j3=MessageTools.getMessage(login);
		j.put("messages:", j3);
		return j;
	}

	/**
	 * private function only called on cascade when modifing users 
	 */
	private static void updateMessagesLogin(String login,String newlogin) {
		JSONObject j=new JSONObject();
		MongoDatabase db = Database.getMongoDBConnection();
		MongoCollection<Document> mess = db.getCollection(DBStatic.MONGO_MESSAGE);
		Document query = new Document();
		
		query.append("login", login);
		MongoCursor<Document> curs = mess.find(query).iterator();
		
		// si le message existe bien 
		while(curs.hasNext()) {
			Document message=curs.next();
			mess.updateOne(query, new Document("$set", new Document("login",newlogin)));
				
		}
	}
	//Je n'ai pas trouvé le moyen de faire une requête qui séléctionne automatiquement les attributs non nuls pour les modifier
	public static void updateUser(String login, String nom, String prenom, String mdp, String date, String mail, String newlogin) throws SQLException, MailException{
		Connection conn=Database.getMySQLConnection();
		Statement st=conn.createStatement();
		//mise à jour du login
		if(newlogin!=null && !newlogin.equals("")) {
			String Query="update "+DBStatic.USER_TABLE+" set login='"+newlogin+"' where login= '"+login+"' ;";
			int res=st.executeUpdate(Query);
			updateMessagesLogin(login, newlogin);
		}
		// mise à jour du nom
		if(nom!=null && !nom.equals("")) {
			String Query="update "+DBStatic.USER_TABLE+" set nomU='"+nom+"' where login= '"+login+"' ;";
			int res=st.executeUpdate(Query);
		}
		
		//mise à jour du prénom
		if(prenom!=null && !prenom.equals("")) {
			String Query="update "+DBStatic.USER_TABLE+" set prenomU='"+prenom+"' where login= '"+login+"' ;";
			int res=st.executeUpdate(Query);
		}
		// mise à jour du mot de passe
		if(mdp!=null && !mdp.equals("")) {
			String Query="update "+DBStatic.USER_TABLE+" set mdp=PASSWORD('"+mdp+"') where login= '"+login+"' ;";
			int res=st.executeUpdate(Query);
		}
		//mise à jour de la date de naissance
		if(date!=null && !date.equals("")) {
			String Query="update "+DBStatic.USER_TABLE+" set dateNaiss=TIMESTAMP('"+date+"') where login= '"+login+"' ;";
			int res=st.executeUpdate(Query);
		}
		if(mail!=null && !mail.equals("")) {
			if(mailExists(mail)) {
				throw new MailException("un utilisateur existe déjà avec ce mail");
			}
			String Query="update "+DBStatic.USER_TABLE+" set mail='"+mail+"'where login= '"+login+"' ;";
			int res=st.executeUpdate(Query);
		}
		st.close();
		conn.close();
	}
	

	/**
	 * 
	 * @param login
	 * @return récupérer l'ID en connaissant le login qui est unique dans notre base de données
	 * @throws SQLException
	 */
	public static int getId(String login) throws SQLException {
		Connection conn=Database.getMySQLConnection();
		Statement st=conn.createStatement();
		int id=-1;
		String Query="select idU from "+DBStatic.USER_TABLE+" where login='"+login+"';";
		ResultSet res=st.executeQuery(Query);
		if(res.next()) {
			id=res.getInt("idU");
		}
		
		st.close();
		conn.close();
		return  id;
	}
	
	public static String getLogin(int id) throws SQLException{
		String s="";
		Connection conn=Database.getMySQLConnection();
		Statement st=conn.createStatement();
		
		String Query="select login from "+DBStatic.USER_TABLE+" where idU="+id+";";
		ResultSet res=st.executeQuery(Query);
		if(res.next()) {
			s=res.getString("login");
		}
		
		st.close();
		conn.close();
		
		
		return s;
	}
	/**
	 * 
	 * @param login
	 * @param password
	 * @return true si le mot de passe est correct
	 * @throws SQLException
	 */
	public static boolean checkPassword(String login, String password) throws SQLException {
		boolean b=false;
		Connection conn=Database.getMySQLConnection();
		Statement st=conn.createStatement();
		
		String Query="select * from "+DBStatic.USER_TABLE+" where login='"+login+"' AND mdp=PASSWORD('"+password+"')";
		ResultSet res=st.executeQuery(Query);
		if(res.next()) {
			b=true;
		}
		st.close();
		conn.close();
		return b;
	}
	/**
	 * 
	 * @param id
	 * @return true si l'utilisateur est admi, si root=true
	 */
	public static boolean isAdmin(int id) throws SQLException{
		boolean b=false;
		Connection conn=Database.getMySQLConnection();
		Statement st=conn.createStatement();
		String Query="select root from "+DBStatic.USER_TABLE+" where idU='"+id+"';";
		ResultSet res=st.executeQuery(Query);
		if(res.next()) {
			b=res.getBoolean("root");
		}
		st.close();
		conn.close();
		return b;
	}
	/**
	 * upgrade the user into admin to give him access to deleting messages or users 
	 * @param id
	 * @param code
	 * @return true si l'opération d'upgrade a réussit
	 */
	public static boolean UpgradeAdmin(int id, String code) throws SQLException{
		if(!code.equals(DBStatic.ADMIN_CODE)) {
			return false;
		}
		Connection conn=Database.getMySQLConnection();
		Statement st=conn.createStatement();
		
		String Query="update "+DBStatic.USER_TABLE+" set root="+DBStatic.ADMIN_ROOT+" where idU= "+id+" ;";
		int res=st.executeUpdate(Query);
		
		st.close();
		conn.close();
		return isAdmin(id);
	}
	/**
	 * checks if a user has already created an account using this mail adress 
	 * @param mail
	 * @return
	 * @throws SQLException 
	 */
	public static boolean mailExists(String mail) throws SQLException {
		Connection conn=Database.getMySQLConnection();
		Statement st=conn.createStatement();
		
		String Query="Select * from "+DBStatic.USER_TABLE+" where mail='"+mail+"'";
		ResultSet res=st.executeQuery(Query);
		boolean b=res.next();
		
		st.close();
		conn.close();
		return b;
	}
	/**
	 * La mot clé recherché peut être soit dans le prenom, soit dans le nom soit dans le login
	 * La fonction retourne les login nom et prénom qui satisfont la recherche
	 * après l'appel de cette fonction, l'utilisateur peut séléctionner un login pour afficher toutes les informations dont il a besoin sur l'utilisateur voulu
	 * donc à ce moment là l'action d'appel de getUser(login) pour a démarrer, mais elle vient en deuxième étape de cette fonction
	 * celle-ci n'est pas destiné à afficher tous les profils des utilisateurs 
	 * @param keyWords chaine de caractères contenant des mots séparés par des -
	 * @return un JSONObject cotenant une liste des utilisateurs qui sont identifiés par un des mots clés donnés en paramètres
	 * @throws SQLException 
	 * @throws JSONException 
	 */
	public static JSONObject getByKeyWords(String keyWords) throws SQLException, JSONException {
		JSONObject j=new JSONObject();
		// obtenir la chaie en minuscule
		String min=keyWords.toLowerCase();
		StringBuilder s=new StringBuilder();
		s.append("'(");
	//  séparer en plusieurs mots séparés par un tiret -
			String[] words=min.split("-");
		for(int i=0; i<words.length; i++) {
			if(!words[i].equals("")) {
				s.append(words[i]);
				if( i<words.length-1) {
					s.append("|");
				}
			}
		}
		s.append(")'");
		
		
		//REGEXP permet une selection multiple, x REGEXP (a|b) est équivalent à x=a or x=b
		// ? sert de paramètre dans la requête
		
		//String Query="Select login, nomU, prenomU from "+DBStatic.USER_TABLE+" where (LOWER(login) REGEXP ?  ) OR (LOWER(nomU) REGEXP ? ) OR (LOWER(prenomU) REGEXP ? )" ;
		String Query="Select login, nomU, prenomU from "+DBStatic.USER_TABLE+" where (LOWER(login) REGEXP "+s.toString()+"  ) OR (LOWER(nomU) REGEXP "+s.toString()+" ) OR (LOWER(prenomU) REGEXP "+s.toString()+" )" ;
		
		Connection conn=Database.getMySQLConnection();
		/*PreparedStatement st=conn.prepareStatement(Query);
		st.setString(1, s.toString());
		st.setString(2, s.toString());
		st.setString(3, s.toString());
		System.out.println(Query);*/
		Statement st=conn.createStatement();
		ResultSet res=st.executeQuery(Query);
		//ResultSet res=st.executeQuery();
		List<JSONObject> utilisateurs=new ArrayList<>();
		
		  while (res.next()) {
			  
                JSONObject o = new JSONObject();
                o.put("login", res.getString("login"));
                o.put("nomU", res.getString("nomU"));
                o.put("prenomU", res.getString("prenomU"));

                utilisateurs.add(o);
           }


		  j.put("liste utilisateurs", utilisateurs);

		
		st.close();
		conn.close();
		
		return j;
		
	}
}
