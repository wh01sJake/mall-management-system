package com.intelijake.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author Jake
 * @since 2025-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerOrder implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * Order Number
     */
    @TableField("order_no")
    private Long orderNo;

    /**
     * User ID
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * Shipping Address ID
     */
    @TableField("shipping_id")
    private Integer shippingId;

    /**
     * Actual payment amount in EUR, with two decimal places
     */
    @TableField("payment_amount")
    private BigDecimal paymentAmount;

    /**
     * Payment Type: 1-Stripe, 2-PayPal, 3-Credit Card, 4-Cash on Delivery
     */
    @TableField("payment_type")
    private Integer paymentType;

    /**
     * Shipping fee, in EUR
     */
    @TableField("postage_fee")
    private Integer postageFee;

    /**
     * Payment time
     */
    @TableField("payment_time")
    private LocalDateTime paymentTime;

    /**
     * Shipping time
     */
    @TableField("shipping_time")
    private LocalDateTime shippingTime;

    /**
     * Transaction completion time
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * Transaction closing time
     */
    @TableField("close_time")
    private LocalDateTime closeTime;

    /**
     * Order status: 0-Cancelled, 1-Unpaid, 2-Paid, 3-Shipped, 4-Completed, 5-Closed
     */
    private Integer status;

    /**
     * Logical delete (1: deleted, 0: not deleted)
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;

    /**
     * Creation time
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * Last update time
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
