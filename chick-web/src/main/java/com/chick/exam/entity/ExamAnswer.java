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
 *
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamAnswer extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
      @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 题目id
     */
    private String questionId;

    /**
     * 答案内容
     */
    private String name;

    /**
     * 是否为正确答案0正确，1错误
     */
    private Integer correct;

    /**
     * 如果一个题目占有一个空，则为0，如果占多个空，则为对应空的次序，从1开始 1、2、3、4
     */
    private Integer takeUpSort;


}
