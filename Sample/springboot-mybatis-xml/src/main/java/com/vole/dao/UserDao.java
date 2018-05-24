package com.vole.dao;

import com.vole.entity.User;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 编写者： vole
 * Time： 2018/5/24.10:10
 * 内容： 注解版
 */
@Mapper
public interface UserDao {

    @Select("SELECT * FROM t_user")
    List<User> findAll();

    @Insert(" insert into t_user values(null,#{password},#{username})")
    int add(@Param("username")String username,@Param("password")String password);
//    int add(@Param(User user);
}
