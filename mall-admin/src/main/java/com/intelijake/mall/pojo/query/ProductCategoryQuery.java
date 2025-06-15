package com.intelijake.mall.pojo.query;

import lombok.Data;

@Data
public class ProductCategoryQuery {
    private Integer page;
    private Integer limit;
    private String name;
}
