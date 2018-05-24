package com.vole.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vole.entity.User;
import com.vole.service.UserService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 编写者： vole
 * Time： 2018/5/23.14:19
 * 内容：
 */
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/add")
    public int add(User user){
        return userService.add(user);
    }

    @RequestMapping("/list")
    public String findAllUser(){
        Map<String,Object> map = new HashMap<>();
        map.put("data",userService.findAll());
        return JSON.toJSONString(userService.findAll());
    }

    @RequestMapping("/map")
    public Object findAll(){
        Map<String,Object> map = new HashMap<>();
        map.put("data",userService.findAll());
        return map;
    }

    @RequestMapping("/page")
    public Object page(){
        PageHelper.startPage(1,5);
        PageInfo<User> result =  new PageInfo<>(userService.findAll());
        return result;
    }
}
