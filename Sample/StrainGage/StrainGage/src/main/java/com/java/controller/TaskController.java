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
 * ����Controller��
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
	 * �����û�id��ҳ��ѯ����
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
		List<Task> list=taskService.createTaskQuery() // ���������ѯ
				.taskCandidateUser(userId) // �����û�id��ѯ
				.taskNameLike("%"+s_name+"%") // �����������Ʋ�ѯ
				.listPage(pageBean.getStart(), pageBean.getPageSize()); // ���ش���ҳ�Ľ������
		List<MyTask> taskList=new ArrayList<MyTask>();
		for(Task t:list){
			MyTask myTask=new MyTask();
			myTask.setId(t.getId());
			myTask.setName(t.getName());
			myTask.setCreateTime(t.getCreateTime());
			taskList.add(myTask);
		}
		long total=taskService.createTaskQuery().taskCandidateUser(userId).taskNameLike("%"+s_name+"%").count(); // ��ȡ�ܼ�¼��
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
	 * ��ѯ��ǰ����ͼ
	 * @param taskId
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/showCurrentView")
	public ModelAndView showCurrentView(String taskId,HttpServletResponse response)throws Exception{
		ModelAndView mav=new ModelAndView();
		Task task=taskService.createTaskQuery() // ���������ѯ
				.taskId(taskId) // ��������id��ѯ
				.singleResult(); 
		String processDefinitionId=task.getProcessDefinitionId(); // ��ȡ���̶���id
		ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery() // �������̶����ѯ
				.processDefinitionId(processDefinitionId) // �������̶���id��ѯ
				.singleResult(); 
		mav.addObject("deploymentId",processDefinition.getDeploymentId()); // ����id
		mav.addObject("diagramResourceName", processDefinition.getDiagramResourceName()); // ͼƬ��Դ�ļ�����
		
	    ProcessDefinitionEntity	processDefinitionEntity=(ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId); 
	    String processInstanceId=task.getProcessInstanceId(); // ��ȡ����ʵ��id
	    ProcessInstance pi=runtimeService.createProcessInstanceQuery() // ��������ʵ��id��ȡ����ʵ��
	    		.processInstanceId(processInstanceId)
	    		.singleResult();
	    ActivityImpl activityImpl=processDefinitionEntity.findActivity(pi.getActivityId()); // ���ݻid��ȡ�ʵ��
	    mav.addObject("x", activityImpl.getX()); // x����
	    mav.addObject("y", activityImpl.getY()); // y����
	    mav.addObject("width", activityImpl.getWidth()); // ����
	    mav.addObject("height", activityImpl.getHeight()); // �߶�
		mav.setViewName("page/currentView");
		return mav;
	}
	
	/**
	 * �ض�����˴���ҳ��
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
	 * �μ�����ǩ��
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
		Task task=taskService.createTaskQuery() // ���������ѯ
		        .taskId(taskId) // ��������id��ѯ
		        .singleResult();
		Map<String,Object> variables=new HashMap<String,Object>();
		if(state==1){
			variables.put("msg", "ͨ��");
		}else{
			Integer STRAINID=(Integer) taskService.getVariable(taskId, "STRAINID");
			StrainGage strainGage=strainGageService.findById(STRAINID);
			strainGage.setSTATE("���δͨ��");
			Map<String, Object> map =new HashMap<String, Object>();
			strainGageService.updateStrainGage(strainGage,map); // ���������Ϣ
			variables.put("msg", "δͨ��");
		}
		variables.put("days", leaveDays); // �������̱���
		String processInstanceId=task.getProcessInstanceId(); // ��ȡ����ʵ��id
		MemberShip currentMemberShip=(MemberShip) session.getAttribute("currentMemberShip");
		User currentUser=currentMemberShip.getUser();
		Group currentGroup=currentMemberShip.getGroup();
		Authentication.setAuthenticatedUserId(currentUser.getFirstName()+"["+currentGroup.getName()+"]"); // �����û�id
		taskService.addComment(taskId, processInstanceId, comment); // ������ע��Ϣ
		taskService.complete(taskId, variables); // �������
		JSONObject result=new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
	
	
	/**
	 * ����ʵ��δ�������ʷ���� 
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
			htiList=historyService.createHistoricTaskInstanceQuery() // ������ʷ����ʵ����ѯ
					.taskCandidateUser(userId)
					.taskNameLike("%"+s_name+"%") // �����������Ʋ�ѯ
					.list();
			
		}else {
			htiList=historyService.createHistoricTaskInstanceQuery() // ������ʷ����ʵ����ѯ
					.taskCandidateGroup(groupId) // ���ݽ�ɫ��ѯ
					.taskNameLike("%"+s_name+"%") // �����������Ʋ�ѯ
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
	 * ����ʵ���������ʷ���� 
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
			htiList=historyService.createHistoricTaskInstanceQuery() // ������ʷ����ʵ����ѯ
					   .taskCandidateUser(userId)
					   .taskNameLike("%"+s_name+"%") // �����������Ʋ�ѯ
					   .list();
			
		}else {
			htiList=historyService.createHistoricTaskInstanceQuery() // ������ʷ����ʵ����ѯ
					   .taskCandidateGroup(groupId) // ���ݽ�ɫ��ѯ
					   .taskNameLike("%"+s_name+"%") // �����������Ʋ�ѯ
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
	 * ��ʷ��ע��ѯ ��������id
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
		Collections.reverse(commentList); // ����Ԫ�ط�ת
		JsonConfig jsonConfig=new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
		JSONObject result=new JSONObject();
		JSONArray jsonArray=JSONArray.fromObject(commentList,jsonConfig);
		result.put("rows", jsonArray);
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * ��������id��ѯ����ʵ���ľ���ִ�й���
	 * @param taskId
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listAction")
	public String listAction(String taskId,HttpServletResponse response)throws Exception{
		HistoricTaskInstance hti=historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		String processInstanceId=hti.getProcessInstanceId(); // ��ȡ����ʵ��id
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
	 * ��ѯ��ǰ����ͼ �����õ���
	 * @param taskId
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/showHisCurrentView")
	public ModelAndView showHisCurrentView(String taskId,HttpServletResponse response)throws Exception{
		ModelAndView mav=new ModelAndView();
		HistoricTaskInstance hti=historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		String processDefinitionId=hti.getProcessDefinitionId(); // ��ȡ���̶���id
		ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery() // �������̶����ѯ
				.processDefinitionId(processDefinitionId) // �������̶���id��ѯ
				.singleResult(); 
		mav.addObject("deploymentId",processDefinition.getDeploymentId()); // ����id
		mav.addObject("diagramResourceName", processDefinition.getDiagramResourceName()); // ͼƬ��Դ�ļ�����
		
	    ProcessDefinitionEntity	processDefinitionEntity=(ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId); 
	    String processInstanceId=hti.getProcessInstanceId(); // ��ȡ����ʵ��id
	    ProcessInstance pi=runtimeService.createProcessInstanceQuery() // ��������ʵ��id��ȡ����ʵ��
	    		.processInstanceId(processInstanceId)
	    		.singleResult();
	    ActivityImpl activityImpl=processDefinitionEntity.findActivity(pi.getActivityId()); // ���ݻid��ȡ�ʵ��
	    mav.addObject("x", activityImpl.getX()); // x����
	    mav.addObject("y", activityImpl.getY()); // y����
	    mav.addObject("width", activityImpl.getWidth()); // ����
	    mav.addObject("height", activityImpl.getHeight()); // �߶�
		mav.setViewName("page/currentView");
		return mav;
	}
	
	
	
	/**
	 * �쵼����
	 * @param taskId ����id
	 * @param comment ��ע��Ϣ
	 * @param state ���״̬ 1 ͨ�� 2 ����
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/audit_ld")
	public String audit_ld(String taskId,String comment,Integer state,HttpServletResponse response,HttpSession session)throws Exception{
		Task task=taskService.createTaskQuery() // ���������ѯ
		        .taskId(taskId) // ��������id��ѯ
		        .singleResult();
		Integer STRAINID=(Integer) taskService.getVariable(taskId, "STRAINID");
		StrainGage strainGage=strainGageService.findById(STRAINID);
		Map<String, Object> map =new HashMap<String, Object>();
		if(state==1){
			strainGage.setSTATE("���ͨ��");
			map.put("applybu", strainGage.getBU());
			map.put("projectname", strainGage.getPROJECTNAME());
			map.put("applydate", strainGage.getAPPLYDATE());
			map.put("starttime", "2000-00-00 00:00");
			map.put("endtime", "2000-00-00 00:00");
			map.put("handlerstate", "������");
			
			strainGageService.updateStrainGage(strainGage,map); // ���������Ϣ
			
			
		}else{
			strainGage.setSTATE("���δͨ��");
			strainGageService.updateStrainGage(strainGage,map); // ���������Ϣ
		}
		String processInstanceId=task.getProcessInstanceId(); // ��ȡ����ʵ��id
		MemberShip currentMemberShip=(MemberShip) session.getAttribute("currentMemberShip");
		User currentUser=currentMemberShip.getUser();
		Group currentGroup=currentMemberShip.getGroup();
		Authentication.setAuthenticatedUserId(currentUser.getFirstName()+"["+currentGroup.getName()+"]"); // �����û�id
		taskService.addComment(taskId, processInstanceId, comment); // ������ע��Ϣ
		taskService.complete(taskId); // �������
		JSONObject result=new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
	
	
	/**
	 * ��ʷ��ע ͨ������ʵ��id��ѯ
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
		Collections.reverse(commentList); // ����Ԫ�ط�ת
		JsonConfig jsonConfig=new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
		JSONObject result=new JSONObject();
		JSONArray jsonArray=JSONArray.fromObject(commentList,jsonConfig);
		result.put("rows", jsonArray);
		ResponseUtil.write(response, result);
		return null;
	}
	
	
}