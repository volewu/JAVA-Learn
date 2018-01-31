package com.vole.web;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.vole.dao.GradeDao;
import com.vole.model.Grade;
import com.vole.model.PageBean;
import com.vole.util.DbUtil;
import com.vole.util.JsonUtil;
import com.vole.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * User: vole date: 2017年9月21日下午4:37:58 
 * Function: 班级 Servlet
 */
public class GradeListServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	DbUtil dbUtil = new DbUtil();
	GradeDao gradeDao = new GradeDao();

	@Override
	protected void doGet(HttpServletRequest requset, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(requset, response);
	}

	@Override
	protected void doPost(HttpServletRequest requset, HttpServletResponse response)
			throws ServletException, IOException {
		String page = requset.getParameter("page");
		String rows = requset.getParameter("rows");
		String gradeName= requset.getParameter("gradeName");
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		if(gradeName==null) gradeName="";
		Grade grade=new Grade();
		grade.setGradeName(gradeName);
		Connection con = null;
		try {
			con = dbUtil.getCon();
			JSONObject result = new JSONObject();
			int total = gradeDao.gradeCount(con,grade);
			JSONArray array = JsonUtil.formatRsToJsonArray(gradeDao.gradeList(con, pageBean,grade));
			result.put("rows", array);
			result.put("total", total);
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
