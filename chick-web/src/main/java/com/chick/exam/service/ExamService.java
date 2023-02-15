package com.chick.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chick.base.R;
import com.chick.exam.entity.Exam;
import com.chick.tools.vo.ToolsVO;

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

    /**
     * 获取考试列表
     *
     * @param validPage 分页信息
     * @param keyword   关键字
     * @param delFlag   删除标记
     * @return 分页列表
     */
    R list(Page<Exam> validPage, String keyword, String delFlag, String type);

    /**
     * 删除或恢复考试
     *
     * @param exam 考试
     * @return R
     */
    R deleteOrRenew(Exam exam);

    /**
     * 保存
     *
     * @param exam 考试
     * @return R
     */
    R saveExam(Exam exam);

    /**
     * 更新
     *
     * @param exam 考试
     * @return R
     */
    R update(Exam exam);
    R checkFile();



}
