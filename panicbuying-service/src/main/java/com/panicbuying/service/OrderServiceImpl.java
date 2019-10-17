package com.panicbuying.service;

import com.alibaba.fastjson.JSONObject;
import com.panicbuying.api.OrderService;
import com.panicbuying.domain.SeckillOrder;
import com.panicbuying.dto.GoodsMqDTO;
import com.panicbuying.dto.SeckillRankDTO;
import com.panicbuying.mapper.SeckillOrderMapper;
import com.panicbuying.repository.OrderRepository;
import com.panicbuying.utils.IdWorker;
import com.panicbuying.utils.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 订单接口实现
 *
 * @author: zhaozhonghui
 * @create: 2019-04-25 11:47
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private SeckillOrderMapper orderMapper;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Boolean checkRepeat(Long goodsCode, Long userId) {
        String userSeckillGoodsKey = RedisKeyUtil.getUserSeckillGoodsKey(userId, goodsCode);
        return redisTemplate.hasKey(userSeckillGoodsKey);
    }

    @Override
    public void createOrder(GoodsMqDTO dto) {
        SeckillOrder order = new SeckillOrder();
        order.setOrderCode(getOrderNo());
        order.setUserId(dto.getUserId());
        order.setGoodsCode(dto.getGoodsCode());
        order.setId(new IdWorker().nextId());
        int count = orderMapper.insertSelective(order);
        if (count > 0) {

            saveOrderRedis(order);
            saveOrderMongo(order);
            log.info("创建订单成功，订单号:{}", order.getOrderCode());

        } else {
            log.error("创建订单失败!");
        }
    }

    @Override
    public List<SeckillRankDTO> getSeckillRank() {
        if (redisTemplate.hasKey(RedisKeyUtil.getUserSeckillRankKey())) {
            Set<ZSetOperations.TypedTuple> set = redisTemplate.opsForZSet().rangeWithScores(RedisKeyUtil.getUserSeckillRankKey(), 0, -1);
            List<SeckillRankDTO> collect = set.stream().map(item -> {
                SeckillRankDTO seckillRankDTO = new SeckillRankDTO();
                seckillRankDTO.setCount(item.getScore().intValue());
                seckillRankDTO.setUserId(Long.valueOf(String.valueOf(item.getValue())));
                return seckillRankDTO;
            }).collect(Collectors.toList());

            return collect;
        }
        return Collections.emptyList();
    }

    /**
     * 缓存进redis
     * @param dto
     */
    private void saveOrderRedis(SeckillOrder dto) {
        String userSeckillGoodsKey = RedisKeyUtil.getUserSeckillGoodsKey(dto.getUserId(), dto.getGoodsCode());
        redisTemplate.opsForValue().set(userSeckillGoodsKey, dto.getGoodsCode(),1, TimeUnit.HOURS);
        if (!redisTemplate.hasKey(RedisKeyUtil.getUserSeckillRankKey())) {
            redisTemplate.opsForZSet().add(RedisKeyUtil.getUserSeckillRankKey(),dto.getUserId(),1);
        } else {
            redisTemplate.opsForZSet().incrementScore(RedisKeyUtil.getUserSeckillRankKey(),dto.getUserId(),1);
        }
    }

    /**
     * 缓存进mongodb
     * @param order
     */
    private void saveOrderMongo(SeckillOrder order){
        orderRepository.save(order);
        // 自定义查询
        List<SeckillOrder> byGoodsCodeEquals = orderRepository.findByGoodsCodeEquals(order.getGoodsCode());
        log.info(JSONObject.toJSONString(byGoodsCodeEquals));
    }

    /**
     * 生成订单号
     *
     * @return
     */
    private Long getOrderNo() {
        String no = String.valueOf((Math.random() * 9 + 1) * 1000000);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = format.format(new Date());
        no = no.substring(0, 4).concat(date);
        return Long.valueOf(no);
    }
}
