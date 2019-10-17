package com.panicbuying.mq;

import com.alibaba.fastjson.JSONObject;
import com.panicbuying.api.OrderService;
import com.panicbuying.dto.GoodsMqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * mq消费者
 *
 * @author: zhaozhonghui
 * @create: 2019-04-26 11:40
 */
@Slf4j
@Component
public class MqConsumer {

    @Autowired
    private OrderService orderService;

    /**
     * 消费创建订单消息
     *
     * @param msg
     */
    @RabbitListener(queues = MqSource.CREATE_ORDER_QUEUE)
    public void receiveCreateOrderMsg(String msg) {
        log.info("{}:接受创建订单消息:{}",Thread.currentThread().getName(), msg);
        if (StringUtils.isEmpty(msg)) {
            log.error("订单消息内容为空！");
            return;
        }
        GoodsMqDTO goodsMqDTO = JSONObject.parseObject(msg, GoodsMqDTO.class);
        // 判断是否重复下单
        Boolean flag = orderService.checkRepeat(goodsMqDTO.getGoodsCode(), goodsMqDTO.getUserId());
        if (!flag) {
            // 创建订单
            orderService.createOrder(goodsMqDTO);
        } else {
            log.info("请勿重复下单2!:{}",msg);
        }
    }
}
