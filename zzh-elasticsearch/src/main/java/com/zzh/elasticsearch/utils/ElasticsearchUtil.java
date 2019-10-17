package com.zzh.elasticsearch.utils;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.settings.Setting;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * es工具类
 *
 * @author: 赵中晖
 * @create: 2019-10-12 10:51
 */
@Component
public class ElasticsearchUtil {

    @Autowired
    private RestHighLevelClient client;

    /**
     * 同步创建索引
     * @param
     * @return void
     * @author 赵中晖
     */
    public void createIndex(String index){
        CreateIndexRequest request = buildCreateIndexRequest(index);
        try {
            client.indices().create(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步创建索引
     * @param
     * @return void
     * @author 赵中晖
     */
    public void createIndexAsync(String index,ActionListener<CreateIndexResponse> listener){
        CreateIndexRequest request = buildCreateIndexRequest(index);
        if (null == listener) {
            listener = new ActionListener<CreateIndexResponse>() {
                @Override
                public void onResponse(CreateIndexResponse createIndexResponse) {

                }

                @Override
                public void onFailure(Exception e) {
                    throw new RuntimeException("创建索引失败！");
                }
            };
        }
        client.indices().createAsync(request, RequestOptions.DEFAULT, listener);
    }

    /**
     * 同步插入数据
     * @param index 索引
     * @param item 数据对象
     * @param id id
     * @return void
     * @author 赵中晖
     */
    public void insert(String index,String item,String id){
        IndexRequest request = new IndexRequest(index);
        request.id(id);
        request.source(item, XContentType.JSON);
        try {
            client.index(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步插入数据(默认)
     * @param index 索引
     * @param item 数据对象
     * @param id id
     * @return void
     * @author 赵中晖
     */
    public void insertAsync(String index,Object item,String id,ActionListener<IndexResponse> listener){
        IndexRequest request = new IndexRequest(index);
        request.id(id);
        request.source(item, XContentType.JSON);
        if (null == listener) {
            listener = new ActionListener<IndexResponse>() {
                @Override
                public void onResponse(IndexResponse indexResponse) {

                }

                @Override
                public void onFailure(Exception e) {
                    throw new RuntimeException("插入数据失败！");
                }
            };
        }
        client.indexAsync(request, RequestOptions.DEFAULT, listener);
    }

    /**
     * 删除索引 同步
     * @param
     * @return void
     * @author 赵中晖
     */
    public void deleteIndex(String index){
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        try {
            client.indices().delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除索引 异步
     * @param
     * @return void
     * @author 赵中晖
     */
    public void deleteIndexAsync(String index,ActionListener<AcknowledgedResponse> listener){
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        if (null == listener) {
            listener = new ActionListener<AcknowledgedResponse>() {
                @Override
                public void onResponse(AcknowledgedResponse acknowledgedResponse) {

                }

                @Override
                public void onFailure(Exception e) {
                    throw new RuntimeException("删除索引失败！");
                }
            };
            client.indices().deleteAsync(request, RequestOptions.DEFAULT, listener);
        }
    }

    /**
     * 判断索引是否存在
     * @param index 索引
     * @return java.lang.Boolean
     * @author 赵中晖
     */
    public Boolean existsIndex(String index){
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
        try {
            return client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            return false;
        }
    }

    private CreateIndexRequest buildCreateIndexRequest(String index){
        CreateIndexRequest request = new CreateIndexRequest(index);

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓可选配置↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        // 设置分片和副本集
        request.settings(Settings.builder()
                // 分片
                .put("index.number_of_shards",3)
                // 副本
                .put("index.number_of_replicas",2));

        // 为索引设置别名
        request.alias(new Alias("new_index"));
        // 等待所有节点确认的 超时时间
        request.setTimeout(TimeValue.timeValueMinutes(2L));
        // 连接master节点超时时间
        request.setMasterTimeout(TimeValue.timeValueMinutes(1L));

        return request;
    }


}
