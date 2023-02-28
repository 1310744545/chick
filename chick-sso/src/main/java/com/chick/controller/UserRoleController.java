package com.chick.controller;


import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户角色配置表  前端控制器
 * </p>
 *
 * @author 肖可欣
 * @since 2022-05-27 16:07
 */
@RestController
@RequestMapping("/sys/userRole")
@Api(tags = "用户角色相关接口")
public class UserRoleController extends BaseController {

}
