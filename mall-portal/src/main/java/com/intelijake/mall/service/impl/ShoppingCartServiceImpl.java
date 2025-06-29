package com.intelijake.mall.service.impl;

import com.intelijake.mall.mapper.ShoppingCartMapper;
import com.intelijake.mall.pojo.query.CartQuery;
import com.intelijake.mall.pojo.vo.CartVO;
import com.intelijake.mall.service.IShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.intelijake.pojo.ShoppingCart;
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
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements IShoppingCartService {

    @Autowired
    ShoppingCartMapper shoppingCartMapper;

    @Override
    public List<CartVO> list(CartQuery cartQuery) {

        return shoppingCartMapper.list(cartQuery);
    }

    @Override
    public boolean updateCheckedAllByUserId(Integer userId, Integer checked) {
        try {
            // 使用MyBatis-Plus的条件更新
            ShoppingCart updateCart = new ShoppingCart();
            updateCart.setIsChecked(checked);

            // 创建更新条件：根据用户ID更新
            com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<ShoppingCart> updateWrapper =
                new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<>();
            updateWrapper.eq("user_id", userId);
            updateWrapper.eq("is_deleted", 0); // 只更新未删除的记录

            return this.update(updateCart, updateWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
