package com.java.entity;

/**
 * ��ɫʵ����չ
 * @author user
 *
 */
public class Group {

	private String id; // ���� ��ɫ��
	private String name; // ����
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Group [id=" + id + ", name=" + name + "]";
	}
	
	
}
