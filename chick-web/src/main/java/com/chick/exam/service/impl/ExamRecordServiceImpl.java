package com.chick.exam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.exam.entity.ExamRecord;
import com.chick.exam.mapper.ExamRecordMapper;
import com.chick.exam.service.ExamRecordService;
import org.springframework.stereotype.Service;

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

}
