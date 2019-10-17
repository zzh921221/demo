package com.zzh.elasticsearch.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * es配置
 *
 * @author: 赵中晖
 * @create: 2019-09-30 15:41
 */
@Slf4j
@Configuration
public class ElasticsearchRestClient {

    private static final int ADDRESS_LENGTH = 2;

    private static final String HTTP_SCHEME = "http";

    /**
     * 地址
     * 192.168.1.112:9300,192.168.1.114:9300
     */
    @Value("${elasticsearch.ip}")
    String[] ipAddress;

    @Bean
    public RestClientBuilder restClientBuilder() {
        HttpHost[] httpHosts = Arrays.stream(ipAddress).map(this::makeHttpHost).filter(Objects::nonNull).toArray(HttpHost[]::new);
        log.debug("hosts:{}", Arrays.toString(httpHosts));
        return RestClient.builder(httpHosts);
    }

    @Bean
    public RestHighLevelClient highLevelClient(@Autowired RestClientBuilder restClientBuilder) {
        return new RestHighLevelClient(restClientBuilder);
    }

    // 构建HttpHost对象
    private HttpHost makeHttpHost(String ipAddress) {
        assert !StringUtils.isEmpty(ipAddress);
        String[] address = ipAddress.split(":");
        if (address.length == ADDRESS_LENGTH) {
            String ip = address[0];
            int port = Integer.parseInt(address[1]);
            return new HttpHost(ip, port, HTTP_SCHEME);
        } else {
            return null;
        }
    }
}
