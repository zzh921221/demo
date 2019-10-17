package com.panicbuying.utils;

/**
 * redis key定义常量
 *
 * @author: zhaozhonghui
 * @create: 2019-04-25 10:40
 */
public class RedisKeyUtil {
    /**商品库存*/
    private static String GOOD_STOCK = "goods_stock";
    /**用户已经秒杀的商品*/
    private static String USER_SECKILL_GOODS = "user_seckill_goods";
    /** 用户秒杀排行 */
    private static String USER_SECKILL_RANK = "user_seckill_rank";
    /**生成商品库存的key*/
    public static String getGoodsStockKey(Long goodsCode){
        return GOOD_STOCK +":"+goodsCode;
    }
    /**生成用户秒杀商品的key*/
    public static String getUserSeckillGoodsKey(Long userId,Long goodsCode){
        return USER_SECKILL_GOODS+":"+userId+"_"+goodsCode;
    }
    /** 生成用户秒杀商品排行 */
    public static String getUserSeckillRankKey(){
        return USER_SECKILL_RANK;
    }
}
