package com.chick.pojo.bo;

import cn.hutool.core.util.ObjectUtil;
import com.chick.base.CommonConstants;
import com.chick.pojo.entity.User;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @ClassName UserInfoDetail
 * @Author 肖可欣
 * @Descrition 登录用户身份权限
 * @Create 2022-05-27 16:07
 */
@Data
public class UserInfoDetail implements UserDetails {
    /**
     * sysUser
     */
    private User user;

    /**
     * id
     */
    private String userId;

    /**
     * 账号
     */
    private String userName;

    /**
     * 权限
     */
    private Collection<? extends GrantedAuthority> authorities;

    public UserInfoDetail() {
    }

    public UserInfoDetail(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public UserInfoDetail(User user) {
        this.user = user;
        this.userId = user.getUserId();
        this.userName = user.getUsername();
    }

    public UserInfoDetail(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        if (ObjectUtil.isNull(user)) {
            return null;
        }
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.userName;
    }


    /**
     * 账户是否未过期,过期无法验证
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    /**
     * 指定用户是否解锁,锁定的用户无法进行身份验证
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        if (ObjectUtil.isNull(user)) {
            return true;
        }
        return StringUtils.endsWithIgnoreCase(CommonConstants.NO, user.getLockFlag());
    }

    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可用 ,禁用的用户不能身份验证
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        if (ObjectUtil.isNull(user)) {
            return true;
        }
        return StringUtils.endsWithIgnoreCase(CommonConstants.NO, user.getEnabledFlag());
    }
}
