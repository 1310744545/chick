package com.chick.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.chick.common.domin.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author 肖可欣
 * @since 2022-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user")
public class User extends BaseEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.UUID)
    private String userId;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 名字
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 出生日期
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String birthday;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 是否锁定 1是0否
     */
    private String lockFlag;

    /**
     * 是否禁用 1是0否
     */
    private String enabledFlag;

    /**
     * 最近一次登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 是头像地址
     */
    private String headPortraitUrl;

    public User() {
    }

    public User(String userId, String name, String sex, String birthday, String phone, String email) {
        this.userId = userId;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
    }
}
