package com.panicbuying.repository;

import com.panicbuying.domain.SeckillOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单Repository
 *
 * @author: zhaozhonghui
 * @create: 2019-04-27 13:49
 */
@Component
public interface  OrderRepository extends MongoRepository<SeckillOrder,Long> {

    List<SeckillOrder> findByGoodsCodeEquals(Long goodsCode);
}
