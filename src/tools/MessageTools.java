package tools;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import bd.DBStatic;
import bd.Database;
import services.Follow;


public class MessageTools {
	
	/**
	 * 
	 * @return listes de tous les messages déjà postés
	 * @throws JSONException 
	 */
	public static JSONObject getMessages() throws JSONException {
		JSONObject j=new JSONObject(); 
		MongoDatabase db = Database.getMongoDBConnection();
		MongoCollection<Document> mess = db.getCollection(DBStatic.MONGO_MESSAGE);
		Document query= new Document();
		query.append("isComment", false);
		MongoCursor<Document> curs = mess.find(query).iterator();
		LinkedList<Document> liste=new LinkedList<Document>();
		
		//créer le JSON
		
		while(curs.hasNext()) {
			liste.add(curs.next());
			//System.out.println(curs.next());
		}
		
			j.put("messages", liste);
		
			
		
		return j;
	}
	/**
	 * 
	 * @param login
	 * @return liste des messages postés par login
	 * @throws JSONException 
	 * @throws SQLException 
	 */
	public static JSONObject getMessage(String login) throws JSONException, SQLException {
		
			JSONObject j=new JSONObject(); 
		
			MongoDatabase db = Database.getMongoDBConnection();
			MongoCollection<Document> mess = db.getCollection(DBStatic.MONGO_MESSAGE);
			Document query = new Document();
			ArrayList<Document> conditions=new ArrayList<>();
			
			conditions.add(new Document().append("idU", UserTools.getId(login)));
			conditions.add(new Document().append("isComment", false));
			
			query.put("$and", conditions);
			
			MongoCursor<Document> curs = mess.find(query).iterator();
			ArrayList<Document> liste=new ArrayList<>();
			while(curs.hasNext()) {
				liste.add(curs.next());
				//System.out.println(curs.next());
			
			}
			j.put("messages "+login, liste);
			return j;

		
	}
	
	/**
	 * 
	 * @param login
	 * @return la liste des messages des utilisateurs auxquels on est abonnés
	 * @throws JSONException 
	 * @throws SQLException 
	 */
	public static JSONObject getMessagesFollowed(String login) throws SQLException, JSONException {
		JSONObject j=new JSONObject();
		JSONObject followed=FollowTools.getFollowed(login);
		List<Integer> followedList=new LinkedList<>();
		
		JSONArray list=(JSONArray) followed.get("liste utilisateurs");
		//Iterator<String> iterator=list.it ;
		for (int i=0; i<list.length(); ++i) {
			int id=list.getInt(i);
			followedList.add(id);
		}
		JSONArray finalArray=new JSONArray();
		for(int i=0; i<followedList.size(); i++) {
			//on récupère le login d'un follower
			String log=UserTools.getLogin(followedList.get(i));
			// on récupère ses messages 
			JSONArray array=(JSONArray) getMessage(log).get("messages "+log);
			// on le rajoute à la liste de tous les messages
			for(int k=0; k<array.length(); k++) {
				finalArray.put(array.get(k));
			}
			j.put("messages followed", finalArray);
		}
		return j;
	}
	/**
	 * 
	 * @param query condition qu'on veut verifier
	 * par exemple  par date d'apparition ou par nombre de j'aime
	 * @return retourne la liste des messages verifiant la condition
	 */
	public static JSONObject GetMessageQuery(String query) {
		JSONObject j=new JSONObject();
		
		return j;
	}
	/**
	 * 
	 * @param login
	 * @param message
	 * @return l'id du message crée
	 * @throws SQLException
	 */
	public static String addMessage(String login, String message) throws SQLException {
		MongoDatabase db = Database.getMongoDBConnection();
		MongoCollection<Document> mess = db.getCollection(DBStatic.MONGO_MESSAGE);
		Document query = new Document();
		//-----------------------
		query.append("idU", UserTools.getId(login));
		//----------------------------
		query.append("login", login);
		// je préfère mettre contenu à la place de message pour qu'il n'y ait pas de confondus avec le nom de la collection
		query.append("contenu", message);
		GregorianCalendar cal = new GregorianCalendar();
		Date date_actuelle = cal.getTime();
		query.append("date", date_actuelle);
		// Je préfère rajouter cette option, afin d'avoir plus de facilité à supprimer tous les commentaires aussi
		//lorsque je supprime un utilisateur, plutot que d'aller les chercher dans tous les posts existants
		query.append("isComment", false);
		//les likes sont formés par une lite de logins des utilisateurs qui ont liké le message
		LinkedList<String> likes=new LinkedList<>();
		
		query.append("likes",likes);
		LinkedList<String> list=new LinkedList<>();
		query.append("comments", list);
		mess.insertOne(query);
		
		ObjectId idM=(ObjectId) query.get("_id");
		return idM.toString();
	}


	/**
	 * Un message peut être supprimé par l'utilisateur qui l'a crée ou par un administrateur de l'application
	 * @param id_M identifiant du message
	 * @param id_U identifiant de l'utilisateur
	 * @return
	 */
	public static boolean deleteMessage(int id_U, String id_M) throws SQLException {
		JSONObject j=new JSONObject();
		MongoDatabase db = Database.getMongoDBConnection();
		MongoCollection<Document> mess = db.getCollection(DBStatic.MONGO_MESSAGE);
		Document query = new Document();
		
		query.append("_id", new ObjectId(id_M));
		MongoCursor<Document> curs = mess.find(query).iterator();
		
		// si le message existe bien 
		if(curs.hasNext()) {
			//supprimer d'abord les commentaires associés
			Document message=curs.next();
			LinkedList<String> comments=(LinkedList<String>)message.get("comments");
			for(String s: comments) {
				Document doc=new Document();
				doc.append("_id", new ObjectId(s));
				mess.deleteOne(doc);
			}
			if(UserTools.isAdmin(id_U) || id_U==Integer.parseInt(message.get("idU").toString())) {
				mess.deleteOne(query);
				return true;
			}
		}
		return false;
	}
	
	public static JSONObject searchMessages(String keyWords) throws JSONException {
		JSONObject j=new JSONObject();
		// obtenir la chaie en minuscule
		String min=keyWords.toLowerCase();
		StringBuilder s=new StringBuilder();
		s.append("(");
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
		s.append(")");
		
		MongoDatabase db = Database.getMongoDBConnection();
		MongoCollection<Document> mess = db.getCollection(DBStatic.MONGO_MESSAGE);
		Document query = new Document();
		
		// options de recherche
		Document options=new Document();
		options.put("$regex", s.toString());
		// i: insensitivity to match upper and lower cases 
		options.put("$options", "i");
		query.put("contenu", options);
		System.out.println(query);
		// find
		MongoCursor<Document> curs = mess.find(query).iterator();
		ArrayList<Document> liste=new ArrayList<>();
		while(curs.hasNext()) {
			liste.add(curs.next());
			//System.out.println(curs.next());
		
		}
		j.put("messages by keyWords", liste);
		
		
		return j;
	}
	/**
	 * retirer les commentaires d'une publication
	 * @param id_M
	 * @return
	 * @throws SQLException 
	 */
	public  static boolean addComment(String id_M, String content, String login) throws SQLException {
		MongoDatabase db = Database.getMongoDBConnection();
		MongoCollection<Document> mess = db.getCollection(DBStatic.MONGO_MESSAGE);
		Document query = new Document();
		
		query.append("_id", new ObjectId(id_M));
		MongoCursor<Document> curs = mess.find(query).iterator();
		if(!curs.hasNext()) {
			return false;
		}
		// sinon je récupère le message
		Document msg=curs.next();
		
		//Document comment=new Document();
		String s=addMessage(login, content);
		// modification du message pour dire que c'est un commentaire
		Document doc=new Document("_id", new ObjectId(s));
		MongoCursor<Document> curs1 = mess.find(doc).iterator();
		if(!curs1.hasNext()) {
			System.out.println("problème qui n'est pas censé arrivé après l'insertion");
			return false;
		}
		// On ajoute le commentaire dans la collection de message
		mess.updateOne(doc, new Document("$set", new Document("isComment", true)));
		// on ajoute la référence vers ce commentaire sur le message posté
		ArrayList<String> comments=(ArrayList<String>) msg.get("comments");
		comments.add(s);
		
		mess.updateOne(query, new Document("$set", new Document("comments", comments)));
		
		return false;
	}
	/**
	 * 
	 * @param idM identifiant du message
	 * @param login login du joueur qui va liker
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject addLike(String idM, String login) throws JSONException {
		// vérifier que le message qu'on veut liker existe déjà dans la base de donnée
		MongoDatabase db = Database.getMongoDBConnection();
		MongoCollection<Document> mess = db.getCollection(DBStatic.MONGO_MESSAGE);
		Document query = new Document();
		query.put("_id", new ObjectId(idM));
		MongoCursor<Document> curs = mess.find(query).iterator();
		if(!curs.hasNext()) {
			
			return ErrorJSON.serviceRefused("message non existant ", ErrorJSON.ERROR_MONGO);
		}
		ArrayList<String> likes=(ArrayList<String>) curs.next().get("likes");
		if(!likes.contains(login)) {
			likes.add(login);
			mess.updateOne(query, new Document("$set", new Document("likes", likes)));
		}
		return ErrorJSON.serviceAccepted() ;
	}
	public static JSONObject removeLike(String idM, String login) throws JSONException {
		// vérifier que le message qu'on veut liker existe déjà dans la base de donnée
				MongoDatabase db = Database.getMongoDBConnection();
				MongoCollection<Document> mess = db.getCollection(DBStatic.MONGO_MESSAGE);
				Document query = new Document();
				query.put("_id", new ObjectId(idM));
				MongoCursor<Document> curs = mess.find(query).iterator();
				if(!curs.hasNext()) {
					
					return ErrorJSON.serviceRefused("message non existant ", ErrorJSON.ERROR_MONGO);
				}
				ArrayList<String> likes=(ArrayList<String>) curs.next().get("likes");
				if(likes.contains(login)) {
					likes.remove(login);
					mess.updateOne(query, new Document("$set", new Document("likes", likes)));
				}
				return ErrorJSON.serviceAccepted() ;
		
	}
}
