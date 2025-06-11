package com.intelijake.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
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
public class Customer implements Serializable {


    /**
     * User table ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * Username
     */
    private String username;

    /**
     * User password, hashed
     */
    private String password;

    private String email;

    private String phone;

    /**
     * Password recovery question
     */
    @TableField("recovery_question")
    private String recoveryQuestion;

    /**
     * Password recovery answer
     */
    @TableField("recovery_answer")
    private String recoveryAnswer;

    /**
     * Status (1: Active, 0: Disabled)
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
