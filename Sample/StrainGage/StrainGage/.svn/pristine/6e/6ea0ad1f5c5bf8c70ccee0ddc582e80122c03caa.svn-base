package com.java.service.Impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.java.dao.FileDao;
import com.java.dao.GroupDao;
import com.java.entity.File;
import com.java.entity.Group;
import com.java.service.FileService;
import com.java.service.GroupService;


/**
 * 角色Service实现类
 * @author user
 *
 */
@Service("fileService")
public class FileServiceImpl implements FileService{

	@Resource
	private FileDao fileDao;

	public List<File> findAllFile(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return fileDao.findAllFile(map);
	}

	public int countAllFile(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return fileDao.countAllFile(map);
	}

	public int addFile(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return fileDao.addFile(map);
	}

	public int deleteFiles(String[] ids) {
		// TODO Auto-generated method stub
		return fileDao.deleteFiles(ids);
	}

	public File findById(String id) {
		// TODO Auto-generated method stub
		return fileDao.findById(id);
	}
	
}
