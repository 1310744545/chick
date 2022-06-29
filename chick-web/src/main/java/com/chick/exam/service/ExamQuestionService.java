package com.chick.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chick.base.R;
import com.chick.exam.entity.ExamQuestion;
import com.chick.exam.entity.ExamSubject;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-14
 */
public interface ExamQuestionService extends IService<ExamQuestion> {

    /**
     * @Author xkx
     * @Description 查询考试详情
     * @Date 2022-06-17 13:54
     * @Param [validPage, keyword, delFlag, examId]
     * @return com.chick.base.R
     **/
    R getList(Page<ExamQuestion> validPage, String keyword, String delFlag, String examId, String detailId, String subjectId);

    /**
     * @Author xkx
     * @Description 保存考试详情
     * @Date 2022-06-17 13:54
     * @Param [exam]
     * @return com.chick.base.R
     **/
    R insert(ExamQuestion examQuestion);

    /**
     * @Author xkx
     * @Description 更新考试详情
     * @Date 2022-06-17 13:55
     * @Param [exam]
     * @return com.chick.base.R
     **/
    R update(ExamQuestion examQuestion);


    /**
     * @Author xkx
     * @Description 删除或恢复考试详情
     * @Date 2022-06-17 13:55
     * @Param [exam]
     * @return com.chick.base.R
     **/
    R removeOrRenew(ExamQuestion examQuestion);


    R getExamDetailByExamId(String examId);

    R getExamSubjectByExamId(String examId, String getExamSubjectByExamId);
}
