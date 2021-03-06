package com.java.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.entity.PageBean;
import com.java.entity.StrainGage;
import com.java.entity.User;
import com.java.service.StrainGageService;
import com.java.service.UserService;
import com.java.util.ResponseUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 用户Controller
 * @author user
 *
 */
@Controller
@RequestMapping("/straingage")
public class StrainGageController {
	
	@Resource
	private UserService userService;
	
	@Resource
	private StrainGageService strainGageService;
	
	@Resource
	private RuntimeService runtimeService;
	
	@Resource
	private TaskService taskService;
	
	/**
	 * 添加申请
	 * @param request
	 * @param response
	 */
	@RequestMapping("/save")
	public void login(HttpServletRequest request,HttpServletResponse response) {
		String zg=request.getParameter("zg");
		String BU=request.getParameter("BU");
		String APPLYMAN=request.getParameter("APPLYMAN");
		String ICNUM=request.getParameter("ICNUM");
		String APPLYDATE=request.getParameter("APPLYDATE");
		String PHONE=request.getParameter("PHONE");
		String EXPENSESCODE=request.getParameter("EXPENSESCODE");
		String REASON=request.getParameter("REASON");
		String PROJECTNAME=request.getParameter("PROJECTNAME");
		String PROJECTNAME2=request.getParameter("PROJECTNAME2");
		String LOC1=request.getParameter("LOC1");
		String LOC2=request.getParameter("LOC2");
		String LOC3=request.getParameter("LOC3");
		String LOC4=request.getParameter("LOC4");
		String LOC5=request.getParameter("LOC5");
		String LOC6=request.getParameter("LOC6");
		String LOC7=request.getParameter("LOC7");
		String LOC8=request.getParameter("LOC8");
		String LOC9=request.getParameter("LOC9");
		String LOC10=request.getParameter("LOC10");
		String LOC11=request.getParameter("LOC11");
		String LOC12=request.getParameter("LOC12");
		String LOC13=request.getParameter("LOC13");
		String LOC14=request.getParameter("LOC14");
		String SENSORQUANTITY=request.getParameter("SENSORQUANTITY");
		String SENSORAMOUNT=request.getParameter("SENSORAMOUNT");
		String SURPORTQUANTITY=request.getParameter("SURPORTQUANTITY");
		String SURPORTAMOUNT=request.getParameter("SURPORTAMOUNT");
		String LOANQUANTITY=request.getParameter("LOANQUANTITY");
		String LOANAMOUNT=request.getParameter("LOANAMOUNT");
		String EMAIL=request.getParameter("EMAIL");
		String TOTALAMOUNT=request.getParameter("TOTALAMOUNT");
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("BU", BU);map.put("APPLYMAN", APPLYMAN);
		map.put("ICNUM", ICNUM);map.put("APPLYDATE", APPLYDATE);
		map.put("PHONE", PHONE);map.put("EXPENSESCODE", EXPENSESCODE);
		map.put("REASON", REASON);map.put("PROJECTNAME", PROJECTNAME);
		map.put("PROJECTNAME2", PROJECTNAME2);map.put("LOC1", LOC1);
		map.put("LOC2", LOC2);map.put("LOC3", LOC3);
		map.put("LOC4", LOC4);map.put("LOC5", LOC5);
		map.put("LOC6", LOC6);map.put("LOC7", LOC7);
		map.put("LOC8", LOC8);map.put("LOC9", LOC9);
		map.put("LOC10", LOC10);map.put("LOC11", LOC11);
		map.put("LOC12", LOC12);map.put("LOC13", LOC13);
		map.put("LOC14", LOC14);map.put("SENSORQUANTITY", SENSORQUANTITY);
		map.put("SENSORAMOUNT", SENSORAMOUNT);map.put("SURPORTQUANTITY", SURPORTQUANTITY);
		map.put("SURPORTAMOUNT", SURPORTAMOUNT);map.put("LOANQUANTITY", LOANQUANTITY);
		map.put("LOANAMOUNT", LOANAMOUNT);map.put("EMAIL", EMAIL);
		map.put("TOTALAMOUNT", TOTALAMOUNT);
		map.put("STATE", "未提交");
		map.put("PROCESSINSTANCEID", "0");
		map.put("ZG", zg);
		
		
		int resultNum=0;
		resultNum=strainGageService.add(map);
		
		JSONObject result=new JSONObject();
		if(resultNum>0){
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
	}
	
	/**
	 * 查询所有的申请列表
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/list")
	public String StrainGageList(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,HttpServletResponse response) throws Exception{
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());
		List<StrainGage> list=strainGageService.List(map);
		int total=strainGageService.getTotal();
		JSONObject result=new JSONObject();
		JSONArray jsonArray=JSONArray.fromObject(list);
		result.put("rows", jsonArray);
		result.put("total", total);
		ResponseUtil.write(response, result);
		return null;
	}
	
	
	/**
	 * 发起请假申请，修改请假单
	 * @param leaveId
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/startApply")
	public String startApply(Integer STRAINID,String ZG,HttpServletRequest request,HttpServletResponse response)throws Exception{
		
		String info=null;
		if (ZG.equals("true")) {
			info="重要";
		}else {
			info="一般";
		}
		Map<String,Object> variables=new HashMap<String,Object>();
		HttpSession session=request.getSession();
		//String applyId=(String) session.getAttribute("applyId");
		String rev=(String) session.getAttribute("rev");
		String applyId="";
		List<User> userGcs= userService.findGcsByUserId(rev);
		for (User user : userGcs) {
			applyId=applyId+","+user.getId();
		}
		String applyIds=applyId.replaceFirst("\\,","").trim();
		List<User> users= userService.findKjByUserId(rev);
		String userId="";
		for (User user : users) {
			userId=userId+","+user.getId();
		}
		String userIds=userId.replaceFirst("\\,","").trim();
		variables.put("STRAINID", STRAINID);
		variables.put("applyIds", applyIds);
		variables.put("userIds", userIds);
		variables.put("info", info);
		
		List<User> UserBjs= userService.findBjByUserId(rev);
		String bjId="";
		for (User user : UserBjs) {
			bjId=bjId+","+user.getId();
		}
		String bjIds=bjId.replaceFirst("\\,","").trim();
		variables.put("bjIds", bjIds);
		ProcessInstance pi= runtimeService.startProcessInstanceByKey("strainGageProcess",variables);  // 启动流程
		Task task=taskService.createTaskQuery().processInstanceId(pi.getProcessInstanceId()).singleResult(); // 根据流程实例Id查询任务
		taskService.complete(task.getId());  //任务ID
		
		
		StrainGage strainGage=strainGageService.findById(STRAINID);
		strainGage.setSTATE("审核中");
		strainGage.setPROCESSINSTANCEID(pi.getProcessInstanceId());
		Map<String, Object> map =new HashMap<String, Object>();
		strainGageService.updateStrainGage(strainGage,map); // 修改请假单状态
		JSONObject result=new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
	
	
	/**
	 * 根据Id获取,StrainGage申请单
	 * @param taskId
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getStrainGageByTaskId")
	public String GetStrainGageByTaskId(String taskId,HttpServletResponse response)throws Exception{
		Integer STRAINID=(Integer) taskService.getVariable(taskId, "STRAINID");
		StrainGage strainGage=strainGageService.findById(STRAINID);
		JSONObject result=new JSONObject();
		result.put("strainGage", JSONObject.fromObject(strainGage));
		ResponseUtil.write(response, result);
		return null;
		
	}
	
	/**
	 * 根据Id获取申请单详情
	 * @param STRAINID
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getStrainGageById")
	public String GetStrainGageById(String STRAINID,HttpServletResponse response)throws Exception{
		StrainGage strainGage=strainGageService.findById(Integer.parseInt(STRAINID));
		JSONObject result=new JSONObject();
		result.put("strainGage", JSONObject.fromObject(strainGage));
		ResponseUtil.write(response, result);
		return null;
		
	}
}
