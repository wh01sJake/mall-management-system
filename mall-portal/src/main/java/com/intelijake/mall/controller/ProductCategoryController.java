package com.intelijake.mall.controller;

import com.intelijake.mall.pojo.vo.ProductCategoryVO;
import com.intelijake.mall.service.IProductCategoryService;
import com.intelijake.mall.util.Result;
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
@RequestMapping("/product-category")
public class ProductCategoryController {


    @Autowired
    private IProductCategoryService productCategoryService;

    @GetMapping("/listAll")
    public Result<List<ProductCategoryVO>> listAll(){

       List<ProductCategoryVO> list = productCategoryService.listAll();

       return Result.ok(list);
    }

}
