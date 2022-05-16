package com.chick.annotation;

import java.lang.annotation.*;

/**
 * 登录用户信息
 *
 * @author green
 * @date 2019/12/30
 * @company 安人股份
 * @desc
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginUser {
}
