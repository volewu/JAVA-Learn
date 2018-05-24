package com.vole.service;

import com.vole.entity.User;

import java.util.List;

/**
 * 编写者： vole
 * Time： 2018/5/23.14:12
 * 内容：
 */
public interface UserService {

    List<User> findAll();

    int add(User user);

}
