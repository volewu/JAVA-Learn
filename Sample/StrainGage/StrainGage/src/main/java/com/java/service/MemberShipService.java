package com.java.service;

import java.util.Map;

import com.java.entity.MemberShip;


/**
 * 用户角色关系Service
 * @author user
 *
 */
public interface MemberShipService {

	/**
	 * 用户登录
	 * @param map
	 * @return
	 */
	public MemberShip findMemberShip(Map<String,Object> map);
	
	/**
	 * 删除指定用户所有角色
	 * @param userId
	 * @return
	 */
	public int deleteAllGroupsByUserId(String userId);
	
	/**
	 * 添加用户权限
	 * @param memberShip
	 * @return
	 */
	public int add(MemberShip memberShip);
}
