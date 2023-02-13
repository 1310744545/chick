package com.chick.exam.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.exam.entity.ExamReal;
import com.chick.exam.entity.ExamRecord;
import com.chick.exam.mapper.ExamRecordMapper;
import com.chick.exam.service.ExamRecordService;
import com.chick.exam.vo.ExamRecordVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
//        List<ExamRecord> examRecords = baseMapper.selectList(Wrappers.<ExamRecord>lambdaQuery()
//                .eq(ExamRecord::getExamId, examId)
//                .eq(ExamRecord::getDetailId, detailId)
//                .eq(ExamRecord::getSubjectId, subjectId)
//                .eq(ExamRecord::getUserId, userId)
//                .eq(ExamRecord::getDelFlag, CommonConstants.UN_DELETE_FLAG));
        for (ExamRecordVO examRecord : examRecordVOS.getRecords()){
            // 这里先不管智能和模拟，到时候再改,这里也是
            examRecord.setCountDo(StringUtils.isBlank(examRecord.getDoQuestion()) ? 0 : examRecord.getDoQuestion().split(",").length);
        }
        return R.ok(examRecordVOS);
    }
}
