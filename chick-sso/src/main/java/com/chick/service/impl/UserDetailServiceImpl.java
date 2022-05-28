package com.chick.service.impl;


import com.chick.pojo.bo.UserInfoDetail;
import com.chick.pojo.entity.Role;
import com.chick.pojo.entity.User;
import com.chick.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 肖可欣
 * @Create 2022-05-27 9:39
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据该用户名称查询在数据库中是否存在
        User user = userService.getUserByUserName(username);
        if (user == null){
            return null;
        }
        //2.查询对应的用户权限
        List<String> permissions = userService.getPermissionByUserName(username);
        List<Role> roles = userService.getUserRole(username);

        //3.将该权限添加到secutity
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String permission : permissions){
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        for (Role permission : roles){
            authorities.add(new SimpleGrantedAuthority(permission.getRoleCode()));
        }
            //创建用户
        UserInfoDetail userInfoDetail = new UserInfoDetail(user);
            //添加权限和角色
        userInfoDetail.setAuthorities(authorities);
        return userInfoDetail;
    }
}
