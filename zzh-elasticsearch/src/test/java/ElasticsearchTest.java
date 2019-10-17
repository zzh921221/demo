import com.alibaba.fastjson.JSONObject;
import com.zzh.elasticsearch.Application;
import com.zzh.elasticsearch.domain.Item;
import com.zzh.elasticsearch.utils.ElasticsearchUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContent;
import org.elasticsearch.common.xcontent.XContentParserUtils;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * es测试类
 *
 * @author: 赵中晖
 * @create: 2019-09-30 11:31
 */
@Slf4j
@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ElasticsearchTest {

    //    @Autowired
//    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private RestHighLevelClient highLevelClient;
    @Autowired
    private ElasticsearchUtil elasticsearchUtil;

    @Test
    public void hello() {
        System.out.println("hello es!");
    }

    @Test
    public void query() {
        String[] indices = {"personal_user_account"};
        SearchRequest searchRequest = new SearchRequest(indices);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery("id", 99));
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = searchResponse.getHits().getHits();
            Arrays.stream(hits).forEach(item -> {
                log.info(JSONObject.toJSONString(item.getSourceAsMap()));

            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (highLevelClient != null) {
                try {
                    highLevelClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 创建索引
     */
    @Test
    public void createIndex() {
        String index = "test_es_index";
        if (elasticsearchUtil.existsIndex(index)) {
            elasticsearchUtil.deleteIndex(index);
        }
        elasticsearchUtil.createIndex(index);
    }

    /**
     * 插入数据
     */
    @Test
    public void createIndex2(){

        String index = "test_es_index";
        Item item = new Item();
        item.setId(2L);
        item.setTitle("小当家");
        item.setCategory("干脆面");
        item.setBrand("面制品");
        elasticsearchUtil.insert(index, JSONObject.toJSONString(item), String.valueOf(item.getId()));
    }

}
