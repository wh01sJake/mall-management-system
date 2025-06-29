package com.intelijake.mall.controller;

import com.intelijake.mall.pojo.query.CartQuery;
import com.intelijake.mall.pojo.vo.CartVO;
import com.intelijake.mall.service.IShoppingCartService;
import com.intelijake.mall.util.Result;
import com.intelijake.pojo.Customer;
import com.intelijake.pojo.ShoppingCart;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jake
 * @since 2025-06-23
 */
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    @Autowired
    IShoppingCartService shoppingCartService;

    @RequestMapping("/add")
    public Result add(ShoppingCart cart, HttpSession httpSession){

        Customer customer = (Customer) httpSession.getAttribute("customer");

        cart.setUserId(customer.getId());

        cart.setIsChecked(1);

        shoppingCartService.add(cart);

        return Result.ok("add success");

    }

    @RequestMapping("/list")
    public Result list(HttpSession session, CartQuery cartQuery){

        //get cartVO details by customer id
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            return Result.error("用户未登录");
        }

        if (cartQuery == null) {
            cartQuery = new CartQuery();
        }
        cartQuery.setCustomerId(customer.getId());

        List<CartVO> list = shoppingCartService.list(cartQuery);
        return Result.ok(list);
    }

    @RequestMapping("/update")
    public Result update(ShoppingCart cart, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            return Result.error("用户未登录");
        }

        cart.setUserId(customer.getId());
        boolean success = shoppingCartService.updateById(cart);

        if (success) {
            return Result.ok("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

    @RequestMapping("/deleteById")
    public Result deleteById(Integer id, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            return Result.error("用户未登录");
        }

        boolean success = shoppingCartService.removeById(id);

        if (success) {
            return Result.ok("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    @RequestMapping("/updateCheckedAll")
    public Result updateCheckedAll(Integer checked, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            return Result.error("用户未登录");
        }

        // 更新该用户所有购物车项目的选中状态
        boolean success = shoppingCartService.updateCheckedAllByUserId(customer.getId(), checked);

        if (success) {
            return Result.ok("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

}
