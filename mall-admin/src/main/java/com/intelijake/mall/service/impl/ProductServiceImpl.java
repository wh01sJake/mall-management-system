package com.intelijake.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.intelijake.mall.mapper.ProductMapper;
import com.intelijake.mall.pojo.query.ProductQuery;
import com.intelijake.mall.pojo.vo.ProductVO;
import com.intelijake.mall.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.intelijake.pojo.Product;
import com.intelijake.pojo.Product;
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
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Autowired
    ProductMapper productMapper;

    @Override
    public IPage<ProductVO> list(ProductQuery productQuery) {

        IPage<ProductVO> page = new Page<>(productQuery.getPage(), productQuery.getLimit());

        productMapper.list(page,productQuery);
        return page;
    }
}
