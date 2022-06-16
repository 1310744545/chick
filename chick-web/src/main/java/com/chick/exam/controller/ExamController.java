package com.chick.exam.controller;


import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.controller.BaseController;
import com.chick.exam.entity.Exam;
import com.chick.exam.service.ExamService;
import com.chick.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 考试 例：软考、教师资格证 前端控制器
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-14
 */
@RestController
@RequestMapping("/exam")
public class ExamController extends BaseController {

    @Resource
    private ExamService examService;

    @GetMapping("/getExamList")
    public R getExamList(String type, String keyword, Integer current, Integer size){
        if (StringUtils.isNotBlank(keyword) && keyword.length() > CommonConstants.MAX_NAME_LENGTH) {
            return R.failed("关键字过长");
        }

        return examService.getExamList(PageUtils.validPage(current, size), type, keyword);
    }

    @GetMapping("/getExamDetailByExamId")
    public R getExamDetailByExamId(String examId){
        if (StringUtils.isBlank(examId)) {
            return R.failed("考试id为空");
        }
        return examService.getExamDetailByExamId(examId);
    }

    @GetMapping("/getExamAnswerHappeningByUserAndDetailId")
    public R getExamAnswerHappeningByUserAndDetailId(String detailId){
        if (StringUtils.isBlank(detailId)) {
            return R.failed("详情id为空");
        }
        return examService.getExamAnswerHappeningByUserAndDetailId(detailId, getUserId());
    }
}

