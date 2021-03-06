package com.java.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.java.entity.Group;
import com.java.entity.MemberShip;
import com.java.entity.MyTask;
import com.java.entity.PageBean;
import com.java.entity.StrainGage;
import com.java.entity.User;
import com.java.service.StrainGageService;
import com.java.util.ResponseUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 任务Controller层
 * @author user
 *
 */
@Controller
@RequestMapping("/task")
public class TaskController {

	@Resource
	private TaskService taskService;
	
	@Resource
	private RepositoryService repositoryService;
	
	@Resource
	private RuntimeService runtimeService;
	
	@Resource
	private FormService formService;
	
	@Resource
	private HistoryService historyService;
	
	@Resource
	private StrainGageService strainGageService;
	
	
	/**
	 * 根据用户id分页查询任务
	 * @param page
	 * @param rows
	 * @param s_name
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public String list(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,String userId,String s_name,HttpServletResponse response)throws Exception{
		if(s_name==null){
			s_name="";
		}
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		List<Task> list=taskService.createTaskQuery() // 创建任务查询
				.taskCandidateUser(userId) // 根据用户id查询
				.taskNameLike("%"+s_name+"%") // 根据任务名称查询
				.listPage(pageBean.getStart(), pageBean.getPageSize()); // 返回带分页的结果集合
		List<MyTask> taskList=new ArrayList<MyTask>();
		for(Task t:list){
			MyTask myTask=new MyTask();
			myTask.setId(t.getId());
			myTask.setName(t.getName());
			myTask.setCreateTime(t.getCreateTime());
			taskList.add(myTask);
		}
		long total=taskService.createTaskQuery().taskCandidateUser(userId).taskNameLike("%"+s_name+"%").count(); // 获取总记录数
		JsonConfig jsonConfig=new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
		JSONObject result=new JSONObject();
		JSONArray jsonArray=JSONArray.fromObject(taskList,jsonConfig);
		result.put("rows", jsonArray);
		result.put("total", total);
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 查询当前流程图
	 * @param taskId
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/showCurrentView")
	public ModelAndView showCurrentView(String taskId,HttpServletResponse response)throws Exception{
		ModelAndView mav=new ModelAndView();
		Task task=taskService.createTaskQuery() // 创建任务查询
				.taskId(taskId) // 根据任务id查询
				.singleResult(); 
		String processDefinitionId=task.getProcessDefinitionId(); // 获取流程定义id
		ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery() // 创建流程定义查询
				.processDefinitionId(processDefinitionId) // 根据流程定义id查询
				.singleResult(); 
		mav.addObject("deploymentId",processDefinition.getDeploymentId()); // 部署id
		mav.addObject("diagramResourceName", processDefinition.getDiagramResourceName()); // 图片资源文件名称
		
	    ProcessDefinitionEntity	processDefinitionEntity=(ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId); 
	    String processInstanceId=task.getProcessInstanceId(); // 获取流程实例id
	    ProcessInstance pi=runtimeService.createProcessInstanceQuery() // 根据流程实例id获取流程实例
	    		.processInstanceId(processInstanceId)
	    		.singleResult();
	    ActivityImpl activityImpl=processDefinitionEntity.findActivity(pi.getActivityId()); // 根据活动id获取活动实例
	    mav.addObject("x", activityImpl.getX()); // x坐标
	    mav.addObject("y", activityImpl.getY()); // y坐标
	    mav.addObject("width", activityImpl.getWidth()); // 宽度
	    mav.addObject("height", activityImpl.getHeight()); // 高度
		mav.setViewName("page/currentView");
		return mav;
	}
	
	/**
	 * 重定向审核处理页面
	 * @param taskId
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/redirectPage")
	public String redirectPage(String taskId,HttpServletResponse response)throws Exception{
		TaskFormData formData=formService.getTaskFormData(taskId);
		String url=formData.getFormKey();
		JSONObject result=new JSONObject();
		result.put("url", url);
		ResponseUtil.write(response, result);
		return null;
	}
	
	
	/**
	 * 课级主管签核
	 * @param taskId
	 * @param leaveDays
	 * @param comment
	 * @param state
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/audit_kj")
	public String audit_bz(String taskId,Integer leaveDays,String comment,Integer state,HttpServletResponse response,HttpSession session)throws Exception{
		Task task=taskService.createTaskQuery() // 创建任务查询
		        .taskId(taskId) // 根据任务id查询
		        .singleResult();
		Map<String,Object> variables=new HashMap<String,Object>();
		if(state==1){
			variables.put("msg", "通过");
		}else{
			Integer STRAINID=(Integer) taskService.getVariable(taskId, "STRAINID");
			StrainGage strainGage=strainGageService.findById(STRAINID);
			strainGage.setSTATE("审核未通过");
			Map<String, Object> map =new HashMap<String, Object>();
			strainGageService.updateStrainGage(strainGage,map); // 更新审核信息
			variables.put("msg", "未通过");
		}
		variables.put("days", leaveDays); // 设置流程变量
		String processInstanceId=task.getProcessInstanceId(); // 获取流程实例id
		MemberShip currentMemberShip=(MemberShip) session.getAttribute("currentMemberShip");
		User currentUser=currentMemberShip.getUser();
		Group currentGroup=currentMemberShip.getGroup();
		Authentication.setAuthenticatedUserId(currentUser.getFirstName()+"["+currentGroup.getName()+"]"); // 设置用户id
		taskService.addComment(taskId, processInstanceId, comment); // 添加批注信息
		taskService.complete(taskId, variables); // 完成任务
		JSONObject result=new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
	
	
	/**
	 * 流程实例未走完的历史任务 
	 * @param page
	 * @param rows
	 * @param s_name
	 * @param groupId
	 * @param userId
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/unFinishedList")
	public String unFinishedList(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,String s_name,String groupId,String userId,HttpServletResponse response)throws Exception{
		if(s_name==null){
			s_name="";
		}
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		List<HistoricTaskInstance> htiList=null;
		if (groupId.equals("kjzg") || groupId.equals("bjzg") || groupId.equals("gcs")) {
			htiList=historyService.createHistoricTaskInstanceQuery() // 创建历史任务实例查询
					.taskCandidateUser(userId)
					.taskNameLike("%"+s_name+"%") // 根据任务名称查询
					.list();
			
		}else {
			htiList=historyService.createHistoricTaskInstanceQuery() // 创建历史任务实例查询
					.taskCandidateGroup(groupId) // 根据角色查询
					.taskNameLike("%"+s_name+"%") // 根据任务名称查询
					.list();
		}
		
		
		List<MyTask> taskList=new ArrayList<MyTask>();
		for(HistoricTaskInstance hti:htiList){
			if((taskService.createTaskQuery().processInstanceId(hti.getProcessInstanceId()).singleResult()!=null)
				&&(taskService.createTaskQuery().taskCandidateUser(userId).taskId(hti.getId()).list().size()==0)){
				MyTask myTask=new MyTask();
				myTask.setId(hti.getId());
				myTask.setName(hti.getName());
				myTask.setCreateTime(hti.getCreateTime());
				myTask.setEndTime(hti.getEndTime());
				taskList.add(myTask);
			}
		}
		if(taskList.size()>(pageBean.getStart()+pageBean.getPageSize())){
			taskList=taskList.subList(pageBean.getStart(), pageBean.getStart()+pageBean.getPageSize());			
		}
		JsonConfig jsonConfig=new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
		JSONObject result=new JSONObject();
		JSONArray jsonArray=JSONArray.fromObject(taskList,jsonConfig);
		result.put("rows", jsonArray);
		result.put("total", taskList.size());
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 流程实例走完的历史任务 
	 * @param page
	 * @param rows
	 * @param s_name
	 * @param groupId
	 * @param userId
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/finishedList")
	public String finishedList(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,String s_name,String userId,String groupId,HttpServletResponse response)throws Exception{
		if(s_name==null){
			s_name="";
		}
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		List<HistoricTaskInstance> htiList=null;
		if (groupId.equals("kjzg") || groupId.equals("bjzg") || groupId.equals("gcs")) {
			htiList=historyService.createHistoricTaskInstanceQuery() // 创建历史任务实例查询
					   .taskCandidateUser(userId)
					   .taskNameLike("%"+s_name+"%") // 根据任务名称查询
					   .list();
			
		}else {
			htiList=historyService.createHistoricTaskInstanceQuery() // 创建历史任务实例查询
					   .taskCandidateGroup(groupId) // 根据角色查询
					   .taskNameLike("%"+s_name+"%") // 根据任务名称查询
					   .list();
		}
		List<MyTask> taskList=new ArrayList<MyTask>();
		for(HistoricTaskInstance hti:htiList){
			if((taskService.createTaskQuery().processInstanceId(hti.getProcessInstanceId()).singleResult()==null)){
				MyTask myTask=new MyTask();
				myTask.setId(hti.getId());
				myTask.setName(hti.getName());
				myTask.setCreateTime(hti.getCreateTime());
				myTask.setEndTime(hti.getEndTime());
				taskList.add(myTask);
			}
		}
		if(taskList.size()>(pageBean.getStart()+pageBean.getPageSize())){
			taskList=taskList.subList(pageBean.getStart(), pageBean.getStart()+pageBean.getPageSize());			
		}
		JsonConfig jsonConfig=new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
		JSONObject result=new JSONObject();
		JSONArray jsonArray=JSONArray.fromObject(taskList,jsonConfig);
		result.put("rows", jsonArray);
		result.put("total", taskList.size());
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 历史批注查询 根据任务id
	 * @param taskId
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listHistoryComment")
	public String listHistoryComment(String taskId,HttpServletResponse response)throws Exception{
		if(taskId==null){
			return null;
		}
		HistoricTaskInstance hti=historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		List<Comment> commentList=taskService.getProcessInstanceComments(hti.getProcessInstanceId()); 
		Collections.reverse(commentList); // 集合元素反转
		JsonConfig jsonConfig=new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
		JSONObject result=new JSONObject();
		JSONArray jsonArray=JSONArray.fromObject(commentList,jsonConfig);
		result.put("rows", jsonArray);
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 根据任务id查询流程实例的具体执行过程
	 * @param taskId
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listAction")
	public String listAction(String taskId,HttpServletResponse response)throws Exception{
		HistoricTaskInstance hti=historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		String processInstanceId=hti.getProcessInstanceId(); // 获取流程实例id
		List<HistoricActivityInstance> haiList=historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
		JsonConfig jsonConfig=new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
		JSONObject result=new JSONObject();
		JSONArray jsonArray=JSONArray.fromObject(haiList,jsonConfig);
		result.put("rows", jsonArray);
		ResponseUtil.write(response, result);
		return null;
	}

	/**
	 * 查询当前流程图 待办用到的
	 * @param taskId
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/showHisCurrentView")
	public ModelAndView showHisCurrentView(String taskId,HttpServletResponse response)throws Exception{
		ModelAndView mav=new ModelAndView();
		HistoricTaskInstance hti=historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		String processDefinitionId=hti.getProcessDefinitionId(); // 获取流程定义id
		ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery() // 创建流程定义查询
				.processDefinitionId(processDefinitionId) // 根据流程定义id查询
				.singleResult(); 
		mav.addObject("deploymentId",processDefinition.getDeploymentId()); // 部署id
		mav.addObject("diagramResourceName", processDefinition.getDiagramResourceName()); // 图片资源文件名称
		
	    ProcessDefinitionEntity	processDefinitionEntity=(ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId); 
	    String processInstanceId=hti.getProcessInstanceId(); // 获取流程实例id
	    ProcessInstance pi=runtimeService.createProcessInstanceQuery() // 根据流程实例id获取流程实例
	    		.processInstanceId(processInstanceId)
	    		.singleResult();
	    ActivityImpl activityImpl=processDefinitionEntity.findActivity(pi.getActivityId()); // 根据活动id获取活动实例
	    mav.addObject("x", activityImpl.getX()); // x坐标
	    mav.addObject("y", activityImpl.getY()); // y坐标
	    mav.addObject("width", activityImpl.getWidth()); // 宽度
	    mav.addObject("height", activityImpl.getHeight()); // 高度
		mav.setViewName("page/currentView");
		return mav;
	}
	
	
	
	/**
	 * 领导审批
	 * @param taskId 任务id
	 * @param comment 批注信息
	 * @param state 审核状态 1 通过 2 驳回
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/audit_ld")
	public String audit_ld(String taskId,String comment,Integer state,HttpServletResponse response,HttpSession session)throws Exception{
		Task task=taskService.createTaskQuery() // 创建任务查询
		        .taskId(taskId) // 根据任务id查询
		        .singleResult();
		Integer STRAINID=(Integer) taskService.getVariable(taskId, "STRAINID");
		StrainGage strainGage=strainGageService.findById(STRAINID);
		Map<String, Object> map =new HashMap<String, Object>();
		if(state==1){
			strainGage.setSTATE("审核通过");
			map.put("applybu", strainGage.getBU());
			map.put("projectname", strainGage.getPROJECTNAME());
			map.put("applydate", strainGage.getAPPLYDATE());
			map.put("starttime", "2000-00-00 00:00");
			map.put("endtime", "2000-00-00 00:00");
			map.put("handlerstate", "待处理");
			
			strainGageService.updateStrainGage(strainGage,map); // 更新审核信息
			
			
		}else{
			strainGage.setSTATE("审核未通过");
			strainGageService.updateStrainGage(strainGage,map); // 更新审核信息
		}
		String processInstanceId=task.getProcessInstanceId(); // 获取流程实例id
		MemberShip currentMemberShip=(MemberShip) session.getAttribute("currentMemberShip");
		User currentUser=currentMemberShip.getUser();
		Group currentGroup=currentMemberShip.getGroup();
		Authentication.setAuthenticatedUserId(currentUser.getFirstName()+"["+currentGroup.getName()+"]"); // 设置用户id
		taskService.addComment(taskId, processInstanceId, comment); // 添加批注信息
		taskService.complete(taskId); // 完成任务
		JSONObject result=new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
	
	
	/**
	 * 历史批注 通过流程实例id查询
	 * @param processInstanceId
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listHistoryCommentWithProcessInstanceId")
	public String listHistoryCommentWithProcessInstanceId(String processInstanceId,HttpServletResponse response)throws Exception{
		if(processInstanceId==null){
			return null;
		}
		List<Comment> commentList=taskService.getProcessInstanceComments(processInstanceId); 
		Collections.reverse(commentList); // 集合元素反转
		JsonConfig jsonConfig=new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
		JSONObject result=new JSONObject();
		JSONArray jsonArray=JSONArray.fromObject(commentList,jsonConfig);
		result.put("rows", jsonArray);
		ResponseUtil.write(response, result);
		return null;
	}
	
	
}
