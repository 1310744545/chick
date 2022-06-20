package com.chick.exam.vo;

import lombok.Data;

/**
 * @ClassName ExamQuestionTypeDetailVO
 * @Author xiaokexin
 * @Date 2022-06-16 13:49
 * @Description ExamQuestionTypeDetailVO
 * @Version 1.0
 */
@Data
public class ExamQuestionTypeDetailVO {

    private String id;

    private String name;

    private Integer allQuestion;

    private Integer answeredQuestion;

}
