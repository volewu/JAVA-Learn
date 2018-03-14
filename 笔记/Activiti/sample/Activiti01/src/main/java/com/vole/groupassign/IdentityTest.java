package com.vole.groupassign;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.junit.Test;

public class IdentityTest {

	/**
	 * ��ȡĬ����������ʵ�������Զ���ȡactiviti.cfg.xml�ļ�
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * ����û�����
	 */
	@Test
	public void testSaveUser() {
		IdentityService identityService = processEngine.getIdentityService();
		User user = new UserEntity();
		user.setId("wuji");
		user.setPassword("123456");
		user.setEmail("wuvole@gmail.com");
		identityService.saveUser(user );
		System.out.println("��� success");
	}
	
	/**
	 * ɾ���û�
	 */
	@Test
	public void testDeleteUser(){
		IdentityService indentityService=processEngine.getIdentityService();
		indentityService.deleteUser("gakki");
		System.out.println("ɾ�� success");
	}
	
	/**
	 * �����
	 */
	@Test
	public void testSaveGroup(){
		IdentityService indentityService=processEngine.getIdentityService();
		Group group=new GroupEntity();
		group.setId("test");
		indentityService.saveGroup(group);
		System.out.println("��� success");
	}
	
	/**
	 * ����ɾ����
	 */
	@Test
	public void testDeleteGroup(){
		IdentityService indentityService=processEngine.getIdentityService();
		indentityService.deleteGroup("test");
		System.out.println("ɾ�� success");
	}
	
	/**
	 * ��������û�����Ĺ�����ϵ
	 */
	@Test
	public void testSaveMembership(){
		IdentityService indentityService=processEngine.getIdentityService();
		indentityService.createMembership("gakki", "dev");
		System.out.println("��� success");
	}
	
	/**
	 * ����ɾ���û�����Ĺ�����ϵ
	 */
	@Test
	public void testDeleteMembership(){
		IdentityService indentityService=processEngine.getIdentityService();
		indentityService.deleteMembership("lisi", "dev");
		System.out.println("ɾ�� success");
	}

}
