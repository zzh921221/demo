package com.panicbuying.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * redis配置类
 *
 * @author: zhaozhonghui
 * @create: 2019-04-26 18:06
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class MyRedisProperties {
    private String host;

    private String port;

    private String password;

    private String database;

}
