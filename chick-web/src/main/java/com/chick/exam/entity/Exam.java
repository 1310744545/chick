package com.chick.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.chick.common.domin.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 考试 例：软考、教师资格证
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Exam extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 考试名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
    * @Author xkx
    * @Description 简述
    * @Date 2022-06-14 15:07
    * @Param
    * @return
    **/
    private String shortDescription;

    /**
     * 封面图片
     */
    private String image;

    /**
     * 类型，此类型为考试的分类
     */
    private String type;

}
