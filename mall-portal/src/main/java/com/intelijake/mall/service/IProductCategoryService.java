package com.intelijake.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.intelijake.mall.pojo.vo.ProductCategoryVO;
import com.intelijake.pojo.ProductCategory;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jake
 * @since 2025-06-23
 */
public interface IProductCategoryService extends IService<ProductCategory> {

    List<ProductCategoryVO> listAll();
}
