package com.chick.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chick.base.R;
import com.chick.exam.entity.ExamRecord;

/**
 * <p>
 * 考试记录 服务类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-16
 */
public interface ExamRecordService extends IService<ExamRecord> {
    R getRecordById(String id);
}
