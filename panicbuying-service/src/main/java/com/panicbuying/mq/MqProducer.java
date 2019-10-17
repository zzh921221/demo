package com.panicbuying.mq;

import com.alibaba.fastjson.JSONObject;
import com.panicbuying.dto.GoodsMqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mq生产
 *
 * @author: zhaozhonghui
 * @create: 2019-04-26 11:24
 */
@Slf4j
@Configuration
public class MqProducer {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Bean
    public Queue queue(){
        return new Queue(MqSource.CREATE_ORDER_QUEUE);
    }

    // fanout模式交换机
    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }
    // topic模式交换机
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }

    // 广播模式绑定队列
    @Bean
    Binding fanoutBindingExchange(@Qualifier("queue") Queue AMessage, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(AMessage).to(fanoutExchange);
    }
    // topic模式绑定队列
    @Bean
    Binding topicBindingExchange(@Qualifier("queue") Queue AMessage, TopicExchange topicExchange) {
        return BindingBuilder.bind(AMessage).to(topicExchange).with(MqSource.CREATE_ORDER_QUEUE);
    }

    /**
     * 发送创建订单的mq topic模式
     * @param goodsMqDTO
     */
    public void sendCreateOrderMsg(GoodsMqDTO goodsMqDTO){
        log.info("{}:发送创建订单信息:{}", Thread.currentThread().getName(),JSONObject.toJSONString(goodsMqDTO));
        amqpTemplate.convertAndSend("topicExchange",MqSource.CREATE_ORDER_QUEUE,JSONObject.toJSONString(goodsMqDTO));
    }
}
