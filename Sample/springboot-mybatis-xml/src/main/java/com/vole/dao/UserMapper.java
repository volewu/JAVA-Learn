package com.vole.dao;

import com.vole.entity.User;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 编写者： vole
 * Time： 2018/5/24.10:10
 * 内容： xml 版
 */

@Mapper
public interface UserMapper {

    List<User> findAll();

    int add(User user);

}