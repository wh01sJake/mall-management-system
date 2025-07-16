package com.intelijake.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.intelijake.mall.annotation.MyLog;
import com.intelijake.mall.constant.RedisConstants;
import com.intelijake.mall.pojo.query.CustomerQuery;
import com.intelijake.mall.service.ICustomerService;
import com.intelijake.mall.util.Result;
import com.intelijake.pojo.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * <p>
 *  Customer Management Controller
 * </p>
 *
 * @author Jake
 * @since 2025-06-11
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/list")
    public Result list(CustomerQuery customerQuery) {
        IPage<Customer> page = customerService.list(customerQuery);
        return Result.ok(page);
    }

    @MyLog(module = "customer module")
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        customerService.removeById(id);
        return Result.ok("Deleted successfully");
    }

    @MyLog(module = "customer module")
    @DeleteMapping("/deleteAll/{ids}")
    public Result deleteAll(@PathVariable Integer[] ids) {
        customerService.removeBatchByIds(Arrays.asList(ids));
        return Result.ok("Deleted successfully");
    }

    @PostMapping("/add")
    public Result add(@RequestBody Customer customer) {
        // Set default status if not provided
        if (customer.getStatus() == null) {
            customer.setStatus(1); // Default to active
        }
        customerService.save(customer);
        return Result.ok("Added successfully");
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Customer customer = customerService.getById(id);
        return Result.ok(customer);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Customer customer) {
        customerService.updateById(customer);
        return Result.ok("Updated successfully");
    }

    @MyLog(module = "customer module")
    @PutMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Integer id, @PathVariable Integer status) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setStatus(status);
        customerService.updateById(customer);
        
        String statusText = status == 1 ? "activated" : "blocked";
        return Result.ok("Customer " + statusText + " successfully");
    }
}

