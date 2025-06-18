package com.intelijake.mall.annotation;

import java.lang.annotation.*;

/**
 * ClassName: MyLog
 * Description:
 * <p>
 * Datetime: 18/06/2025 17:10
 * Author: @Likun.Fang
 * Version: 1.0
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyLog {

    String module() default "";
}
