package com.vole.activiti.flow;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;


public class HelloWorldProcess {
	
	/**
	 * ��ȡĬ����������ʵ�������Զ���ȡ activiti.cfg.xml �ļ�
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * �������̶���
	 */
	@Test
	public void deploy(){
		Deployment deployment= processEngine.getRepositoryService()// ��ȡ�������Service
					.createDeployment()// ��������
					.addClasspathResource("diagrams/HelloWorld.bpmn") // ������Դ�ļ�
					.addClasspathResource("diagrams/HelloWorld.png")
					.name("HelloWorld ����")// ������������
					.deploy();// ����
		System.out.println("���̲���ID:"+deployment.getId()); //���̲���ID:1
		System.out.println("���̲���Name:"+deployment.getName());//���̲���Name:HelloWorld ����
	}
	
	/**
	 * ��������ʵ��
	 */
	@Test
	public void start(){
		ProcessInstance pi=processEngine.getRuntimeService() // ����ʱService
				.startProcessInstanceByKey("myFirstProcess"); // ���̶�����KEY�ֶ�ֵ
			System.out.println("����ʵ��ID:"+pi.getId());//����ʵ��ID:2501
			System.out.println("���̶���ID:"+pi.getProcessDefinitionId()); //���̶���ID:myFirstProcess:1:4
	}
	
	/**
	 * �鿴����
	 */
	@Test
	public void findTask(){
		System.out.println("��ʼ�鿴����");
		List<Task> taskList = processEngine.getTaskService() // �������Service
			.createTaskQuery()// ���������ѯ
			.taskAssignee("vole")// ָ��ĳ����
			.list();
		for(Task task:taskList){
			System.out.println("����ID:"+task.getId()); 
			System.out.println("��������:"+task.getName());
			System.out.println("���񴴽�ʱ��:"+task.getCreateTime());
			System.out.println("����ί����:"+task.getAssignee());
			System.out.println("����ʵ��ID:"+task.getProcessInstanceId());
		}
		/*
		 ����ID:2504
		��������:�û��ڵ�
		���񴴽�ʱ��:Fri Mar 09 16:31:19 CST 2018
		����ί����:vole
		����ʵ��ID:2501
		*/
	}
	
	/**
	 * �������
	 */
	@Test
	public void completeTask(){
		processEngine.getTaskService() // �������Service
			.complete("2504");
		System.out.println("�������");
	}
}
