package com.example.superpay.dao;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.entity.IpList;
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
public class AdminDao {
    @Autowired
    private MongoTemplate mongoTemplate;
    public Long getOrdersCount(String id){
        if (StringUtils.isEmpty(id)) return 0L;
        Criteria criteria =new Criteria();
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where("_id").regex(id));
        criteriaList.add(Criteria.where("address").regex(id));
        criteria.orOperator(criteriaList);
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                match(criteria)
                ,group().count().as("count")), "ipList", JSONObject.class);
//        System.out.printf("ids: %s\n", results.getMappedResults());
        if(results.getMappedResults().isEmpty()) return 0L;
        return results.getMappedResults().get(0).getLong("count");
    }
    public Page<IpList> getIps(String id, Pageable pageable){
        if (StringUtils.isEmpty(id)) return null;
        Criteria criteria =new Criteria();
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where("_id").regex(id));
        criteriaList.add(Criteria.where("address").regex(id));
        criteria.orOperator(criteriaList);
        AggregationResults<IpList> results = mongoTemplate.aggregate(newAggregation(
                match(criteria)
                ,sort(pageable.getSort())
//                ,skip(pageable.getPageNumber())
                ,limit(pageable.getPageSize())
        ), "ipList", IpList.class);
//        System.out.printf("ids: %s\n", results.getMappedResults());
        Long count = getOrdersCount(id);
        int total = count> 0?Long.valueOf(count/ pageable.getPageSize()).intValue():0;
        double to = count> 0?count.doubleValue()/ pageable.getPageSize():0;
        return new Page<IpList>() {
            @Override
            public int getTotalPages() {
                return to>total?total+1:total;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public <U> Page<U> map(Function<? super IpList, ? extends U> converter) {
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
            public List<IpList> getContent() {
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
            public Iterator<IpList> iterator() {
                return null;
            }
        };
    }
    public boolean isExistsIp(String uid, String ip) {
        Criteria criteria = new Criteria();
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where("uid").is(uid));
        criteriaList.add(Criteria.where("address").is(ip));
        criteria.andOperator(criteriaList);
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                match(criteria)
        ), "ipList", JSONObject.class);
        return !results.getMappedResults().isEmpty();
    }
    public boolean isIpAddress(String uid, String ip){
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                match(Criteria.where("uid").is(uid))
                ,group("address")
        ), "ipList", JSONObject.class);
        if(results.getMappedResults().isEmpty()) {
//            IpList ipList = new IpList(uid,ip);
            IpList ipList = new IpList(uid,"0.0.0.0");
            mongoTemplate.save(ipList);
            return true;
        }
//        IpList ipList = new IpList(uid,"0.0.0.0");
//        mongoTemplate.save(ipList);
//        System.out.printf("ip list:%s\n",results.getMappedResults());
        for (JSONObject o: results.getMappedResults()) {
            if (o != null && o.get("_id") != null
                    && (o.getString("_id").equals("0.0.0.0") || o.getString("_id").equals(ip))){
                return true;
            }
        }
        return false;
    }
}
