package com.asset.service;

import com.mongodb.session.ClientSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 余额
 * @param
 * @return
 * @author 赵中晖
 * @date 2019/7/3
 */
@Service
public class BalanceService {
    @Autowired
    private MongoTemplate mongoTemplate;

    public void initBalance(){
        Map<String,Object> map=new HashMap<>();
        map.put("id",1);
        map.put("balance",10000L);
        mongoTemplate.insert(map,"balance");
    }

    public Integer updateBalance(long amount){
        Query query = new Query(Criteria.where("id").is(1));
        Update update = new Update().inc("balance",amount);
//        mongoTemplate.update
        return null;
    }
}
