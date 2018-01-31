package com.vole.web;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.vole.dao.GradeDao;
import com.vole.util.DbUtil;
import com.vole.util.JsonUtil;
import com.vole.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * User: vole date: 2017年9月21日下午4:37:58 
 * Function: 班级名称 Servlet
 */
public class GradeComboServlet extends HttpServlet {

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

		Connection con = null;
		try {
			con = dbUtil.getCon();
			JSONObject jsonObject = new JSONObject();
			JSONArray array = new JSONArray();
			jsonObject.put("id", "");
			jsonObject.put("gradeName", "请选择...");
			array.addAll(JsonUtil.formatRsToJsonArray(gradeDao.gradeList(con, null,null)));
			array.add(jsonObject);
			ResponseUtil.write(response, array);
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
