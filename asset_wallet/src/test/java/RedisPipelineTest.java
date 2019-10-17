import com.alibaba.fastjson.JSONObject;
import com.asset.StartApplication;
import com.asset.bo.BalanceBO;
import com.asset.bo.DealContractBO;
import com.asset.bo.UserInfo;
import com.asset.config.ApplicationContextProvider;
import com.asset.repository.BalanceRepository;
import com.asset.repository.UserInfoRepository;
import com.asset.utils.IdWorker;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.TransactionOptions;
import com.mongodb.client.*;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.*;

/**
 * redis通道测试
 *
 * @author: zhaozhonghui
 * @create: 2019-06-25 16:48
 */
@Slf4j
@SpringBootTest(classes = StartApplication.class)
@RunWith(SpringRunner.class)
public class RedisPipelineTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private BalanceRepository balanceRepository;



    /**
     * 普通插入redis
     *
     * @param
     * @return void
     */
    @Test
    public void insertRedis() {
        long begin = System.currentTimeMillis();
        log.info("==========================普通测试开始====================");
        String key = "ASSET_PIPELINE:BTC";

        for (int i = 0; i < 1000; i++) {
            saveContract(i, key);
        }
        long time = (System.currentTimeMillis() - begin) / 1000;
        log.info("========================普通测试结束，用时:{}秒============================", time);

    }

    /**
     * redis幂等性
     *
     * @param
     * @return void
     * @author 赵中晖
     * @date 2019/6/26
     */
    @Test
    public void redisIdempotent() {
        String key = "balance";
        List<Object> objects = stringRedisTemplate.executePipelined(new RedisCallback<Object>() {

            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                connection.multi();
                for (int i = 0; i < 3; i++) {
                    connection.hIncrBy(key.getBytes(), "1".getBytes(), -1);
                }
                connection.exec();
                return null;
            }
        });
//        stringRedisTemplate.exec();
        log.info("自减结果:{}", objects);
//        throw new RuntimeException();
    }

    /**
     * 通过管道插入redis
     *
     * @param
     * @return void
     */
    @Test
    public void insertRedisByPipeline() {
        long begin = System.currentTimeMillis();
        log.info("==========================通道测试开始====================");
        for (int i = 0; i < 1000; i++) {
            final int index = i;
            stringRedisTemplate.executePipelined(new RedisCallback<Object>() {

                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.openPipeline();
//                    connection.multi();
                    for (int j = 0; j < 1000; j++) {
                        String key = "ASSET_PIPELINE:BTC:";
                        DealContractBO bo = new DealContractBO();
                        bo.setContractId(String.valueOf(new IdWorker().nextId()));
                        String orderId = (System.currentTimeMillis() + "").substring(6);
                        bo.setBuyOrderId(Long.valueOf(orderId));
                        bo.setCreateTime(System.currentTimeMillis());
                        bo.setBuyUid(Integer.valueOf(index + "" + j));
                        key = key.concat(bo.getContractId());
                        connection.hSet(key.getBytes(), orderId.getBytes(), JSONObject.toJSONString(bo).getBytes());
                    }

//                }
                    return null;
                }
            });
        }

        long time = (System.currentTimeMillis() - begin) / 1000;
        log.info("========================通道测试结束，用时:{}秒============================", time);
    }

    /**
     * 通过管道插入redis
     *
     * @param
     * @return void
     */
    @Test
    public void insertRedisByPipeline2() {


    }

    @Autowired
    private MongoClient client;

    @Test
    public void mongo4() {
        try(ClientSession clientSession = client.startSession()){
            clientSession.startTransaction();

            MongoDatabase db = mongoTemplate.getDb();
            String collectionName = mongoTemplate.getCollectionName(UserInfo.class);
            MongoCollection<Document> collection = db.getCollection(collectionName);

            UserInfo userInfo = new UserInfo();
            userInfo.setId(5L);
            userInfo.setName("小明");
            userInfo.setAge(7);
            userInfo.setStatus((byte) 0);

            Document parse = Document.parse(JSONObject.toJSONString(userInfo));
            collection.insertOne(clientSession,parse);



            userInfo = new UserInfo();
            userInfo.setId(6L);
            userInfo.setName("小红");
            userInfo.setAge(9);
            userInfo.setStatus((byte) 0);

            collection.insertOne(clientSession,Document.parse(JSONObject.toJSONString(userInfo)));
//            collection.insertOne(clientSession,null);

            // 提交事务
            clientSession.commitTransaction();
            log.info("....");

        }

    }

    @Test
    public void mongo4InsertMany(){
        CountDownLatch countDownLatch=new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {

            taskExecutor.submit(()->{

                for (int k = 1; k <= 300; k++) {
                    try(ClientSession clientSession = client.startSession()) {
                        // 开始事务
                        clientSession.startTransaction();
                        // 获取mongodb属性
                        MongoDatabase db = mongoTemplate.getDb();
                        String collectionName = mongoTemplate.getCollectionName(UserInfo.class);
                        MongoCollection<Document> collection = db.getCollection(collectionName);

                        long begin = System.currentTimeMillis();
                        List<Document> list = new ArrayList<>();
                        for (int j = 1; j <= 1000; j++) {
                            if (j % 5 == 0) {
                                Thread.yield();
                            }
                            UserInfo userInfo = new UserInfo();
                            userInfo.setId((long)j++);
                            userInfo.setName("小明");
                            userInfo.setAge(j * 7);
                            userInfo.setStatus((byte) 0);
                            list.add(Document.parse(JSONObject.toJSONString(userInfo)));
                            if (list.size() >= 200) {
                                log.info("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                                collection.insertMany(clientSession,list);
                                list.clear();
                                commitWithRetry(clientSession);
                                clientSession.startTransaction();
                            }
                            if ( j == 10000000 && !list.isEmpty()) {
                                collection.insertMany(clientSession,list);
                                long end = System.currentTimeMillis();
                                log.info("插入1000w数据结束，耗时:{}秒",(end-begin)/1000);
                            }
                        }
                        commitWithRetry(clientSession);

                    }
                }
                countDownLatch.countDown();

            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("结束----");

    }

    /**
     * 提交失败时，进行重新提交
     * @param clientSession
     */
    void commitWithRetry(ClientSession clientSession) {
        while (true) {
            try {
                // 提交事务
                clientSession.commitTransaction();
                System.out.println("Transaction committed");
                break;
            } catch (MongoException e) {
                // can retry commit
                if (e.hasErrorLabel(MongoException.UNKNOWN_TRANSACTION_COMMIT_RESULT_LABEL)) {
                    System.out.println("UnknownTransactionCommitResult, retrying commit operation ...");
                    continue;
                } else {
                    System.out.println("Exception during commit ...");
                    log.info("Exception:{}",e);
                }
            }
        }
    }


    /**
     * mongo幂等性测试
     *
     * @param
     * @return void
     */
    @Test
    public void mongoIdempotent() {
        ThreadPoolTaskExecutor taskExecutor = ApplicationContextProvider.getBean("taskExecutor", ThreadPoolTaskExecutor.class);
        UserInfo userInfo = userInfoRepository.findById(1L).get();
        if (null != userInfo) {
            // 如果查到对象，启用三次线程去对其中的count值进行叠加 初始是0 期望值是1
            for (int i = 0; i < 5; i++) {
                taskExecutor.submit(() -> {
                    log.info("开始更新数据");
                    int count = userInfo.getCount();
                    userInfo.setCount(++count);
                    userInfo.setStatus((byte) 0);
                    Query query = new Query(Criteria.where("id").is(userInfo.getId()).and("status").is((byte) 0));
                    Update update = new Update().inc("status", 1).inc("count", 1);
                    mongoTemplate.findAndModify(query, update, userInfo.getClass());
//                    userInfoRepository.save(userInfo);
                    log.info("更新结束");
                });
            }

        }

    }

    @Test
    public void saveUser() {


        List<UserInfo> list = new ArrayList<>();
        for (int i = 1; i <= 500000; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId((long) i);
            userInfo.setName("小明");
            userInfo.setAge(i * 7);
            userInfo.setStatus((byte) 0);
            list.add(userInfo);
            if (list.size() >= 1000) {
                userInfoRepository.insert(list);
                list.clear();
            }
            if (i == 500000 && !list.isEmpty()) {
                userInfoRepository.insert(list);
            }
        }

    }


    // 保存redis
    private void saveContract(int i, String key) {


        DealContractBO bo = new DealContractBO();
        bo.setContractId(i + "");
        String orderId = (System.currentTimeMillis() + "").substring(6);
        bo.setBuyOrderId(Long.valueOf(orderId));
        bo.setCreateTime(System.currentTimeMillis());
        bo.setBuyUid(i);
        key = key.concat(bo.getBuyUid() + "");

        stringRedisTemplate.opsForHash().put(key, orderId, JSONObject.toJSONString(bo));
    }

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void translate(){
        String msg = "爱你三千遍";
        String appId = "20190729000322173";
        String securityKey = "OzWQqq1cSI0JT32aidkT";
        Long salt = System.currentTimeMillis();
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", msg);
        params.put("from", "zh");
        params.put("to", "kor");

        params.put("appid", appId);

        String sign = appId+msg+salt+securityKey;
        params.put("sign", sign);

        String url = "http://api.fanyi.baidu.com/api/trans/vip/translate";
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class, params);
        log.info(forEntity.getBody().toString());
    }

}
