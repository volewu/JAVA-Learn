package com.vole.webservice.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import com.vole.entity.Role;
import com.vole.entity.User;
import com.vole.webservice.HelloWorld;

@WebService
public class HelloWorldImpl implements HelloWorld {

	public String say(String str) {
		return "Hello: " + str;
	}

	public List<Role> getRoleByUser(User user) {
		List<Role> roles = new ArrayList<Role>();
		// 模拟写死
		if (user != null) {
			if(user.getUserName().equals("gakki")&&user.getPassword().equals("123456")){
				roles.add(new Role(1, "wife"));
				roles.add(new Role(2, "lover"));
			}else if (user.getUserName().equals("vole")&&user.getPassword().equals("123456")) {
				roles.add(new Role(3, "程序员"));
			}
			return roles;
		} else
			return null;
	}

	public Map<String, List<Role>> getRole() {
		Map<String, List<Role>> map = new HashMap<String, List<Role>>();
		List<Role> roleList1 = new ArrayList<Role>();
		roleList1.add(new Role(1, "wife"));
		roleList1.add(new Role(2, "lover"));
		map.put("gakki", roleList1);
		
		List<Role> roleList2 = new ArrayList<Role>();
		roleList2.add(new Role(3, "程序员"));
		map.put("vole", roleList2);
		
		return map;
	}

}
