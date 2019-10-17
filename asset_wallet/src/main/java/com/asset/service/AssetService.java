package com.asset.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 资产service
 *
 * @author: zhaozhonghui
 * @create: 2019-06-24 16:02
 */
@Slf4j
@Service
public class AssetService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private String key = "ASSET_TEST:BTC_ADV";

    /**
     * 更新数据 自增数据
     *
     * @param
     * @return void
     * @author 赵中晖
     * @date 2019/6/24
     */
    public void updateAssetAmount(String userId) {

        if (!redisTemplate.hasKey(key)) {
            redisTemplate.opsForHash().put(key, userId, "0");
        }
        redisTemplate.opsForHash().increment(key, userId, Double.valueOf(1.2567*Math.pow(10,8)));

    }

    /**
     * 自减数据
     * @param
     * @return void
     * @author 赵中晖
     * @date 2019/6/25
     */
    public void decrAssetAmount(String userId){

         redisTemplate.opsForHash().increment(key, userId, Double.valueOf(-1.2567*Math.pow(10,8)));

    }

    /**
     * 获取value
     *
     * @param
     * @return java.util.List<java.util.Map   <   java.lang.Integer   ,   java.lang.String>>
     * @author 赵中晖
     * @date 2019/6/24
     */
    public String getScore() {
        Object obj = redisTemplate.opsForHash().get(key, "1");
        log.info("计数:{}",obj.toString());
        return obj.toString();
    }

    /**
     * 删除key
     * @param
     * @return void
     * @author 赵中晖
     * @date 2019/6/24
     */
    public void deleteKey(){
        if (redisTemplate.hasKey(key)) {
            redisTemplate.opsForHash().put(key, "1", "0");
        }
    }

}
