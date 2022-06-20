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
public class ExamQuestion extends BaseEntity implements Serializable {

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
     * 考试详情id
     */
    private String detailId;

    /**
     * 科目id
     */
    private String subjectId;

    /**
     * 科目id
     */
    private String questionTypeId;

    /**
     * 题目名
     */
    private String name;

    /**
     * 解析
     */
    private String parse;

    /**
     * 题目类型 单选、多选、判断、填空、简答等
     */
    private String type;

    /**
     * 占用题目个数
     */
    private Integer takeUp;
}
