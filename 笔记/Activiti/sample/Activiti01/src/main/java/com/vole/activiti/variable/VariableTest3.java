package com.vole.activiti.variable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import com.vole.model.Student;

/**
 * 流程变量测试
 * 
 * @author user
 *
 */
public class VariableTest3 {

	/**
	 * 获取默认流程引擎实例，会自动读取activiti.cfg.xml文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deploy() {
		Deployment deployment = processEngine.getRepositoryService() // 获取部署相关Service
				.createDeployment() // 创建部署
				.addClasspathResource("diagrams/StudentLeaveProcess.bpmn") // 加载资源文件
				.addClasspathResource("diagrams/StudentLeaveProcess.png") // 加载资源文件
				.name("学生请假流程") // 流程名称
				.deploy(); // 部署
		System.out.println("流程部署ID:" + deployment.getId());
		System.out.println("流程部署Name:" + deployment.getName());
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void start() {
		Student student = new Student();
		student.setId(1);
		student.setName("张三");
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("days", 2);
		variables.put("date", new Date());
		variables.put("reason", "发烧");
		variables.put("student", student);

		ProcessInstance pi = processEngine.getRuntimeService() // 运行时Service
				.startProcessInstanceByKey("studentLevaeProcess", variables); // 启动流程的时候，设置流程变量

		System.out.println("流程实例ID:" + pi.getId());
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
	}

	/**
	 * 查看任务
	 */
	@Test
	public void findTask() {
		List<Task> taskList = processEngine.getTaskService() // 任务相关Service
				.createTaskQuery() // 创建任务查询
				.taskAssignee("王五") // 指定某个人
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
				.complete("107502");
	}

	/**
	 * 设置流程变量数据
	 */
	@Test
	public void setVariableValues() {
		RuntimeService runtimeService = processEngine.getRuntimeService(); // 任务Service
		String executionId = "90001";
		runtimeService.setVariable(executionId, "days", 2);
		runtimeService.setVariable(executionId, "date", new Date());
		runtimeService.setVariable(executionId, "reason", "发烧");
		Student student = new Student();
		student.setId(1);
		student.setName("张三");
		runtimeService.setVariable(executionId, "student", student); // 存序列化对象
	}

	/**
	 * 获取流程变量数据
	 */
	@Test
	public void getVariableValues() {
		RuntimeService runtimeService = processEngine.getRuntimeService(); // 任务Service
		String executionId = "102501";
		Integer days = (Integer) runtimeService.getVariable(executionId, "days");
		Date date = (Date) runtimeService.getVariable(executionId, "date");
		String reason = (String) runtimeService.getVariable(executionId, "reason");
		Student student = (Student) runtimeService.getVariable(executionId, "student");
		System.out.println("请假天数：" + days);
		System.out.println("请假日期：" + date);
		System.out.println("请假原因：" + reason);
		System.out.println("请假对象：" + student.getId() + "," + student.getName());
	}
}
