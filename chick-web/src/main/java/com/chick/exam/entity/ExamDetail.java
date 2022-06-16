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
 * 考试详情
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamDetail extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
      @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 考试id
     */
    private String examId;

    /**
     * 考试级别id
     */
    private String examTypeId;

    /**
     * 具体考试名
     */
    private String detailName;

    /**
     * 类型，此类型为考试的分类
     */
    private String type;

    /**
     * 单选个数
     */
    private Integer oneSelect;

    /**
     * 多选个数
     */
    private Integer multipleSelect;

    /**
     * 判断个数
     */
    private Integer judge;

    /**
     * 填空个数
     */
    private Integer fillBlank;

    /**
     * 简答个数
     */
    private Integer shortAnswer;

    /**
     * 是否开放
     */
    private String open;


    /**
     * 排序
     */
    private String sort;

    /**
     * 考试编号
     */
    private String code;
}
