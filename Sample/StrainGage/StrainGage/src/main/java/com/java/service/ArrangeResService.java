package com.java.service;

import java.util.List;
import java.util.Map;

import com.java.entity.ArrangeRes;



/**
 * 角色Service
 * @author user
 *
 */
public interface ArrangeResService {

	/**
	 *查找全部的排配信息 
	 * @param map
	 * @return
	 */
	public List<ArrangeRes> findAllList(Map<String, Object> map); 
		
	/**
	 *统计排配信息记录数 
	 * @param map
	 * @return
	 */
	public int countAllList(Map<String, Object> map); 
	
}
