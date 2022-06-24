package com.example.superpay.repository;

import com.example.superpay.entity.PayType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository("PayTypeRepository")
public interface PayTypeRepository extends PagingAndSortingRepository<PayType, String> {
    //分页查询
    public Page<PayType> findAll(Pageable pageable);
    public PayType findAllById(String id);
    public PayType findAllByType(String type);
}
