package com.java.service;

import java.util.List;
import java.util.Map;

import com.java.entity.StrainGage;
import com.java.entity.User;

public interface StrainGageService {

	/**
	 * 添加StrainGage申请
	 * @param map
	 * @return
	 */
	public int add(Map<String, Object> map);
	
	/**
	 * 查询所有的StrainGage申请单列表
	 * @return
	 */
	public List<StrainGage> List(Map<String, Object> map);
	
	/**
	 * 统计全部记录数
	 * @return
	 */
	public int getTotal();
	
	/**
	 * 根据Id查找数据
	 * @param id
	 * @return
	 */
	public StrainGage findById(Integer id);
	
	/**
	 * 更新StrainGage表的State和ProcessInstanceId
	 * @param strainGage
	 * @return
	 */
	public int updateStrainGage(StrainGage strainGage,Map<String, Object> map);
	
}
