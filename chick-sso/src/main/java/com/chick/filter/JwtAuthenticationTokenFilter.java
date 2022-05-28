package com.chick.filter;


import com.chick.pojo.bo.UserInfoDetail;
import com.chick.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName JwtAuthenticationTokenFilter
 * @Author 肖可欣
 * @Descrition 鉴权过滤器
 * @Create 2022-05-27 16:07
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    JwtUtils jwtUtils;
    @Value("${jwt.head}")
    private String head;


    //每次请求都会执行一次,进行鉴权
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization,token, content-type"); //这里要加上content-type
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String JwtToken = request.getHeader(jwtUtils.getHeader());
        //token不为空,以head开头
        if (!StringUtils.isEmpty(JwtToken) && JwtToken.startsWith(head)){
            //token减去头部分
            String Token = JwtToken.substring(head.length());
            //验证是否过期
            if (jwtUtils.isTokenExpired(Token)){
                String username = jwtUtils.getUsernameFromToken(Token);
                //用户名不为空,且还没有进行认证的
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    //获取token中的权限
                    List<SimpleGrantedAuthority> authorities = jwtUtils.getUserroleFromToken(Token);
                    //创建用户
                    UserInfoDetail userInfoDetail = new UserInfoDetail();
                    //添加ID用户名和权限
                    userInfoDetail.setUserId(jwtUtils.getIDFromToken(Token));
                    userInfoDetail.setUserName(username);
                    userInfoDetail.setAuthorities(authorities);
                    //给使用改令牌的用户进行授权
                    UsernamePasswordAuthenticationToken authenticationToken
                            = new UsernamePasswordAuthenticationToken(userInfoDetail, null, authorities);
                    //交给SpringSecurity
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        //继续执行
        filterChain.doFilter(request, response);
    }
}
