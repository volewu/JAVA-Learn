package com.java.service;

import java.util.List;
import java.util.Map;

import com.java.entity.Arrange;



/**
 * 角色Service
 * @author user
 *
 */
public interface ArrangeService {

	/**
	 *查找全部的排配信息 
	 * @param map
	 * @return
	 */
	public List<Arrange> findAllList(Map<String, Object> map); 
		
	/**
	 *统计排配信息记录数 
	 * @param map
	 * @return
	 */
	public int countAllList(Map<String, Object> map); 
	

	/**
	 * 更新选中Id的排配信息
	 * @param map
	 * @return
	 */
	public int updateArrange(Map<String, Object> map);
	
	/**
	 * 处理状态为“处理结束”时，删除排配信息
	 * @param map
	 * @return
	 */
	public int deleteArrange(Map<String, Object> map);
}
