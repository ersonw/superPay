package com.example.superpay.repository;

import com.example.superpay.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository("OrderRepository")
public interface OrderRepository  extends PagingAndSortingRepository<Order, String> {
    //分页查询
    public Page<Order> findAll(Pageable pageable);
    public Order findAllById(String id);
}
