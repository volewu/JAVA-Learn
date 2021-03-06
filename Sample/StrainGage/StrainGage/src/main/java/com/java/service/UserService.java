package com.java.service;

import java.util.List;
import java.util.Map;

import com.java.entity.User;

public interface UserService {

	/**
	 * 通过用户名查询用户
	 * @param userName
	 * @return
	 */
	public User getByUserName(String userName);
	
	/**
	 * 通过id查询用户实体
	 * @param id
	 * @return
	 */
	public User findById(String id);
	
	/**
	 * 根据条件查询用户集合
	 * @param map
	 * @return
	 */
	public List<User> find(Map<String,Object> map);
	
	/**
	 * 根据条件获取总记录数
	 * @param map
	 * @return
	 */
	public Long getTotal(Map<String,Object> map);
	
	/**
	 * 删除用户
	 * @param id
	 * @return
	 */
	public int delete(String id);
	
	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	public int update(User user);
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	public int add(User user);
	
	/**
	 * 添加课级用户
	 * @param user
	 * @return
	 */
	public int addKjUser(Map<String, Object> map);
	
	/**
	 * 添加部级用户
	 * @param user
	 * @return
	 */
	public int addBjUser(Map<String, Object> map);
	
	
	/**
	 * 根据申请人Id查找具有相同Id申请人
	 * @param userId
	 * @return
	 */
	 public List<User> findGcsByUserId(String rev);
	
	
	/**
	 * 根据申请人Id查找课级主管Id
	 * @param userId
	 * @return
	 */
	public List<User> findKjByUserId(String rev);
	
	/**
	 * 根据申请人Id查找部级主管Id
	 * @param rev
	 * @return
	 */
	public List<User> findBjByUserId(String rev);
	
	/**
	 * 查找所有课级主管
	 * @param map
	 * @return
	 */
	public List<User> findAllKjZg(Map<String, Object> map);
	
	/**
	 * 查找所有课级主管总记录数
	 * @param map
	 * @return
	 */
	public int getTotalKjZg(Map<String,Object> map);
	
	/**
	 * 查找所有部级主管
	 * @param map
	 * @return
	 */
	public List<User> findAllBjZg(Map<String, Object> map);
	
	/**
	 * 查找所有部级主管总记录数
	 * @param map
	 * @return
	 */
	public int getTotalBjZg(Map<String,Object> map);
	
}
