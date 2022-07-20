package com.chick.exam.vo;

import com.chick.exam.entity.ExamAnswer;
import com.chick.exam.entity.ExamFile;
import com.chick.exam.entity.ExamKnowledge;
import com.chick.exam.entity.ExamQuestion;
import lombok.Data;

import java.util.ArrayList;

/**
 * @ClassName ExamQuestionVO
 * @Author xiaokexin
 * @Date 2022-07-05 10:50
 * @Description ExamQuestionVO
 * @Version 1.0
 */
@Data
public class ExamQuestionDetailVO {
    // 题目
    private ExamQuestion examQuestion;
    // 题号
    private String questionNum;
    // 题目排序
    private String sort;
    // 考试文件
    private ArrayList<ExamFile> examFiles;
    // 考试知识点
    private ArrayList<ExamKnowledge> examKnowledges;
    // 考试答案
    private ArrayList<ExamAnswer> examAnswers;
    // 问题类型
    private String questionType;
}
