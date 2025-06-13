package com.intelijake.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.intelijake.mall.mapper.AdminMapper;
import com.intelijake.mall.pojo.query.AdminQuery;
import com.intelijake.mall.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.intelijake.pojo.Admin;
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
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    AdminMapper adminMapper;

    @Override
    public IPage<Admin> list(AdminQuery adminQuery) {

        IPage<Admin> page = new Page<>(adminQuery.getPage(), adminQuery.getLimit());
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!ObjectUtils.isEmpty(adminQuery.getUsername()), "username", adminQuery.getUsername());
        queryWrapper.like(!ObjectUtils.isEmpty(adminQuery.getEmail()), "email", adminQuery.getEmail());
        adminMapper.selectPage(page,queryWrapper); 
        return page;
    }
}
