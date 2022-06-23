package com.example.superpay.repository;

import com.example.superpay.entity.Login;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository("LoginRepository")
public interface LoginRepository extends PagingAndSortingRepository<Login, String> {
    //分页查询
    public Page<Login> findAll(Pageable pageable);
    public Login findAllById(String id);
    public Login findAllByUid(String uid);
}
