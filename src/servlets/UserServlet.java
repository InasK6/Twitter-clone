package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import services.User;

//import services.User;

/**
 * Servlet implementation class User
 */
//@WebServlet("/User")
public class UserServlet extends HttpServlet {
	//private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
			String login = request.getParameter("login");
			String key=request.getParameter("key");
			
			PrintWriter writer = response.getWriter();		
				writer.println(User.getUser(key,login));
			
			//response.setStatus(404);
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//defines which operation we want to make: update or create a new user
		String operation=request.getParameter("op");
		
		// Create a user
		String login=request.getParameter("login");
		String mdp=request.getParameter("mdp");
		String nom=request.getParameter("nomU");
		String prenom=request.getParameter("prenomU");
		String date=request.getParameter("dateNaiss");
		String mail=request.getParameter("mail");
		
		PrintWriter writer = response.getWriter();	
		if(operation==null || operation.equals("")) {
					
			
			writer.println("choose an operation!!!!");
		}
		if(operation.equals("create")) {
			writer.println(User.createUser(login, nom, prenom, mdp, date, mail));
		}
		
		if(operation.equals("update")) {
			String newlogin=request.getParameter("newlogin");
			String newmdp=request.getParameter("newmdp");
			String key=request.getParameter("key");
			writer.println(User.updateUser(key, login, nom, prenom, mdp, date, mail, newlogin, newmdp));
		}
		if(operation.equals("upgrade")) {
			String code=request.getParameter("code");
			writer.println(User.upgradeAdmin(login, code));
		}
		
	}
	@Override 
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		String login=req.getParameter("login");
		String mdp=req.getParameter("mdp");
		String key=req.getParameter("key");
		PrintWriter writer=resp.getWriter();
		
		writer.println(User.deleteUser(key,login, mdp));
	}

}
