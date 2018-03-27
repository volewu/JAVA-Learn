package com.java.service;

import java.util.List;
import java.util.Map;

import com.java.entity.StrainGage;
import com.java.entity.User;

public interface StrainGageService {

	/**
	 * ����StrainGage����
	 * @param map
	 * @return
	 */
	public int add(Map<String, Object> map);
	
	/**
	 * ��ѯ���е�StrainGage���뵥�б�
	 * @return
	 */
	public List<StrainGage> List(Map<String, Object> map);
	
	/**
	 * ͳ��ȫ����¼��
	 * @return
	 */
	public int getTotal();
	
	/**
	 * ����Id��������
	 * @param id
	 * @return
	 */
	public StrainGage findById(Integer id);
	
	/**
	 * ����StrainGage����State��ProcessInstanceId
	 * @param strainGage
	 * @return
	 */
	public int updateStrainGage(StrainGage strainGage,Map<String, Object> map);
	
}