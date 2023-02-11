package com.chick.exam.vo;

import com.chick.exam.entity.*;
import lombok.Data;

import java.util.List;

/**
 * @ClassName ExamRealGoExamVO
 * @Author xiaokexin
 * @Date 2023-02-10 15:21
 * @Description ExamRealGoExamVO
 * @Version 1.0
 */
@Data
public class ExamRealGoExamVO {
    Exam exam;
    ExamReal examReal;
    ExamSubject examSubject;
    ExamDetail examDetail;
    ExamRecord examRecord;
    List<ExamRealQuestion> examRealQuestions;
}
