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
 * @since 2022-06-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamAnswerQuestions extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
      @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 考试id
     */
    private String examId;

    /**
     * 考试详情id
     */
    private String detailId;

    /**
     * 答案id
     */
    private String questionId;

    /**
     * 科目id
     */
    private String subjectId;

    /**
     * 知识点id
     */
    private String questionTypeId;

    /**
     * 考试记录id
     */
    private String recordId;

    /**
     * 是否作答
     */
    private String answered;

    /**
     * 示例单选 A、多选 ABC、 判断A、填空 答案、简答 自己对答案
     */
    private String answer;

    /**
     * 是否正确，0 正确、1错误
     */
    private String isRight;



}
