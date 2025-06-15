package com.intelijake.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.intelijake.mall.pojo.query.ProductQuery;
import com.intelijake.mall.pojo.vo.ProductVO;
import com.intelijake.pojo.Product;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jake
 * @since 2025-06-11
 */
public interface ProductMapper extends BaseMapper<Product> {

    IPage<ProductVO> list(IPage<ProductVO> page, ProductQuery productQuery);
}
