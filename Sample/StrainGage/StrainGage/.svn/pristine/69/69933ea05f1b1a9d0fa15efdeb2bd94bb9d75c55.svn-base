package com.java.realm;


import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.java.entity.User;
import com.java.service.UserService;


public class MyRealm extends AuthorizingRealm{

	@Resource
	private UserService userService;
	
	/**
	 * 为当限前登录的用户授予角色和权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		//String userName=(String)principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
		//System.out.println("包含的角色： "+userService.getRoles(userName));
		
/*		User user=userService.getRoles(userName);
		List<Role> list=user.getRoles();
		Set<String> set=new HashSet<String>();
		for (Role role : list) {
			set.add(role.getRoleName());
			
		}
		
	//	System.out.println("包含的角色： "+set);
		
		authorizationInfo.setRoles(set);*/
		
		//authorizationInfo.setStringPermissions(userService.getPermissions(userName));
		return authorizationInfo;
	}

	/**
	 * 验证当前登录的用户
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String userName=(String)token.getPrincipal();
			User user=userService.getByUserName(userName);
			if(user!=null){
				AuthenticationInfo authcInfo=new SimpleAuthenticationInfo(user.getId(),user.getPassword(),"xx");
				return authcInfo;
			}else{
				return null;				
			
	}

	}
}


