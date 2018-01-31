package com.vole.util;


import java.sql.Connection;
import java.sql.DriverManager;


/**
 * 
 * User: vole
 * date: 2017年9月21日上午10:42:01
 * Function: JDBC 工具类
 */
public class DbUtil {
	
	private String dbUrl="jdbc:mysql://localhost:3306/db_studentinfo";
	private String dbUser="root";
	private String dbPassword="123456";
	private String jdbcName="com.mysql.jdbc.Driver";
	
	public Connection getCon()throws Exception{
		Class.forName(jdbcName);
		return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
	}
	
	public void closeCon(Connection con)throws Exception{
		if(con!=null)
			con.close();
	}
	
	public static void main(String[] args) {
		DbUtil dbUtil = new DbUtil();
		try {
			dbUtil.getCon();
			System.out.println("success");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
