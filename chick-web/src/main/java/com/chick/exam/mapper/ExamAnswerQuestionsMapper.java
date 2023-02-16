package com.chick.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chick.exam.entity.ExamAnswerQuestions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-15
 */
public interface ExamAnswerQuestionsMapper extends BaseMapper<ExamAnswerQuestions> {

    int deleteByRecordId(String recordId);
    List<String> selectQuestionByExamIdDetailIdSubjectIdUserId(@Param("examId") String examId, @Param("detailId")String detailId, @Param("subjectId")String subjectId, @Param("userId")String userId);
}
