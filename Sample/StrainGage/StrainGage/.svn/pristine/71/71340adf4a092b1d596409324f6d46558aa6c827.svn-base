package com.java.service.Impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.java.dao.MemberShipDao;
import com.java.entity.MemberShip;
import com.java.service.MemberShipService;


/**
 * 用户角色关系Service实现类
 * @author user
 *
 */
@Service("memberShipService")
public class MemberShipServiceImpl implements MemberShipService{

	@Resource
	private MemberShipDao memberShipDao;
	
	public MemberShip findMemberShip(Map<String, Object> map) {
		return memberShipDao.findMemberShip(map);
	}

	public int deleteAllGroupsByUserId(String userId) {
		return memberShipDao.deleteAllGroupsByUserId(userId);
	}

	public int add(MemberShip memberShip) {
		return memberShipDao.add(memberShip);
	}

}
