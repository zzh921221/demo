package com.panicbuying.api;

import com.panicbuying.domain.Goods;
import com.panicbuying.dto.GoodsResultDTO;

import java.util.List;

/**
 * 商品接口
 *
 * @author: zhaozhonghui
 * @create: 2019-04-25 9:42
 */
public interface GoodsService {
    /**
     * 初始化商品
     * @return String
     */
    String initGoods();

    /**
     * 更新商品库存
     * @param goodsCode
     * @return Boolean
     */
    Boolean updateGoodsStock(Long goodsCode);

    /**
     * 从搜索引擎查询商品
     * @param param
     * @return
     */
    List<GoodsResultDTO> getGoodsListFromElasticSearch(String param);
}
