package com.chick.utils;


import com.chick.pojo.bo.UserInfoDetail;
import com.chick.base.HttpStatus;
import com.chick.exception.CustomException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @description: 安全服务工具类
 * @author: 肖可欣
 * @createDate: 2020-01-24 10:17
 * @updateDate: 2020-01-24 10:17
 * @others: 如果有修改人员，请在此处说明修改的内容以及修改人、修改时间。
 * 修改内容：XXX
 * 修改人：XXX
 * 修改时间：yyyy-MM-dd hh:mm
 */
public class SecurityUtils {
    /**
     * @description: 获取用户账户
     * @author: 肖可欣
     * @createDate: 2020-01-24 10:17
     * @updateDate: 2020-01-24 10:17
     * @return: java.lang.String
     * @others: 如果有修改人员，请在此处说明修改的内容以及修改人、修改时间。
     * 修改内容：XXX
     * 修改人：XXX
     * 修改时间：yyyy-MM-dd hh:mm
     **/
    public static String getUsername() {
        try {
            return getUserInfo().getUsername();
        } catch (Exception e) {
            throw new CustomException("获取用户账户异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户ID
     *
     * @return
     */
    public static String getUserId() {
        try {
            return getUserInfo().getUserId();
        } catch (Exception e) {
            throw new CustomException("获取用户账户异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * @description: 获取用户
     * @author: 肖可欣
     * @createDate: 2020-01-24 10:17
     * @updateDate: 2020-01-24 10:17
     * @return: com.xkx.chick.common.security.UserInfoDetail
     * @others: 如果有修改人员，请在此处说明修改的内容以及修改人、修改时间。
     * 修改内容：XXX
     * 修改人：XXX
     * 修改时间：yyyy-MM-dd hh:mm
     **/
    public static UserInfoDetail getUserInfo() {
        try {
            return (UserInfoDetail) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new CustomException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * @description: 获取Authentication
     * @author: 肖可欣
     * @createDate: 2020-01-24 10:17
     * @updateDate: 2020-01-24 10:17
     * @return: org.springframework.security.core.Authentication
     * @others: 如果有修改人员，请在此处说明修改的内容以及修改人、修改时间。
     * 修改内容：XXX
     * 修改人：XXX
     * 修改时间：yyyy-MM-dd hh:mm
     **/
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    /**
     * @param password: 密码
     * @description: 生成BCryptPasswordEncoder密码
     * @author: 肖可欣
     * @createDate: 2020-01-24 10:17
     * @updateDate: 2020-01-24 10:17
     * @return: java.lang.String 加密字符串
     * @others: 如果有修改人员，请在此处说明修改的内容以及修改人、修改时间。
     * 修改内容：XXX
     * 修改人：XXX
     * 修改时间：yyyy-MM-dd hh:mm
     **/
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * @param rawPassword:     真实密码
     * @param encodedPassword: 加密后字符
     * @description: 判断密码是否相同
     * @author: 肖可欣
     * @createDate: 2020-01-24 10:17
     * @updateDate: 2020-01-24 10:17
     * @return: boolean 结果
     * @others: 如果有修改人员，请在此处说明修改的内容以及修改人、修改时间。
     * 修改内容：XXX
     * 修改人：XXX
     * 修改时间：yyyy-MM-dd hh:mm
     **/
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
