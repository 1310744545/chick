package com.chick.tools.vo;

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
public class ToolsVO extends BaseEntity implements Serializable {

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

    /**
     * 文件id
     */
    private String fileId;

    /**
     * 图片地址
     */
    private String imgUrl;

    private String type;

    private String describe;
}
