package com.vole.gakki.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 编写者： Wu
 * Time： 2018/4/10.16:48
 * 内容：
 */
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello(){
        return "Spring Boot Hello";
    }
}
