package com.java.service;

import java.util.List;
import java.util.Map;

import com.java.entity.File;


/**
 * 角色Service
 * @author user
 *
 */
public interface FileService {

	/**
	 * 分页查找所有文件
	 * @param map
	 * @return
	 */
	public List<File> findAllFile(Map<String, Object> map);
	
	/**
	 * 统计所有文件记录数
	 * @param map
	 * @return
	 */
	public int countAllFile(Map<String, Object> map);
	
	/**
	 * 添加问价
	 * @param map
	 * @return
	 */
	public int addFile(Map<String, Object> map);
	
	/**
	 * 批量删除问文件
	 * @param ids
	 * @return
	 */
	public int deleteFiles(String[] ids);
	
	/**
	 * 根据Id查找File
	 * @param id
	 * @return
	 */
	public File findById(String id);
}
