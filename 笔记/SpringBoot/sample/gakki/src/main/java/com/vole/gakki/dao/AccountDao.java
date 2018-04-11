package com.vole.gakki.dao;

import com.vole.gakki.entity.Account;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 编写者： Wu
 * Time： 2018/4/11.15:03
 * 内容：dao 层
 */
public interface AccountDao extends JpaRepository<Account, Integer> {

}
