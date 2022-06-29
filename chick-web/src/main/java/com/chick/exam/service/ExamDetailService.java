package com.chick.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chick.base.R;
import com.chick.exam.entity.ExamDetail;
import com.chick.exam.entity.ExamQuestionType;
import com.chick.exam.entity.ExamType;

/**
 * <p>
 * 考试详情 服务类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-14
 */
public interface ExamDetailService extends IService<ExamDetail> {

    /**
    * @Author xkx
    * @Description 查询考试详情
    * @Date 2022-06-17 13:54
    * @Param [validPage, keyword, delFlag, examId]
    * @return com.chick.base.R
    **/
    R getList(Page<ExamDetail> validPage, String keyword, String delFlag, String examId, String examTypeId, String types);


    /**
    * @Author xkx
    * @Description 查询考试
    * @Date 2022-06-17 14:33
    * @Param []
    * @return com.chick.base.R
    **/
    R getExamList();

    /**
    * @Author xkx
    * @Description 根据考试id获取考试中的每个分类
    * @Date 2022-06-17 14:33
    * @Param []
    * @return com.chick.base.R
    **/
    R getExamDetailByExamId(String examId);

    /**
    * @Author xkx
    * @Description 通过考试id、详情id、科目id获取知识点
    * @Date 2022-06-17 14:33
    * @Param []
    * @return com.chick.base.R
    **/
    R getExamQuestionTypeByExamId(Page<ExamQuestionType> validPage, String keyword, String delFlag, String examId, String detailId, String subjectId);

    /**
    * @Author xkx
    * @Description 保存考试详情
    * @Date 2022-06-17 13:54
    * @Param [exam]
    * @return com.chick.base.R
    **/
    R insert(ExamDetail exam);

    /**
    * @Author xkx
    * @Description 更新考试详情
    * @Date 2022-06-17 13:55
    * @Param [exam]
    * @return com.chick.base.R
    **/
    R update(ExamDetail exam);


    /**
    * @Author xkx
    * @Description 删除或恢复考试详情
    * @Date 2022-06-17 13:55
    * @Param [exam]
    * @return com.chick.base.R
    **/
    R removeOrRenew(ExamDetail exam);

}
