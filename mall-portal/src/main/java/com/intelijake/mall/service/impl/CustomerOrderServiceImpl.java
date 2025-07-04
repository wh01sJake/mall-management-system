package com.intelijake.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.intelijake.mall.constant.MqConstant;
import com.intelijake.mall.constant.OrderStatusConstant;
import com.intelijake.mall.mapper.CustomerOrderMapper;
import com.intelijake.mall.mapper.OrderItemMapper;
import com.intelijake.mall.mapper.ShoppingCartMapper;
import com.intelijake.mall.pojo.query.CartQuery;
import com.intelijake.mall.pojo.vo.CartVO;
import com.intelijake.mall.service.ICustomerOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.intelijake.mall.util.MultiDelayMessage;
import com.intelijake.pojo.CustomerOrder;
import com.intelijake.pojo.OrderItem;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jake
 * @since 2025-06-23
 */
@Service
public class CustomerOrderServiceImpl extends ServiceImpl<CustomerOrderMapper, CustomerOrder> implements ICustomerOrderService {

    @Autowired
    private CustomerOrderMapper customerOrderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private IdentifierGenerator identifierGenerator;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void add(CustomerOrder order) {

        Long orderNo = (Long) identifierGenerator.nextId(order);

        order.setOrderNo(orderNo);

        //get the items need to be checked out by id

        CartQuery cartQuery = new CartQuery();

        cartQuery.setCustomerId(order.getUserId());

        cartQuery.setIsChecked(1);

        List<CartVO> list = shoppingCartMapper.list(cartQuery);

        BigDecimal payment = BigDecimal.ZERO;

        for (CartVO cartVO: list){
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderNo(orderNo);
            orderItem.setUserId(order.getUserId());
            orderItem.setProductId(cartVO.getProductId());
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setProductImage(cartVO.getProductMainImage());
            orderItem.setCurrentUnitPrice(cartVO.getProductPrice());// should be named the same

            //calculate the current product X qty

            BigDecimal productPrice = cartVO.getProductPrice();
            BigDecimal qty = BigDecimal.valueOf(cartVO.getQuantity());

            BigDecimal totalPrice = productPrice.multiply(qty);

            payment = payment.add(totalPrice);

            orderItem.setTotalPrice(totalPrice);

            orderItemMapper.insert(orderItem);

            //clear cart after checked out

            shoppingCartMapper.deleteById(cartVO.getId());
        }

        order.setPaymentAmount(payment);

        customerOrderMapper.insert(order);

        // send delayed msg
        MultiDelayMessage<Long> msg = new MultiDelayMessage<>(order.getOrderNo(),10000L, 10000L, 10000L, 15000L, 15000L, 30000L, 30000L);

        Long delay = msg.removeNextDelay();

        rabbitTemplate.convertAndSend(MqConstant.DELAY_EXCHANGE, MqConstant.DELAY_ORDER_ROUTING_KEY, msg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {

                message.getMessageProperties().setDelayLong(delay);
                return message;
            }
        });
    }

    @Override
    public void cancel(Long orderNo) {

        System.out.println("cancelling order " + orderNo);
        UpdateWrapper<CustomerOrder> updateWrapper = new UpdateWrapper<>();

        updateWrapper.eq("order_no",orderNo);

        updateWrapper.set("status", OrderStatusConstant.ORDER_STATUS_CANCEL);

        customerOrderMapper.update(updateWrapper);
    }
}
