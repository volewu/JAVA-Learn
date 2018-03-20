package com.java.dao;

import java.util.Map;

import com.java.entity.MemberShip;


/**
 * �û���ɫ����Dao
 * @author user
 *
 */
public interface MemberShipDao {

	/**
	 * �û���¼
	 * @param map
	 * @return
	 */
	public MemberShip findMemberShip(Map<String,Object> map);
	
	/**
	 * ɾ��ָ���û����н�ɫ
	 * @param userId
	 * @return
	 */
	public int deleteAllGroupsByUserId(String userId);
	
	/**
	 * ����û�Ȩ��
	 * @param memberShip
	 * @return
	 */
	public int add(MemberShip memberShip);
	
	/**
	 * ��ӿμ��û�Ȩ��
	 * @param memberShip
	 * @return
	 */
	public int addZgMem(Map<String,Object> map);
}
