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
	 * 获取默认流程引擎实例，会自动读取activiti.cfg.xml文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 添加用户测试
	 */
	@Test
	public void testSaveUser() {
		IdentityService identityService = processEngine.getIdentityService();
		User user = new UserEntity();
		user.setId("wuji");
		user.setPassword("123456");
		user.setEmail("wuvole@gmail.com");
		identityService.saveUser(user );
		System.out.println("添加 success");
	}
	
	/**
	 * 删除用户
	 */
	@Test
	public void testDeleteUser(){
		IdentityService indentityService=processEngine.getIdentityService();
		indentityService.deleteUser("gakki");
		System.out.println("删除 success");
	}
	
	/**
	 * 添加组
	 */
	@Test
	public void testSaveGroup(){
		IdentityService indentityService=processEngine.getIdentityService();
		Group group=new GroupEntity();
		group.setId("test");
		indentityService.saveGroup(group);
		System.out.println("添加 success");
	}
	
	/**
	 * 测试删除组
	 */
	@Test
	public void testDeleteGroup(){
		IdentityService indentityService=processEngine.getIdentityService();
		indentityService.deleteGroup("test");
		System.out.println("删除 success");
	}
	
	/**
	 * 测试添加用户和组的关联关系
	 */
	@Test
	public void testSaveMembership(){
		IdentityService indentityService=processEngine.getIdentityService();
		indentityService.createMembership("gakki", "dev");
		System.out.println("添加 success");
	}
	
	/**
	 * 测试删除用户和组的关联关系
	 */
	@Test
	public void testDeleteMembership(){
		IdentityService indentityService=processEngine.getIdentityService();
		indentityService.deleteMembership("lisi", "dev");
		System.out.println("删除 success");
	}

}
