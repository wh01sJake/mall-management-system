package com.intelijake.mall.controller;

import com.intelijake.mall.service.IProductService;
import com.intelijake.mall.util.Result;
import com.intelijake.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

}
