package com.intelijake.mall.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ClassName: ServiceLogAspect
 * Description:
 * <p>
 * Datetime: 16/06/2025 18:14
 * Author: @Likun.Fang
 * Version: 1.0
 */

@Aspect
@Component
public class ServiceLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);




//    @Pointcut(value = "execution(public * com.intelijake.mall.service.impl.*.*(..))")
    @Pointcut(value = "@annotation(com.intelijake.mall.annotation.MyLog)")

    public void point(){

    }


    @Around(value = "point()")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable{

        logger.info("=== start processing {}, {} === ",joinPoint.getTarget().getClass(),joinPoint.getSignature().getName());

        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();

        long total = end-start;

        if (total > 3000){
            logger.error("===transaction completed in {} ms",Long.valueOf(total));
        }
        else if (total > 2000){
            logger.warn("===transaction completed in {} ms",Long.valueOf(total));
        }
        else {
            logger.info("=== transaction completed in {} ms ===",Long.valueOf(total));
        }

        return result;

    }


}
