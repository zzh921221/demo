package com.panicbuying.api;

import com.panicbuying.dto.GoodsMqDTO;
import com.panicbuying.dto.SeckillRankDTO;

import java.util.List;
import java.util.Set;

/**
 * 订单接口
 *
 * @author: zhaozhonghui
 * @create: 2019-04-24 16:11
 */
public interface OrderService {

    /**
     * 检验是否重复下单
     * @param goodsCode
     * @param userId
     * @return Boolean
     */
    Boolean checkRepeat(Long goodsCode, Long userId);

    /**
     * 下单
     * @param dto
     */
    void createOrder(GoodsMqDTO dto);

    /**
     * 获取用户抢购排行榜
     * @return SeckillRankDTO
     */
    List<SeckillRankDTO> getSeckillRank();
}
