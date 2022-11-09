package com.chick.controller;


import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.pojo.vo.LoginUserVO;
import com.chick.pojo.vo.RegisterEmailUserVO;
import com.chick.pojo.vo.RegisterUserVO;
import com.chick.service.IUserService;
import com.chick.utils.EmailUtil;
import com.chick.utils.JwtUtils;
import com.chick.utils.SecurityUtils;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author 肖可欣
 * @since 2022-05-27 16:07
 */

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private IUserService userService;
    @Autowired
    private DefaultKaptcha defaultKaptcha;
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 登录
     * @param request 请求头
     * @return
     */
    @ApiOperation(value = "登录", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query"),
    })
    @PostMapping("/login")
    public R login(@RequestBody LoginUserVO loginUserVO, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(loginUserVO)){
            return R.failed("系统错误");
        }
        if (StringUtils.isAnyBlank(loginUserVO.getUsername(), loginUserVO.getPassword())) {
            return R.failed("用户名和密码不能为空");
        }
        if (StringUtils.isAnyBlank(loginUserVO.getCode())) {
            return R.failed("验证码不能为空");
        }
        return userService.login(loginUserVO, request);
    }

    /**
     * 用户名密码注册
     * @param registerUserVO 注册人信息
     * @return
     */
    @ApiOperation(value = "用户名密码注册", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query"),
    })
    @PostMapping("/register")
    public R register(@RequestBody RegisterUserVO registerUserVO) {
        if (ObjectUtils.isEmpty(registerUserVO)){
            return R.failed("系统错误");
        }
        if (StringUtils.isAnyBlank(registerUserVO.getUsername(), registerUserVO.getPassword())) {
            return R.failed("用户名和密码不能为空");
        }
        if (StringUtils.isAnyBlank(registerUserVO.getCode())) {
            return R.failed("验证码不能为空");
        }
        return userService.register(registerUserVO);
    }

    /**
     * 邮箱注册
     * @param registerEmailUserVO 注册人信息
     * @return
     */
    @ApiOperation(value = "邮箱注册", httpMethod = "POST")
    @PostMapping("/registerEmail")
    public R registerEmail(@RequestBody RegisterEmailUserVO registerEmailUserVO) {
        if (ObjectUtils.isEmpty(registerEmailUserVO)){
            return R.failed("系统错误");
        }
        if (StringUtils.isAnyBlank(registerEmailUserVO.getEmail(), registerEmailUserVO.getCode())) {
            return R.failed("邮件或验证码不能为空");
        }
        return userService.registerEmail(registerEmailUserVO);
    }

    /**
     * 退出登录,暂时没用,后期将token存到redis中时需要用这里先不用
     * @return
     */
    @ApiOperation(value = "退出登录", httpMethod = "GET")
    @GetMapping("/logout")
    public R logout() {
        return userService.logout();
    }

    /**
     * 获取验证码
     * @param request 请求头
     * @param response  响应头
     * @throws IOException
     */
    @ApiOperation(value = "验证码", httpMethod = "GET")
    @GetMapping("/captcha")
    public R captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HashMap<String, String> result = new HashMap<>();
        try {
            // 将生成的验证码保存在session中
            String text = defaultKaptcha.createText();
            request.getSession().setAttribute("rightCode", text);
            BufferedImage bi = defaultKaptcha.createImage(text);
            ImageIO.write(bi, "jpg", out);
            BASE64Encoder encoder = new BASE64Encoder();
            String encode = encoder.encode(out.toByteArray());
            result.put("text",text);
            result.put("captchaBase64",encode);
            return R.ok(result);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return R.failed();
        }
    }

    /**
     * 通过jwt获取用户信息
     * @return
     */
    @ApiOperation(value = "获取用户信息", httpMethod = "POST")
    @PostMapping("/getUserByJwt")
    public R getUserByJwt(HttpServletRequest request) {
        if (StringUtils.isBlank(request.getHeader(jwtUtils.getHeader()))){
            return R.failed("令牌不能为空");
        }
        return R.ok(userService.getUserByJwt(request.getHeader(jwtUtils.getHeader())));
    }

    @ApiOperation(value = "更换头像", position = 1, httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "文件类型id", paramType = "query"),
    })
    @PostMapping("/uploadHeadPortrait")
    @PreAuthorize(CommonConstants.HAS_ROLE_ADMIN)
    public R uploadHeadPortrait(@RequestParam(name = "file") MultipartFile file){
        if (ObjectUtils.isEmpty(file)){
            return R.failed("请上传文件");
        }
        return userService.uploadHeadPortrait(file, SecurityUtils.getUserId());
    }

    @ApiOperation(value = "更换头像", position = 1, httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "文件类型id", paramType = "query"),
    })
    @PostMapping("/updateUser")
    @PreAuthorize(CommonConstants.HAS_ROLE_ADMIN)
    public R updateUser(String userId, String sex, String phone, String name, String email, String birthday){
        if (StringUtils.isBlank(userId)){
            return R.failed("用户id为空");
        }
        if (!getUserId().equals(userId)){
            return R.failed("无权操作");
        }
        return userService.updateUser(userId, sex, phone, name, email, birthday);
    }


    @GetMapping("/sendEmailCodeRegister")
    public R sendEmailCodeRegister(String email){
        if (StringUtils.isBlank(email)){
            return R.failed("邮件为空");
        }
        return userService.sendEmailCodeRegister(email);
    }

    @GetMapping("/sendEmailCodeLogin")
    public R sendEmailCodeLogin(String email){
        if (StringUtils.isBlank(email)){
            return R.failed("邮件为空");
        }
        return userService.sendEmailCodeLogin(email);
    }
}
