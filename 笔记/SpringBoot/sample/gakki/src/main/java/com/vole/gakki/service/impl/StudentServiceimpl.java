package com.vole.gakki.service.impl;

import com.vole.gakki.dao.StudentDao;
import com.vole.gakki.entity.Student;
import com.vole.gakki.service.StudentService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 编写者： Wu
 * Time： 2018/4/11.16:34
 * 内容：
 */
@Service("studentService")
public class StudentServiceimpl implements StudentService {

    @Resource
    private StudentDao studentDao;

    @Override
    public void addStudent(Student student) {
        studentDao.save(student);
    }
}
