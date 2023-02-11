package com.chick.exam.service;

import com.chick.base.R;
import com.chick.exam.entity.ExamRealQuestion;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-07-05
 */
public interface ExamRealQuestionService extends IService<ExamRealQuestion> {

    R getRealQuestionByRealId(String realId, String userId);
}
