package com.example.superpay.dao;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.entity.Order;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class OrderDao {
    @Autowired
    private MongoTemplate mongoTemplate;
    public Page<Order> getOrders(String id, Pageable pageable){
        if (StringUtils.isEmpty(id)) return null;
        Criteria criteria =new Criteria();
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where("_id").regex(id));
        criteriaList.add(Criteria.where("orderId").regex(id));
        criteriaList.add(Criteria.where("outTradeNo").regex(id));
        criteriaList.add(Criteria.where("tradeNo").regex(id));
        criteria.orOperator(criteriaList);
        AggregationResults<Order> results = mongoTemplate.aggregate(newAggregation(
                match(criteria)
                ,sort(pageable.getSort())
//                ,skip(pageable.getPageNumber())
                ,limit(pageable.getPageSize())
        ), "order", Order.class);
//        System.out.printf("ids: %s\n", results.getMappedResults());
        Long count = getOrdersCount(id);
        int total = count> 0?Long.valueOf(count/ pageable.getPageSize()).intValue():0;
        double to = count> 0?count.doubleValue()/ pageable.getPageSize():0;
        return new Page<Order>() {
            @Override
            public int getTotalPages() {
                return to>total?total+1:total;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public <U> Page<U> map(Function<? super Order, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List<Order> getContent() {
                return results.getMappedResults();
            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<Order> iterator() {
                return null;
            }
        };
    }
    public Long getOrdersCount(String id){
        if (StringUtils.isEmpty(id)) return 0L;
        Criteria criteria =new Criteria();
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where("_id").regex(id));
        criteriaList.add(Criteria.where("orderId").regex(id));
        criteriaList.add(Criteria.where("outTradeNo").regex(id));
        criteriaList.add(Criteria.where("tradeNo").regex(id));
        criteria.orOperator(criteriaList);
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                match(criteria)
                ,group().count().as("count")), "order", JSONObject.class);
//        System.out.printf("ids: %s\n", results.getMappedResults());
        if(results.getMappedResults().isEmpty()) return 0L;
        return results.getMappedResults().get(0).getLong("count");
    }
    public List<Order> getOrders(List<String> ids){
        if (ids.isEmpty()) return new ArrayList<>();
        Criteria criteria =new Criteria();
        List<Criteria> criteriaList = new ArrayList<>();
        for (String id : ids) {
            criteriaList.add(Criteria.where("_id").is(id));
        }
        criteria.orOperator(criteriaList);
        AggregationResults<Order> results = mongoTemplate.aggregate(newAggregation(
                match(criteria)
        ), "order", Order.class);
//        System.out.printf("ids: %s\n", results.getMappedResults());
        return results.getMappedResults();
    }
    public Double getOrderAdminSum(long start, long end){
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                match(Criteria.where("state").is(1))
                ,match(Criteria.where("addTime").gte(start).lte(end))
                ,group().sum("money").as("sum")
        ), "order", JSONObject.class);
        if(results.getMappedResults().isEmpty()) return 0D;
        return results.getMappedResults().get(0).getDouble("sum");
    }
    public Double getOrderSum(String uid,long start, long end){
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                match(Criteria.where("uid").is(uid))
                ,match(Criteria.where("state").is(1))
                ,match(Criteria.where("addTime").gte(start).lte(end))
                ,group().sum("money").as("sum")
//                ,match(Criteria.where("uid").is(uid))
//                project("count"),
//                sort(Sort.Direction.DESC, "count"),
//                match(Criteria.where("addTime").gte(start).lte(end)),
//               match(Criteria.where("uid").is(uid))
        ), "order", JSONObject.class);
//        System.out.printf(results.getMappedResults().toString());
//        List<ArticleResult> tagCount = results.getMappedResults();
//        for (ArticleResult studentResult : tagCount) {
//            System.out.println(studentResult.getName() + "\t" + studentResult.getCount());
//        }
        if(results.getMappedResults().isEmpty()) return 0D;
        return results.getMappedResults().get(0).getDouble("sum");
    }
    public Double getOrderSum(long start, long end){
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                match(Criteria.where("addTime").gte(start).lte(end))
                ,match(Criteria.where("state").is(1))
                ,group().sum("money").as("sum")), "order", JSONObject.class);
        if(results.getMappedResults().isEmpty()) return 0D;
        return results.getMappedResults().get(0).getDouble("sum");
    }
    public Double getOrderSum(String uid){
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                match(Criteria.where("uid").is(uid))
                ,match(Criteria.where("state").is(1))
                ,group().sum("money").as("sum")), "order", JSONObject.class);
        if(results.getMappedResults().isEmpty()) return 0D;
        return results.getMappedResults().get(0).getDouble("sum");
    }
    public long getOrderAdminCount(long start,long end){
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                match(Criteria.where("addTime").gte(start).lte(end))
                ,group().count().as("count")), "order", JSONObject.class);
        if(results.getMappedResults().isEmpty()) return 0;
        return results.getMappedResults().get(0).getLong("count");
    }
    public long getOrderCount(String uid, long start,long end){
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                match(Criteria.where("uid").is(uid))
                ,match(Criteria.where("addTime").gte(start).lte(end))
                ,group().count().as("count")), "order", JSONObject.class);
        if(results.getMappedResults().isEmpty()) return 0;
        return results.getMappedResults().get(0).getLong("count");
    }
    public long getOrderCount(long start,long end, int state){
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                match(Criteria.where("state").is(state))
                ,match(Criteria.where("addTime").gte(start).lte(end))
                ,group().count().as("count")), "order", JSONObject.class);
        if(results.getMappedResults().isEmpty()) return 0;
        return results.getMappedResults().get(0).getLong("count");
    }
    public long getOrderCount(String uid, long start,long end, int state){
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                match(Criteria.where("uid").is(uid))
                ,match(Criteria.where("state").is(state))
                ,match(Criteria.where("addTime").gte(start).lte(end))
                ,group().count().as("count")), "order", JSONObject.class);
        if(results.getMappedResults().isEmpty()) return 0;
        return results.getMappedResults().get(0).getLong("count");
    }
    public long getOrderCount(long start,long end){
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                match(Criteria.where("addTime").gte(start).lte(end))
                ,group().count().as("count")), "order", JSONObject.class);
        if(results.getMappedResults().isEmpty()) return 0;
        return results.getMappedResults().get(0).getLong("count");
    }
    public long getOrderCount(String uid, int state){
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                match(Criteria.where("uid").is(uid))
                ,match(Criteria.where("state").is(state))
                ,group().count().as("count")), "order", JSONObject.class);
        if(results.getMappedResults().isEmpty()) return 0;
        return results.getMappedResults().get(0).getLong("count");
    }
    public long getOrderCount(){
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                group().count().as("count")), "order", JSONObject.class);
        if(results.getMappedResults().isEmpty()) return 0;
        return results.getMappedResults().get(0).getLong("count");
    }
    public long getOrderCount(int state){
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                match(Criteria.where("state").is(state))
                ,group().count().as("count")), "order", JSONObject.class);
        if(results.getMappedResults().isEmpty()) return 0;
        return results.getMappedResults().get(0).getLong("count");
    }
    public long getOrderCount(String uid){
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                match(Criteria.where("uid").is(uid))
                ,group().count().as("count")), "order", JSONObject.class);
        if(results.getMappedResults().isEmpty()) return 0;
        return results.getMappedResults().get(0).getLong("count");
    }

    public Double getAllMoney(String uid){
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                match(Criteria.where("uid").is(uid))
                ,match(Criteria.where("state").is(1))
                ,group().sum("totalFee").as("sum").sum("fee").as("fee")
        ), "order", JSONObject.class);
        if(results.getMappedResults().isEmpty()) return 0D;
        return results.getMappedResults().get(0).getDouble("sum") - results.getMappedResults().get(0).getDouble("fee");
    }
    public Double getAllMoney(){
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                match(Criteria.where("state").is(1))
                ,group().sum("totalFee").as("sum").sum("fee").as("fee")
        ), "order", JSONObject.class);
        if(results.getMappedResults().isEmpty()) return 0D;
        return results.getMappedResults().get(0).getDouble("sum") - results.getMappedResults().get(0).getDouble("fee");
    }
}
