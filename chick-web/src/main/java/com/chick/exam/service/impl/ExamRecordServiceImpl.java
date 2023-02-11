package com.chick.exam.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.base.R;
import com.chick.exam.entity.ExamRecord;
import com.chick.exam.mapper.ExamRecordMapper;
import com.chick.exam.service.ExamRecordService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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

    @Override
    public R getRecordById(String id) {
        ExamRecord examRecord = baseMapper.selectOne(Wrappers.<ExamRecord>lambdaQuery().eq(ExamRecord::getId, id));
        if (StringUtils.isBlank(examRecord.getDoQuestionId())){
            return R.ok(new ArrayList<>());
        }
        return R.ok(examRecord.getDoQuestionId().split(","));
    }
}
