package com.chick.exam.mapper;

import com.chick.exam.entity.ExamRealQuestion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chick.exam.vo.ExamQuestionByRecordVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaokexin
 * @since 2022-07-05
 */
public interface ExamRealQuestionMapper extends BaseMapper<ExamRealQuestion> {
    List<ExamQuestionByRecordVO> selectByRealId(String realId);
}
