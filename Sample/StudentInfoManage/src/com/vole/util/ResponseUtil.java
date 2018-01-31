package com.vole.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * 
 * User: vole date: 2017年9月21日下午5:52:32 Function: response 向页面书写工具类
 */
public class ResponseUtil {

	public static void write(HttpServletResponse response, Object o) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write(o.toString());
		out.flush();
		out.close();

	}
}
