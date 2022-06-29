package com.chick.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chick.base.R;
import com.chick.exam.entity.ExamDetail;
import com.chick.exam.entity.ExamSubject;

/**
 * <p>
 * 考试科目 服务类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-15
 */
public interface ExamSubjectService extends IService<ExamSubject> {

    /**
     * @Author xkx
     * @Description 查询考试详情
     * @Date 2022-06-17 13:54
     * @Param [validPage, keyword, delFlag, examId]
     * @return com.chick.base.R
     **/
    R getList(Page<ExamSubject> validPage, String keyword, String delFlag, String examId, String detailId);

    /**
     * @Author xkx
     * @Description 保存考试详情
     * @Date 2022-06-17 13:54
     * @Param [exam]
     * @return com.chick.base.R
     **/
    R insert(ExamSubject exam);

    /**
     * @Author xkx
     * @Description 更新考试详情
     * @Date 2022-06-17 13:55
     * @Param [exam]
     * @return com.chick.base.R
     **/
    R update(ExamSubject exam);


    /**
     * @Author xkx
     * @Description 删除或恢复考试详情
     * @Date 2022-06-17 13:55
     * @Param [exam]
     * @return com.chick.base.R
     **/
    R removeOrRenew(ExamSubject exam);
}
