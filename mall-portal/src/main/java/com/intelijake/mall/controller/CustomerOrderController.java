package com.intelijake.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.intelijake.mall.service.ICustomerOrderService;
import com.intelijake.mall.service.IOrderItemService;
import com.intelijake.mall.service.IShippingAddressService;
import com.intelijake.mall.util.Result;
import com.intelijake.pojo.Customer;
import com.intelijake.pojo.CustomerOrder;
import com.intelijake.pojo.OrderItem;
import com.intelijake.pojo.ShippingAddress;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jake
 * @since 2025-06-23
 */
@RestController
@RequestMapping("/order")
public class CustomerOrderController {

    @Autowired
    ICustomerOrderService customerOrderService;

    @Autowired
    IOrderItemService orderItemService;

    @Autowired
    IShippingAddressService shippingAddressService;

    /**
     * 提交订单
     */
    @PostMapping("/add")
    public Result<String> add(@RequestBody CustomerOrder order, HttpSession httpSession){

        Customer customer = (Customer) httpSession.getAttribute("customer");

        if (customer == null) {
            return Result.error("Please login first");
        }

        order.setUserId(customer.getId());
        order.setStatus(1); // Set order status to unpaid

        customerOrderService.add(order);

        return Result.ok("Order submitted successfully");
    }

    /**
     * Get user order list
     */
    @GetMapping("/list")
    public Result<List<CustomerOrder>> list(HttpSession httpSession){
        Customer customer = (Customer) httpSession.getAttribute("customer");

        if (customer == null) {
            return Result.error("Please login first");
        }

        QueryWrapper<CustomerOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", customer.getId());
        wrapper.orderByDesc("create_time");

        List<CustomerOrder> orders = customerOrderService.list(wrapper);

        return Result.ok(orders);
    }

    /**
     * Get order details
     */
    @GetMapping("/detail/{orderNo}")
    public Result<CustomerOrder> detail(@PathVariable Long orderNo, HttpSession httpSession){
        Customer customer = (Customer) httpSession.getAttribute("customer");

        if (customer == null) {
            return Result.error("Please login first");
        }

        QueryWrapper<CustomerOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        wrapper.eq("user_id", customer.getId());

        CustomerOrder order = customerOrderService.getOne(wrapper);

        if (order == null) {
            return Result.error("Order does not exist");
        }

        return Result.ok(order);
    }

    /**
     * Get order items list
     */
    @GetMapping("/items/{orderNo}")
    public Result<List<OrderItem>> getOrderItems(@PathVariable Long orderNo, HttpSession httpSession){
        Customer customer = (Customer) httpSession.getAttribute("customer");

        if (customer == null) {
            return Result.error("Please login first");
        }

        QueryWrapper<OrderItem> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        wrapper.eq("user_id", customer.getId());

        List<OrderItem> orderItems = orderItemService.list(wrapper);

        return Result.ok(orderItems);
    }

    /**
     * Get order shipping address
     */
    @GetMapping("/address/{orderNo}")
    public Result<ShippingAddress> getOrderAddress(@PathVariable Long orderNo, HttpSession httpSession){
        Customer customer = (Customer) httpSession.getAttribute("customer");

        if (customer == null) {
            return Result.error("Please login first");
        }

        // First get order information
        QueryWrapper<CustomerOrder> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq("order_no", orderNo);
        orderWrapper.eq("user_id", customer.getId());

        CustomerOrder order = customerOrderService.getOne(orderWrapper);

        if (order == null) {
            return Result.error("Order does not exist");
        }

        // Get shipping address
        if (order.getShippingId() != null) {
            ShippingAddress address = shippingAddressService.getById(order.getShippingId());
            return Result.ok(address);
        } else {
            return Result.error("Order has no shipping address");
        }
    }

}
