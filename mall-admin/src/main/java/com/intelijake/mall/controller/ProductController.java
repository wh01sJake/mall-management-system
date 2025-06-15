package com.intelijake.mall.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.intelijake.mall.pojo.query.ProductQuery;
import com.intelijake.mall.pojo.vo.ProductVO;
import com.intelijake.mall.service.IProductService;
import com.intelijake.mall.util.JwtUtil;
import com.intelijake.mall.util.Result;
import com.intelijake.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jake
 * @since 2025-06-11
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;




    @GetMapping("/list")
    public Result list(ProductQuery productQuery) {

        IPage<ProductVO> page = productService.list(productQuery);
        return Result.ok(page);
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        productService.removeById(id);
        return Result.ok("删除成功");
    }

    @DeleteMapping("/deleteAll/{ids}")
    public Result deleteAll(@PathVariable Integer[] ids) {
        productService.removeBatchByIds(Arrays.asList(ids));
        return Result.ok("删除成功");
    }


    @PostMapping("/add")
    public Result add(@RequestBody Product product) {
        productService.save(product);
        return Result.ok("添加成功");
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Product product = productService.getById(id);
        return Result.ok(product);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Product product) {
        productService.updateById(product);
        return Result.ok("更新成功");
    }



    @GetMapping("/productInfo")
    public Result productInfo(@RequestHeader(name = "Authorization") String token) {
        Map<String, Object> map = JwtUtil.parseToken(token);
        int id = (int) map.get("id");
        Product product = productService.getById(id);
        return Result.ok(product);
    }
}

