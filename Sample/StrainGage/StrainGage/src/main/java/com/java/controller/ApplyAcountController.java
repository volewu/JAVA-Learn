package com.java.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.service.ApplyAcountService;
import com.java.util.ResponseUtil;
import com.java.util.StringUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/apply")
public class ApplyAcountController {
	
	@Resource
	private ApplyAcountService applyAcountService;
	
	@RequestMapping("/applyAccount")
	public String ApplyAccount(@RequestParam(value="kjzgip",required=false)String kjzgip,@RequestParam(value="bjzgip",required=false)String bjzgip,HttpServletRequest request,HttpServletResponse response){
		
		String id=request.getParameter("appUserName");
		String userName=request.getParameter("appUserName2");
		String password=request.getParameter("appPassword");
		String email=request.getParameter("appEmail");
		String groupId=request.getParameter("groupId");

		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", id);
		map.put("userName", userName);
		map.put("password", password);
		map.put("email", email);
		map.put("groupId", groupId);
		
		map.put("kjzgip", kjzgip);
		map.put("bjzgip", bjzgip);
		int resultTotal=applyAcountService.addApplyAcount(map);
		
		JSONObject result=new JSONObject();
		if(resultTotal>0){
			result.put("success", true);
		}else{
			result.put("success", false);
		}
		try {
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 添加课级主管签核人
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addKjZg")
	public String addKjZg(HttpServletRequest request,HttpServletResponse response){
		String jobnumber=request.getParameter("jobnumber");
		String userName=request.getParameter("userName");
		String email=request.getParameter("email");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", jobnumber);  //工号
		map.put("userName", userName); //用户名
		map.put("email", email);  //邮箱
		map.put("password", 123); //密码
		map.put("groupId", "kjzg"); //密码
		JSONObject result=new JSONObject();
		int resultTotal=applyAcountService.addApplyAcount(map);
		if(resultTotal>0){
			result.put("success", true);
		}else{
			result.put("success", false);
		}
		try {
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	
	/**
	 * 添加部级主管签核人
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addBjZg")
	public String addBjZg(HttpServletRequest request,HttpServletResponse response){
		String jobnumber=request.getParameter("jobnumber");
		String userName=request.getParameter("userName");
		String email=request.getParameter("email");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", jobnumber);  //工号
		map.put("userName", userName); //用户名
		map.put("email", email);  //邮箱
		map.put("password", 123); //密码
		map.put("groupId", "bjzg"); //角色
		JSONObject result=new JSONObject();
		int resultTotal=applyAcountService.addApplyAcount(map);
		if(resultTotal>0){
			result.put("success", true);
		}else{
			result.put("success", false);
		}
		try {
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
}
