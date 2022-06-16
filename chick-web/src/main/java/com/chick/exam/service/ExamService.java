package com.chick.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chick.base.R;
import com.chick.exam.entity.Exam;

/**
 * <p>
 * 考试 例：软考、教师资格证 服务类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-14
 */
public interface ExamService extends IService<Exam> {

    R getExamList(Page<Exam> validPage, String type, String keyword);

    R getExamDetailByExamId(String examId);

    R getExamAnswerHappeningByUserAndDetailId(String detailId, String userId);
}
