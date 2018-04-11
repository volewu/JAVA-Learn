package com.vole.gakki.controller;

import com.vole.gakki.dao.BookDao;
import com.vole.gakki.entity.Book;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;

/**
 * 编写者： Wu
 * Time： 2018/4/11.10:36
 * 内容：
 */
@Controller
@RequestMapping("/book")
public class BookController {

    @Resource
    private BookDao bookDao;

    /**
     * 查找图书
     *
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("bookList", bookDao.findAll());
        mav.setViewName("bookList");
        return mav;
    }

    /**
     * 根据条件动态查询:拼接 sql 查询，按条件查询
     *
     * @return
     */
    @RequestMapping("/list2")
    public ModelAndView list2(Book book) {
        ModelAndView mav = new ModelAndView();
        List<Book> bookList = bookDao.findAll((root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if(book!=null){
                if(book.getName()!=null&&!"".equals(book.getName()))
                    predicate.getExpressions().add(cb.like(root.get("name"),"%"+book.getName()+"%"));
                if(book.getName()!=null&&!"".equals(book.getName()))
                    predicate.getExpressions().add(cb.like(root.get("author"),"%"+book.getAuthor()+"%"));
            }
            return predicate;
        });
        mav.addObject("bookList", bookList);
        mav.setViewName("bookList");
        return mav;
    }


    /**
     * 添加图书
     *
     * @param book
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(Book book) {
        bookDao.save(book);
        return "forward:/book/list";
    }

    /**
     * 根据id查询book实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/preUpdate/{id}")
    public ModelAndView preUpdate(@PathVariable("id") Integer id) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("book", bookDao.getOne(id));
        mav.setViewName("bookUpdate");
        return mav;
    }

    /**
     * 修改图书
     *
     * @param book
     * @return
     */
    @PostMapping("/update")
    public String update(Book book) {
        bookDao.save(book);
        return "forward:/book/list";
    }

    /**
     * 删除图书
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public String delete(Integer id) {
        bookDao.delete(id);
        return "forward:/book/list";
    }

    @ResponseBody
    @GetMapping("/findByName")
    public List<Book> findByName() {
        return bookDao.findByName("编程");
    }

    @ResponseBody
    @GetMapping("/randomList")
    public List<Book> randomList() {
        return bookDao.randomList(2);
    }
}
