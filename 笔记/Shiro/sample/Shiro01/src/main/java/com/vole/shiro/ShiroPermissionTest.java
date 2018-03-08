package com.vole.shiro;

import java.util.Arrays;

import org.apache.shiro.subject.Subject;
import org.junit.Test;

import com.vole.util.ShiroUtil;

/**
 * 
 * @User: vole
 * @date: 2018��2��1������10:22:26
 * @Function:����Ȩ�޵ķ��ʿ���
 */
public class ShiroPermissionTest {

	@Test
	public void testHasPermission() {
		Subject currentUser = ShiroUtil.login("classpath:shiro_permission.ini", "vole", "123456");
		//Subject currentUser = ShiroUtil.login("classpath:shiro_permission.ini", "gakki", "520");
		System.out.println(currentUser.isPermitted("user:select")?"Have user:select":"No user:select");
		boolean [] result= currentUser.isPermitted("user:select","user:add","user:update");
		System.out.println(result[0]?"�� user:select Ȩ��":"û�� user:select Ȩ��");
		System.out.println(result[1]?"�� user:add Ȩ��":"û�� user:addȨ��");
		System.out.println(result[2]?"�� user:update Ȩ��":"û�� user:updateȨ��");
		System.out.println(currentUser.isPermittedAll("user:select","user:add","user:update")?
				"��������Ȩ��":"���в���Ȩ��");
		currentUser.logout();
	}

	/**
	 * ���� checkPermission ʱ������и�Ȩ�ޣ��򲻷���ֵ��ֱ��ͨ������֮�ᱨ AuthorizationException �쳣
	 */
	@Test
	public void testCheckPermission(){
		Subject currentUser = ShiroUtil.login("classpath:shiro_permission.ini", "vole", "123456");
		currentUser.checkPermission("user:select");
		currentUser.checkPermissions("user:select","user:add","user:update");
		currentUser.logout();
	}
}
