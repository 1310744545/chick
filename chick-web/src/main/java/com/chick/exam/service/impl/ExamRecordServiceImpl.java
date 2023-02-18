package com.chick.exam.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.exam.entity.*;
import com.chick.exam.mapper.*;
import com.chick.exam.service.ExamRecordService;
import com.chick.exam.vo.CreateRecordVO;
import com.chick.exam.vo.ExamQuestionByRecordVO;
import com.chick.exam.vo.ExamRealGoExamVO;
import com.chick.exam.vo.ExamRecordVO;
import com.chick.util.ExamUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * <p>
 * 考试记录 服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-16
 */
@Service
public class ExamRecordServiceImpl extends ServiceImpl<ExamRecordMapper, ExamRecord> implements ExamRecordService {

    @Resource
    private ExamRecordMapper examRecordMapper;
    @Resource
    private ExamQuestionMapper examQuestionMapper;
    @Resource
    private ExamRealMapper examRealMapper;
    @Resource
    private ExamRealQuestionMapper examRealQuestionMapper;
    @Resource
    private ExamMapper examMapper;
    @Resource
    private ExamSubjectMapper examSubjectMapper;
    @Resource
    private ExamDetailMapper examDetailMapper;
    @Resource
    private ExamAnswerQuestionsMapper examAnswerQuestionsMapper;

    @Override
    public R createRecord(CreateRecordVO createRecordVO, String userId) {
        switch (createRecordVO.getType()) {
            case CommonConstants.REAL:
                // 真题信息
                ExamRecord examRecordInfo = examRecordMapper.selectExamRecordInfoByRealId(createRecordVO.getRealId());
                // 检查该用户是否做过这套题
                ExamRecord examRecord = examRecordMapper.selectOne(Wrappers.<ExamRecord>lambdaQuery()
                        .eq(ExamRecord::getExamId, examRecordInfo.getExamId())
                        .eq(ExamRecord::getDetailId, examRecordInfo.getDetailId())
                        .eq(ExamRecord::getRealId, examRecordInfo.getRealId())
                        .eq(ExamRecord::getUserId, userId)
                        .eq(ExamRecord::getType, CommonConstants.REAL)
                        .eq(ExamRecord::getDelFlag, CommonConstants.UN_DELETE_FLAG));
                // 如果没做过就添加记录
                if (ObjectUtils.isEmpty(examRecord)) {
                    examRecord = new ExamRecord(userId, examRecordInfo.getExamId(), examRecordInfo.getDetailId(), CommonConstants.REAL, examRecordInfo.getRealId(), "", "", examRecordInfo.getSubjectId());
                    examRecordMapper.insert(examRecord);
                }
                return R.ok(examRecord.getId(), "试题生成成功");
            case CommonConstants.INTELLIGENT_EXERCISE:
                // 智能练题
                if (createRecordVO.getCount() <= 0) {
                    return R.failed("练题数需大于0");
                }
                List<String> allQuestionIds = new ArrayList<>(); // 要抽取的题目id啊
                List<String> doQuestions = new ArrayList<>(); // 去除的
                switch (createRecordVO.getQuestionType()) {
                    case CommonConstants.UNLIMITED:
                        // 不限
                        allQuestionIds = examQuestionMapper.selectQuestionByExamIdDetailIdSubjectId(createRecordVO.getExamId(), createRecordVO.getDetailId(), createRecordVO.getSubjectId());
                        break;
                    case CommonConstants.UN_DO:
                        // 未做题
                        allQuestionIds = examQuestionMapper.selectQuestionByExamIdDetailIdSubjectId(createRecordVO.getExamId(), createRecordVO.getDetailId(), createRecordVO.getSubjectId());
                        doQuestions = examAnswerQuestionsMapper.selectQuestionByExamIdDetailIdSubjectIdUserId(createRecordVO.getExamId(), createRecordVO.getDetailId(), createRecordVO.getSubjectId(), userId);
                        allQuestionIds.removeAll(doQuestions);
                        if (allQuestionIds.size() < createRecordVO.getCount()) {
                            return R.failed("未做题不足所选个数，请重新选择");
                        }
                        break;
                    case CommonConstants.DO:
                        // 已做题
                        allQuestionIds = examAnswerQuestionsMapper.selectQuestionByExamIdDetailIdSubjectIdUserId(createRecordVO.getExamId(), createRecordVO.getDetailId(), createRecordVO.getSubjectId(), userId);
                        if (allQuestionIds.size() < createRecordVO.getCount()) {
                            return R.failed("已做题不足所选个数，请重新选择");
                        }
                        break;
                }
                List<String> questionIds = ExamUtils.randomValues(allQuestionIds, createRecordVO.getCount());
                StringJoiner sj = new StringJoiner(",");
                questionIds.forEach(sj::add);
                examRecord = new ExamRecord(userId, createRecordVO.getExamId(), createRecordVO.getDetailId(), createRecordVO.getType(), createRecordVO.getRealId(), sj.toString(), "", createRecordVO.getSubjectId());
                examRecordMapper.insert(examRecord);
                return R.ok(examRecord.getId(), "试题生成成功");
            case CommonConstants.SIMULATION_TEST:
                // 模拟测验
        }
        return null;
    }


    @Override
    public R getQuestionByRecordId(String recordId, String userId) {
        ExamRecord examRecord = examRecordMapper.selectOne(Wrappers.<ExamRecord>lambdaQuery().eq(ExamRecord::getId, recordId));
        Exam exam = examMapper.selectOne(Wrappers.<Exam>lambdaQuery().eq(Exam::getId, examRecord.getExamId()));
        ExamSubject examSubject = examSubjectMapper.selectOne(Wrappers.<ExamSubject>lambdaQuery().eq(ExamSubject::getId, examRecord.getSubjectId()));
        ExamDetail examDetail = examDetailMapper.selectOne(Wrappers.<ExamDetail>lambdaQuery().eq(ExamDetail::getId, examRecord.getDetailId()));

        ExamRealGoExamVO examRealGoExamVO = new ExamRealGoExamVO();
        List<ExamQuestionByRecordVO> examQuestionByRecordVOS = new ArrayList<>();

        switch (examRecord.getType()) {
            case CommonConstants.REAL:
                // 真题
                ExamReal examReal = examRealMapper.selectOne(Wrappers.<ExamReal>lambdaQuery().eq(ExamReal::getId, examRecord.getRealId()));
                examRealGoExamVO.setExamReal(examReal);
                // 查询题目，因为这是真题，可以直接查。如果是智能和模拟，就要用记录中的id查了
                examQuestionByRecordVOS = examRealQuestionMapper.selectByRealId(examRecord.getRealId());
                break;
            case CommonConstants.INTELLIGENT_EXERCISE:
                // 智能练题
                String[] split = examRecord.getAllQuestionId().split(",");
                int i = 1;
                for (String questionId : split) {
                    examQuestionByRecordVOS.add(new ExamQuestionByRecordVO(questionId, "第" + i + "题"));
                    i++;
                }
            case CommonConstants.SIMULATION_TEST:
                // 模拟测验
        }
        examRealGoExamVO.setExamQuestionByRecordVOS(examQuestionByRecordVOS);
        examRealGoExamVO.setExam(exam);
        examRealGoExamVO.setExamSubject(examSubject);
        examRealGoExamVO.setExamDetail(examDetail);
        examRealGoExamVO.setExamRecord(examRecord);
        examRealGoExamVO.setType(examRecord.getType());
        return R.ok(examRealGoExamVO);
    }

    @Override
    public R getRecordById(String id) {
        ExamRecord examRecord = baseMapper.selectOne(Wrappers.<ExamRecord>lambdaQuery()
                .eq(ExamRecord::getId, id)
                .eq(ExamRecord::getDelFlag, CommonConstants.UN_DELETE_FLAG));
        if (StringUtils.isBlank(examRecord.getDoQuestionId())) {
            return R.ok(new ArrayList<>());
        }
        return R.ok(examRecord.getDoQuestionId().split(","));
    }

    @Override
    public R getExaminationRecord(Page<ExamRecordVO> validPage, String keyword, String delFlag, String examId, String subjectId, String detailId, String userId) {
        IPage<ExamRecordVO> examRecordVOS = examRecordMapper.selectExamRecord(validPage, examId, subjectId, detailId, userId);
        for (ExamRecordVO examRecord : examRecordVOS.getRecords()) {
            // 已做题数
            examRecord.setCountDo(StringUtils.isBlank(examRecord.getDoQuestion()) ? 0 : examRecord.getDoQuestion().split(",").length);
            // 总题数 智能和模拟测验用
            if (CommonConstants.INTELLIGENT_EXERCISE.equals(examRecord.getType()) || CommonConstants.SIMULATION_TEST.equals(examRecord.getType())){
                examRecord.setCountAll(examRecord.getAllQuestion().split(",").length);
            }
        }
        return R.ok(examRecordVOS);
    }
}
