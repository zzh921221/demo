package com.panicbuying.repository;

import com.panicbuying.domain.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 商品SearchRepository
 *
 * @author: zhaozhonghui
 * @create: 2019-04-28 15:16
 */
public interface
GoodsSearchRepository extends ElasticsearchRepository<Goods,Long> {

}
