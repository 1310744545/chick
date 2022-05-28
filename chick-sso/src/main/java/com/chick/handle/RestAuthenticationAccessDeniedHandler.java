package com.chick.handle;

import cn.hutool.json.JSONUtil;
import com.chick.base.CommonConstants;
import com.chick.base.HttpStatus;
import com.chick.base.R;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName RestAuthenticationAccessDeniedHandler
 * @Description 登陆身份认证
 * @Author 肖可欣
 * @Date 2022-05-27 16:07
 * @Version 1.0
 */
@Component("RestAuthenticationAccessDeniedHandler")
public class RestAuthenticationAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        //登陆状态下，权限不足执行该方法
        System.out.println("权限不足：" + e.getMessage());
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(JSONUtil.toJsonStr(R.failed(HttpStatus.FORBIDDEN, CommonConstants.ACCESS_IS_DENIED)));
        printWriter.flush();
    }
}
