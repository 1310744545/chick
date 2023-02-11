package com.chick.exam.controller;


import com.chick.base.R;
import com.chick.exam.entity.ExamRecord;
import com.chick.exam.service.ExamRealQuestionService;
import com.chick.exam.service.ExamRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 考试记录 前端控制器
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-16
 */
@RestController
@RequestMapping("/examRecord")
public class ExamRecordController {
    @Resource
    private ExamRecordService examRecordService;

    /**
     * @Author xkx
     * @Description 查询真题
     * @Date 2023-02-10 14:16
     * @Param [current, size, keyword, delFlag, examId, subjectId]
     * @return com.chick.base.R
     **/
    @GetMapping("/getRecordById")
    public R getRecordById(String id) {
        if (StringUtils.isEmpty(id)){
            return R.failed("id不能为空");
        }
        return examRecordService.getRecordById(id);
    }
}

