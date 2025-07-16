package com.intelijake.mall.listener;

import com.intelijake.mall.constant.MqConstant;
import com.intelijake.mall.service.ICustomerOrderService;
import com.intelijake.mall.util.MultiDelayMessage;
import com.intelijake.pojo.CustomerOrder;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName: OrderStatusListener
 * Description:
 * <p>
 * Datetime: 2025/7/3 23:27
 * Author: @Likun.Fang
 * Version: 1.0
 */

@Component
public class OrderStatusListener {

    @Autowired
    ICustomerOrderService customerOrderService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(MqConstant.DELAY_ORDER_QUEUE),
            exchange = @Exchange(name = MqConstant.DELAY_EXCHANGE, delayed = "true"),
            key = MqConstant.DELAY_ORDER_ROUTING_KEY
    ))
    public void listenDelayMsg(MultiDelayMessage<Long> message){

        System.out.println("OrderStatusListener.listening");

        //get order no from msg

        Long orderNo = message.getData();

        //get order status
        CustomerOrder order = customerOrderService.getById(orderNo);

        // if order doesn't exist or paid

        if (order == null || order.getStatus() > 1){
            return;
        }
        else {
            // check if remaining delay
            if (message.hasNextDelay()){

                List<Long> delayMillis = message.getDelayMillis();

                System.out.println(delayMillis);

                Long delay = message.removeNextDelay();

                rabbitTemplate.convertAndSend(MqConstant.DELAY_EXCHANGE, MqConstant.DELAY_ORDER_ROUTING_KEY, message, new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {

                        message.getMessageProperties().setDelayLong(delay);
                        return message;
                    }
                });

            }
            else {
                // remove order if unpaid after 30 min
                customerOrderService.cancel(orderNo);
            }
        }

    }
}
