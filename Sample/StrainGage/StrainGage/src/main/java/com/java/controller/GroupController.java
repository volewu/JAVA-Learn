package com.java.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.entity.Group;
import com.java.entity.PageBean;
import com.java.service.GroupService;
import com.java.util.ResponseUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 角色Controller
 * @author group
 *
 */
@Controller
@RequestMapping("/group")
public class GroupController {

	@Resource
	private GroupService groupService;
	
	//private IdentityService identityService;
	/**
	 * 查询所有角色  下拉框用到
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/groupComboList")
	public String groupComboList(HttpServletResponse response)throws Exception{
		JSONArray jsonArray=new JSONArray();
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("id", -1);
		jsonObject.put("name", "请选择角色...");
		jsonArray.add(jsonObject);
		List<Group> groupList=groupService.findAll();
		for (int i = 0; i <4; i++) {
			
			groupList.remove(groupList.size()-1);
		}
		
		JSONArray rows=JSONArray.fromObject(groupList);
		jsonArray.addAll(rows);
		ResponseUtil.write(response, jsonArray);
		return null;
	}
	
	
	/**
	 * 根据条件分页查询角色集合
	 * @param page
	 * @param rows
	 * @param s_group
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public String list(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,HttpServletResponse response)throws Exception{
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());
		List<Group> groupList=groupService.find(map);
		Long total=groupService.getTotal(map);
		JSONObject result=new JSONObject();
		JSONArray jsonArray=JSONArray.fromObject(groupList);
		result.put("rows", jsonArray);
		result.put("total", total);
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 删除角色
	 * @param ids
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public String delete(@RequestParam(value="ids",required=false)String ids,HttpServletResponse response)throws Exception{
		String []idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++){
			groupService.delete(idsStr[i]);
		}
		JSONObject result=new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 添加或者修改角色
	 * @param group
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public String save(Group group,HttpServletResponse response,Integer flag)throws Exception{
		int resultTotal=0;
		if(flag==1){
			
			/*org.activiti.engine.identity.Group group1=new GroupEntity();
			group1.setId(group.getId());
			group1.setName(group.getName());
			identityService.saveGroup(group1);
			resultTotal=1;*/
			resultTotal=groupService.add(group);
		}else{
			resultTotal=groupService.update(group);
		}
		JSONObject result=new JSONObject();
		if(resultTotal>0){
			result.put("success", true);
		}else{
			result.put("success", false);
		}
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 判断是否存在指定角色名
	 * @param groupName
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/existGroupName")
	public String existGroupName(String groupName,HttpServletResponse response)throws Exception{
		JSONObject result=new JSONObject();
		if(groupService.findById(groupName)==null){
			result.put("exist", false);
		}else{
			result.put("exist", true);
		}
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 查询所有角色
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listAllGroups")
	public String listAllGroups(HttpServletResponse response)throws Exception{
		List<Group> groupList=groupService.findAll();
		JSONObject result=new JSONObject();
		JSONArray jsonArray=JSONArray.fromObject(groupList);
		result.put("groupList", jsonArray);
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 通过用户Id查询角色集合 多个角色逗号隔开
	 * @param userId
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/findGroupByUserId")
	public String findGroupByUserId(String userId,HttpServletResponse response)throws Exception{
		List<Group> groupList=groupService.findByUserId(userId);
		StringBuffer groups=new StringBuffer();
		for(Group g:groupList){
			groups.append(g.getId()+",");
		}
		JSONObject result=new JSONObject();
		if(groups.length()>0){
			result.put("groups", groups.deleteCharAt(groups.length()-1).toString());
		}else{
			result.put("groups",groups.toString());
		}
		ResponseUtil.write(response, result);
		return null;
	}
}
