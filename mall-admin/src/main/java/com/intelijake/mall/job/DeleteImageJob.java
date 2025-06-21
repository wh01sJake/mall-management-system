package com.intelijake.mall.job;

import com.intelijake.mall.constant.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Set;

/**
 * ClassName: DeleteImageJob
 * Description:
 * <p>
 * Datetime: 21/06/2025 20:15
 * Author: @Likun.Fang
 * Version: 1.0
 */

@Configuration
public class DeleteImageJob {


    @Autowired
    RedisTemplate redisTemplate;


    @Scheduled(cron = "0 0 */6 * * *")
    public void deleteImage(){

        System.out.println("deleteImage operated");
        // get the difference from the two sets

        Set<String> difference = redisTemplate.opsForSet().difference(RedisConstants.UPLOAD_IMAGE, RedisConstants.UPLOAD_IMAGE_TO_DB);

        for (String img : difference){

            redisTemplate.delete(img);

            System.out.println("deleted img from aws " +img);
        }

        //remove from redis
        redisTemplate.delete(RedisConstants.UPLOAD_IMAGE);
        redisTemplate.delete(RedisConstants.UPLOAD_IMAGE_TO_DB);



    }
}
