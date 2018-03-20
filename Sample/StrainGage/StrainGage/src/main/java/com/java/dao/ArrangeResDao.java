package com.java.dao;

import java.util.List;
import java.util.Map;

import com.java.entity.Arrange;
import com.java.entity.ArrangeRes;

public interface ArrangeResDao {
	
	/**
	 *����ȫ����������Ϣ 
	 * @param map
	 * @return
	 */
	public List<ArrangeRes> findAllList(Map<String, Object> map); 
		
	/**
	 *ͳ��������Ϣ��¼�� 
	 * @param map
	 * @return
	 */
	public int countAllList(Map<String, Object> map); 
	
	/**
	 * �����������Ϣ
	 * @param map
	 * @return
	 */
	public int addArrangeRes(Map<String, Object> map); 
}
