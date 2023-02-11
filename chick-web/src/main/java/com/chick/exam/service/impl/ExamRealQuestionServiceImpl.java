package com.chick.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.exam.entity.*;
import com.chick.exam.mapper.*;
import com.chick.exam.service.ExamRealQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.exam.vo.ExamRealGoExamVO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-07-05
 */
@Service
public class ExamRealQuestionServiceImpl extends ServiceImpl<ExamRealQuestionMapper, ExamRealQuestion> implements ExamRealQuestionService {

    @Resource
    private ExamRealMapper examRealMapper;
    @Resource
    private ExamMapper examMapper;
    @Resource
    private ExamSubjectMapper examSubjectMapper;
    @Resource
    private ExamDetailMapper examDetailMapper;
    @Resource
    private ExamRecordMapper examRecordMapper;

    @Override
    public R getRealQuestionByRealId(String realId, String userId) {
        LambdaQueryWrapper<ExamRealQuestion> wrapper = Wrappers.lambdaQuery();
        // 添加科目id
        if (StringUtils.isNotBlank(realId)) {
            wrapper.eq(ExamRealQuestion::getExamRealId, realId);
        }
        // 排序
        wrapper.orderByAsc(ExamRealQuestion::getSort);
        ExamRealGoExamVO examRealGoExamVO = new ExamRealGoExamVO();
        ExamReal examReal = examRealMapper.selectOne(Wrappers.<ExamReal>lambdaQuery().eq(ExamReal::getId, realId));
        Exam exam = examMapper.selectOne(Wrappers.<Exam>lambdaQuery().eq(Exam::getId, examReal.getExamId()));
        ExamSubject examSubject = examSubjectMapper.selectOne(Wrappers.<ExamSubject>lambdaQuery().eq(ExamSubject::getId, examReal.getSubjectId()));
        ExamDetail examDetail = examDetailMapper.selectOne(Wrappers.<ExamDetail>lambdaQuery().eq(ExamDetail::getId, examSubject.getDetailId()));

        // 检查该用户是否做过这套题
        ExamRecord examRecord = examRecordMapper.selectOne(Wrappers.<ExamRecord>lambdaQuery()
                .eq(ExamRecord::getExamId, exam.getId())
                .eq(ExamRecord::getDetailId, examDetail.getId())
                .eq(ExamRecord::getRealId, examReal.getId())
                .eq(ExamRecord::getUserId, userId)
                .eq(ExamRecord::getType, CommonConstants.REAL)
                .eq(ExamRecord::getDelFlag, CommonConstants.UN_DELETE_FLAG));
        // 如果没做过就添加记录
        if (ObjectUtils.isEmpty(examRecord)){
            examRecord = new ExamRecord(userId, exam.getId(), examDetail.getId(), CommonConstants.REAL, examReal.getId(), "", "", examSubject.getId());
            examRecordMapper.insert(examRecord);
        }
        // 查询题目，因为这是真题，可以直接查。如果是智能和模拟，就要用记录中的id查了
        List<ExamRealQuestion> examRealQuestions = baseMapper.selectList(wrapper);
        examRealGoExamVO.setExamReal(examReal);
        examRealGoExamVO.setExamRealQuestions(examRealQuestions);
        examRealGoExamVO.setExam(exam);
        examRealGoExamVO.setExamSubject(examSubject);
        examRealGoExamVO.setExamDetail(examDetail);
        examRealGoExamVO.setExamRecord(examRecord);

        return R.ok(examRealGoExamVO);
    }
}
