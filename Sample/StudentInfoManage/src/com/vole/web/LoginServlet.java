package com.vole.web;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vole.dao.UserDao;
import com.vole.model.User;
import com.vole.util.DbUtil;
import com.vole.util.StringUtil;

/**
 * 
 * User: vole
 * date: 2017��9��21������4:37:58
 * Function: ���� Servlet
 */
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	DbUtil dbUtil=new DbUtil();
	UserDao userDao = new UserDao();

	@Override
	protected void doGet(HttpServletRequest requset, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(requset, response);
	}

	@Override
	protected void doPost(HttpServletRequest requset, HttpServletResponse response)
			throws ServletException, IOException {
		String userName =requset.getParameter("userName");
		String password =requset.getParameter("password");
		System.out.println(userName+"  "+password);
		requset.setAttribute("userName", userName);
		requset.setAttribute("password", password);
		
		if(StringUtil.isEmpty(userName)||StringUtil.isEmpty(password)){
			requset.setAttribute("error", "�û�����������Ϊ��");
			requset.getRequestDispatcher("index.jsp").forward(requset, response);// ��������ת
			return;
		}
		
		Connection con =null;
		User user =new User(userName, password);
		try {
			con =dbUtil.getCon();
			User currentUser=userDao.getUser(con, user);
			if (currentUser!=null) {
				//���û����� session
				HttpSession session = requset.getSession();
				session.setAttribute("currenUser", currentUser);
				// �ͻ�����ת
				response.sendRedirect("main.jsp");
			}else{
				// ��������ת
				requset.setAttribute("error", "�û��������������");
				requset.getRequestDispatcher("index.jsp").forward(requset, response);
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
