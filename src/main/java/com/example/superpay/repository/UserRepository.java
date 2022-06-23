package com.example.superpay.repository;

import com.example.superpay.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("UserRepository")
public interface UserRepository extends PagingAndSortingRepository<User, String> {
    //分页查询
    public Page<User> findAll(Pageable pageable);
    public User findAllById(String id);
    public User findAllByUsername(String username);
//
//    //根据author查询
//    public List<User> findByAuthor(String author);
//
//    //根据作者和标题查询
//    public List<User> findByAuthorAndTitle(String author, String title);
//
//    //忽略参数大小写
//    public List<User> findByAuthorIgnoreCase(String author);
//
//    //忽略所有参数大小写
//    public List<User> findByAuthorAndTitleAllIgnoreCase(String author, String title);
//
//    //排序
//    public List<User> findByAuthorOrderByVisitCountDesc(String author);
//    public List<User> findByAuthorOrderByVisitCountAsc(String author);
//
//    //自带排序条件
//    public List<User> findByAuthor(String author, Sort sort);
}
