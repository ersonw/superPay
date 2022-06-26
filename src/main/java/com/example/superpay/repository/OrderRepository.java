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
    public Order findAllById(String id);
    public Order findAllByOutTradeNo(String out_trade_no);

    /**
     * 统计数量
     */
    public Page<Order> findAllByAddTimeGreaterThanEqual(long time, Pageable pageable);
    public Page<Order> findAllByAddTimeGreaterThanEqualAndAddTimeLessThanEqual(long time, Pageable pageable);

    public Page<Order> findAllByAddTimeGreaterThanEqualAndUid(long time, String uid, Pageable pageable);
    public Page<Order> findAllByAddTimeGreaterThanEqualAndAddTimeLessThanEqualAndUid(long gte, long lte, String uid, Pageable pageable);

    public Long countAllByAddTimeGreaterThanEqual(long time);
    public Long countAllByAddTimeGreaterThanEqualAndAddTimeLessThanEqual(long gte, long lte);

    public Long countAllByAddTimeGreaterThanEqualAndUid(long time, String uid);
    public Long countAllByAddTimeGreaterThanEqualAndAddTimeLessThanEqualAndUid(long gte, long lte, String uid);


//    @Query(value = "{ add_time: {$gte: ?0} }")
//    public Page<Order> getAllByTime(long time, Pageable pageable);
}
