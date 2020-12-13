package test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import services.Authentification;
import services.Follow;
import tools.AuthentificationTools;
import tools.UserTools;

public class FollowTest {
	public static String key;
	@Test
	public static void testAdd() {
		try{
		//System.out.println(Authentification.insertConnexion("Test2", "1234"));
		key=AuthentificationTools.getKey(UserTools.getId("Test2"));
		System.out.println(key);
		System.out.println(Follow.addFollow(key,"Test2","Nono"));
		System.out.println(Follow.addFollow(key, "Test2","BOSS"));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}//OK
	@Test
	public static void testAddTarget() {
		System.out.println(Authentification.insertConnexion("BOSS", "5678"));
		try {
			
			key=AuthentificationTools.getKey(UserTools.getId("BOSS"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Follow.addFollow(key,"BOSS","Test2"));
		
	}//OK
	@Test
	public static void testGetFollow() {
	
			// doit retourner la liste des id des personnes auxquels Test2 est abonné
		JSONObject obj;
		try {
			obj = Follow.getListFollow(key, UserTools.getId("Test2"));
			System.out.println(obj);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}//OK
	@Test 
	public static void testGetFollowers() {
		JSONObject obj;
		try {
			obj = Follow.getFollowers(key, UserTools.getId("Test2"));
			System.out.println(obj);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//Ok
	@Test static void testDelete() {
		Follow.deleteFollow(key, "Test2", "1234");
	}//Ok
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testAdd();
		//testGetFollow();
		//testAddTarget();
		testDelete();
		//testGetFollowers();
		/*try {
			System.out.println(Authentification.insertConnexion("Test2", "1234"));
			key=AuthentificationTools.getKey(UserTools.getId("Test2"));
			System.out.println(Follow.deleteFollow(key, UserTools.getLogin(2), UserTools.getLogin(6)));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
	}
}