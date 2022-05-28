package com.chick.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @ClassName loginUserDTO
 * @Author xiaokexin
 * @Date 2022-05-27 23:52
 * @Description loginUserDTO
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDTO implements Serializable {

    @ApiModelProperty(value = "username")
    @Size(max = 20, message = "用户名过长")
    @Size(min = 5, message = "用户名过段")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "password")
    @Size(max = 20, message = "用户名过长")
    @Size(min = 5, message = "用户名过段")
    @NotBlank(message = "用户名不能为空")
    private String password;

    @ApiModelProperty(value = "code")
    @NotBlank(message = "用户名不能为空")
    private String code;
}
