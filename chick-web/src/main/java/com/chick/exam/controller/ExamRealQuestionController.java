package com.chick.exam.controller;

import com.chick.base.R;
import com.chick.controller.BaseController;
import com.chick.exam.service.ExamRealQuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaokexin
 * @since 2022-07-05
 */
@RestController
@RequestMapping("/examRealQuestion")
public class ExamRealQuestionController extends BaseController{

    @Resource
    private ExamRealQuestionService examRealQuestionService;

    /**
    * @Author xkx
    * @Description 查询真题
    * @Date 2023-02-10 14:16
    * @Param [current, size, keyword, delFlag, examId, subjectId]
    * @return com.chick.base.R
    **/
    @GetMapping("/getRealQuestionByRealId")
    public R getRealQuestionByRealId(String realId) {
        if (StringUtils.isEmpty(realId)){
            return R.failed("id不能为空");
        }
        return examRealQuestionService.getRealQuestionByRealId(realId, getUserId());
    }
}

