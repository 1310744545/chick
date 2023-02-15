package com.chick.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.exam.entity.ExamRecord;
import com.chick.exam.vo.ExamRecordVO;

import java.util.List;

/**
 * <p>
 * 考试记录 Mapper 接口
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-16
 */
public interface ExamRecordMapper extends BaseMapper<ExamRecord> {
    IPage<ExamRecordVO> selectExamRecord(Page<ExamRecordVO> validPage, String examId, String subjectId, String detailId, String userId);

    ExamRecord selectExamRecordInfoByRealId(String realId);
}
