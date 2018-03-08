package com.vole.shiro;

import java.util.Arrays;

import org.apache.shiro.subject.Subject;
import org.junit.Test;

import com.vole.util.ShiroUtil;

/**
 * 
 * @User: vole
 * @date: 2018年2月1日上午10:22:26
 * @Function:基于权限的访问控制
 */
public class ShiroPermissionTest {

	@Test
	public void testHasPermission() {
		Subject currentUser = ShiroUtil.login("classpath:shiro_permission.ini", "vole", "123456");
		//Subject currentUser = ShiroUtil.login("classpath:shiro_permission.ini", "gakki", "520");
		System.out.println(currentUser.isPermitted("user:select")?"Have user:select":"No user:select");
		boolean [] result= currentUser.isPermitted("user:select","user:add","user:update");
		System.out.println(result[0]?"有 user:select 权限":"没有 user:select 权限");
		System.out.println(result[1]?"有 user:add 权限":"没有 user:add权限");
		System.out.println(result[2]?"有 user:update 权限":"没有 user:update权限");
		System.out.println(currentUser.isPermittedAll("user:select","user:add","user:update")?
				"包含所有权限":"具有部分权限");
		currentUser.logout();
	}

	/**
	 * 调用 checkPermission 时，如果有该权限，则不返回值，直接通过，反之会报 AuthorizationException 异常
	 */
	@Test
	public void testCheckPermission(){
		Subject currentUser = ShiroUtil.login("classpath:shiro_permission.ini", "vole", "123456");
		currentUser.checkPermission("user:select");
		currentUser.checkPermissions("user:select","user:add","user:update");
		currentUser.logout();
	}
}
