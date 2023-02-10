package com.chick.exam.controller;


import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.exam.entity.ExamReal;
import com.chick.exam.service.ExamQuestionService;
import com.chick.exam.service.ExamRealService;
import com.chick.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 历年真题 前端控制器
 * </p>
 *
 * @author xiaokexin
 * @since 2022-07-05
 */
@RestController
@RequestMapping("/examReal")
public class ExamRealController {
    @Resource
    private ExamRealService examRealService;

    /**
     * @Author xkx
     * @Description 查询真题列表
     * @Date 2022-06-17 13:54
     * @Param [current, size, keyword, delFlag, examId]
     * @return com.chick.base.R
     **/
    @GetMapping("/getRealListByExamIdSubjectId")
    public R getRealListByExamIdSubjectId(Integer current, Integer size, String keyword, String delFlag, String examId, String subjectId) {
        if (StringUtils.isNotBlank(keyword) && keyword.length() > CommonConstants.MAX_NAME_LENGTH) {
            return R.failed("关键字过长");
        }
        if (!StringUtils.isNotBlank(delFlag)) {
            return R.failed("是否删除标记为空");
        }
        return examRealService.getRealListByExamIdSubjectId(PageUtils.validPage(current, size), keyword, delFlag, examId, subjectId);
    }
}

