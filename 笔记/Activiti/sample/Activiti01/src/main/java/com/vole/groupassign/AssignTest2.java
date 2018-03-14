package com.vole.groupassign;

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
 * ���������--ʹ�����̱���
 * @User: vole
 * @date: 2018��3��13������11:37:58
 * @Function:
 */
public class AssignTest2 {

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
				.addClasspathResource("diagrams/GroupProcess2.bpmn") // ������Դ�ļ�
				.addClasspathResource("diagrams/GroupProcess2.png")
				.name("ѧ���������_���û�2")// ������������
				.deploy();// ����
		System.out.println("���̲���ID:" + deployment.getId()); 
		System.out.println("���̲���Name:" + deployment.getName());
	}

	/**
	 * ��������ʵ��
	 */
	@Test
	public void start() {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("groupId", "test");
		ProcessInstance pi = processEngine.getRuntimeService() // ����ʱService
				.startProcessInstanceByKey("GroupProcess2",variables); // ���̶�����KEY�ֶ�ֵ
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
				.taskCandidateUser("wuji")
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
				.complete("167505");// ���� ID
		System.out.println("complete task");
	}
	
}
