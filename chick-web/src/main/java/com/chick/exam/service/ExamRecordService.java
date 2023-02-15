package com.chick.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chick.base.R;
import com.chick.exam.entity.ExamReal;
import com.chick.exam.entity.ExamRecord;
import com.chick.exam.vo.CreateRecordVO;
import com.chick.exam.vo.ExamRecordVO;

/**
 * <p>
 * 考试记录 服务类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-16
 */
public interface ExamRecordService extends IService<ExamRecord> {
    R getRecordById(String id);

    R getExaminationRecord(Page<ExamRecordVO> validPage, String keyword, String delFlag, String examId, String subjectId, String detailId, String userId);

    R getQuestionByRecordId(String recordId, String userId);

    R createRecord(CreateRecordVO createRecordVO, String userId);
}
