package com.chick.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chick.base.R;
import com.chick.exam.entity.ExamQuestion;
import com.chick.exam.entity.ExamQuestionType;

/**
 * <p>
 * 知识点分类 服务类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-15
 */
public interface ExamQuestionTypeService extends IService<ExamQuestionType> {

    /**
     * @Author xkx
     * @Description 查询考试详情
     * @Date 2022-06-17 13:54
     * @Param [validPage, keyword, delFlag, examId]
     * @return com.chick.base.R
     **/
    R getList(Page<ExamQuestionType> validPage, String keyword, String delFlag, String examId, String detailId);

    /**
     * @Author xkx
     * @Description 保存考试详情
     * @Date 2022-06-17 13:54
     * @Param [exam]
     * @return com.chick.base.R
     **/
    R insert(ExamQuestionType examQuestionType);

    /**
     * @Author xkx
     * @Description 更新考试详情
     * @Date 2022-06-17 13:55
     * @Param [exam]
     * @return com.chick.base.R
     **/
    R update(ExamQuestionType examQuestionType);


    /**
     * @Author xkx
     * @Description 删除或恢复考试详情
     * @Date 2022-06-17 13:55
     * @Param [exam]
     * @return com.chick.base.R
     **/
    R removeOrRenew(ExamQuestionType examQuestionType);
}
