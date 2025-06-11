package com.intelijake.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ClassName: AdminApplication
 * Description:
 * <p>
 * Datetime: 11/06/2025 17:30
 * Author: @Likun.Fang
 * Version: 1.0
 */

@SpringBootApplication
@MapperScan("com.intelijake.mall.mapper")
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
