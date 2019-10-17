package com.panicbuying.service;

import com.panicbuying.api.GoodsService;
import com.panicbuying.api.OrderService;
import com.panicbuying.api.SeckillService;
import com.panicbuying.dto.GoodsMqDTO;
import com.panicbuying.mq.MqProducer;
import com.panicbuying.utils.LockProvider;
import com.panicbuying.utils.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 秒杀接口实现
 *
 * @author: zhaozhonghui
 * @create: 2019-04-25 10:17
 */
@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private OrderService orderSerivce;
    @Resource
    private GoodsService goodsService;
    @Autowired
    private MqProducer mqProducer;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String doSeckill(Long goodsCode, Long userId) {
        log.info(Thread.currentThread().getName());
        String goodsStockKey = RedisKeyUtil.getGoodsStockKey(goodsCode);
        String key = "lock:" + goodsCode + "_" + userId;
        // 在商品编码和用户id上加锁，过期时间1秒
        RLock lock = LockProvider.getLock(key);
        try {
            log.info("{}:锁:{},状态:{}",Thread.currentThread().getName(),key,lock.isLocked());
            lock.lock(10, TimeUnit.SECONDS);
            if (!redisTemplate.hasKey(goodsStockKey)) {
                return "商品不存在!";
            }
            Long size = redisTemplate.opsForList().size(goodsStockKey);
            // 检验库存
            if (size <= 0L) {
                return "商品已售完！";
            }
            // 重复下单检验
            if (orderSerivce.checkRepeat(goodsCode, userId)) {
                log.info("请勿重复下单1:{}",goodsCode + "_" + userId);
                return "请勿重复下单";
            }
            // redis商品库存弹出
             redisTemplate.opsForList().leftPop(goodsStockKey);
            Boolean flag = goodsService.updateGoodsStock(goodsCode);
            if (flag) {
                // 异步通过mq发送消息创建订单
                GoodsMqDTO goodsMqDTO = new GoodsMqDTO();
                goodsMqDTO.setGoodsCode(goodsCode);
                goodsMqDTO.setUserId(userId);
                mqProducer.sendCreateOrderMsg(goodsMqDTO);
                return "抢购完成";
            } else {
                log.error("减库存失败!");
            }
        } finally {
            // 判断锁的状态 最终解锁
            boolean locked = lock.isLocked();
            if (locked) {
                lock.unlock();
            }
        }
        return "抢单失败，请稍后重试";
    }
}
