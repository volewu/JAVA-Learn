package com.vole.shiro;

import java.util.Arrays;

import org.apache.shiro.subject.Subject;
import org.junit.Test;

import com.vole.util.ShiroUtil;

/**
 * 
 * @User: vole
 * @date: 2018��2��1������10:16:25
 * @Function: ���ڽ�ɫ�ķ��ʿ���
 */
public class ShiroRoleTest {

	@Test
	public void testHasRole() {
		Subject currentUser = ShiroUtil.login("classpath:shiro_role.ini", "vole", "123456");
		System.out.println(currentUser.hasRole("role1")?"�� role1 Ȩ��":"û�� role1 Ȩ��");
		
		boolean [] result= currentUser.hasRoles(Arrays.asList("role1","role2","role3"));
		System.out.println(result[0]?"�� role1 Ȩ��":"û�� role1 Ȩ��");
		System.out.println(result[1]?"�� role2 Ȩ��":"û�� role2Ȩ��");
		System.out.println(result[2]?"�� role3 Ȩ��":"û�� role3Ȩ��");
		
		System.out.println(currentUser.hasAllRoles(Arrays.asList("role1","role2","role3"))?
				"��������Ȩ��":"���в���Ȩ��");
		currentUser.logout();
	}
	
	/**
	 * ���� checkRole ʱ������и�Ȩ�ޣ��򲻷���ֵ��ֱ��ͨ������֮�ᱨ AuthorizationException �쳣
	 */
	@Test
	public void testCheckRole(){
		Subject currentUser = ShiroUtil.login("classpath:shiro_role.ini", "vole", "123456");
		currentUser.checkRole("role1");
		currentUser.checkRoles("role1","role2");
		currentUser.logout();
	}

}
