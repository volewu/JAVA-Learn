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
	 * ��ȡĬ����������ʵ�������Զ���ȡ activiti.cfg.xml �ļ�
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * �������̶���
	 */
	@Test
	public void deploy() {
		Deployment deployment = processEngine.getRepositoryService()// ��ȡ�������Service
				.createDeployment()// ��������
				.addClasspathResource("diagrams/StudentLeaveProcess.bpmn") // ������Դ�ļ�
				.addClasspathResource("diagrams/StudentLeaveProcess.png").name("ѧ���������")// ������������
				.deploy();// ����
		System.out.println("���̲���ID:" + deployment.getId()); // ���̲���ID:12501
		System.out.println("���̲���Name:" + deployment.getName());// ���̲���Name:ѧ���������
	}

	/**
	 * ��������ʵ��
	 */
	@Test
	public void start() {
		ProcessInstance pi = processEngine.getRuntimeService() // ����ʱService
				.startProcessInstanceByKey("StudentLeaveProcess"); // ���̶�����KEY�ֶ�ֵ
		System.out.println("����ʵ��ID:" + pi.getId());// ����ʵ��ID:15001
		System.out.println("���̶���ID:" + pi.getProcessDefinitionId()); // ���̶���ID:StudentLeaveProcess:1:12504
	}

	/**
	 * �鿴����
	 */
	@Test
	public void findTask() {
		System.out.println("��ʼ�鿴����");
		List<Task> taskList = processEngine.getTaskService() // �������Service
				.createTaskQuery()// ���������ѯ
				.taskAssignee("����")// ָ��ĳ����
				.list();
		for (Task task : taskList) {
			System.out.println("����ID:" + task.getId());
			System.out.println("��������:" + task.getName());
			System.out.println("���񴴽�ʱ��:" + task.getCreateTime());
			System.out.println("����ί����:" + task.getAssignee());
			System.out.println("����ʵ��ID:" + task.getProcessInstanceId());
		}
	}

	/**
	 * �������
	 */
	@Test
	public void completeTask() {
		processEngine.getTaskService() // �������Service
				.complete("32502");// ���� ID
		System.out.println("complete task");
	}

	/**
	 * �������̱�������
	 */
	@Test
	public void setVariableValues() {
		TaskService taskService = processEngine.getTaskService();
		System.out.println("�������̱���");
		String taskId = "25004";
		taskService.setVariable(taskId, "days", 2);
		taskService.setVariable(taskId, "date", new Date());
		//taskService.setVariableLocal(taskId,"date", new Date()); �ֲ�����
		taskService.setVariable(taskId, "reason", "����");
		Student student = new Student();
		student.setId(1);
		student.setName("����");
		taskService.setVariable(taskId, "student", student); // �����л�����
	}

	/**
	 * ��ȡ���̱�������
	 */
	@Test
	public void getVariableValues() {
		TaskService taskService = processEngine.getTaskService();
		String taskId = "32502";
		Integer days = (Integer) taskService.getVariable(taskId, "days");
		Date date = (Date) taskService.getVariable(taskId, "date");
		//Date date=(Date) taskService.getVariableLocal(taskId, "date");�ֲ�����
		String reason = (String) taskService.getVariable(taskId, "reason");
		Student student = (Student) taskService.getVariable(taskId, "student");
		System.out.println("���������" + days);
		System.out.println("������ڣ�" + date);
		System.out.println("���ԭ��" + reason);
		System.out.println("��ٶ���" + student.getId() + "," + student.getName());
	}
	
	/**
	 * �������̱�������
	 */
	@Test
	public void setVariableValues2(){
		TaskService taskService=processEngine.getTaskService(); // ����Service
		String taskId="60004";
		Student student=new Student();
		student.setId(1);
		student.setName("����");

		Map<String, Object> variables=new HashMap<String,Object>();
		variables.put("days", 2);
		variables.put("date", new Date());
		variables.put("reason", "����");
		variables.put("student", student);
		taskService.setVariables(taskId, variables);
	}
	
	/**
	 * ��ȡ���̱�������
	 */
	@Test
	public void getVariableValues2(){
		TaskService taskService=processEngine.getTaskService(); // ����Service
		String taskId="65002";
		Map<String,Object> variables=taskService.getVariables(taskId);
		Integer days=(Integer) variables.get("days");
		Date date=(Date) variables.get("date");
		String reason=(String) variables.get("reason");
		Student student=(Student)variables.get("student"); 
		System.out.println("���������"+days);
		System.out.println("������ڣ�"+date);
		System.out.println("���ԭ��"+reason);
		System.out.println("��ٶ���"+student.getId()+","+student.getName());
	}
}
