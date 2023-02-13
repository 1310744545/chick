package com.chick.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chick.base.R;
import com.chick.exam.entity.ExamAnswerQuestions;
import com.chick.exam.vo.DoAnswerVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-15
 */
public interface ExamAnswerQuestionsService extends IService<ExamAnswerQuestions> {

    public R doAnswer(DoAnswerVO doAnswerVO, String userId);

    public R clearData(String recordId);

}
