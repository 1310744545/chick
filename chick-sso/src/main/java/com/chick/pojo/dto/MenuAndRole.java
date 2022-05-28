package com.chick.pojo.dto;

import lombok.Data;

/**
 * @ClassName MenuAndRole
 * @Author xiaokexin
 * @Date 2022-05-27 15:09
 * @Description MenuAndRole
 * @Version 1.0
 */
@Data
public class MenuAndRole {

    private String path;

    private String permission;

    private String role;
}
