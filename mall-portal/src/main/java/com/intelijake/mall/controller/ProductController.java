package com.intelijake.mall.controller;

import com.intelijake.mall.service.IProductService;
import com.intelijake.mall.util.Result;
import com.intelijake.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jake
 * @since 2025-06-23
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping("/selectByCategoryId")
    public Result<List<Product>> selectByCategoryId(Integer id){

       List<Product> list = productService.selectByCategoryId(id);

       return Result.ok(list);
    }

    @GetMapping("/selectById")
    public Result<Product> selectById(Integer id){

        Product product = productService.getById(id);

        return Result.ok(product);
    }

    @GetMapping("/list")
    public Result<Map<String, Object>> list(Integer categoryId, Integer limit){

        List<Product> products;

        if (categoryId != null) {
            // Get products by category
            products = productService.selectByCategoryId(categoryId);
        } else {
            // Get all products
            products = productService.list();
        }

        // Apply limit if specified
        if (limit != null && limit > 0 && products.size() > limit) {
            products = products.subList(0, limit);
        }

        // Return in the format expected by the frontend (with records property)
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("records", products);
        result.put("total", products.size());

        return Result.ok(result);
    }

}
