package com.vole.entity;

import java.util.List;

/**
 * 自定义实体 cxf 能接受
 * @User: vole
 * @date: 2018年3月8日上午10:33:45
 * @Function:
 */
public class MyRole {

	private String key;
	private List<Role> value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<Role> getValue() {
		return value;
	}

	public void setValue(List<Role> value) {
		this.value = value;
	}

}
