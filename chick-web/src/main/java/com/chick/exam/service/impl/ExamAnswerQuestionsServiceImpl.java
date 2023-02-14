package com.chick.exam.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.exam.entity.ExamAnswerQuestions;
import com.chick.exam.entity.ExamRecord;
import com.chick.exam.mapper.ExamAnswerQuestionsMapper;
import com.chick.exam.mapper.ExamRecordMapper;
import com.chick.exam.service.ExamAnswerQuestionsService;
import com.chick.exam.vo.DoAnswerVO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-15
 */
@Service
public class ExamAnswerQuestionsServiceImpl extends ServiceImpl<ExamAnswerQuestionsMapper, ExamAnswerQuestions> implements ExamAnswerQuestionsService {

    @Resource
    private ExamRecordMapper examRecordMapper;
    @Resource
    private ExamAnswerQuestionsMapper examAnswerQuestionsMapper;

    @Override
    public R doAnswer(DoAnswerVO doAnswerVO, String userId) {
        // 查询做题记录
        ExamRecord examRecord = examRecordMapper.selectOne(Wrappers.<ExamRecord>lambdaQuery().eq(ExamRecord::getId, doAnswerVO.getRecordId()));
        // 查询做题情况
        ExamAnswerQuestions examAnswerQuestions = examAnswerQuestionsMapper.selectOne(Wrappers.<ExamAnswerQuestions>lambdaQuery()
                .eq(ExamAnswerQuestions::getRecordId, doAnswerVO.getRecordId())
                .eq(ExamAnswerQuestions::getQuestionId, doAnswerVO.getQuestionId())
                .eq(ExamAnswerQuestions::getDetailId, doAnswerVO.getDetailId())
                .eq(ExamAnswerQuestions::getSubjectId, doAnswerVO.getSubjectId())
                .eq(ExamAnswerQuestions::getExamId, doAnswerVO.getExamId()));
        if (examRecord.getDoQuestionId().contains(doAnswerVO.getQuestionId()) && ObjectUtils.isNotEmpty(examAnswerQuestions.getAnswered()) && CommonConstants.ANSWERED.equals(examAnswerQuestions.getAnswered())) {
            return R.failed("请勿重复作答");
        }
        // 作答
        // 更新记录中的已做题
        examRecord.setDoQuestionId(StringUtils.isBlank(examRecord.getDoQuestionId()) ? doAnswerVO.getQuestionId() : examRecord.getDoQuestionId() + "," + doAnswerVO.getQuestionId());
        examRecordMapper.updateById(examRecord);
        // 插入或更新做题情况
        if (ObjectUtils.isNotEmpty(examAnswerQuestions)) {
            examAnswerQuestions.setAnswered(CommonConstants.ANSWERED);
            examAnswerQuestions.setIsRight(doAnswerVO.getIsRight());
            examAnswerQuestionsMapper.updateById(examAnswerQuestions);
        } else {
            examAnswerQuestionsMapper.insert(new ExamAnswerQuestions(doAnswerVO, userId));
        }
        return null;
    }

    @Override
    public R clearData(String recordId) {
        // 清理记录数据
        ExamRecord examRecord = examRecordMapper.selectOne(Wrappers.<ExamRecord>lambdaQuery()
                .eq(ExamRecord::getId, recordId));
        examRecord.setDoQuestionId("");
        examRecordMapper.updateById(examRecord);
        // 清理答题数据
        examAnswerQuestionsMapper.deleteByRecordId(recordId);
        return R.ok("清理成功");
    }
}
