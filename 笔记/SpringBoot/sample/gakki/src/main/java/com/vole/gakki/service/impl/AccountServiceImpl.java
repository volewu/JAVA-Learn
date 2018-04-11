package com.vole.gakki.service.impl;

import com.vole.gakki.dao.AccountDao;
import com.vole.gakki.entity.Account;
import com.vole.gakki.service.AccountService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * 编写者： Wu
 * Time： 2018/4/11.15:08
 * 内容：Service 实现层
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountDao accountDao;

    //实现事物
    @Transactional
    public void transferAccounts(int fromUser, int toUser, float account) {
        Account oneUser = accountDao.getOne(fromUser);
        oneUser.setBalance(oneUser.getBalance()-account);
        accountDao.save(oneUser);

        Account twoUser = accountDao.getOne(toUser);
        twoUser.setBalance(twoUser.getBalance()+account);
        int i =1/0;
        accountDao.save(twoUser);
    }
}
