package tools;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * —  Code -1 erreur d’arguments passé au Web service 
 * (argument manquant, mauvais format, ...)
 * —  Code 100 erreur de JSON
 * —  Code 1000 erreur de SQL
 * —  Code 10000 erreur de JAVA
 */
public class ErrorJSON {
	
	public final static int ERROR_ARG=-1;
	public final static int ERROR_JSON=100;
	public final static int ERROR_SQL=1000;
	public final static int ERROR_JAVA=10000;
	public final static int ERROR_MONGO=50; 
	/**
	 * génére un JSON en cas d'erreur du service
	 * @param message message d'erreur
	 * @param codeErreur
	 * @return un JSON
	 * @throws JSONException
	 */
	public static JSONObject serviceRefused(String message, int codeErreur)  {
		//Création d'un JSONObject
		try{
			JSONObject j=new JSONObject();

			j.put("message", message);
			j.put("ErrorCode", new Integer(codeErreur));
			return j;
			
		}catch(JSONException e) {
			
			return serviceRefused("Erreur format json", 401);

		}

	}
	
	public static JSONObject serviceAccepted() throws JSONException {
		JSONObject j=new JSONObject();
		j.put("Message", "service accepté");
		return j;
	}
	
	
}
