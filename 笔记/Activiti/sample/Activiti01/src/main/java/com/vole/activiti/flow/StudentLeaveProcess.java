package com.vole.activiti.flow;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class StudentLeaveProcess {

	/**
	 * 获取默认流程引擎实例，会自动读取 activiti.cfg.xml 文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deploy() {
		Deployment deployment = processEngine.getRepositoryService()// 获取部署相关Service
				.createDeployment()// 创建部署
				.addClasspathResource("diagrams/StudentLeaveProcess.bpmn") // 加载资源文件
				.addClasspathResource("diagrams/StudentLeaveProcess.png").name("学生请假流程")// 定义流程名称
				.deploy();// 部署
		System.out.println("流程部署ID:" + deployment.getId()); // 流程部署ID:12501
		System.out.println("流程部署Name:" + deployment.getName());// 流程部署Name:学生请假流程
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void start() {
		ProcessInstance pi = processEngine.getRuntimeService() // 运行时Service
				.startProcessInstanceByKey("StudentLeaveProcess"); // 流程定义表的KEY字段值
		System.out.println("流程实例ID:" + pi.getId());// 流程实例ID:15001
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); // 流程定义ID:StudentLeaveProcess:1:12504
	}

	/**
	 * 查看任务
	 */
	@Test
	public void findTask() {
		System.out.println("开始查看任务");
		List<Task> taskList = processEngine.getTaskService() // 任务相关Service
				.createTaskQuery()// 创建任务查询
				.taskAssignee("王五")// 指定某个人
				.list();
		for (Task task : taskList) {
			System.out.println("任务ID:" + task.getId());
			System.out.println("任务名称:" + task.getName());
			System.out.println("任务创建时间:" + task.getCreateTime());
			System.out.println("任务委派人:" + task.getAssignee());
			System.out.println("流程实例ID:" + task.getProcessInstanceId());
		}
		/*
		 * 任务ID:15004 任务名称:学生请假 任务创建时间:Mon Mar 12 15:22:47 CST 2018 任务委派人:张三 \李四
		 * \王五 流程实例ID:15001 \17502 \20002
		 */
	}

	/**
	 * 完成任务
	 */
	@Test
	public void completeTask() {
		processEngine.getTaskService() // 任务相关Service
				.complete("20002");// 任务 ID
		System.out.println("complete task");
	}

	/**
	 * 查询流程状态（正在执行 or 已经执行结束）--> 结束流程实例后会清空所有运行时的表数据，以此来判断
	 */
	@Test
	public void processState() {
		ProcessInstance pi = processEngine.getRuntimeService()// 获取运行时Service
				.createProcessInstanceQuery() // 创建流程实例查询
				.processInstanceId("35001")// 用流程实例id查询
				.singleResult();

		System.out.println(pi != null ? "流程正在执行！" : "流程已经执行结束！");
	}
	
	/**
	 * 历史任务查询--相关的表 act_hi_taskinst
	 */
	@Test
	public void historyTaskList(){
		List<HistoricTaskInstance> list=processEngine.getHistoryService() // 历史相关Service
			.createHistoricTaskInstanceQuery() // 创建历史任务实例查询
			.processInstanceId("15001") // 用流程实例id查询
			.finished() // 查询已经完成的任务
			.list(); 
		for(HistoricTaskInstance hti:list){
			System.out.println("任务ID:"+hti.getId());
			System.out.println("流程实例ID:"+hti.getProcessInstanceId());
			System.out.println("任务名称："+hti.getName());
			System.out.println("办理人："+hti.getAssignee());
			System.out.println("开始时间："+hti.getStartTime());
			System.out.println("结束时间："+hti.getEndTime());
			System.out.println("=================================");
		}
	}
	
	/**
	 * 历史活动查询（一般用这种方式查询）----相关表 act_hi_actinst
	 */
	@Test
	public void historyActInstanceList(){
		List<HistoricActivityInstance>  list=processEngine.getHistoryService() // 历史相关Service
			.createHistoricActivityInstanceQuery() // 创建历史活动实例查询
			.processInstanceId("15001") // 执行流程实例id
			.finished()
			.list();
		for(HistoricActivityInstance hai:list){
			System.out.println("活动ID:"+hai.getId());
			System.out.println("流程实例ID:"+hai.getProcessInstanceId());
			System.out.println("活动名称："+hai.getActivityName());
			System.out.println("办理人："+hai.getAssignee());
			System.out.println("开始时间："+hai.getStartTime());
			System.out.println("结束时间："+hai.getEndTime());
			System.out.println("=================================");
		}
	}

}
