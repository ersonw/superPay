package com.example.superpay.config;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.util.ToolsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;

@Slf4j
@Repository
public class MongoAnimal<T> {
    @Autowired
    private MongoTemplate mongoTemplate;
    private Class<T> clazz;
    private String collectionName;
    public MongoAnimal(){}
    public MongoAnimal(Class<T> clazz){
        this.clazz = clazz;
        String[] collectionNames = clazz.getCanonicalName().split("\\.");
        this.collectionName = ToolsUtil.toLowerCaseFirstOne(collectionNames[collectionNames.length - 1]);
    }
    public MongoAnimal(Class<T> clazz, String collectionName){
        this.clazz = clazz;
        this.collectionName = collectionName;
    }
    public List<T> aggregate(AggregationOperation... operations){
        AggregationResults<T> results = mongoTemplate.aggregate(Aggregation.newAggregation(operations), this.collectionName, this.clazz);
        return results.getMappedResults();
    }
    public AggregationOperation getSort(Pageable pageable){
        return getSort(pageable.getSort());
    }
    public AggregationOperation getSort(Sort sort){
        return Aggregation.sort(sort);
    }
    public AggregationOperation getLimit(int limit){
        return Aggregation.limit(limit);
    }
    public AggregationOperation getSkip(long skip){
        return Aggregation.skip(skip);
    }
    public AggregationOperation getMatch(String field, Object value){
        return getMatch(where(field,value));
    }public AggregationOperation getMatch(Criteria criteria){
        return Aggregation.match(criteria);
    }
    public Criteria where(String field, Object value){
        return Criteria.where(field).is(value);
    }
    public Criteria where(String field){
        return Criteria.where(field);
    }
    public Criteria where(String field, String value){
        return Criteria.where(field).is(value);
    }
    public Criteria whereIs(String field, String value){
        return Criteria.where(field).is(value);
    }
    public Criteria and(Criteria... criterias){
        Criteria criteria = new Criteria();
        return criteria.andOperator(Arrays.asList(criterias));
    }
    public Criteria or(Criteria... criterias){
        Criteria criteria = new Criteria();
        return criteria.orOperator(Arrays.asList(criterias));
    }
    public AggregationOperation getGroup(){
        return getGroup("count");
    }
    public AggregationOperation getSum(String field){
        return getSum(field,"count");
    }
    public AggregationOperation getSum(String field,String alias){
        return group().sum(field).as(alias);
    }
    public AggregationOperation getGroup(String alias){
        return group().count().as(alias);
    }
    public Long count(AggregationOperation... operations){
        return count("count", operations);
    }
    public Long count(String alias,AggregationOperation... operations){AggregationResults<JSONObject> results = mongoTemplate.aggregate(Aggregation.newAggregation(operations), collectionName, JSONObject.class);
        if(results.getMappedResults().isEmpty()) return 0L;
        return results.getMappedResults().get(0).getLong(alias);
    }
    public List<T> findAll(){
        AggregationOperation limit = Aggregation.limit(3000);
        AggregationOperation sort = Aggregation.sort(Sort.Direction.DESC, "addTime");
        return aggregate(sort,limit);
    }
    protected Page<T> newPage(Pageable pageable, List<T> list, long total) {
        return new Page<T>() {
            @Override
            public int getTotalPages() {
                if (total == 0) return 0;
                if (total < pageable.getPageSize())return 1;
                double dTotal = (total * 1D / pageable.getPageSize());
                long sTotal = total / pageable.getPageSize();
                if (dTotal > sTotal) {
                    sTotal++;
                }
                return (int) sTotal;
            }

            @Override
            public long getTotalElements() {
                return total;
            }

            @Override
            public <U> Page<U> map(Function<? super T, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return pageable.getPageNumber();
            }

            @Override
            public int getSize() {
                return pageable.getPageSize();
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List<T> getContent() {
                return list;
            }

            @Override
            public boolean hasContent() {
                return list.size()>0;
            }

            @Override
            public Sort getSort() {
                return pageable.getSort();
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
            public Iterator<T> iterator() {
                return null;
            }
        };
    }
    public Page<T> findAll(Pageable pageable){
        AggregationOperation limit = Aggregation.limit(pageable.getPageSize());
        AggregationOperation skip = Aggregation.skip(pageable.getOffset());
        AggregationOperation sort = Aggregation.sort(pageable.getSort());
        AggregationResults<T> results = mongoTemplate.aggregate(Aggregation.newAggregation(skip,limit,sort), collectionName, this.clazz);
        long total = count(getGroup());
        return newPage(pageable,results.getMappedResults(),total);
    }
    public List<T> findAllById(String id){
        AggregationOperation limit = Aggregation.limit(3000);
        AggregationOperation sort = Aggregation.sort(Sort.Direction.DESC, "addTime");
        Criteria criteria = new Criteria();
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where("_id").is(id));
        criteria.andOperator(criteriaList);
        AggregationOperation match = Aggregation.match(criteria);
        return aggregate(match, sort,limit);
    }
    public T findById(String id){
        AggregationOperation limit = Aggregation.limit(1);
        AggregationOperation sort = Aggregation.sort(Sort.Direction.DESC, "addTime");
        Criteria criteria = new Criteria();
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where("_id").is(id));
        criteria.andOperator(criteriaList);
        AggregationOperation match = Aggregation.match(criteria);
        List<T> list = aggregate(match, sort,limit);
        if (list.size() == 0) return null;
        return list.get(0);
    }
    public void save(T object) {
        mongoTemplate.save(object);
    }
    public void saveAll(List<T> objects) {
        for (Object object: objects) {
            mongoTemplate.remove(object);
        }
    }
    public void delete(T object) {
        mongoTemplate.remove(object);
    }
    public void deleteAll(List<T> objects) {
        for (Object object: objects) {
            mongoTemplate.remove(object);
        }
    }
    public void remove(Criteria criteria){
        mongoTemplate.remove(Query.query(criteria),this.clazz,this.collectionName);
    }
    public void deleteById(String id){
        remove(whereIs("_id",id));
    }
    public void deleteAllById(List<String> ids){
        for (String id: ids) {
            remove(whereIs("_id",id));
        }
    }

    protected AggregationOperation getSort(Sort.Direction sort, String... fields) {
        return Aggregation.sort(sort,fields);
    }

    public List<T> findAllByIds(List<String> ids) {
        Criteria[] criteriaList = new Criteria[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            criteriaList[i] = where("_id",ids.get(i));
        }
        return aggregate(getMatch(or(criteriaList)));
    }
}
