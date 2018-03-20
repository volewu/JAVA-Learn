package com.java.service.Impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.java.dao.MemberShipDao;
import com.java.dao.UserDao;
import com.java.entity.User;
import com.java.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource
	private UserDao userDao; 
	
	@Resource
	private MemberShipDao memberShipDao; 
	
	public User getByUserName(String userName) {
		return userDao.getByUserName(userName);
	}

	public User findById(String id) {
		// TODO Auto-generated method stub
		return userDao.findById(id);
	}
	public List<User> find(Map<String, Object> map) {
		return userDao.find(map);
	}

	public Long getTotal(Map<String, Object> map) {
		return userDao.getTotal(map);
	}

	public int delete(String id) {
		return userDao.delete(id);
	}

	public int update(User user) {
		return userDao.update(user);
	}

	public int add(User user) {
		return userDao.add(user);
	}

	public List<User> findKjByUserId(String rev) {
		// TODO Auto-generated method stub
		return userDao.findKjByUserId(rev);
	}

	public List<User> findBjByUserId(String rev) {
		// TODO Auto-generated method stub
		return userDao.findBjByUserId(rev);
	}

	public List<User> findGcsByUserId(String rev) {
		// TODO Auto-generated method stub
		return userDao.findGcsByUserId(rev);
	}

	public List<User> findAllKjZg(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return userDao.findAllKjZg(map);
	}

	public int getTotalKjZg(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return userDao.getTotalKjZg(map);
	}

	public List<User> findAllBjZg(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return userDao.findAllBjZg(map);
	}

	public int getTotalBjZg(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return userDao.getTotalBjZg(map);
	}

	public int addKjUser(Map<String, Object> map) {
			   userDao.addKjUser(map);
		return memberShipDao.addZgMem(map);
	}

	public int addBjUser(Map<String, Object> map) {
		 userDao.addBjUser(map);
	 return memberShipDao.addZgMem(map);
	}

	
}
