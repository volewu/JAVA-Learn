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
	 * ��ȡĬ����������ʵ�������Զ���ȡactiviti.cfg.xml�ļ�
	 */
	//private ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
	
	/**
	 * �������̶���
	 */
	public void deploy(){
		RepositoryService repositoryService=(RepositoryService) ac.getBean("repositoryService");
		Deployment deployment=repositoryService // ��ȡ�������Service
				.createDeployment() // ��������
				.addClasspathResource("flow/StrainGageProcess.bpmn") // ������Դ�ļ�
				.addClasspathResource("flow/StrainGageProcess.png") // ������Դ�ļ�
				.name("StrainGage��������") // ��������
				.deploy(); // ����
		System.out.println("���̲���ID:"+deployment.getId()); 
		System.out.println("���̲���Name:"+deployment.getName());
	}
	
	
	/**
	 * ��������ʵ��
	 */
	
	public void start(){
		Map<String,Object> variables=new HashMap<String,Object>();
		variables.put("applyIds", "java1");
		//variables.put("userIds", "java11");
		//variables.put("bjIds", "bjzg");
		RuntimeService runtimeService=(RuntimeService) ac.getBean("runtimeService");
		ProcessInstance pi=runtimeService // ����ʱService
			.startProcessInstanceByKey("strainGageProcess2",variables); // ���̶�����KEY�ֶ�ֵ
		System.out.println("����ʵ��ID:"+pi.getId());
		System.out.println("���̶���ID:"+pi.getProcessDefinitionId()); 
	}
	
	
	/**
	 * �鿴����
	 */
	
	public void findTask(){
		TaskService taskService=(TaskService) ac.getBean("taskService");
		List<Task> taskList=taskService // �������Service
			.createTaskQuery() // ���������ѯ
			//.taskAssignee("java") // ָ��ĳ����
			.taskCandidateUser("java")
			.list();
		for(Task task:taskList){
			System.out.println("����ID:"+task.getId()); 
			System.out.println("��������:"+task.getName());
			System.out.println("���񴴽�ʱ��:"+task.getCreateTime());
			System.out.println("����ί����:"+task.getAssignee());
			System.out.println("����ʵ��ID:"+task.getProcessInstanceId());
		}
	}
	
	
	/**
	 * �������
	 */
	
	public void completeTask(){
		TaskService taskService=(TaskService) ac.getBean("taskService"); // �������Service
				taskService.complete("287505");  //����ID
	}
	
	
	/**
	 * �������ʱ���ñ���
	 */
	
	public void completeTask2(){
		TaskService taskService=(TaskService) ac.getBean("taskService"); // �������Service
		Map<String, Object> variables=new HashMap<String, Object>();  
		variables.put("msg", "ͨ��");
		variables.put("bjIds", "java");
		//variables.put("info", "��Ҫ");
		taskService // �������Service
			.complete("297505", variables);  //����ID
	}
	
	
	/**
	 * ��ѯ����״̬������ִ�� or �Ѿ�ִ�н�����
	 */
	
	public void processState(){
		RuntimeService runtimeService=(RuntimeService) ac.getBean("runtimeService");
		ProcessInstance pI=runtimeService // ��ȡ����ʱService
		.createProcessInstanceQuery()
		.processInstanceId("190001")
		.singleResult();
		
		if(pI!=null){
			System.out.println("��������ִ�У�");
		}else{
			System.out.println("�����Ѿ�ִ�н�����");
		}
		
	}
	
	/**
	 * ��ʷ�����ѯ
	 */
	public void historyTaskList(){
		HistoryService historyService=(HistoryService) ac.getBean("historyService");
		List<HistoricTaskInstance>	list=historyService
			.createHistoricTaskInstanceQuery() //������ʷ����ʵ����ѯ
			.processInstanceId("22501") //������ʵ��id��ѯ
			.finished() // ��ѯ�Ѿ���ɵ�����
			.list();
		
		for (HistoricTaskInstance hti : list) {
			System.out.println("����ID:"+hti.getId());
			System.out.println("����ʵ��ID:"+hti.getProcessInstanceId());
			System.out.println("��������:"+hti.getName());
			System.out.println("������:"+hti.getAssignee());
			System.out.println("��ʼʱ��:"+hti.getStartTime());
			System.out.println("����ʱ��:"+hti.getEndTime());
			System.out.println("================================");
		}
	}
	
	/**
	 * ��ʷ���ѯ
	 */
	
	public void historyActInstanceList(){
		HistoryService historyService=(HistoryService) ac.getBean("historyService");
		List<HistoricActivityInstance> list =historyService
			.createHistoricActivityInstanceQuery() //������ʷ����ʵ����ѯ
			.processInstanceId("230001") //������ʵ��id��ѯ
			.finished() // ��ѯ�Ѿ���ɵ�����
			.list();
		
		for (HistoricActivityInstance hti : list) {
			System.out.println("�ID:"+hti.getId());
			System.out.println("����ʵ��ID:"+hti.getProcessInstanceId());
			System.out.println("�����:"+hti.getActivityName());
			System.out.println("������:"+hti.getAssignee());
			System.out.println("��ʼʱ��:"+hti.getStartTime());
			System.out.println("����ʱ��:"+hti.getEndTime());
			System.out.println("================================");
		}
	}
}
