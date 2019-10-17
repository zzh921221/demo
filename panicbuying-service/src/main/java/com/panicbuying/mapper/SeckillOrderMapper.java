package com.panicbuying.mapper;

import com.panicbuying.domain.SeckillOrder;

/**
 * 订单mapper
 * @author zhaozhonghui
 */
public interface SeckillOrderMapper {
    /**
     * 插入订单数据
     * @param record
     * @return
     */
    int insertSelective(SeckillOrder record);
}