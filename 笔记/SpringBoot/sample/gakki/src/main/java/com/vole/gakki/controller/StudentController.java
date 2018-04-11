package com.vole.gakki.controller;

import com.vole.gakki.entity.Student;
import com.vole.gakki.service.StudentService;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 编写者： Wu
 * Time： 2018/4/11.16:36
 * 内容：
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private StudentService studentService;


    @RequestMapping("/add")
    public String add(@Valid Student student, BindingResult bindingResult){
            if(bindingResult.hasErrors())
                return bindingResult.getFieldError().getDefaultMessage();
            else {
                studentService.addStudent(student);
                return "添加成功";
            }

    }

}
