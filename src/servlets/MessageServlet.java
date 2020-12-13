package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.Messages;
import services.User;

/**
 * Servlet implementation class MessageServlet
 */
@WebServlet("/MessageServlet")
public class MessageServlet extends HttpServlet {
	//private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MessageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String login=request.getParameter("login");
		String followed=request.getParameter("followed");
		Boolean b=Boolean.parseBoolean(followed);
		String key=request.getParameter("key");
		PrintWriter writer = response.getWriter();	
		writer.println("b="+b);
		writer.println(Messages.getMessages(key,login, b));
		
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login=request.getParameter("login");
		String message=request.getParameter("message");
		String key=request.getParameter("key");
		String op=request.getParameter("op");
		//String login=request.getParameter("login");
		PrintWriter writer = response.getWriter();	
		//par défaut, doPost sert à ajouter des messages  
		if(op==null || op.equals("")) {
			writer.println(Messages.addMessage(key,login, message));
		}
		String idM=request.getParameter("idM");
		if(op.equals("like")) {
			Messages.like(key, idM);
		}
		if(op.equals("dislike")) {
			Messages.dislike(key, idM);
		}
		if(op.equals("comment")) {
			String content=request.getParameter("content");
			Messages.comment(key, idM, content);
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String key=request.getParameter("key");
		String idM=request.getParameter("IdM");
		
		PrintWriter writer = response.getWriter();		
		writer.println(Messages.deleteMessage( key,  idM ));
		
		
	}

}
