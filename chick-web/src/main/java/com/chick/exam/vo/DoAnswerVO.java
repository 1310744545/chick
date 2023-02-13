package com.chick.exam.vo;

import lombok.Data;

/**
 * @ClassName DoAnswerVO
 * @Author xiaokexin
 * @Date 2023-02-12 16:23
 * @Description DoAnswerVO
 * @Version 1.0
 */
@Data
public class DoAnswerVO {
    private String examId;
    private String detailId;
    private String questionId;
    private String subjectId;
    private String recordId;
    private String answerId;
    private String isRight;
    private String answer;
}
