package com.intelijake.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.intelijake.mall.mapper.ProductMapper;
import com.intelijake.mall.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.intelijake.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jake
 * @since 2025-06-23
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Autowired
    private  ProductMapper productMapper;


    @Override
    public List<Product> selectByCategoryId(Integer id) {

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("category_id",id);

        return productMapper.selectList(queryWrapper);

    }
}
