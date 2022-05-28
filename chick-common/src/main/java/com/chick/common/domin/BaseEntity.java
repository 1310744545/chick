package com.chick.common.domin;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName BaseEntity
 * @Author xiaokexin
 * @Date 2022-05-27 18:23
 * @Description 基础实体类
 * @Version 1.0
 */

@Data
@Accessors(chain = true)
public class BaseEntity {

    /**
     * '创建者'
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * '创建时间'
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private Date createDate;

    /**
     * '更新者'
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    private String updateBy;

    /**
     * '更新时间'
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "update_date", fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

    /**
     * 是否逻辑删除 1是0否
     */
    @TableField(fill = FieldFill.INSERT)
    private String delFlag;
}
