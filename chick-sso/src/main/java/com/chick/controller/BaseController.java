package com.chick.controller;

import com.chick.base.R;
import com.chick.pojo.bo.UserInfoDetail;
import com.chick.service.impl.UserServiceImpl;
import com.chick.utils.SecurityUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

/**
 * @ClassName BaseController
 * @Author xiaokexin
 * @Date 2022-05-27 16:07
 * @Description 基础Controller
 * @Version 1.0
 */
@Controller
@Api(hidden = true)
public class BaseController {

    @Autowired
    private UserServiceImpl userService;

    public UserInfoDetail getUser() {
        return SecurityUtils.getUserInfo();
    }

    public String getUserId() {
        UserInfoDetail userDO = getUser();
        if (userDO == null || userDO.getUserId() == null) {
            return null;
        }
        return userDO.getUserId()+"";
    }

    public String getUsername() {
        return getUser().getUsername();
    }

    public R validate(BindingResult result) {
        if(result.hasErrors()){
            for (ObjectError error : result.getAllErrors()) {
                return  R.failed(error.getDefaultMessage());
            }
        }
        return  R.ok();
    }
}
