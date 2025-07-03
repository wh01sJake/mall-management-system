package com.intelijake.mall;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * ClassName: AdminApplication
 * Description:
 * <p>
 * Datetime: 11/06/2025 17:30
 * Author: @Likun.Fang
 * Version: 1.0
 */

@EnableScheduling
@SpringBootApplication
@MapperScan("com.intelijake.mall.mapper")
@EnableCaching
public class PortalApplication {
    public static void main(String[] args) {
        SpringApplication.run(PortalApplication.class, args);
    }


    @Bean
    public IdentifierGenerator identifierGenerator(){
        return new DefaultIdentifierGenerator();
    }
}
