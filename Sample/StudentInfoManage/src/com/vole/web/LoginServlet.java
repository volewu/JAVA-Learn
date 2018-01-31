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
 * date: 2017年9月21日下午4:37:58
 * Function: 登入 Servlet
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
			requset.setAttribute("error", "用户名或者密码为空");
			requset.getRequestDispatcher("index.jsp").forward(requset, response);// 服务器跳转
			return;
		}
		
		Connection con =null;
		User user =new User(userName, password);
		try {
			con =dbUtil.getCon();
			User currentUser=userDao.getUser(con, user);
			if (currentUser!=null) {
				//把用户传入 session
				HttpSession session = requset.getSession();
				session.setAttribute("currenUser", currentUser);
				// 客户端跳转
				response.sendRedirect("main.jsp");
			}else{
				// 服务器跳转
				requset.setAttribute("error", "用户名或者密码错误");
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
