package com.intelijake.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.intelijake.mall.service.ICustomerOrderService;
import com.intelijake.mall.service.IOrderItemService;
import com.intelijake.mall.service.IShippingAddressService;
import com.intelijake.mall.util.Result;
import com.intelijake.pojo.CustomerOrder;
import com.intelijake.pojo.OrderItem;
import com.intelijake.pojo.ShippingAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Admin Order Management Controller
 * </p>
 *
 * @author Jake
 * @since 2025-06-11
 */
@RestController
@RequestMapping("/admin/order")
public class CustomerOrderController {

    @Autowired
    private ICustomerOrderService customerOrderService;

    @Autowired
    private IOrderItemService orderItemService;

    @Autowired
    private IShippingAddressService shippingAddressService;

    /**
     * Get paginated order list for admin
     */
    @GetMapping("/list")
    public Result<IPage<CustomerOrder>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String customerName) {

        Page<CustomerOrder> pageObj = new Page<>(page, limit);
        QueryWrapper<CustomerOrder> wrapper = new QueryWrapper<>();

        // Add search conditions
        if (orderNo != null && !orderNo.trim().isEmpty()) {
            wrapper.like("order_no", orderNo);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        // Note: customerName search would require joining with customer table
        // For now, we'll implement basic search

        wrapper.orderByDesc("create_time");
        IPage<CustomerOrder> result = customerOrderService.page(pageObj, wrapper);

        return Result.ok(result);
    }

    /**
     * Get order details by order number
     */
    @GetMapping("/detail/{orderNo}")
    public Result<CustomerOrder> getOrderDetail(@PathVariable Long orderNo) {
        QueryWrapper<CustomerOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);

        CustomerOrder order = customerOrderService.getOne(wrapper);
        if (order == null) {
            return Result.error("Order not found");
        }

        return Result.ok(order);
    }

    /**
     * Update order status
     */
    @PutMapping("/updateStatus")
    public Result<String> updateOrderStatus(@RequestBody Map<String, Object> params) {
        Long orderNo = Long.valueOf(params.get("orderNo").toString());
        Integer status = Integer.valueOf(params.get("status").toString());

        QueryWrapper<CustomerOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);

        CustomerOrder order = customerOrderService.getOne(wrapper);
        if (order == null) {
            return Result.error("Order not found");
        }

        order.setStatus(status);
        boolean updated = customerOrderService.updateById(order);

        if (updated) {
            return Result.ok("Order status updated successfully");
        } else {
            return Result.error("Failed to update order status");
        }
    }

    /**
     * Get order items for specific order
     */
    @GetMapping("/items/{orderNo}")
    public Result<List<OrderItem>> getOrderItems(@PathVariable Long orderNo) {
        QueryWrapper<OrderItem> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);

        List<OrderItem> orderItems = orderItemService.list(wrapper);
        return Result.ok(orderItems);
    }

    /**
     * Get order shipping address
     */
    @GetMapping("/address/{orderNo}")
    public Result<ShippingAddress> getOrderAddress(@PathVariable Long orderNo) {
        // First get order info
        QueryWrapper<CustomerOrder> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq("order_no", orderNo);

        CustomerOrder order = customerOrderService.getOne(orderWrapper);
        if (order == null) {
            return Result.error("Order not found");
        }

        // Get shipping address
        if (order.getShippingId() != null) {
            ShippingAddress address = shippingAddressService.getById(order.getShippingId());
            return Result.ok(address);
        } else {
            return Result.error("No shipping address for this order");
        }
    }

    /**
     * Get order statistics for dashboard
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getOrderStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // Total orders
        long totalOrders = customerOrderService.count();
        stats.put("totalOrders", totalOrders);

        // Pending orders (status = 1)
        QueryWrapper<CustomerOrder> pendingWrapper = new QueryWrapper<>();
        pendingWrapper.eq("status", 1);
        long pendingOrders = customerOrderService.count(pendingWrapper);
        stats.put("pendingOrders", pendingOrders);

        // Completed orders (status = 4)
        QueryWrapper<CustomerOrder> completedWrapper = new QueryWrapper<>();
        completedWrapper.eq("status", 4);
        long completedOrders = customerOrderService.count(completedWrapper);
        stats.put("completedOrders", completedOrders);

        // Total revenue (sum of payment_amount for completed orders)
        // This would require a custom query, for now we'll set a placeholder
        stats.put("totalRevenue", 0.0);

        return Result.ok(stats);
    }

    /**
     * Get orders by status
     */
    @GetMapping("/status/{status}")
    public Result<List<CustomerOrder>> getOrdersByStatus(@PathVariable Integer status) {
        QueryWrapper<CustomerOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("status", status);
        wrapper.orderByDesc("create_time");

        List<CustomerOrder> orders = customerOrderService.list(wrapper);
        return Result.ok(orders);
    }

    /**
     * Get order count by date range
     */
    @GetMapping("/count")
    public Result<Map<String, Object>> getOrderCountByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        Map<String, Object> result = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        // Generate sample data for the date range
        // In a real implementation, you would query the database for actual order counts
        java.time.LocalDate start = java.time.LocalDate.parse(startDate);
        java.time.LocalDate end = java.time.LocalDate.parse(endDate);

        for (java.time.LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            dates.add(date.format(java.time.format.DateTimeFormatter.ofPattern("MM-dd")));
            // Placeholder count data - should count actual orders for each date
            counts.add((int) (Math.random() * 20 + 5));
        }

        result.put("dates", dates);
        result.put("counts", counts);

        return Result.ok(result);
    }

    /**
     * Get revenue statistics
     */
    @GetMapping("/revenue")
    public Result<Map<String, Object>> getRevenueStats(@RequestParam(defaultValue = "7d") String period) {
        Map<String, Object> result = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Double> revenues = new ArrayList<>();

        // Generate sample data for the last 7 days
        java.time.LocalDate endDate = java.time.LocalDate.now();
        java.time.LocalDate startDate = endDate.minusDays(6);

        for (java.time.LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dates.add(date.format(java.time.format.DateTimeFormatter.ofPattern("MM-dd")));
            // Placeholder revenue data - should calculate actual revenue from orders
            revenues.add(Math.random() * 1000 + 500);
        }

        result.put("dates", dates);
        result.put("revenues", revenues);

        return Result.ok(result);
    }
}

