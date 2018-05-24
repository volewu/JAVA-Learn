package com.vole.service.impl;

import com.github.pagehelper.PageHelper;
import com.vole.dao.UserMapper;
import com.vole.service.UserService;
import com.vole.entity.User;

import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

/**
 * 编写者： vole
 * Time： 2018/5/23.14:13
 * 内容：
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> findAll() {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
//        PageHelper.startPage(1, 5);
        return userMapper.findAll();
    }

    @Override
    public int add(User user) {
        return userMapper.add(user);
    }




}
