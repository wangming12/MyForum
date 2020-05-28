package cn.cqut.edu.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cqut.edu.service.CommentServise;

public class ReplyPost extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CommentServise servise = new CommentServise();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@SuppressWarnings("unused")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		request.setCharacterEncoding("UTF-8"); // �޸ı��� �����������
		
		String postID = request.getParameter("postID");
		String replyText = request.getParameter("replyText");
		String commentName = request.getParameter("commentName");
		String commentContent = request.getParameter("commentContent");
		
		String userID = String.valueOf("" +request.getSession().getAttribute("loginID"));
		
		String text = "�ظ� " + commentName + "  �� " + commentContent + " : " + replyText;
		
		System.out.println("reply:" + postID + ":" + replyText + ":"  + userID + ":"  + commentName);
		
		int res = servise.addCommentByPostID(Integer.parseInt(userID), Integer.parseInt(postID), text);
		
		request.getServletContext().getRequestDispatcher("/postDetail.jsp?postID=" + postID + "&pageNum=1")
		.forward(request, response);
	}
}
