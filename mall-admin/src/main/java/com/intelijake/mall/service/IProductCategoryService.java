package com.intelijake.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.intelijake.mall.pojo.query.ProductCategoryQuery;
import com.intelijake.pojo.ProductCategory;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jake
 * @since 2025-06-11
 */
public interface IProductCategoryService extends IService<ProductCategory> {

    IPage<ProductCategory> list(ProductCategoryQuery productCategoryQuery);
}
