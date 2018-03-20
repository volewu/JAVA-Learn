package com.java.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

public class StudentLeaveProcess {

	/**
	 * 获取默认流程引擎实例，会自动读取activiti.cfg.xml文件
	 */
	//private ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
	
	/**
	 * 部署流程定义
	 */
	public void deploy(){
		RepositoryService repositoryService=(RepositoryService) ac.getBean("repositoryService");
		Deployment deployment=repositoryService // 获取部署相关Service
				.createDeployment() // 创建部署
				.addClasspathResource("flow/StrainGageProcess.bpmn") // 加载资源文件
				.addClasspathResource("flow/StrainGageProcess.png") // 加载资源文件
				.name("StrainGage申请流程") // 流程名称
				.deploy(); // 部署
		System.out.println("流程部署ID:"+deployment.getId()); 
		System.out.println("流程部署Name:"+deployment.getName());
	}
	
	
	/**
	 * 启动流程实例
	 */
	
	public void start(){
		Map<String,Object> variables=new HashMap<String,Object>();
		variables.put("applyIds", "java1");
		//variables.put("userIds", "java11");
		//variables.put("bjIds", "bjzg");
		RuntimeService runtimeService=(RuntimeService) ac.getBean("runtimeService");
		ProcessInstance pi=runtimeService // 运行时Service
			.startProcessInstanceByKey("strainGageProcess2",variables); // 流程定义表的KEY字段值
		System.out.println("流程实例ID:"+pi.getId());
		System.out.println("流程定义ID:"+pi.getProcessDefinitionId()); 
	}
	
	
	/**
	 * 查看任务
	 */
	
	public void findTask(){
		TaskService taskService=(TaskService) ac.getBean("taskService");
		List<Task> taskList=taskService // 任务相关Service
			.createTaskQuery() // 创建任务查询
			//.taskAssignee("java") // 指定某个人
			.taskCandidateUser("java")
			.list();
		for(Task task:taskList){
			System.out.println("任务ID:"+task.getId()); 
			System.out.println("任务名称:"+task.getName());
			System.out.println("任务创建时间:"+task.getCreateTime());
			System.out.println("任务委派人:"+task.getAssignee());
			System.out.println("流程实例ID:"+task.getProcessInstanceId());
		}
	}
	
	
	/**
	 * 完成任务
	 */
	
	public void completeTask(){
		TaskService taskService=(TaskService) ac.getBean("taskService"); // 任务相关Service
				taskService.complete("287505");  //任务ID
	}
	
	
	/**
	 * 完成任务时设置变量
	 */
	
	public void completeTask2(){
		TaskService taskService=(TaskService) ac.getBean("taskService"); // 任务相关Service
		Map<String, Object> variables=new HashMap<String, Object>();  
		variables.put("msg", "通过");
		variables.put("bjIds", "java");
		//variables.put("info", "重要");
		taskService // 任务相关Service
			.complete("297505", variables);  //任务ID
	}
	
	
	/**
	 * 查询流程状态（正在执行 or 已经执行结束）
	 */
	
	public void processState(){
		RuntimeService runtimeService=(RuntimeService) ac.getBean("runtimeService");
		ProcessInstance pI=runtimeService // 获取运行时Service
		.createProcessInstanceQuery()
		.processInstanceId("190001")
		.singleResult();
		
		if(pI!=null){
			System.out.println("流程正在执行！");
		}else{
			System.out.println("流程已经执行结束！");
		}
		
	}
	
	/**
	 * 历史任务查询
	 */
	public void historyTaskList(){
		HistoryService historyService=(HistoryService) ac.getBean("historyService");
		List<HistoricTaskInstance>	list=historyService
			.createHistoricTaskInstanceQuery() //创建历史任务实例查询
			.processInstanceId("22501") //用流程实例id查询
			.finished() // 查询已经完成的任务
			.list();
		
		for (HistoricTaskInstance hti : list) {
			System.out.println("任务ID:"+hti.getId());
			System.out.println("流程实例ID:"+hti.getProcessInstanceId());
			System.out.println("任务名称:"+hti.getName());
			System.out.println("办理人:"+hti.getAssignee());
			System.out.println("开始时间:"+hti.getStartTime());
			System.out.println("结束时间:"+hti.getEndTime());
			System.out.println("================================");
		}
	}
	
	/**
	 * 历史活动查询
	 */
	
	public void historyActInstanceList(){
		HistoryService historyService=(HistoryService) ac.getBean("historyService");
		List<HistoricActivityInstance> list =historyService
			.createHistoricActivityInstanceQuery() //创建历史任务实例查询
			.processInstanceId("230001") //用流程实例id查询
			.finished() // 查询已经完成的任务
			.list();
		
		for (HistoricActivityInstance hti : list) {
			System.out.println("活动ID:"+hti.getId());
			System.out.println("流程实例ID:"+hti.getProcessInstanceId());
			System.out.println("活动名称:"+hti.getActivityName());
			System.out.println("办理人:"+hti.getAssignee());
			System.out.println("开始时间:"+hti.getStartTime());
			System.out.println("结束时间:"+hti.getEndTime());
			System.out.println("================================");
		}
	}
}
