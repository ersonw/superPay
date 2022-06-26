package com.example.superpay.dao;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class WithDrawDao {
    @Autowired
    private MongoTemplate mongoTemplate;
    public Double getWithDrawSum(String uid){
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                match(Criteria.where("uid").is(uid))
                ,group().sum("money").as("sum")), "withdraw", JSONObject.class);
        if(results.getMappedResults().isEmpty()) return 0D;
        return results.getMappedResults().get(0).getDouble("sum");
    }
    public Double getWithDrawSum(String uid, int state){
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(newAggregation(
                match(Criteria.where("uid").is(uid))
                ,match(Criteria.where("state").is(state))
                ,group().sum("money").as("sum")), "withdraw", JSONObject.class);
        if(results.getMappedResults().isEmpty()) return 0D;
        return results.getMappedResults().get(0).getDouble("sum");
    }
}
