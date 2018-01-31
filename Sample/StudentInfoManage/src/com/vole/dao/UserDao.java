package com.vole.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.vole.model.User;

/**
 * 
 * User: vole
 * date: 2017年9月21日上午10:57:34
 * Function: 用户名 Dao
 */
public class UserDao {
	
	public User getUser(Connection con,User user)throws Exception{
		User resultUser = null;
		String sql="select * from t_user where userName=? and password=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, user.getUserName());
		pstmt.setString(2, user.getPassword());
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()){
			resultUser = new User();
			resultUser.setUserName(rs.getString("userName"));
			resultUser.setPassword(rs.getString("password"));
		}
		return resultUser;
	}

}
