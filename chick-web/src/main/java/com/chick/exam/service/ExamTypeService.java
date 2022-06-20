package com.chick.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chick.base.R;
import com.chick.exam.entity.ExamType;

/**
 * <p>
 * 考试级别/类别 服务类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-14
 */
public interface ExamTypeService extends IService<ExamType> {

    /**
     * 获取考试列表
     *
     * @param validPage 分页信息
     * @param keyword   关键字
     * @param delFlag   删除标记
     * @return 分页列表
     */
    R list(Page<ExamType> validPage, String keyword, String delFlag, String examId);

    /**
     * 删除或恢复考试
     *
     * @param exam 考试
     * @return R
     */
    R deleteOrRenew(ExamType exam);

    /**
     * 保存
     *
     * @param exam 考试
     * @return R
     */
    R saveExam(ExamType exam);

    /**
     * 更新
     *
     * @param exam 考试
     * @return R
     */
    R update(ExamType exam);

}
