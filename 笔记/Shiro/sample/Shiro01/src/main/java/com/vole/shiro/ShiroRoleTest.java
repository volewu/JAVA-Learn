package com.vole.shiro;

import java.util.Arrays;

import org.apache.shiro.subject.Subject;
import org.junit.Test;

import com.vole.util.ShiroUtil;

/**
 * 
 * @User: vole
 * @date: 2018年2月1日上午10:16:25
 * @Function: 基于角色的访问控制
 */
public class ShiroRoleTest {

	@Test
	public void testHasRole() {
		Subject currentUser = ShiroUtil.login("classpath:shiro_role.ini", "vole", "123456");
		System.out.println(currentUser.hasRole("role1")?"有 role1 权限":"没有 role1 权限");
		
		boolean [] result= currentUser.hasRoles(Arrays.asList("role1","role2","role3"));
		System.out.println(result[0]?"有 role1 权限":"没有 role1 权限");
		System.out.println(result[1]?"有 role2 权限":"没有 role2权限");
		System.out.println(result[2]?"有 role3 权限":"没有 role3权限");
		
		System.out.println(currentUser.hasAllRoles(Arrays.asList("role1","role2","role3"))?
				"包含所有权限":"具有部分权限");
		currentUser.logout();
	}
	
	/**
	 * 调用 checkRole 时，如果有该权限，则不返回值，直接通过，反之会报 AuthorizationException 异常
	 */
	@Test
	public void testCheckRole(){
		Subject currentUser = ShiroUtil.login("classpath:shiro_role.ini", "vole", "123456");
		currentUser.checkRole("role1");
		currentUser.checkRoles("role1","role2");
		currentUser.logout();
	}

}
