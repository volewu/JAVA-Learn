package com.java.dao;

import java.util.List;
import java.util.Map;

import com.java.entity.Arrange;
import com.java.entity.ArrangeRes;

public interface ArrangeResDao {
	
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
	
	/**
	 * 添加排配结果信息
	 * @param map
	 * @return
	 */
	public int addArrangeRes(Map<String, Object> map); 
}
