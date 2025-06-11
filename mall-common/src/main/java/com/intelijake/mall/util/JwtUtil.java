package com.intelijake.mall.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: JwtUtil
 * Description:
 * <p>
 * Datetime: 07/06/2025 16:38
 * Author: @Likun.Fang
 * Version: 1.0
 */
public class JwtUtil {

    private static final String KEY = "easy";

    //接收业务数据,生成token并返回
    public static String createToken(Map<String, Object> map) {
        return JWT.create()
                .withClaim("claims", map) //有效载荷
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 )) //失效时间
                .sign(Algorithm.HMAC256(KEY)); //秘钥
    }

    //接收token,验证token,并返回业务数据
    public static Map<String, Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "zhangsan");
        String token = createToken(map);
        System.out.println(token);

        String token2 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbGFpbXMiOnsibmFtZSI6InpoYW5nc2FuIn0sImV4cCI6MTc0OTMxNDM5OX0.CpStsVEiU-3k4smgNJieTnenii5azHFeU0HAuzqGAp4";
        Map<String, Object> map2 = parseToken(token2);
        System.out.println(map2);

    }
}