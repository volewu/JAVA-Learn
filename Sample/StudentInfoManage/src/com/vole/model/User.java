package com.vole.model;


/**
 * 
 * User: vole
 * date: 2017��9��21������10:48:31
 * Function: �û���ʵ����
 */
public class User {

	private int id;
	private String userName;
	private String password;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
