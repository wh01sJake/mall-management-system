package com.intelijake.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.intelijake.mall.mapper.ProductCategoryMapper;
import com.intelijake.mall.pojo.query.ProductCategoryQuery;
import com.intelijake.mall.service.IProductCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.intelijake.pojo.ProductCategory;
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
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements IProductCategoryService {

    @Autowired
    ProductCategoryMapper productCategoryMapper;

    @Override
    public IPage<ProductCategory> list(ProductCategoryQuery productCategoryQuery) {

        IPage<ProductCategory> page = new Page<>(productCategoryQuery.getPage(), productCategoryQuery.getLimit());
        QueryWrapper<ProductCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!ObjectUtils.isEmpty(productCategoryQuery.getName()), "name", productCategoryQuery.getName());
        productCategoryMapper.selectPage(page,queryWrapper);
        return page;
    }
}
