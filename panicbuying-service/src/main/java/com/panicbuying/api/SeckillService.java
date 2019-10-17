package com.panicbuying.api;

/**
 * 秒杀接口
 *
 * @author: zhaozhonghui
 * @create: 2019-04-25 9:43
 */
public interface SeckillService {

    /**
     * 进行秒杀
     * @param goodsCode
     * @param userId
     * @return String
     */
    String doSeckill(Long goodsCode, Long userId);
}
