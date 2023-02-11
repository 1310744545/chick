package com.chick.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.exam.entity.*;
import com.chick.exam.mapper.*;
import com.chick.exam.service.ExamQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.exam.vo.ExamQuestionAnswerVO;
import com.chick.exam.vo.ExamTypeDetailVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.chick.common.utils.ChickUtil.DoId;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-14
 */
@Service
public class ExamQuestionServiceImpl extends ServiceImpl<ExamQuestionMapper, ExamQuestion> implements ExamQuestionService {

    @Resource
    private ExamTypeMapper examTypeMapper;
    @Resource
    private ExamDetailMapper examDetailMapper;
    @Resource
    private ExamSubjectMapper examSubjectMapper;
    @Resource
    private ExamAnswerMapper examAnswerMapper;

    @Override
    public R getList(Page<ExamQuestion> validPage, String keyword, String delFlag, String examId, String detailId, String subjectId) {
        LambdaQueryWrapper<ExamQuestion> wrapper = Wrappers.<ExamQuestion>lambdaQuery()
                .eq(ExamQuestion::getDelFlag, delFlag);

        // 添加考试id
        if (StringUtils.isNotBlank(examId)) {
            wrapper.eq(ExamQuestion::getExamId, examId);
        }
        // 添加考试详情id
        if (StringUtils.isNotBlank(detailId)) {
            wrapper.eq(ExamQuestion::getDetailId, detailId);
        }
        // 添加科目id
        if (StringUtils.isNotBlank(subjectId)) {
            wrapper.eq(ExamQuestion::getSubjectId, subjectId);
        }
        //3.添加关键字
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(wr -> wr.like(ExamQuestion::getName, keyword));
        }
        return R.ok(baseMapper.selectPage(validPage, wrapper));
    }

    @Override
    public R insert(ExamQuestion examQuestion) {
        examQuestion.setId(DoId());
        if (baseMapper.insert(examQuestion) > 0) {
            return R.ok("保存成功");
        }
        return R.failed("保存失败");
    }

    @Override
    public R update(ExamQuestion examQuestion) {
        if (baseMapper.updateById(examQuestion) > 0) {
            return R.ok("更新成功");
        }
        return R.failed("更新失败");
    }

    @Override
    public R removeOrRenew(ExamQuestion examQuestion) {
        if (CommonConstants.UN_DELETE_FLAG.equals(examQuestion.getDelFlag())) {
            examQuestion.setDelFlag(CommonConstants.DELETE_FLAG);
        } else {
            examQuestion.setDelFlag(CommonConstants.UN_DELETE_FLAG);
        }
        if (baseMapper.updateById(examQuestion) > 0) {
            return R.ok("删除成功");
        }
        return R.failed("更新失败");
    }

    @Override
    public R getExamDetailByExamId(String examId) {
        List<ExamDetail> examDetails = examDetailMapper.selectList(Wrappers.<ExamDetail>lambdaQuery()
                .eq(ExamDetail::getDelFlag, CommonConstants.UN_DELETE_FLAG)
                .eq(ExamDetail::getExamId, examId));
        return R.ok(examDetails);
    }

    @Override
    public R getExamSubjectByExamId(String examId, String getExamSubjectByExamId) {
        List<ExamSubject> examSubjects = examSubjectMapper.selectList(Wrappers.<ExamSubject>lambdaQuery()
                .eq(ExamSubject::getDelFlag, CommonConstants.UN_DELETE_FLAG)
                .eq(ExamSubject::getExamId, examId)
                .eq(ExamSubject::getDetailId, getExamSubjectByExamId));
        return R.ok(examSubjects);
    }

    @Override
    public R getQuestionByQuestionId(String questionId) {
        ExamQuestion examQuestion = baseMapper.selectOne(Wrappers.<ExamQuestion>lambdaQuery().eq(ExamQuestion::getId, questionId));
        List<ExamAnswer> examAnswers = examAnswerMapper.selectList(Wrappers.<ExamAnswer>lambdaQuery().eq(ExamAnswer::getQuestionId, questionId)
                .orderByAsc(ExamAnswer::getSort));
        ExamQuestionAnswerVO examQuestionAnswerVO = new ExamQuestionAnswerVO();
        examQuestionAnswerVO.setExamQuestion(examQuestion);
        examQuestionAnswerVO.setExamAnswers(examAnswers);
        return R.ok(examQuestionAnswerVO);
    }
}
