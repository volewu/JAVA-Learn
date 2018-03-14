package com.vole.gateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

/**
 * 连线
 * @User: vole
 * @date: 2018年3月13日上午11:37:35
 * @Function:
 */
public class LineTest {

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
				.addClasspathResource("diagrams/StudentLeaveProcess2.bpmn") // 加载资源文件
				.addClasspathResource("diagrams/StudentLeaveProcess2.png")
				.name("学生请假流程2")// 定义流程名称
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
				.startProcessInstanceByKey("StudentLeaveProcess2"); // 流程定义表的KEY字段值
		System.out.println("流程实例ID:" + pi.getId());
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); 
	}

	/**
	 * 查看任务--act_ru_task
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
	}

	/**
	 * 完成任务
	 */
	@Test
	public void completeTask() {
		processEngine.getTaskService() // 任务相关Service
				.complete("57503");// 任务 ID
		System.out.println("complete task");
	}
	
	/**
	 * 完成任务
	 */
	@Test
	public void completeTask2() {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("msg", "特殊情况");
		processEngine.getTaskService() // 任务相关Service
				.complete("55002", variables); // 完成任务的时候，设置流程变量
	}
}
