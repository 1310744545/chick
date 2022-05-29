package com.chick.pojo.vo;

import lombok.Data;

/**
 * @ClassName loginUserVO
 * @Author xiaokexin
 * @Date 2022-05-29 16:17
 * @Description loginUserVO
 * @Version 1.0
 */
@Data
public class RegisterUserVO {
    private String username;
    private String password;
    private String captchaText;
    private String code;
}
