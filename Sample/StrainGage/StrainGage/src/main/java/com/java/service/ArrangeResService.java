package com.java.service;

import java.util.List;
import java.util.Map;

import com.java.entity.ArrangeRes;



/**
 * ��ɫService
 * @author user
 *
 */
public interface ArrangeResService {

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
	
}
