package com.vole.gakki.dao;

import com.vole.gakki.entity.Book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 编写者： Wu
 * Time： 2018/4/11.10:34
 * 内容：
 */
public interface BookDao extends JpaRepository<Book,Integer>,JpaSpecificationExecutor<Book> {

    //模糊查询 sql 方式，1 代表第一个参数
    @Query("select b from Book b where b.name like %?1%")
    List<Book> findByName(String name);

    //本地 sql 方式，随机查询几条数据，1 代表第一个参数
    @Query(value = "select * from t_book order by RAND() limit ?1",nativeQuery = true)
    List<Book> randomList(Integer number);
}
