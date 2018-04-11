package com.vole.gakki.service;

/**
 * 编写者： Wu
 * Time： 2018/4/11.15:04
 * 内容：service 层
 */
public interface AccountService {

    void transferAccounts(int fromUser, int toUser, float account);
}
