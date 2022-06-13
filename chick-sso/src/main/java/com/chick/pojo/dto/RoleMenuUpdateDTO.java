package com.chick.pojo.dto;

import lombok.Data;

import java.util.List;

/**
 * @ClassName RoleMenuUpdateDTO
 * @Author xiaokexin
 * @Date 2022-06-09 13:19
 * @Description RoleMenuUpdateDTO
 * @Version 1.0
 */
@Data
public class RoleMenuUpdateDTO {

    private String roleId;

    private List<String> menuIds;
}
