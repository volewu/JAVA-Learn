package com.vole.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * 
 * User: vole date: 2017��9��21������5:52:32 Function: response ��ҳ����д������
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
