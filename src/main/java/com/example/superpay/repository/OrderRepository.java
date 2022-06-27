package com.example.superpay.repository;

import com.example.superpay.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository("OrderRepository")
public interface OrderRepository  extends PagingAndSortingRepository<Order, String>, CrudRepository<Order, String> {
    //分页查询
    public Page<Order> findAll(Pageable pageable);
    public Page<Order> findAllByUid(String uid,Pageable pageable);
    //精确查询
    public Order findAllById(String id);
    public Order findAllByOutTradeNo(String out_trade_no);
    //统计查询
    public Long findAllByUid(String uid);
}
