package com.intelijake.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.intelijake.mall.pojo.query.ProductQuery;
import com.intelijake.pojo.Product;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jake
 * @since 2025-06-11
 */
public interface IProductService extends IService<Product> {

    IPage<Product> list(ProductQuery productQuery);
}
