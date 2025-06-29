package com.intelijake.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.intelijake.pojo.Customer;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jake
 * @since 2025-06-23
 */
public interface ICustomerService extends IService<Customer> {

    Customer login(String username, String password);
}
