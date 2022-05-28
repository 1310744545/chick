package com.chick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chick.base.R;
import com.chick.pojo.entity.Role;
import com.chick.pojo.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author 肖可欣
 * @since 2022-05-27
 */
public interface IUserService extends IService<User> {

    /**
     * 查询用户是否存在
     * @param username
     * @return
     */
    User getUserByUserName(String username);

    /**
     * 根据用户ID获取用户角色
     *
     * @param username 用户名
     * @return 用户角色
     */
    List<Role> getUserRole(String username);

    /**
     * 查询用户的所有权限
     * @param username
     * @return
     */
    List<String> getPermissionByUserName(String username);

    /**
     * 用户登录
     *
     * @param username 账号
     * @param password 密码
     * @param request
     * @return
     */
    R login(String username, String password, String captchaText, String code, HttpServletRequest request);

    /**
     * 用户注册
     *
     * @param username 账号
     * @param password 密码
     * @return
     */
    R register(String username, String password);

    /**
     * 退出登录
     *
     * @return
     */
    R logout();

    /**
     * 根据用户id获取用户名
     * @param userId
     * @return
     */
    String getNameByUserId(String userId);

    /**
     * 根据Jwt获取用户信息
     * @param JwtToken
     * @return
     */
    User getUserByJwt(String JwtToken);

    /**
     * 上传头像
     * @param userId 用户id
     * @return
     */
    R uploadHeadPortrait(MultipartFile file, String userId);

    /**
     * 更新用户
     * @param userId 用户id
     * @param sex 用户id
     * @param phone 用户id
     * @param name 用户id
     * @param email 用户id
     * @param email 用户id
     * @return
     */
    R updateUser(String userId, String sex, String phone, String name, String email, String birthday);
}
