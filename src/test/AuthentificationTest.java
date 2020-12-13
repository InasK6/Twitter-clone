package test;

import java.sql.SQLException;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import services.Authentification;
import tools.AuthentificationTools;
/**
 * 
 * @author inas
 * Test de toutes les méthodes présentes dans tools et service 
 * avant de faire le test sur servlet avec url
 */
public class AuthentificationTest {
	public static String key;
	@Test
	public static void addTest() {
		System.out.println(Authentification.insertConnexion("Lady","1234"));

	}//OK
	@Test public static void deleteTest() {
		System.out.println(Authentification.deleteConnexion(key));
	}
	@Test public static void keyGenTest() {
		try {
			System.out.println(AuthentificationTools.keyGenerator());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//OK
	@Test public static void checkKeyTest() {
		try {
			System.out.println(AuthentificationTools.checkKey(key));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//OK
	@Test public static void keyExistTest() {
		try {
			System.out.println(AuthentificationTools.keyExists(key));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//OK
	@Test public static void connectedTest() {
		try {
			System.out.println(AuthentificationTools.alreadyConnected(5));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//OK
	@Test public static void updateDate() {
		try {
			System.out.println(AuthentificationTools.updateDate(key));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//OK
	@Test public static void getKeyTest() {
		try {
			key=AuthentificationTools.getKey(5);
			System.out.println(key);
			//System.out.println(AuthentificationTools.checkKey("5c1feef989fc4fe491aa2c4f1801607e"));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//OK
	public static void getIDTest() {
		try {
			System.out.println(AuthentificationTools.getID(key));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		addTest();
		//getKeyTest();
		//keyExistTest();
		//checkKeyTest();
		//deleteTest();
		//keyGenTest() ;
		//connectedTest();
		//updateDate();
		//deleteTest() ;
		//getIDTest();
	}
}
