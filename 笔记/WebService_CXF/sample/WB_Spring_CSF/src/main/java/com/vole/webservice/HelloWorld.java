package com.vole.webservice;

import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.vole.adapter.MapAdapter;
import com.vole.entity.Role;
import com.vole.entity.User;

@WebService
public interface HelloWorld {

	public String say(String str);
	
	public List<Role> getRoleByUser(User user);
	
	/**
	 * 获取用户相对应的所有角色
	 * @return
	 */
	@XmlJavaTypeAdapter(MapAdapter.class)
	public Map<String, List<Role>> getRole();
}
