package com.chick.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.exam.entity.*;
import com.chick.exam.mapper.*;
import com.chick.exam.service.ExamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.exam.vo.ExamTypeDetailVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
                .eq(ExamSubject::getDelFlag, CommonConstants.UN_DELETE_FLAG));
        for (ExamSubject examSubject : examSubjects){
            // 查询科目的题目总数
            Integer questionCount = examQuestionMapper.selectCount(Wrappers.<ExamQuestion>lambdaQuery()
                    .eq(ExamQuestion::getSubjectId, examSubject.getId())
                    .eq(ExamQuestion::getDelFlag, CommonConstants.UN_DELETE_FLAG));

        }
        return null;
    }
}
