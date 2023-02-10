package com.chick.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.base.R;
import com.chick.exam.entity.ExamQuestion;
import com.chick.exam.entity.ExamReal;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 历年真题 服务类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-07-05
 */
public interface ExamRealService extends IService<ExamReal> {

    /**
     * @Author xkx
     * @Description 真题列表
     * @Date 2022-06-17 13:54
     * @Param [validPage, keyword, delFlag, examId]
     * @return com.chick.base.R
     **/
    R getRealListByExamIdSubjectId(Page<ExamReal> validPage, String keyword, String delFlag, String examId, String subjectId);
}
