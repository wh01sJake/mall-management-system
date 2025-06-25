package com.intelijake.mall.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * ClassName: ProductCategoryVO
 * Description:
 * <p>
 * Datetime: 23/06/2025 16:14
 * Author: @Likun.Fang
 * Version: 1.0
 */
@Data
public class ProductCategoryVO {
    private Integer id;
    private Integer parentId;
    private String name;
    private List<ProductCategoryVO> childList;
}
