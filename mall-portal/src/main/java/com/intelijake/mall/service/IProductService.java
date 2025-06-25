package com.intelijake.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.intelijake.pojo.Product;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jake
 * @since 2025-06-23
 */
public interface IProductService extends IService<Product> {

    List<Product> selectByCategoryId(Integer id);
}
