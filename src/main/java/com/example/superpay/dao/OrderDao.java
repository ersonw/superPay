package com.example.superpay.dao;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.util.TimeUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class OrderDao {
    @Autowired
    private MongoTemplate mongoTemplate;
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
