package com.vole.gakki.controller;

import com.vole.gakki.service.AccountService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 编写者： Wu
 * Time： 2018/4/11.15:13
 * 内容：控制层
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    @RequestMapping("/tranfers")
    public String tranfers() {
        try {
            accountService.transferAccounts(1, 2, 200);
            return "ok";
        } catch (Exception e) {
            return "no";
        }

    }
}
