package com.intelijake.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.intelijake.mall.pojo.query.CartQuery;
import com.intelijake.mall.pojo.vo.CartVO;
import com.intelijake.pojo.ShoppingCart;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jake
 * @since 2025-06-23
 */
public interface IShoppingCartService extends IService<ShoppingCart> {

    List<CartVO> list(CartQuery cartQuery);

    boolean updateCheckedAllByUserId(Integer userId, Integer checked);
}
