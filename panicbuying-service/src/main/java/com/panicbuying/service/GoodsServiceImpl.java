package com.panicbuying.service;

import com.alibaba.fastjson.JSONObject;
import com.panicbuying.api.GoodsService;
import com.panicbuying.domain.Goods;
import com.panicbuying.dto.GoodsResultDTO;
import com.panicbuying.mapper.GoodsMapper;
import com.panicbuying.repository.GoodsSearchRepository;
import com.panicbuying.utils.BeanMapper;
import com.panicbuying.utils.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 商品接口实现
 *
 * @author: zhaozhonghui
 * @create: 2019-04-25 10:20
 */
@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private GoodsSearchRepository goodsSearchRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String initGoods() {
        log.info("开始初始化商品库存");
        goodsMapper.initGoodsStock();

        log.info("存入redis");
        List<Goods> goodsList = goodsMapper.selectAllGoods();
        saveGoodsStockListRedis(goodsList);
        saveGoodsStockElasticSearch(goodsList);
        log.info("商品初始化完成");
        return "ok";
    }

    /**
     * 库存存进redis String
     *
     * @param goodsList
     */
    private void saveGoodsStockRedis(List<Goods> goodsList) {
        goodsList.stream().forEach(goods -> {
            String goodsStockKey = RedisKeyUtil.getGoodsStockKey(goods.getGoodsCode());
            redisTemplate.opsForValue().set(goodsStockKey, goods.getGoodsStock());

        });
    }

    /**
     * 库存存进redis List
     *
     * @param goodsList
     */
    private void saveGoodsStockListRedis(List<Goods> goodsList) {
        goodsList.stream().forEach(goods -> {
            String goodsStockKey = RedisKeyUtil.getGoodsStockKey(goods.getGoodsCode());
            List<Integer> stockList = new ArrayList<>();
            for (int i = 1; i <= goods.getGoodsStock(); i++) {
                stockList.add(i);
            }
            redisTemplate.opsForList().rightPushAll(goodsStockKey, stockList);
        });
    }

    private void saveGoodsStockElasticSearch(List<Goods> goodsList) {
//        goodsSearchRepository.deleteAll();
//        Iterable<Goods> iterable = () -> goodsList.listIterator();
//        goodsSearchRepository.saveAll(iterable);
        boolean flag = elasticsearchTemplate.indexExists("demo_goods");
        if (flag) {
            DeleteQuery deleteQuery = new DeleteQuery();
            deleteQuery.setIndex("demo_goods");
            deleteQuery.setType("goods");
            deleteQuery.setQuery(QueryBuilders.matchAllQuery());
            elasticsearchTemplate.delete(deleteQuery);
        }

        List<IndexQuery> indexQueryList2 = new ArrayList<>(goodsList.size());
        goodsList.stream().forEach(goods -> {
            IndexQuery indexQuery2 = new IndexQueryBuilder()
                        .withId(goods.getId().toString()).
            withObject(goods).
            withIndexName("demo_goods").
            withType("goods").
            build();
            indexQueryList2.add(indexQuery2);
        });
        elasticsearchTemplate.bulkIndex(indexQueryList2);
    }


    @Override
    public Boolean updateGoodsStock(Long goodsCode) {
        // 根据更新的数据的数量判断是否减了库存，在sql里已对现有库存做了判断
        long count = goodsMapper.updateGoodsStockByGoodsCode(goodsCode);
        return count > 0;
    }

    @Override
    public List<GoodsResultDTO> getGoodsListFromElasticSearch(String param) {
//        goodsSearchRepository.deleteAll();
//        QueryStringQueryBuilder builder = new QueryStringQueryBuilder(param);
//        Iterable<Goods> search = goodsSearchRepository.search(builder);
//        Iterator<Goods> goodsIterator = search.iterator();
//        List goodsList = IteratorUtils.toList(goodsIterator);
//        List<GoodsResultDTO> goodsResultDTOS = BeanMapper.mapList(goodsList, GoodsResultDTO.class);
//        return goodsResultDTOS;
        return getGoodsList(param);
    }

    private List<GoodsResultDTO> getGoodsList(String param){
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(param)) {
            Map<String,Float> map = new HashMap<>(2);
            map.put("id",1F);
            map.put("goodsCode",2F);
            query.should(QueryBuilders.queryStringQuery(param).fields(map));
        }
        String color = "#FA4425";
        HighlightBuilder.Field hlFieldGoodsName = new HighlightBuilder
                .Field("goodsName")
                .preTags("<font color='" + color+ "'>")
                .postTags("</font>");
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("demo_goods")
                .withTypes("goods")
                .withQuery(query)
                .withHighlightFields(hlFieldGoodsName)
                .build();
        List<GoodsResultDTO> goodsResultDTOS = elasticsearchTemplate.queryForList(searchQuery, GoodsResultDTO.class);
        return goodsResultDTOS;
//        SearchQuery query =
    }
//
//    public static void main(String[] args) {
//        List<String> charList = Arrays.asList("a1","b2","c3");
//        List<String> number = new ArrayList<>(charList.size());
//        charList.stream().forEach(item -> {
//            item = item.substring(1);
//            number.add(item);
//        });
//
//        log.info(JSONObject.toJSONString(number));
//    }
}
