package com.panicbuying.mapper;

import com.panicbuying.domain.Goods;

import java.util.List;

import io.lettuce.core.dynamic.annotation.Param;

/**
 * 商品mapper
 * @author zhaozhonghui
 */
public interface GoodsMapper {
    /**
     * 查询所有商品
     * @return
     */
    List<Goods> selectAllGoods();

    /**
     * 初始化所有商品库存
     * @return
     */
    long initGoodsStock();

    /**
     * 更新指定商品库存
     * @param goodsCode
     * @return
     */
    long updateGoodsStockByGoodsCode(@Param("goodsCode")Long goodsCode);
}