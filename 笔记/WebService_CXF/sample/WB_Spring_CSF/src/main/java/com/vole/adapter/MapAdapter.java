package com.vole.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.vole.entity.MyRole;
import com.vole.entity.Role;

public class MapAdapter extends XmlAdapter<MyRole[], Map<String, List<Role>>> {

	/**
	 * ÊÊÅä×ª»» MyRole[] -> Map<String, List<Role>>
	 */
	@Override
	public Map<String, List<Role>> unmarshal(MyRole[] v) throws Exception {
		Map<String, List<Role>> map = new HashMap<String, List<Role>>();
		for (int i = 0; i < v.length; i++) {
			MyRole r = v[i];
			map.put(r.getKey(), r.getValue());
		}
		return map;
	}

	/**
	 * ÊÊÅä×ª»» Map<String, List<Role>> -> MyRole[]
	 */
	@Override
	public MyRole[] marshal(Map<String, List<Role>> v) throws Exception {
		MyRole[] myRoles = new MyRole[v.size()];
		int i = 0;
		for (String key : v.keySet()) {
			myRoles[i] = new MyRole();
			myRoles[i].setKey(key);
			myRoles[i].setValue(v.get(key));
			i++;
		}
		return myRoles;
	}

}
