package com.java.service.Impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.java.dao.ArrangeDao;
import com.java.dao.StrainGageDao;
import com.java.entity.StrainGage;
import com.java.service.StrainGageService;

@Service("strainGageService")
public class StrainGageServiceImpl implements StrainGageService {

	@Resource
	private StrainGageDao strainGageDao;
	
	@Resource
	private ArrangeDao arrangeDao;

	public int add(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return strainGageDao.add(map);
	}

	public java.util.List<StrainGage> List(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return strainGageDao.List(map);
	}

	public int getTotal() {
		// TODO Auto-generated method stub
		return strainGageDao.getTotal();
	}

	public StrainGage findById(Integer id) {
		// TODO Auto-generated method stub
		return strainGageDao.findById(id);
	}

	public int updateStrainGage(StrainGage strainGage,Map<String, Object> map) {
		// TODO Auto-generated method stub
		if (!map.isEmpty()) {
			
			arrangeDao.addArrange(map);
		}
		return strainGageDao.updateStrainGage(strainGage);
	} 
	
	
	
}
