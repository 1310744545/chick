package com.chick.exam.controller;


import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.controller.BaseController;
import com.chick.exam.entity.ExamQuestion;
import com.chick.exam.entity.ExamSubject;
import com.chick.exam.service.ExamQuestionService;
import com.chick.exam.service.ExamSubjectService;
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
 * @since 2022-06-14
 */
@RestController
@RequestMapping("/examQuestion")
public class ExamQuestionController extends BaseController {

    @Resource
    private ExamQuestionService examQuestionService;

    /**
     * @Author xkx
     * @Description 查询考试题目(后台使用)
     * @Date 2022-06-17 13:54
     * @Param [current, size, keyword, delFlag, examId]
     * @return com.chick.base.R
     **/
    @GetMapping("/getList")
    public R getList(Integer current, Integer size, String keyword, String delFlag, String examId, String detailId, String subjectId) {
        if (StringUtils.isNotBlank(keyword) && keyword.length() > CommonConstants.MAX_NAME_LENGTH) {
            return R.failed("关键字过长");
        }
        if (!StringUtils.isNotBlank(delFlag)) {
            return R.failed("是否删除标记为空");
        }
        return examQuestionService.getList(PageUtils.validPage(current, size), keyword, delFlag, examId, detailId, subjectId);
    }


    /**
     * @Author xkx
     * @Description 保存考试题目
     * @Date 2022-06-17 13:54
     * @Param [examDetail]
     * @return com.chick.base.R
     **/
    @PostMapping("/insert")
    public R insert(@RequestBody ExamQuestion examQuestion) {
        if (ObjectUtils.isEmpty(examQuestion)){
            return R.failed("考试题目为空");
        }
        return examQuestionService.insert(examQuestion);
    }

    /**
     * @Author xkx
     * @Description 更新考试题目
     * @Date 2022-06-17 13:53
     * @Param [examDetail]
     * @return com.chick.base.R
     **/
    @PostMapping("/update")
    public R update(@RequestBody ExamQuestion examQuestion) {
        if (ObjectUtils.isEmpty(examQuestion)){
            return R.failed("考试题目为空");
        }
        return examQuestionService.update(examQuestion);
    }


    /**
     * @Author xkx
     * @Description 删除或恢复考试题目
     * @Date 2022-06-17 13:53
     * @Param [examDetail]
     * @return com.chick.base.R
     **/
    @PostMapping("/removeOrRenew")
    public R removeOrRenew(@RequestBody ExamQuestion examQuestion) {
        if (ObjectUtils.isEmpty(examQuestion)){
            return R.failed("考试题目为空");
        }
        return examQuestionService.removeOrRenew(examQuestion);
    }

    /**
     * @Author xkx
     * @Description 通过考试id获取考试详情
     * @Date 2022-06-16 17:28
     * @Param [examId]
     * @return com.chick.base.R
     **/
    @GetMapping("/getExamDetailByExamId")
    public R getExamDetailByExamId(String examId){
        if (StringUtils.isBlank(examId)) {
            return R.failed("考试id为空");
        }
        return examQuestionService.getExamDetailByExamId(examId);
    }

    /**
     * @Author xkx
     * @Description 通过考试id和科目id获取考试试题
     * @Date 2022-06-16 17:28
     * @Param [examId]
     * @return com.chick.base.R
     **/
    @GetMapping("/getExamSubjectByExamId")
    public R getExamSubjectByExamId(String examId, String detailId){
        if (StringUtils.isBlank(examId)) {
            return R.failed("考试id为空");
        }
        if (StringUtils.isBlank(detailId)) {
            return R.failed("科目id为空");
        }
        return examQuestionService.getExamSubjectByExamId(examId, detailId);
    }
}

