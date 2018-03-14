package com.vole.activiti.variable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import com.vole.model.Student;

public class VariableTest {

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
				.taskAssignee("李四")// 指定某个人
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
				.complete("32502");// 任务 ID
		System.out.println("complete task");
	}

	/**
	 * 设置流程变量数据
	 */
	@Test
	public void setVariableValues() {
		TaskService taskService = processEngine.getTaskService();
		System.out.println("设置流程变量");
		String taskId = "25004";
		taskService.setVariable(taskId, "days", 2);
		taskService.setVariable(taskId, "date", new Date());
		//taskService.setVariableLocal(taskId,"date", new Date()); 局部变量
		taskService.setVariable(taskId, "reason", "发烧");
		Student student = new Student();
		student.setId(1);
		student.setName("张三");
		taskService.setVariable(taskId, "student", student); // 存序列化对象
	}

	/**
	 * 获取流程变量数据
	 */
	@Test
	public void getVariableValues() {
		TaskService taskService = processEngine.getTaskService();
		String taskId = "32502";
		Integer days = (Integer) taskService.getVariable(taskId, "days");
		Date date = (Date) taskService.getVariable(taskId, "date");
		//Date date=(Date) taskService.getVariableLocal(taskId, "date");局部变量
		String reason = (String) taskService.getVariable(taskId, "reason");
		Student student = (Student) taskService.getVariable(taskId, "student");
		System.out.println("请假天数：" + days);
		System.out.println("请假日期：" + date);
		System.out.println("请假原因：" + reason);
		System.out.println("请假对象：" + student.getId() + "," + student.getName());
	}
	
	/**
	 * 设置流程变量数据
	 */
	@Test
	public void setVariableValues2(){
		TaskService taskService=processEngine.getTaskService(); // 任务Service
		String taskId="60004";
		Student student=new Student();
		student.setId(1);
		student.setName("张三");

		Map<String, Object> variables=new HashMap<String,Object>();
		variables.put("days", 2);
		variables.put("date", new Date());
		variables.put("reason", "发烧");
		variables.put("student", student);
		taskService.setVariables(taskId, variables);
	}
	
	/**
	 * 获取流程变量数据
	 */
	@Test
	public void getVariableValues2(){
		TaskService taskService=processEngine.getTaskService(); // 任务Service
		String taskId="65002";
		Map<String,Object> variables=taskService.getVariables(taskId);
		Integer days=(Integer) variables.get("days");
		Date date=(Date) variables.get("date");
		String reason=(String) variables.get("reason");
		Student student=(Student)variables.get("student"); 
		System.out.println("请假天数："+days);
		System.out.println("请假日期："+date);
		System.out.println("请假原因："+reason);
		System.out.println("请假对象："+student.getId()+","+student.getName());
	}
}
