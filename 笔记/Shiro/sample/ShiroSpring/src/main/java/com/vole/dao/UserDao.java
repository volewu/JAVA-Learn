package com.vole.dao;

import java.util.Set;

import com.vole.entity.User;

public interface UserDao {

	/**
	 * ͨ���û�����ѯ�û�
	 * @param userName
	 * @return
	 */
	public User getByUserName(String UserName);

	/**
	 * ͨ���û�����ѯ��ɫ��Ϣ
	 * @param userName
	 * @return
	 */
	public Set<String> getRoles(String userName);

	/**
	 * ͨ���û�����ѯȨ����Ϣ
	 * @param userName
	 * @return
	 */
	public Set<String> getPermissions(String userName);

}
