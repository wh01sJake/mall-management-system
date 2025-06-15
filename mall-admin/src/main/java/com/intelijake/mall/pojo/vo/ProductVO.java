package com.intelijake.mall.pojo.vo;

import com.intelijake.pojo.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * ClassName: ProductVO
 * Description:
 * <p>
 * Datetime: 2025/6/15 0:50
 * Author: @Likun.Fang
 * Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductVO extends Product {
    private String categoryName;
}
