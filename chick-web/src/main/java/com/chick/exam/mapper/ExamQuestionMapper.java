package com.chick.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chick.exam.entity.ExamQuestion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-14
 */
public interface ExamQuestionMapper extends BaseMapper<ExamQuestion> {

    List<String> selectQuestionByExamIdDetailIdSubjectId(@Param("examId") String examId, @Param("detailId")String detailId, @Param("subjectId")String subjectId);
}
