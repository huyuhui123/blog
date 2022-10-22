package com.z2011.blogapi.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 这个工具包不是我做的，来熟悉一下
 * 主要就是使用jwt包制作和检查token，我们都知道token很有用
 * createToken主要对userId进行生成，加了时间每次token的最后面不一样，我也可以自己通过claims加参数
 * 解密需要用到jwtToken，算法既能用在加密又能用在解密，但是不知道盐的话就没办法
 *
 */
public class JWTUtils {

    private static final String jwtToken = "123456Mszlu!@#$$";

    public static String createToken(Long userId){
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",userId);
//        claims.put("加密默认自带Id",123);
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtToken) // 签发算法，秘钥为jwtToken
                .setClaims(claims) // body数据，要唯一，自行设置
                .setIssuedAt(new Date()) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() +  24*60*60*1000));// 一秒的有效时间
        String token = jwtBuilder.compact();
        return token;
    }

    /**
     * 用来通过token获取存在里面的值，取不出就会报错并返回null
     * @param token
     * @return
     */
    public static Map<String, Object> checkToken(String token){
        try {
            Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String, Object>) parse.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

}