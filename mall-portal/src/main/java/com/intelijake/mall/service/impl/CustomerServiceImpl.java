package com.intelijake.mall.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.intelijake.mall.mapper.CustomerMapper;
import com.intelijake.mall.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.intelijake.pojo.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jake
 * @since 2025-06-23
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Autowired
    CustomerMapper customerMapper;

    @Override
    public Customer login(String username, String password) {

        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("username",username);

        queryWrapper.eq("password",password);


        return customerMapper.selectOne(queryWrapper);

    }

    @Override
    public Customer findByUsername(String username) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return customerMapper.selectOne(queryWrapper);
    }

    @Override
    public Customer findByEmail(String email) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        return customerMapper.selectOne(queryWrapper);
    }
}
