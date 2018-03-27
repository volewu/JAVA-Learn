package com.java.dao;

import java.util.List;
import java.util.Map;

import com.java.entity.Arrange;

public interface ArrangeDao {
	
	/**
	 *����ȫ����������Ϣ 
	 * @param map
	 * @return
	 */
	public List<Arrange> findAllList(Map<String, Object> map); 
		
	/**
	 *ͳ��������Ϣ��¼�� 
	 * @param map
	 * @return
	 */
	public int countAllList(Map<String, Object> map); 
	
	/**
	 * �����µ�������Ϣ
	 * @param map
	 * @return
	 */
	public int addArrange(Map<String, Object> map);
	
	/**
	 * ����ѡ��Id��������Ϣ
	 * @param map
	 * @return
	 */
	public int updateArrange(Map<String, Object> map);
	
	/**
	 * ����״̬Ϊ������������ʱ��ɾ��������Ϣ
	 * @param map
	 * @return
	 */
	public int deleteArrange(Map<String, Object> map);
}