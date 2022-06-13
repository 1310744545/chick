package com.chick.tools.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chick.common.domin.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 在线工具表
 * </p>
 *
 * @author 肖可欣
 * @since 2021-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("chick_tools")
public class Tools extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private String id;

    /**
     * 名字
     */
    private String name;

    /**
     * 路径
     */
    private String path;

}
