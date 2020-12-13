package test;

import java.sql.SQLException;

import org.junit.Test;

import services.Authentification;
import services.Messages;
import tools.AuthentificationTools;
import tools.MessageTools;
import tools.UserTools;

public class MessageTest {
	public static String key;
	@Test
	public static void addMessage() {
		// il faut se connecter 
		//Authentification.insertConnexion("Nono", "1234");
		//System.out.println(Messages.addMessage("Aznavour", "Rest in peace Idir"));
		//System.out.println(Messages.addMessage("BOSS", "leetCode is a good website for preparing technical interviews"));
		//System.out.println(Messages.addMessage("","Nono", "Match won today :D ! let's go to the national championship <3"));
		//System.out.println(Messages.addMessage("", "Test2", "Road to Annaba for the next match !"));
		
		try {
			key = AuthentificationTools.getKey(UserTools.getId("Nono"));
			System.out.println(Messages.addMessage(key,"Nono", "We won the national championship today"));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Test public static void getMessagesUser() {
		System.out.println("Les message de Aznavour");
		System.out.println(Messages.getMessages(key,"Aznavour", false ));
	}
	@Test
	public static void getMessagesFollowed() {
		System.out.println("Les messages des personnes que Test2 follow");
		System.out.println(Messages.getMessages(key, "Test2", true));
	}
	@Test public static void getAllMessages() {
		System.out.println("Tous les messages postés ");
		System.out.println(Messages.getMessages(key,"", false));
	}
	
	@Test public static void deleteMessage() {
		Messages.deleteMessage(key,  "5ed7c343d7461979b35739bc");
	}//OK
	@Test public static void like() {
		Messages.like("f941c071283f4d5d80ed9e7ad80c0875","5eda0b6986ea8409b2d70a4d");
	}
	@Test public static void dislike() {
		Messages.dislike("f941c071283f4d5d80ed9e7ad80c0875","5eda0b6986ea8409b2d70a4d");
	}
	@Test public static void addComment() {
		Messages.comment("f941c071283f4d5d80ed9e7ad80c0875", "5eda0b6986ea8409b2d70a4d", "super !! moi aussi <3");
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			/*addMessage();
			getMessagesUser();
			getMessagesFollowed();*/
			//key = AuthentificationTools.getKey(UserTools.getId("Nono"));
			key="65fa7a4a8a0448168659865f3a27a914";
			//getAllMessages();
			//deleteMessage();
			//UserTools.deleteAllUserMessages("hbibi");

			

			getAllMessages();
			//addComment();
			//getAllMessages();
			//UserTools.deleteAllUserMessages("Lady");
			//System.out.println(MessageTools.searchMessages("ya"));
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
