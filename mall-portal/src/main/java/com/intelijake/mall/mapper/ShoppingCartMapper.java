package com.intelijake.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.intelijake.mall.pojo.query.CartQuery;
import com.intelijake.mall.pojo.vo.CartVO;
import com.intelijake.pojo.ShoppingCart;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jake
 * @since 2025-06-23
 */
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

    List<CartVO> list(CartQuery cartQuery);
}
