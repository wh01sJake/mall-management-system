package com.intelijake.mall.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.intelijake.mall.pojo.query.ProductCategoryQuery;
import com.intelijake.mall.service.IProductCategoryService;
import com.intelijake.mall.util.JwtUtil;
import com.intelijake.mall.util.Result;
import com.intelijake.pojo.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
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
@RequestMapping("/product-category")
public class ProductCategoryController {

    @Autowired
    private IProductCategoryService ProductCategoryService;




    @GetMapping("/list")
    public Result list(ProductCategoryQuery ProductCategoryQuery) {

        IPage<ProductCategory> page = ProductCategoryService.list(ProductCategoryQuery);
        return Result.ok(page);
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        ProductCategoryService.removeById(id);
        return Result.ok("删除成功");
    }

    @DeleteMapping("/deleteAll/{ids}")
    public Result deleteAll(@PathVariable Integer[] ids) {
        ProductCategoryService.removeBatchByIds(Arrays.asList(ids));
        return Result.ok("删除成功");
    }


    @PostMapping("/add")
    public Result add(@RequestBody ProductCategory ProductCategory) {
        ProductCategoryService.save(ProductCategory);
        return Result.ok("添加成功");
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        ProductCategory ProductCategory = ProductCategoryService.getById(id);
        return Result.ok(ProductCategory);
    }

    @PutMapping("/update")
    public Result update(@RequestBody ProductCategory ProductCategory) {
        ProductCategoryService.updateById(ProductCategory);
        return Result.ok("更新成功");
    }



    @GetMapping("/ProductCategoryInfo")
    public Result ProductCategoryInfo(@RequestHeader(name = "Authorization") String token) {
        Map<String, Object> map = JwtUtil.parseToken(token);
        int id = (int) map.get("id");
        ProductCategory ProductCategory = ProductCategoryService.getById(id);
        return Result.ok(ProductCategory);
    }

    @GetMapping("/selectTopCategoryList")
    public Result<List<ProductCategory>> selectTopCategoryList(){

        QueryWrapper queryWrapper = new QueryWrapper();

        queryWrapper.eq("parent_id",0);

        List<ProductCategory> list = ProductCategoryService.list(queryWrapper);

        return Result.ok(list);
    }

    @GetMapping("/selectSecondCategoryListByParentId/{id}")
    public Result<List<ProductCategory>> selectSecondCategoryListByParentId(@PathVariable Integer id){

        QueryWrapper queryWrapper = new QueryWrapper();

        queryWrapper.eq("parent_id",id);

        List<ProductCategory> list = ProductCategoryService.list(queryWrapper);

        return Result.ok(list);
    }


}

