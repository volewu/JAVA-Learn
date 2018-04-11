package com.vole.gakki.dao;

import com.vole.gakki.entity.Student;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 编写者： Wu
 * Time： 2018/4/11.16:32
 * 内容：
 */
public interface StudentDao extends JpaRepository<Student, Integer> {

}
