package com.intelijake.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.intelijake.mall.service.IShippingAddressService;
import com.intelijake.mall.util.Result;
import com.intelijake.pojo.Customer;
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
@RequestMapping("/shipping")
public class ShippingAddressController {


    @Autowired
    IShippingAddressService shippingAddressService;


    @GetMapping("/list")
    public Result<List<ShippingAddress>> list(HttpSession httpSession){

        Customer customer = (Customer) httpSession.getAttribute("customer");

        if (customer == null) {
            return Result.error("请先登录");
        }

        QueryWrapper<ShippingAddress> wrapper = new QueryWrapper<>();

        wrapper.eq("user_id",customer.getId());

        List<ShippingAddress> list = shippingAddressService.list(wrapper);

        return Result.ok(list);
    }

    /**
     * 添加收货地址
     */
    @PostMapping("/add")
    public Result<String> add(@RequestBody ShippingAddress address, HttpSession httpSession){
        Customer customer = (Customer) httpSession.getAttribute("customer");

        if (customer == null) {
            return Result.error("Please login first");
        }

        address.setUserId(customer.getId());

        // If this is set as default, unset other default addresses
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            QueryWrapper<ShippingAddress> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", customer.getId());
            wrapper.eq("is_default", 1);

            List<ShippingAddress> defaultAddresses = shippingAddressService.list(wrapper);
            for (ShippingAddress defaultAddr : defaultAddresses) {
                defaultAddr.setIsDefault(0);
                shippingAddressService.updateById(defaultAddr);
            }
        }

        boolean success = shippingAddressService.save(address);

        if (success) {
            return Result.ok("Address added successfully");
        } else {
            return Result.error("Failed to add address");
        }
    }

    /**
     * 更新收货地址
     */
    @PostMapping("/update")
    public Result<String> update(@RequestBody ShippingAddress address, HttpSession httpSession){
        Customer customer = (Customer) httpSession.getAttribute("customer");

        if (customer == null) {
            return Result.error("Please login first");
        }

        // Verify the address belongs to the current user
        ShippingAddress existingAddress = shippingAddressService.getById(address.getId());
        if (existingAddress == null || !existingAddress.getUserId().equals(customer.getId())) {
            return Result.error("Address not found");
        }

        address.setUserId(customer.getId());

        // If this is set as default, unset other default addresses
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            QueryWrapper<ShippingAddress> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", customer.getId());
            wrapper.eq("is_default", 1);
            wrapper.ne("id", address.getId());

            List<ShippingAddress> defaultAddresses = shippingAddressService.list(wrapper);
            for (ShippingAddress defaultAddr : defaultAddresses) {
                defaultAddr.setIsDefault(0);
                shippingAddressService.updateById(defaultAddr);
            }
        }

        boolean success = shippingAddressService.updateById(address);

        if (success) {
            return Result.ok("Address updated successfully");
        } else {
            return Result.error("Failed to update address");
        }
    }

    /**
     * 删除收货地址
     */
    @PostMapping("/delete")
    public Result<String> delete(@RequestParam Integer id, HttpSession httpSession){
        Customer customer = (Customer) httpSession.getAttribute("customer");

        if (customer == null) {
            return Result.error("Please login first");
        }

        // Verify the address belongs to the current user
        ShippingAddress address = shippingAddressService.getById(id);
        if (address == null || !address.getUserId().equals(customer.getId())) {
            return Result.error("Address not found");
        }

        boolean success = shippingAddressService.removeById(id);

        if (success) {
            return Result.ok("Address deleted successfully");
        } else {
            return Result.error("Failed to delete address");
        }
    }


}
