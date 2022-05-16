package com.chick.config;

import com.chick.annotation.LoginUser;
import groovy.util.logging.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class TokenArgsResolver implements HandlerMethodArgumentResolver {

    /**
     * 单点用户查询实现接口
     */
//    private SsoUserFeign ssoUserFeign;

//    public TokenArgsResolver(SsoUserFeign ssoUserFeign) {
//        this.ssoUserFeign = ssoUserFeign;
//    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 是否存在登录用户数据提取注解
        boolean hasAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        // 是否为应用统一的用户对象类型
        boolean sameClass = parameter.getParameterType().equals(String.class);
        return hasAnnotation && sameClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String queryString = request.getQueryString();
//        String userCode = request.getHeader(HEADER_USER_CODE);
//        if (StringUtils.hasText(userCode)) {
//            SysUser userDetail = ssoUserFeign.getUserDetail(userCode);
//            return userDetail;
//        }
        return "我是你爹";
    }

}
