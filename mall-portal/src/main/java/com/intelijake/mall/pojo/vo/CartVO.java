package com.intelijake.mall.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * ClassName: CartVO
 * Description:
 * <p>
 * Datetime: 2025/6/29 0:16
 * Author: @Likun.Fang
 * Version: 1.0
 */

@Data
public class CartVO {

    private Integer id;

    private Integer customerId;

    private Integer productId;

    private String productName;

    private BigDecimal productPrice;

    private String productMainImage;

    private Integer quantity;

    private Integer checked;
}
