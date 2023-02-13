package com.chick.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.UUID;

import com.chick.base.CommonConstants;
import com.chick.common.domin.BaseEntity;
import com.chick.exam.vo.DoAnswerVO;
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
     * 问题id
     */
    private String questionId;

    /**
     * 科目id
     */
    private String subjectId;


    /**
     * 考试记录id
     */
    private String recordId;

    /**
     * 是否作答
     */
    private String answered;

    /**
     * 答案id
     */
    private String answerId;

    /**
     * 示例单选 A、多选 ABC、 判断A、填空 答案、简答 自己对答案
     */
    private String answer;

    /**
     * 是否正确，0 正确、1错误
     */
    private String isRight;

    public ExamAnswerQuestions() {
    }

    // 多选专用
    public ExamAnswerQuestions(DoAnswerVO doAnswerVO, String userId) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.examId = doAnswerVO.getExamId();
        this.detailId = doAnswerVO.getDetailId();
        this.questionId = doAnswerVO.getQuestionId();
        this.subjectId = doAnswerVO.getSubjectId();
        this.recordId = doAnswerVO.getRecordId();
        this.answered = CommonConstants.ANSWERED;
        this.answerId = doAnswerVO.getAnswerId();
        this.answer = doAnswerVO.getAnswer();
        this.isRight = doAnswerVO.getIsRight();
    }
}
