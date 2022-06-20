package com.chick.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.common.domin.BaseEntity;
import com.chick.exam.entity.*;
import com.chick.exam.mapper.*;
import com.chick.exam.service.ExamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.exam.vo.ExamQuestionTypeDetailVO;
import com.chick.exam.vo.ExamTypeDetailVO;
import com.chick.tools.entity.Tools;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.chick.common.utils.ChickUtil.DoId;

/**
 * <p>
 * 考试 例：软考、教师资格证 服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-14
 */
@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {

    @Resource
    private ExamTypeMapper examTypeMapper;
    @Resource
    private ExamDetailMapper examDetailMapper;
    @Resource
    private ExamSubjectMapper examSubjectMapper;
    @Resource
    private ExamQuestionMapper examQuestionMapper;
    @Resource
    private ExamAnswerQuestionsMapper examAnswerQuestionsMapper;
    @Resource
    private ExamQuestionTypeMapper examQuestionTypeMapper;

    @Override
    public R getExamList(Page<Exam> validPage, String type, String keyword) {
        LambdaQueryWrapper<Exam> wrapper = Wrappers.lambdaQuery();
        //2.添加类型
        if (StringUtils.isNotBlank(type)) {
            wrapper.and(wr -> wr.eq(Exam::getType, type));
        }
        //3.添加关键字
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(wr -> wr.like(Exam::getName, keyword));
        }
        return R.ok(baseMapper.selectPage(validPage, wrapper));
    }

    @Override
    public R getExamDetailByExamId(String examId) {
        List<ExamType> examTypes = examTypeMapper.selectList(Wrappers.<ExamType>lambdaQuery()
                .eq(ExamType::getExamId, examId)
                .eq(ExamType::getDelFlag, CommonConstants.UN_DELETE_FLAG)
                .orderByAsc(ExamType::getSort));
        if (examTypes.size() == 0) {
            return R.failed("暂无数据");
        }
        ArrayList<ExamTypeDetailVO> examTypeDetailVOS = new ArrayList<>();
        for (ExamType examType : examTypes) {
            ExamTypeDetailVO examTypeDetailVO = new ExamTypeDetailVO();
            examTypeDetailVO.setExamId(examType.getExamId());
            examTypeDetailVO.setTypeId(examType.getId());
            examTypeDetailVO.setTypeName(examType.getTypeName());
            List<ExamDetail> examDetails = examDetailMapper.selectList(Wrappers.<ExamDetail>lambdaQuery()
                    .eq(ExamDetail::getExamTypeId, examType.getId())
                    .eq(ExamDetail::getExamId, examId)
                    .eq(ExamDetail::getDelFlag, CommonConstants.UN_DELETE_FLAG));
            examTypeDetailVO.setExamDetailList(examDetails);
            examTypeDetailVOS.add(examTypeDetailVO);
        }
        return R.ok(examTypeDetailVOS);
    }

    @Override
    public R getExamAnswerHappeningByUserAndDetailId(String detailId, String userId) {
        // 获取此考试下的科目
        List<ExamSubject> examSubjects = examSubjectMapper.selectList(Wrappers.<ExamSubject>lambdaQuery()
                .eq(ExamSubject::getDetailId, detailId)
                .eq(ExamSubject::getDelFlag, CommonConstants.UN_DELETE_FLAG)
                .orderByAsc(ExamSubject::getSort));
        ArrayList<Object> resultList = new ArrayList<>();
        for (ExamSubject examSubject : examSubjects) {
            // 查询科目的题目总数
            Integer questionCount = examQuestionMapper.selectCount(Wrappers.<ExamQuestion>lambdaQuery()
                    .eq(ExamQuestion::getSubjectId, examSubject.getId())
                    .eq(ExamQuestion::getDelFlag, CommonConstants.UN_DELETE_FLAG));
            // 查询这一科目做了多少道题
            Integer answeredCount = examAnswerQuestionsMapper.selectCount(Wrappers.<ExamAnswerQuestions>query()
                    .select("distinct question_id").lambda()
                    .eq(ExamAnswerQuestions::getExamId, examSubject.getExamId())
                    .eq(ExamAnswerQuestions::getUserId, userId)
                    .eq(ExamAnswerQuestions::getDetailId, detailId)
                    .eq(ExamAnswerQuestions::getSubjectId, examSubject.getId())
                    .eq(ExamAnswerQuestions::getDelFlag, CommonConstants.UN_DELETE_FLAG));
            // 查询做正确的题数
            Integer rightAnsweredCount = examAnswerQuestionsMapper.selectCount(Wrappers.<ExamAnswerQuestions>query()
                    .select("distinct question_id").lambda()
                    .eq(ExamAnswerQuestions::getExamId, examSubject.getExamId())
                    .eq(ExamAnswerQuestions::getUserId, userId)
                    .eq(ExamAnswerQuestions::getDetailId, detailId)
                    .eq(ExamAnswerQuestions::getSubjectId, examSubject.getId())
                    .eq(ExamAnswerQuestions::getIsRight, CommonConstants.EXAM_ANSWER_RIGHT)
                    .eq(ExamAnswerQuestions::getDelFlag, CommonConstants.UN_DELETE_FLAG));

            // 查询每个知识点分类开始
            // 查询该考试详情下有多少知识点分类
            List<ExamQuestionType> examQuestionTypes = examQuestionTypeMapper.selectList(Wrappers.<ExamQuestionType>lambdaQuery()
                    .eq(ExamQuestionType::getDetailId, detailId)
                    .eq(ExamQuestionType::getSubjectId, examSubject.getId())
                    .eq(ExamQuestionType::getDelFlag, CommonConstants.UN_DELETE_FLAG));
            ArrayList<ExamQuestionTypeDetailVO> examQuestionTypeDetailVOS = new ArrayList<>();
            for (ExamQuestionType examQuestionType : examQuestionTypes) {
                // 知识点分类下共有多少道题
                Integer questionTypeCount = examQuestionMapper.selectCount(Wrappers.<ExamQuestion>lambdaQuery()
                        .eq(ExamQuestion::getSubjectId, examSubject.getId())
                        .eq(ExamQuestion::getQuestionTypeId, examQuestionType.getId())
                        .eq(ExamQuestion::getDelFlag, CommonConstants.UN_DELETE_FLAG));
                // 知识点分类下做了多少道题
                Integer answeredTypeCount = examAnswerQuestionsMapper.selectCount(Wrappers.<ExamAnswerQuestions>query()
                        .select("distinct question_id").lambda()
                        .eq(ExamAnswerQuestions::getExamId, examSubject.getExamId())
                        .eq(ExamAnswerQuestions::getUserId, userId)
                        .eq(ExamAnswerQuestions::getDetailId, detailId)
                        .eq(ExamAnswerQuestions::getSubjectId, examSubject.getId())
                        .eq(ExamAnswerQuestions::getQuestionTypeId, examQuestionType.getId())
                        .eq(ExamAnswerQuestions::getDelFlag, CommonConstants.UN_DELETE_FLAG));
                ExamQuestionTypeDetailVO examQuestionTypeDetailVO = new ExamQuestionTypeDetailVO();
                examQuestionTypeDetailVO.setId(examQuestionType.getId());
                examQuestionTypeDetailVO.setName(examQuestionType.getName());
                examQuestionTypeDetailVO.setAllQuestion(questionTypeCount);
                examQuestionTypeDetailVO.setAnsweredQuestion(answeredTypeCount);
                examQuestionTypeDetailVOS.add(examQuestionTypeDetailVO);
            }
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("questionCount", ObjectUtils.isEmpty(questionCount) ? 0 : questionCount);
            resultMap.put("answered", ObjectUtils.isEmpty(answeredCount) ? 0 : answeredCount);
            resultMap.put("rightAnswered", ObjectUtils.isEmpty(rightAnsweredCount) ? 0 : rightAnsweredCount);
            resultMap.put("rateStart", 0);
            resultMap.put("rateEnd", ObjectUtils.isEmpty(answeredCount) ? 0 : ObjectUtils.isEmpty(questionCount) ? 0 : new BigDecimal((Float.valueOf(answeredCount) * 100) / Float.valueOf(questionCount)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            resultMap.put("examSubject", examSubject);
            resultMap.put("examQuestionTypeDetailVOS", examQuestionTypeDetailVOS);
            resultList.add(resultMap);
        }
        return R.ok(resultList);
    }

    @Override
    public R list(Page<Exam> validPage, String keyword, String delFlag, String type) {
        LambdaQueryWrapper<Exam> wrapper = Wrappers.<Exam>lambdaQuery().eq(Exam::getDelFlag, delFlag);

        //2.添加类型
        if (StringUtils.isNotBlank(type)) {
            wrapper.and(wr -> wr.eq(Exam::getType, type));
        }
        //3.添加关键字
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(wr -> wr.like(Exam::getName, keyword));
        }
        return R.ok(baseMapper.selectPage(validPage, wrapper));
    }

    @Override
    public R deleteOrRenew(Exam exam) {
        if (CommonConstants.UN_DELETE_FLAG.equals(exam.getDelFlag())) {
            exam.setDelFlag(CommonConstants.DELETE_FLAG);
        } else {
            exam.setDelFlag(CommonConstants.UN_DELETE_FLAG);
        }
        if (baseMapper.updateById(exam) > 0) {
            return R.ok();
        }
        return R.failed();
    }

    @Override
    public R saveExam(Exam exam) {
        exam.setId(DoId());
        exam.setDelFlag(CommonConstants.UN_DELETE_FLAG);
        if (baseMapper.insert(exam) > 0) {
            return R.ok("保存成功");
        }
        return R.failed("保存失败");
    }

    @Override
    public R update(Exam exam) {
        if (baseMapper.updateById(exam) > 0) {
            return R.ok("更新成功");
        }
        return R.failed("更新失败");
    }
}
