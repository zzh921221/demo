package com.my.segmentation.service;

/**
 * redis key定义常量
 *
 * @author: zhaozhonghui
 * @create: 2019-04-25 10:40
 */
public class RedisKeyService {
    /**资产钱包索引*/
    private static String COIN_INDEX = "coin_index_%s";
    /** 获取资产钱包索引 */
    public static String getCoinINdexKey(int index){
        return String.format(COIN_INDEX,index);
    }


    public static void main(String[] args) {
        String coinINdexKey = getCoinINdexKey(2);
        System.out.println(coinINdexKey);
    }
}
