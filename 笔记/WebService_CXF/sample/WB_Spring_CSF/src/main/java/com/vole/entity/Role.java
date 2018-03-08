package com.vole.entity;

public class Role {

	private Integer id;
	private String roleName;
	
	public Role(Integer id, String roleName) {
		super();
		this.id = id;
		this.roleName = roleName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Role() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	@Override
	public String toString() {
		return "Role [id=" + id + ", roleName=" + roleName + "]";
	}
	
	

}
