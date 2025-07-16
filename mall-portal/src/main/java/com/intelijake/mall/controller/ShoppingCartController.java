package com.intelijake.mall.controller;

import com.intelijake.mall.pojo.query.CartQuery;
import com.intelijake.mall.pojo.vo.CartVO;
import com.intelijake.mall.service.IShoppingCartService;
import com.intelijake.mall.util.Result;
import com.intelijake.pojo.Customer;
import com.intelijake.pojo.ShoppingCart;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/deleteById")
    public Result deleteById(HttpSession session, jakarta.servlet.http.HttpServletRequest request) {
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            return Result.error("用户未登录");
        }

        // Handle both JSON and form data
        Integer id = null;
        try {
            // First try to get from request parameter (form data)
            String idParam = request.getParameter("id");
            System.out.println("DEBUG: Form parameter 'id' = " + idParam);

            if (idParam != null && !idParam.trim().isEmpty()) {
                id = Integer.valueOf(idParam);
                System.out.println("DEBUG: Parsed ID from form data: " + id);
            } else {
                // Try to read JSON body
                java.io.BufferedReader reader = request.getReader();
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                String jsonBody = sb.toString();
                System.out.println("DEBUG: JSON body: " + jsonBody);

                if (!jsonBody.trim().isEmpty()) {
                    // Parse JSON manually (simple approach)
                    if (jsonBody.contains("\"id\"")) {
                        String idStr = jsonBody.replaceAll(".*\"id\"\\s*:\\s*([0-9]+).*", "$1");
                        id = Integer.valueOf(idStr);
                        System.out.println("DEBUG: Parsed ID from JSON: " + id);
                    }
                }
            }

            if (id == null || id <= 0) {
                return Result.error("无效的ID参数");
            }

        } catch (Exception e) {
            System.out.println("DEBUG: Exception parsing ID: " + e.getMessage());
            e.printStackTrace();
            return Result.error("ID参数格式错误: " + e.getMessage());
        }

        // Add security check: verify the cart item belongs to the current user
        ShoppingCart cartItem = shoppingCartService.getById(id);
        if (cartItem == null || !cartItem.getUserId().equals(customer.getId())) {
            return Result.error("购物车项目不存在或无权限删除");
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
