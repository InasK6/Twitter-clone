package bd;

public class DBStatic {
	public static boolean pooling=false;
	public static String mysql_host="localhost";
	public static String mysql_db="Bracchi_Kaci";
	public static String mysql_user="nom_utilisateur_choisi";
	public static String mysql_password="mot_de_passe_solide";
	public static String mysql_port="3306";
	
	public static String USER_TABLE="utilisateur";
	public static String FOLLOW_TABLE="Follow";
	public static String SESSION_TABLE="session";
	
	public static String mongo_bd="KACI_BRACCHI";
	public static String MONGO_MESSAGE = "message";
	
	public static final int time_limit=3600*2; // 2 heures en secondes 
	
	public static boolean ADMIN_ROOT=true;
	public static boolean USER_ROOT=false;
	
	// code qui doit être fournit afin de pouvoir passer admin 
	// permet d'avoir les permissions sur les supressions d'utilisateurs et de messages
	public static final String ADMIN_CODE="OWNER_PROPERTY";
}
