package com.intelijake.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.intelijake.mall.mapper.CustomerMapper;
import com.intelijake.mall.pojo.query.CustomerQuery;
import com.intelijake.mall.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.intelijake.pojo.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jake
 * @since 2025-06-11
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Autowired
    CustomerMapper customerMapper;

    @Override
    public IPage<Customer> list(CustomerQuery customerQuery) {
        IPage<Customer> page = new Page<>(customerQuery.getPage(), customerQuery.getLimit());
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!ObjectUtils.isEmpty(customerQuery.getUsername()), "username", customerQuery.getUsername());
        queryWrapper.like(!ObjectUtils.isEmpty(customerQuery.getEmail()), "email", customerQuery.getEmail());
        queryWrapper.like(!ObjectUtils.isEmpty(customerQuery.getPhone()), "phone", customerQuery.getPhone());
        customerMapper.selectPage(page, queryWrapper);
        return page;
    }
}
