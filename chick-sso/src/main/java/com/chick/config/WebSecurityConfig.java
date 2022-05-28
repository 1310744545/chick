package com.chick.config;

import com.chick.base.CommonConstants;
import com.chick.common.utils.RedisUtil;
import com.chick.filter.JwtAuthenticationTokenFilter;
import com.chick.filter.MyFilterInvocationSecurityMetadataSource;
import com.chick.handle.JwtAuthenticationEntryPoint;
import com.chick.pojo.dto.MenuAndRole;
import com.chick.service.IMenuService;
import com.chick.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * @Author 肖可欣
 * @Create 2022-05-27 16:07
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailServiceImpl userDetailService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 添加授权账户
     */
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                //  设置userDetailService
                .userDetailsService(userDetailService)
                //  使用BCrypt进行密码hash
                .passwordEncoder(passwordEncoder());
    }

    /**
     * 配置密码加密编码
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置协议
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        //将所有资源相关的权限放入缓存
        List<MenuAndRole> menuAndRoles = menuService.loadMenuAndRole();
        //删除所有的老权限数据
        for (MenuAndRole menuAndRole : menuAndRoles){
            if (!"anonymous".equals(menuAndRole.getPermission())){
                String key = CommonConstants.SYS_ROLE_PATH + ":" + menuAndRole.getPath();
                if (redisUtil.hasKey(key)){
                    redisUtil.delete(key);
                }
            }
        }
        //放入所有的新key
        for (MenuAndRole menuAndRole : menuAndRoles){
            if (!"anonymous".equals(menuAndRole.getPermission())){
                String key = CommonConstants.SYS_ROLE_PATH + ":" + menuAndRole.getPath();
                redisUtil.leftPush(key,  menuAndRole.getRole());
            }
        }
        httpSecurity
                //关闭session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                //不用登录即可访问的资源,放行
                .authorizeRequests()
                //使用自定义的受保护资源过滤器和决策管理器
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                        fsi.setAccessDecisionManager(customAccessDecisionManagerBean());
                        fsi.setSecurityMetadataSource(securityMetadataSource());
                        return fsi;
                    }
                })
                .and()
                //解决匿名用户访问无权限资源时的异常
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                //解决认证过的用户访问无权限资源时的异常
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler).and()
                .authorizeRequests()
                //其余资源需要鉴权
                .anyRequest().authenticated().and();


        //禁用缓存
        httpSecurity.headers().cacheControl();

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    //自定义决策管理器
    @Bean
    public CustomAccessDecisionManager customAccessDecisionManagerBean(){
        return new CustomAccessDecisionManager();
    }

    //自定义权限资源获取
    @Bean
    public FilterInvocationSecurityMetadataSource securityMetadataSource() {
        return new MyFilterInvocationSecurityMetadataSource();
    }

}
