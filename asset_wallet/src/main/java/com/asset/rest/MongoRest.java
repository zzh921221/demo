package com.asset.rest;

import com.alibaba.fastjson.JSONObject;
import com.asset.bo.UserInfo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.TransactionOptions;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * MongodbRest
 *
 * @author: zhaozhonghui
 * @create: 2019-07-05 10:55
 */
@Slf4j
@RestController
@RequestMapping("/mongo")
public class MongoRest {
    @Autowired
    private MongoClient client;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @GetMapping("/batchInsert")
    public void batchInsert(){
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            taskExecutor.submit(()->{

                for (int k = 1; k <= 1000; k++) {
                    try(ClientSession clientSession = client.startSession()) {
                        // 开始事务
                        clientSession.startTransaction();
                        // 获取mongodb属性
                        MongoDatabase db = mongoTemplate.getDb();
                        String collectionName = mongoTemplate.getCollectionName(UserInfo.class);
                        MongoCollection<Document> collection = db.getCollection(collectionName);


                        List<Document> list = new ArrayList<>();
                        for (int j = 1; j <= 1000; j++) {
                            log.info("k:{}-----j:{}",k,j);
                            if (j % 5 == 0) {
                                Thread.yield();
                            }
                            UserInfo userInfo = new UserInfo();
                            userInfo.setId((long)j * k);
                            userInfo.setName("小明");
                            userInfo.setAge(j * 7);
                            userInfo.setStatus((byte) 0);
                            list.add(Document.parse(JSONObject.toJSONString(userInfo)));
//                            if (list.size() >= 200) {
//                                collection.insertMany(clientSession,list);
//                                list.clear();
//                                commitWithRetry(clientSession);
//                                boolean flag = clientSession.hasActiveTransaction();
//
//                            }
                            if ( k==1000 && j == 1000 ) {
                                long end = System.currentTimeMillis();
                                log.info("插入1000w数据结束，耗时:{}秒",(end-begin)/1000);
//                                if (!list.isEmpty()) {
//                                    collection.insertMany(clientSession,list);
//                                }
                            }

                        }
                        collection.insertMany(clientSession,list);
                        commitWithRetry(clientSession);
                        clientSession.startTransaction();

                    }
                }

            });
        }
    }

    /**
     * 提交失败时，进行重新提交
     * @param clientSession
     */
   private void commitWithRetry(ClientSession clientSession) {
        while (true) {
            try {
                // 提交事务
                if (clientSession.hasActiveTransaction()) {
                    clientSession.commitTransaction();
                    System.out.println("Transaction committed");
                    break;
                }
            } catch (MongoException e) {
                // can retry commit
                if (e.hasErrorLabel(MongoException.UNKNOWN_TRANSACTION_COMMIT_RESULT_LABEL)) {
                    System.out.println("UnknownTransactionCommitResult, retrying commit operation ...");
                    continue;
                } else {
                    System.out.println("Exception during commit ...");
                    log.info("Exception:{}",e);
                   break;
                }
            }
        }
    }


}
