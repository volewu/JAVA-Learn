package com.java.service.Impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.java.dao.ArrangeResDao;
import com.java.entity.ArrangeRes;
import com.java.service.ArrangeResService;

/**
 * ≈≈≈‰Service
 * @author F1330026
 *
 */
@Service("arrangeResService")
public class ArrangeResServiceImpl implements ArrangeResService{

	@Resource
	private ArrangeResDao arrageResDao;

	public List<ArrangeRes> findAllList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return arrageResDao.findAllList(map);
	}

	public int countAllList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return arrageResDao.countAllList(map);
	}

	
	
}
