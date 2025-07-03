package com.intelijake.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.intelijake.mall.service.ICustomerOrderService;
import com.intelijake.mall.service.IProductCategoryService;
import com.intelijake.mall.service.IProductService;
import com.intelijake.mall.util.Result;
import com.intelijake.pojo.CustomerOrder;
import com.intelijake.pojo.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * Chart and Statistics Controller for Admin Dashboard
 * </p>
 *
 * @author Jake
 * @since 2025-06-30
 */
@RestController
@RequestMapping("/admin/chart")
public class ChartController {

    @Autowired
    private ICustomerOrderService customerOrderService;

    @Autowired
    private IProductCategoryService productCategoryService;

    @Autowired
    private IProductService productService;

    /**
     * Get category count for pie chart
     */
    @GetMapping("/categoryCount")
    public Result<List<Map<String, Object>>> getCategoryCount() {
        List<ProductCategory> categories = productCategoryService.list();
        List<Map<String, Object>> result = new ArrayList<>();

        for (ProductCategory category : categories) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", category.getName());
            item.put("value", Math.random() * 100); // Placeholder - should count products in category
            result.add(item);
        }

        return Result.ok(result);
    }

    /**
     * Get order status distribution
     */
    @GetMapping("/orderStatus")
    public Result<List<Map<String, Object>>> getOrderStatusDistribution() {
        List<Map<String, Object>> result = new ArrayList<>();

        // Count orders by status
        String[] statusNames = {"Cancelled", "Unpaid", "Paid", "Shipped", "Completed", "Closed"};
        for (int i = 0; i <= 5; i++) {
            QueryWrapper<CustomerOrder> wrapper = new QueryWrapper<>();
            wrapper.eq("status", i);
            long count = customerOrderService.count(wrapper);

            Map<String, Object> item = new HashMap<>();
            item.put("name", statusNames[i]);
            item.put("value", count);
            result.add(item);
        }

        return Result.ok(result);
    }

    /**
     * Get payment method distribution
     */
    @GetMapping("/paymentMethods")
    public Result<List<Map<String, Object>>> getPaymentMethodStats() {
        List<Map<String, Object>> result = new ArrayList<>();

        String[] paymentNames = {"", "Stripe", "PayPal", "Credit Card", "Cash on Delivery"};
        for (int i = 1; i <= 4; i++) {
            QueryWrapper<CustomerOrder> wrapper = new QueryWrapper<>();
            wrapper.eq("payment_type", i);
            long count = customerOrderService.count(wrapper);

            Map<String, Object> item = new HashMap<>();
            item.put("name", paymentNames[i]);
            item.put("value", count);
            result.add(item);
        }

        return Result.ok(result);
    }

    /**
     * Get revenue trends
     */
    @GetMapping("/revenue")
    public Result<Map<String, Object>> getRevenueTrends(@RequestParam(defaultValue = "7d") String period) {
        Map<String, Object> result = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Double> revenues = new ArrayList<>();

        // Generate sample data for the last 7 days
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dates.add(date.format(DateTimeFormatter.ofPattern("MM-dd")));
            // Placeholder revenue data - should calculate actual revenue from orders
            revenues.add(Math.random() * 1000 + 500);
        }

        result.put("dates", dates);
        result.put("revenues", revenues);

        return Result.ok(result);
    }

    /**
     * Get dashboard summary statistics
     */
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboardStats() {
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

        // Total revenue - placeholder
        stats.put("totalRevenue", 12500.50);

        return Result.ok(stats);
    }

    /**
     * Get order count by date range
     */
    @GetMapping("/orderCount")
    public Result<Map<String, Object>> getOrderCountByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        Map<String, Object> result = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        // Generate sample data for the date range
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            dates.add(date.format(DateTimeFormatter.ofPattern("MM-dd")));
            // Placeholder count data - should count actual orders for each date
            counts.add((int) (Math.random() * 20 + 5));
        }

        result.put("dates", dates);
        result.put("counts", counts);

        return Result.ok(result);
    }

    /**
     * Get top selling products
     */
    @GetMapping("/topProducts")
    public Result<List<Map<String, Object>>> getTopProducts(@RequestParam(defaultValue = "10") Integer limit) {
        List<Map<String, Object>> result = new ArrayList<>();

        // Placeholder data - should query actual product sales
        for (int i = 1; i <= limit; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", "Product " + i);
            item.put("sales", (int) (Math.random() * 100 + 10));
            result.add(item);
        }

        return Result.ok(result);
    }

    /**
     * Get sales by category
     */
    @GetMapping("/salesByCategory")
    public Result<List<Map<String, Object>>> getSalesByCategory() {
        List<ProductCategory> categories = productCategoryService.list();
        List<Map<String, Object>> result = new ArrayList<>();

        for (ProductCategory category : categories) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", category.getName());
            item.put("value", Math.random() * 1000 + 100); // Placeholder sales data
            result.add(item);
        }

        return Result.ok(result);
    }

    /**
     * Get monthly sales comparison
     */
    @GetMapping("/monthlySales")
    public Result<Map<String, Object>> getMonthlySales(@RequestParam(required = false) Integer year) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }

        Map<String, Object> result = new HashMap<>();
        List<String> months = Arrays.asList(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        );
        List<Double> sales = new ArrayList<>();

        // Generate placeholder monthly sales data
        for (int i = 0; i < 12; i++) {
            sales.add(Math.random() * 5000 + 1000);
        }

        result.put("months", months);
        result.put("sales", sales);
        result.put("year", year);

        return Result.ok(result);
    }

    /**
     * Get customer registration trends
     */
    @GetMapping("/customerTrends")
    public Result<Map<String, Object>> getCustomerTrends(@RequestParam(defaultValue = "30d") String period) {
        Map<String, Object> result = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Integer> registrations = new ArrayList<>();

        // Generate sample data for the last 30 days
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dates.add(date.format(DateTimeFormatter.ofPattern("MM-dd")));
            registrations.add((int) (Math.random() * 10 + 1));
        }

        result.put("dates", dates);
        result.put("registrations", registrations);

        return Result.ok(result);
    }
}
