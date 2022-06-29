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
 * 考试科目
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamSubject extends BaseEntity implements Serializable {

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
     * 题目名
     */
    private String name;

    /**
     * 排序
     */
    private String sort;

}
