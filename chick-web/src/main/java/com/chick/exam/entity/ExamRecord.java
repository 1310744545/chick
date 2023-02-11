package com.chick.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.UUID;

import com.chick.common.domin.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 考试记录
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamRecord extends BaseEntity implements Serializable {

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
     * 科目id
     */
    private String subjectId;

    /**
     * 类型 1、真题，2、智能、3模拟
     */
    private String type;
    /**
     * 如果是真题，这个不能为空
     */
    private String realId;
    /**
     * 此次记录的所有问题id，以逗号分割，如果是真题为空
     */
    private String allQuestionId;
    /**
     * 此次记录的已做问题id，以逗号分割
     */
    private String doQuestionId;

    public ExamRecord() {
    }

    public ExamRecord(String userId, String examId, String detailId, String type, String realId, String allQuestionId, String doQuestionId, String subjectId) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.examId = examId;
        this.detailId = detailId;
        this.subjectId = subjectId;
        this.type = type;
        this.realId = realId;
        this.allQuestionId = allQuestionId;
        this.doQuestionId = doQuestionId;
    }
}
