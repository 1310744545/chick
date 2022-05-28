package com.chick.utils;

import com.alibaba.fastjson.JSONArray;
import com.chick.pojo.bo.UserInfoDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName JwtUtils
 * @Author 肖可欣
 * @Descrition JWT工具类
 * @Create 2022-05-27 20:41
 */
@Data
@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtUtils {

    private static final String SUBJECT = "username";
    private static final String ROLE_CLAIMS = "roles";
    private static final String ID = "ID";

    private Long expiration;
    private String secret;
    private String header;


    /**
     * 生成token
     *
     * @param userInfoDetail 用户
     * @return token令牌
     */
    public String generateToken(UserInfoDetail userInfoDetail) {
        Map<String, Object> claims = new HashMap<>(3);
        claims.put(ID, userInfoDetail.getUserId());
        claims.put(SUBJECT, userInfoDetail.getUsername());
        claims.put(ROLE_CLAIMS, userInfoDetail.getAuthorities());
        return generateToken(claims);
    }

    /**
     * 从claims生成令牌
     *
     * @param claims 数据声明
     * @return token令牌
     */
    private String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiration);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * 从令牌中获取用户ID
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getIDFromToken(String token) {
        String id;
        try {
            Claims claims = getClaimsFromToken(token);
            id = claims.get(ID).toString();
        } catch (Exception e) {
            id = null;
        }
        return id;
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = (String)claims.get(SUBJECT);
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 从令牌中获取用户角色
     *
     * @param token 令牌
     * @return 用户角色
     */
    public List<SimpleGrantedAuthority> getUserroleFromToken(String token) {
        List<SimpleGrantedAuthority> roles;
        try {
            Claims claims = getClaimsFromToken(token);
            roles = JSONArray.parseArray(JSONArray.toJSONString((List) claims.get(ROLE_CLAIMS)), SimpleGrantedAuthority.class);
        } catch (Exception e) {
            roles = null;
        }
        return roles;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 刷新令牌
     *
     * @param token 令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        String refreshToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.setIssuedAt(new Date());
            refreshToken = generateToken(claims);
        } catch (Exception e) {
            refreshToken = null;
        }
        return refreshToken;
    }

    /**
     * 验证令牌
     *
     * @param token          令牌
     * @param userInfoDetail 用户
     * @return 令牌是否有效
     */
    public Boolean validateToken(String token, UserInfoDetail userInfoDetail) {
        String username = getUsernameFromToken(token);
        return (username.equals(userInfoDetail.getUsername()) && !isTokenExpired(token));
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 用户名
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//            System.out.println(claims.getExpiration());
//            System.out.println(new Date());
        } catch (ExpiredJwtException e) {

            //e.printStackTrace() 当令牌过期 会抛ExpiredJwtException异常
            claims =null;
        }
        return claims;
    }
}

