package com.vole.web;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.vole.dao.GradeDao;
import com.vole.model.Grade;
import com.vole.util.DbUtil;
import com.vole.util.ResponseUtil;
import com.vole.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * 
 * @User: vole
 * @date: 2017年9月22日下午3:43:01
 * @Function:班级 Save servlet
 */
public class GradeSaveServlet extends HttpServlet {

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
		requset.setCharacterEncoding("utf-8");
		String gradeName = requset.getParameter("gradeName");
		String gradeDesc = requset.getParameter("gradeDesc");
		String id = requset.getParameter("id");
		Grade grade = new Grade(gradeName, gradeDesc);

		Connection con = null;
		try {
			int save = 0;
			con = dbUtil.getCon();
			JSONObject result = new JSONObject();

			if(StringUtil.isNotEmpty(id)){
				grade.setId(Integer.parseInt(id));
				save = gradeDao.gradeModify(con, grade);				
			}else{
				save = gradeDao.gradeAdd(con, grade);				
			}
			if (save > 0) {
				result.put("success", "true");
			} else {
				result.put("success", "true");
				result.put("errorMsg", "删除失败");
			}
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
