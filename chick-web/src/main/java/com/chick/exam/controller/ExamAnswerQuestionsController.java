package com.chick.exam.controller;


import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.controller.BaseController;
import com.chick.exam.entity.ExamAnswerQuestions;
import com.chick.exam.service.ExamAnswerQuestionsService;
import com.chick.exam.service.ExamRealService;
import com.chick.exam.vo.DoAnswerVO;
import com.chick.utils.PageUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-15
 */
@RestController
@RequestMapping("/examAnswerQuestions")
public class ExamAnswerQuestionsController extends BaseController {

    @Resource
    private ExamAnswerQuestionsService examAnswerQuestionsService;

    /**
     * @Author xkx
     * @Description 答题接口
     * @Date 2022-06-17 13:54
     * @Param [current, size, keyword, delFlag, examId]
     * @return com.chick.base.R
     **/
    @PostMapping("/doAnswer")
    public R doAnswer(@RequestBody DoAnswerVO doAnswerVO) {
        if (ObjectUtils.isEmpty(doAnswerVO) || StringUtils.isAnyBlank(doAnswerVO.getAnswerId(), doAnswerVO.getExamId(), doAnswerVO.getQuestionId(), doAnswerVO.getDetailId(), doAnswerVO.getRecordId(), doAnswerVO.getSubjectId(), doAnswerVO.getIsRight())){
            return R.failed("参数错误");
        }
        return examAnswerQuestionsService.doAnswer(doAnswerVO, getUserId());
    }

    /**
     * @Author xkx
     * @Description 答题接口
     * @Date 2022-06-17 13:54
     * @Param [current, size, keyword, delFlag, examId]
     * @return com.chick.base.R
     **/
    @GetMapping("/clearData")
    public R clearData(String recordId) {
        if (StringUtils.isEmpty(recordId) ){
            return R.failed("参数错误");
        }
        return examAnswerQuestionsService.clearData(recordId);
    }
}

