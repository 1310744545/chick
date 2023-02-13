package com.chick.exam.vo;

import com.chick.exam.entity.ExamAnswer;
import com.chick.exam.entity.ExamAnswerQuestions;
import com.chick.exam.entity.ExamQuestion;
import lombok.Data;

import java.util.List;

/**
 * @ClassName ExamQuestionAnswerVO
 * @Author xiaokexin
 * @Date 2023-02-10 21:03
 * @Description ExamQuestionAnswerVO
 * @Version 1.0
 */
@Data
public class ExamQuestionAnswerVO {
    private ExamQuestion examQuestion;
    private ExamAnswerQuestions examAnswerQuestions;
    private boolean isAnswered;
    private List<ExamAnswer> examAnswers;

    public ExamQuestionAnswerVO() {
    }

    public ExamQuestionAnswerVO(boolean isAnswered) {
        this.isAnswered = isAnswered;
    }
}
