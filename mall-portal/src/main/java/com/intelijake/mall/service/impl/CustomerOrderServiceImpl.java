package com.intelijake.mall.service.impl;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.intelijake.mall.mapper.CustomerOrderMapper;
import com.intelijake.mall.mapper.OrderItemMapper;
import com.intelijake.mall.mapper.ShoppingCartMapper;
import com.intelijake.mall.pojo.query.CartQuery;
import com.intelijake.mall.pojo.vo.CartVO;
import com.intelijake.mall.service.ICustomerOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.intelijake.pojo.CustomerOrder;
import com.intelijake.pojo.OrderItem;
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
    }
}
