package test;

import static org.junit.Assert.assertFalse;

import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import bd.DBStatic;
import services.User;
import tools.UserTools;

public class UserTest {
	/** looks for a user that doesn't exist
	 * 
	 */
	public static String key="";
	@Test
	public static void testGetUser1() {
		System.out.println(User.getUser(key,"!k-"));
	}
	/**
	 * looks for a user that does exist
	 */
	@Test public static void testGetUser2() {
		System.out.println(User.getUser(key,"Aznavour"));
	}
	/**
	 * Looks for all existing users
	 */
	@Test 
	public static void getAllUsers() {
		System.out.println(User.getUser(key,""));
	}
	
	@Test
	public static void getIdTest() {
		try {
			System.out.println(UserTools.getId("K6"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public static void getLoginTest() {
		try {
			System.out.println(UserTools.getLogin(3));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test 
	public static void isAdminTest() {
		try {
			System.out.println(UserTools.isAdmin(3));
			assertFalse(UserTools.isAdmin(3));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test 
	public static void setAdminTest() {
		try {
			System.out.println(UserTools.UpgradeAdmin(5, DBStatic.ADMIN_CODE));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test public static void  deleteTest() {
		
		System.out.println(User.deleteUser(key,"Test1", "1234"));
	}
	
	@Test public static void updateTest() {
		System.out.println(User.updateUser(key,"Test1", null, null, "5678", "1999-04-04", null,"BOSS", null));
		
	}
	
	@Test public static void createTest() {
		//System.out.println(User.createUser("Test1", "Dr", "eamer", "1234", "1999-04-04", "Dr@gmail.com"));
		//System.out.println(User.createUser("Test2", "lol", "mdr", "1234", "1999-04-04", "k6@gmail.com"));
		//System.out.println("aleez");
		System.out.println(User.createUser("Aznavour", "a", "a", "1234", "1997-02-01", "a"));
		System.out.println(User.createUser("Lady", "Kaci", "Inas", "1234", "1999-04-04", "OK@gmail.com"));
		
	}
	
	@Test
	public static void getByKeyWordsTest() {
		try {
			System.out.println(UserTools.getByKeyWords("KACI-NONO-Inas-Ines"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		
			//isAdminTest();
			//getByKeyWordsTest() ;
			//setAdminTest();
			createTest();
			

	}

}