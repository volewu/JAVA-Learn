package com.java.service.Impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.java.dao.ArrangeDao;
import com.java.dao.ArrangeResDao;
import com.java.entity.Arrange;
import com.java.service.ArrangeService;

/**
 * ≈≈≈‰Service
 * @author F1330026
 *
 */
@Service("arrangeService")
public class ArrangeServiceImpl implements ArrangeService{

	@Resource
	private ArrangeDao arrageDao;
	
	@Resource
	private ArrangeResDao arrageResDao;

	public List<Arrange> findAllList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return arrageDao.findAllList(map);
	}

	public int countAllList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return arrageDao.countAllList(map);
	}

	public int updateArrange(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return arrageDao.updateArrange(map);
	}

	
	public int deleteArrange(Map<String, Object> map) {
		// TODO Auto-generated method stub
		arrageResDao.addArrangeRes(map);
		return arrageDao.deleteArrange(map);
	}

	
}
