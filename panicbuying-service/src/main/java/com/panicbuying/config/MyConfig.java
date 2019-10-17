package com.panicbuying.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panicbuying.properties.MyRedisProperties;
import com.panicbuying.utils.LockProvider;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.IOException;

/**
 * 启动配置项
 *
 * @author: zhaozhonghui
 * @create: 2019-04-26 16:58
 */
@Configuration
public class MyConfig {

    @Autowired
    private MyRedisProperties myRedisProperties;
    /**
     * 装载RedissonClient
     * @return
     * @throws IOException
     */

    @Bean
    public AutoRedissonClient autoRedissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://"+myRedisProperties.getHost()+":"+myRedisProperties.getPort())
                .setPassword(myRedisProperties.getPassword())
                .setDatabase(Integer.valueOf(myRedisProperties.getDatabase()));
        RedissonClient redissonClient = Redisson.create(config);
        LockProvider.setRedissonClient(redissonClient);
        return new AutoRedissonClient();
    }

    /**
     * 替换redistemplate的序列化规则
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


}

class AutoRedissonClient {

}
