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
		/*
		 * ����ID:15004 ��������:ѧ����� ���񴴽�ʱ��:Mon Mar 12 15:22:47 CST 2018 ����ί����:���� \����
		 * \���� ����ʵ��ID:15001 \17502 \20002
		 */
	}

	/**
	 * �������
	 */
	@Test
	public void completeTask() {
		processEngine.getTaskService() // �������Service
				.complete("20002");// ���� ID
		System.out.println("complete task");
	}

	/**
	 * ��ѯ����״̬������ִ�� or �Ѿ�ִ�н�����--> ��������ʵ����������������ʱ�ı����ݣ��Դ����ж�
	 */
	@Test
	public void processState() {
		ProcessInstance pi = processEngine.getRuntimeService()// ��ȡ����ʱService
				.createProcessInstanceQuery() // ��������ʵ����ѯ
				.processInstanceId("35001")// ������ʵ��id��ѯ
				.singleResult();

		System.out.println(pi != null ? "��������ִ�У�" : "�����Ѿ�ִ�н�����");
	}
	
	/**
	 * ��ʷ�����ѯ--��صı� act_hi_taskinst
	 */
	@Test
	public void historyTaskList(){
		List<HistoricTaskInstance> list=processEngine.getHistoryService() // ��ʷ���Service
			.createHistoricTaskInstanceQuery() // ������ʷ����ʵ����ѯ
			.processInstanceId("15001") // ������ʵ��id��ѯ
			.finished() // ��ѯ�Ѿ���ɵ�����
			.list(); 
		for(HistoricTaskInstance hti:list){
			System.out.println("����ID:"+hti.getId());
			System.out.println("����ʵ��ID:"+hti.getProcessInstanceId());
			System.out.println("�������ƣ�"+hti.getName());
			System.out.println("�����ˣ�"+hti.getAssignee());
			System.out.println("��ʼʱ�䣺"+hti.getStartTime());
			System.out.println("����ʱ�䣺"+hti.getEndTime());
			System.out.println("=================================");
		}
	}
	
	/**
	 * ��ʷ���ѯ��һ�������ַ�ʽ��ѯ��----��ر� act_hi_actinst
	 */
	@Test
	public void historyActInstanceList(){
		List<HistoricActivityInstance>  list=processEngine.getHistoryService() // ��ʷ���Service
			.createHistoricActivityInstanceQuery() // ������ʷ�ʵ����ѯ
			.processInstanceId("15001") // ִ������ʵ��id
			.finished()
			.list();
		for(HistoricActivityInstance hai:list){
			System.out.println("�ID:"+hai.getId());
			System.out.println("����ʵ��ID:"+hai.getProcessInstanceId());
			System.out.println("����ƣ�"+hai.getActivityName());
			System.out.println("�����ˣ�"+hai.getAssignee());
			System.out.println("��ʼʱ�䣺"+hai.getStartTime());
			System.out.println("����ʱ�䣺"+hai.getEndTime());
			System.out.println("=================================");
		}
	}

}
