package cn.cqut.edu.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cqut.edu.bean.Post;
import cn.cqut.edu.service.CommentServise;
import cn.cqut.edu.service.PostServise;
import cn.cqut.edu.util.DateJsonValueProcessor;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class PostDetail extends HttpServlet {

	/**
	 * ���ڷ������ӵ������servlet
	 */
	private static final long serialVersionUID = 1L;

	PostServise p_servise = new PostServise();
	CommentServise c_servise = new CommentServise();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@SuppressWarnings("unused")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8"); // �޸ı��� �����������

		response.setContentType("application/json; charset=UTF-8");

		PrintWriter out = response.getWriter();

		// ���ڴ洢����jspҳ���������Ϣ ����mapװ����Ϊjson��
		Map<String, Object> map = new HashMap<String, Object>();

		// ����ĳ���ӵ������Ϣ ����post��sizeΪ1 ��Ϊ�����޶���id ����֮����һ����������
		List<Map<String, Object>> post = new ArrayList<Map<String, Object>>();
		// ����ĳ���ӵ���������
		List<Map<String, Object>> comments = new ArrayList<Map<String, Object>>();

		// ���ӵ�ID
		int postID = Integer.parseInt(request.getParameter("postID"));
		
		String actionType = request.getParameter("actionType");
		if(actionType != null) {
			post = p_servise.getPost(postID);
			map.put("state", "success");
			map.put("post", post);
		}else {
			// ����������ʾ�����ҳ��
			int page = Integer.parseInt(request.getParameter("pageNum"));
	
			// ���ӵ���Ϣ�б�
			post = p_servise.getPost(postID);
	
			// ���۰��ո�����ҳ�����ص�ָ��ҳ����������Ϣ
			comments = c_servise.getCommentByPostID(postID, page);
	
			// ����ָ���������۵���ҳ��
			int totalPages = c_servise.getTotalCommentPagesByPostId(postID);
	
			// ��ĳ���ӵķ�������1
			int result = p_servise.addPageView(postID);
	
			// �����۵����Ӵ���map
			map.put("post", post);
			// �����ӵ�ID����map
			map.put("postID", postID);
			// �����۵���ҳ������map
			map.put("pages", totalPages);
	
			// ����״̬ �������Ϊ�� result = false; �����û���jsp���ж� ����ͨ����ȡmap�е����۴�Сչʾ�Ƿ���������
			if (comments.size() > 0) {
				// �����ӵ����۴���map
				map.put("state", "success");
				map.put("comments", comments);
				// ��ǰҳ��
				map.put("page", page);
			} else {
				map.put("state", "false");
			}
		}
		// ��mapת��Ϊjson��ʽ ����jsp�е�js������ȡ����(ps:js�еķ�����ȡ�ĸ�ʽ����json��ʽ)
		//0516(���JSONObject ��������json���⣩
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Timestamp.class,new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
		
		JSONObject jsonMap =JSONObject.fromObject(map,config);
		 
		System.out.println("ddd:" + jsonMap.toString());
		out.println(jsonMap.toString());
		out.flush();
		out.close();
	}
}
