package com.chick.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.base.R;
import com.chick.exam.entity.ExamQuestion;
import com.chick.exam.entity.ExamReal;
import com.chick.exam.mapper.ExamRealMapper;
import com.chick.exam.service.ExamRealService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 历年真题 服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-07-05
 */
@Service
public class ExamRealServiceImpl extends ServiceImpl<ExamRealMapper, ExamReal> implements ExamRealService {

    @Resource
    @Override
    public R getRealListByExamIdSubjectId(Page<ExamReal> validPage, String keyword, String delFlag, String examId, String subjectId) {
        LambdaQueryWrapper<ExamReal> wrapper = Wrappers.<ExamReal>lambdaQuery()
                .eq(ExamReal::getDelFlag, delFlag);
        // 添加考试id
        if (StringUtils.isNotBlank(examId)) {
            wrapper.eq(ExamReal::getExamId, examId);
        }
        // 添加科目id
        if (StringUtils.isNotBlank(subjectId)) {
            wrapper.eq(ExamReal::getSubjectId, subjectId);
        }
        //3.添加关键字
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(wr -> wr.like(ExamReal::getYear, keyword));
        }
        return R.ok(baseMapper.selectPage(validPage, wrapper));
    }
}
