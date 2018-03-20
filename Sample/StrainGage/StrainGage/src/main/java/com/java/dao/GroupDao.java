package com.java.dao;

import java.util.List;
import java.util.Map;

import com.java.entity.Group;


/**
 * 角色Dao
 * @author group
 *
 */
public interface GroupDao {

	/**
	 * 根据条件查询角色集合
	 * @param id
	 * @return
	 */
	public List<Group> find(Map<String,Object> map);
	
	/**
	 * 查询角色集合
	 * @param id
	 * @return
	 */
	public List<Group> findAll();
	
	/**
	 * 通过id查询角色实体
	 * @param id
	 * @return
	 */
	public Group findById(String id);
	
	/**
	 * 根据条件获取总记录数
	 * @param map
	 * @return
	 */
	public Long getTotal(Map<String,Object> map);
	
	/**
	 * 删除角色
	 * @param id
	 * @return
	 */
	public int delete(String id);
	
	/**
	 * 修改角色
	 * @param group
	 * @return
	 */
	public int update(Group group);
	
	/**
	 * 添加角色
	 * @param group
	 * @return
	 */
	public int add(Group group);
	
	/**
	 * 用过用户Id查询角色集合
	 * @param userId
	 * @return
	 */
	public List<Group> findByUserId(String userId);
}
