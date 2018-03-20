package com.java.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.java.entity.Group;
import com.java.entity.MemberShip;
import com.java.entity.User;
import com.java.service.MemberShipService;
import com.java.util.ResponseUtil;
import com.java.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * 用户角色关系Controller
 * @author 
 *
 */
@Controller
@RequestMapping("/memberShip")
public class MemberShipController {

	@Resource
	private MemberShipService memberShipService;
	
	/**
	 * 更新用户权限 先删除 后批量添加
	 * @param userId
	 * @param groupsIds
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/update")
	public String update(String userId,String groupsIds,HttpServletResponse response)throws Exception{
		memberShipService.deleteAllGroupsByUserId(userId);
		if(StringUtil.isNotEmpty(groupsIds)){
			String idsArr[]=groupsIds.split(",");
			for(String groupId:idsArr){
				User user=new User();user.setId(userId);
				Group group=new Group();group.setId(groupId);
				MemberShip memberShip=new MemberShip();
				memberShip.setUser(user);
				memberShip.setGroup(group);
				memberShipService.add(memberShip);
			}
		}
		JSONObject result=new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
	
	
}
