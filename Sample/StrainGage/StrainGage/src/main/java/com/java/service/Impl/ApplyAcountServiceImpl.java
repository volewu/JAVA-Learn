package com.java.service.Impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.java.dao.ApplyAcountDao;
import com.java.service.ApplyAcountService;

@Service("applyAcountService")
public class ApplyAcountServiceImpl implements ApplyAcountService {
	
	@Resource
	private ApplyAcountDao applyAcountDao;

	public int addApplyAcount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return applyAcountDao.addApplyAcount(map);
	}

}
