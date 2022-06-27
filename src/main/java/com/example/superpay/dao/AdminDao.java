package com.example.superpay.dao;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.entity.IpList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class AdminDao {
    @Autowired
    private MongoTemplate mongoTemplate;
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
