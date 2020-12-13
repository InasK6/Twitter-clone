package servlets;



import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import services.Follow;
import services.User;

public class FollowServlet extends HttpServlet {
	
	
	 public FollowServlet() {
	        super();
	        // TODO Auto-generated constructor stub
	    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			int id = Integer.parseInt(request.getParameter("id"));
			String key=request.getParameter("key");
			String op=request.getParameter("op");
			PrintWriter writer = response.getWriter();
			if(op==null || op.equals("")) {
				
				
				writer.println("choose an operation!!!!");
			}
			if(op.equals("follows")) {
				writer.println(Follow.getListFollow(key,id));
			}
			if(op.equals("followers")) {
				writer.println(Follow.getFollowers(key, id));
			}
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String follower = request.getParameter("follower");
		String followed = request.getParameter("followed");
		String key=request.getParameter("key");
		PrintWriter writer = response.getWriter();		

			writer.println(Follow.addFollow(key,follower, followed));

		
	}
	
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String follower = request.getParameter("follower");
		String followed = request.getParameter("followed");
		String key=request.getParameter("key");
		PrintWriter writer = response.getWriter();		

			writer.println(Follow.deleteFollow(key,follower, followed));

		
	}
}

