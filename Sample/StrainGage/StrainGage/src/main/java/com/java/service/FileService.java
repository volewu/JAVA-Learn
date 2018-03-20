package com.java.service;

import java.util.List;
import java.util.Map;

import com.java.entity.File;


/**
 * ��ɫService
 * @author user
 *
 */
public interface FileService {

	/**
	 * ��ҳ���������ļ�
	 * @param map
	 * @return
	 */
	public List<File> findAllFile(Map<String, Object> map);
	
	/**
	 * ͳ�������ļ���¼��
	 * @param map
	 * @return
	 */
	public int countAllFile(Map<String, Object> map);
	
	/**
	 * ����ʼ�
	 * @param map
	 * @return
	 */
	public int addFile(Map<String, Object> map);
	
	/**
	 * ����ɾ�����ļ�
	 * @param ids
	 * @return
	 */
	public int deleteFiles(String[] ids);
	
	/**
	 * ����Id����File
	 * @param id
	 * @return
	 */
	public File findById(String id);
}
