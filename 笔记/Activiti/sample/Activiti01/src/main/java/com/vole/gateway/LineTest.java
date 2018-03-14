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
 * ����
 * @User: vole
 * @date: 2018��3��13������11:37:35
 * @Function:
 */
public class LineTest {

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
				.addClasspathResource("diagrams/StudentLeaveProcess2.bpmn") // ������Դ�ļ�
				.addClasspathResource("diagrams/StudentLeaveProcess2.png")
				.name("ѧ���������2")// ������������
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
				.startProcessInstanceByKey("StudentLeaveProcess2"); // ���̶�����KEY�ֶ�ֵ
		System.out.println("����ʵ��ID:" + pi.getId());
		System.out.println("���̶���ID:" + pi.getProcessDefinitionId()); 
	}

	/**
	 * �鿴����--act_ru_task
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
				.complete("57503");// ���� ID
		System.out.println("complete task");
	}
	
	/**
	 * �������
	 */
	@Test
	public void completeTask2() {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("msg", "�������");
		processEngine.getTaskService() // �������Service
				.complete("55002", variables); // ��������ʱ���������̱���
	}
}
