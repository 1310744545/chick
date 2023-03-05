package com.chick.exam.controller;


import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.controller.BaseController;
import com.chick.exam.entity.ExamQuestion;
import com.chick.exam.entity.ExamQuestionType;
import com.chick.exam.service.ExamQuestionTypeService;
import com.chick.utils.PageUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 知识点分类 前端控制器
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-15
 */
@RestController
@RequestMapping("/examQuestionType")
public class ExamQuestionTypeController extends BaseController {

    @Resource
    private ExamQuestionTypeService examQuestionTypeService;

    /**
     * @Author xkx
     * @Description 查询考试题目
     * @Date 2022-06-17 13:54
     * @Param [current, size, keyword, delFlag, examId]
     * @return com.chick.base.R
     **/
    @GetMapping("/getList")
    public R getList(Integer current, Integer size, String keyword, String delFlag, String examId, String detailId) {
        if (StringUtils.isNotBlank(keyword) && keyword.length() > CommonConstants.MAX_NAME_LENGTH) {
            return R.failed("关键字过长");
        }
        if (!StringUtils.isNotBlank(delFlag)) {
            return R.failed("是否删除标记为空");
        }
        if (!StringUtils.isNotBlank(examId)) {
            return R.failed("考试id不可为空");
        }
        if (!StringUtils.isNotBlank(detailId)) {
            return R.failed("考试题目id为空");
        }
        return examQuestionTypeService.getList(PageUtils.validPage(current, size), keyword, delFlag, examId, detailId);
    }


    /**
     * @Author xkx
     * @Description 保存考试题目
     * @Date 2022-06-17 13:54
     * @Param [examDetail]
     * @return com.chick.base.R
     **/
    @PostMapping("/insert")
    public R insert(@RequestBody ExamQuestionType examQuestionType) {
        if (ObjectUtils.isEmpty(examQuestionType)){
            return R.failed("考试题目不可为空");
        }
        return examQuestionTypeService.insert(examQuestionType);
    }

    /**
     * @Author xkx
     * @Description 更新考试题目
     * @Date 2022-06-17 13:53
     * @Param [examDetail]
     * @return com.chick.base.R
     **/
    @PostMapping("/update")
    public R update(@RequestBody ExamQuestionType examQuestionType) {
        if (ObjectUtils.isEmpty(examQuestionType)){
            return R.failed("考试题目为空");
        }
        return examQuestionTypeService.update(examQuestionType);
    }


    /**
     * @Author xkx
     * @Description 删除或恢复考试题目
     * @Date 2022-06-17 13:53
     * @Param [examDetail]
     * @return com.chick.base.R
     **/
    @PostMapping("/removeOrRenew")
    public R removeOrRenew(@RequestBody ExamQuestionType examQuestionType) {
        if (ObjectUtils.isEmpty(examQuestionType)){
            return R.failed("考试题目为空");
        }
        return examQuestionTypeService.removeOrRenew(examQuestionType);
    }
}

