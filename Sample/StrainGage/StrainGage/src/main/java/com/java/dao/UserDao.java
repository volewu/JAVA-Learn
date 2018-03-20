package com.java.dao;

import java.util.List;
import java.util.Map;

import com.java.entity.User;

/**
 * �û�Dao
 * @author user
 *
 */
public interface UserDao {

	/**
	 * ͨ��id��ѯ�û�ʵ��
	 * @param id
	 * @return
	 */
	public User findById(String id);
	
	/**
	 * ����������ѯ�û�����
	 * @param map
	 * @return
	 */
	public List<User> find(Map<String,Object> map);
	
	/**
	 * ����������ȡ�ܼ�¼��
	 * @param map
	 * @return
	 */
	public Long getTotal(Map<String,Object> map);
	
	/**
	 * ɾ���û�
	 * @param id
	 * @return
	 */
	public int delete(String id);
	
	/**
	 * �޸��û�
	 * @param user
	 * @return
	 */
	public int update(User user);
	
	/**
	 * ����û�
	 * @param user
	 * @return
	 */
	public int add(User user);
	
	/**
	 * ��ӿμ��û�
	 * @param user
	 * @return
	 */
	public int addKjUser(Map<String, Object> map);
	
	/**
	 * ��Ӳ����û�
	 * @param user
	 * @return
	 */
	public int addBjUser(Map<String, Object> map);
	
	/**
	 * �����û��������û�
	 * @param userName
	 * @return
	 */
	public User getByUserName(String userName);
	
	
	/**
	 * ����������Id���Ҿ�����ͬId������
	 * @param userId
	 * @return
	 */
	 public List<User> findGcsByUserId(String rev);
	
	/**
	 * ����������Id���ҿμ�����Id
	 * @param userId
	 * @return
	 */
	public List<User> findKjByUserId(String rev);
	
	/**
	 * ����������Id���Ҳ�������Id
	 * @param rev
	 * @return
	 */
	public List<User> findBjByUserId(String rev);
	
	/**
	 * �������пμ�����
	 * @param map
	 * @return
	 */
	public List<User> findAllKjZg(Map<String, Object> map);
	
	/**
	 * �������пμ������ܼ�¼��
	 * @param map
	 * @return
	 */
	public int getTotalKjZg(Map<String,Object> map);
	
	
	/**
	 * �������в�������
	 * @param map
	 * @return
	 */
	public List<User> findAllBjZg(Map<String, Object> map);
	
	/**
	 * �������в��������ܼ�¼��
	 * @param map
	 * @return
	 */
	public int getTotalBjZg(Map<String,Object> map);
}
