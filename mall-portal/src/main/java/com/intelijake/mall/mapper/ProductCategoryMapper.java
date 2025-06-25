package com.intelijake.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.intelijake.mall.pojo.vo.ProductCategoryVO;
import com.intelijake.pojo.ProductCategory;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jake
 * @since 2025-06-23
 */
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {

    List<ProductCategoryVO> listAll();
}
