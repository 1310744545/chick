package com.chick.exam.controller;


import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.controller.BaseController;
import com.chick.exam.entity.ExamDetail;
import com.chick.exam.entity.ExamSubject;
import com.chick.exam.service.ExamDetailService;
import com.chick.exam.service.ExamSubjectService;
import com.chick.utils.PageUtils;
import com.chick.web.dictionary.service.ISysDbInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 考试科目 前端控制器
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-15
 */
@RestController
@RequestMapping("/examSubject")
public class ExamSubjectController extends BaseController {

    @Resource
    private ExamSubjectService examSubjectService;

    /**
     * @Author xkx
     * @Description 查询考试详情
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
            return R.failed("考试id为空");
        }
        if (!StringUtils.isNotBlank(detailId)) {
            return R.failed("考试详情id为空");
        }
        return examSubjectService.getList(PageUtils.validPage(current, size), keyword, delFlag, examId, detailId);
    }


    /**
     * @Author xkx
     * @Description 保存考试详情
     * @Date 2022-06-17 13:54
     * @Param [examDetail]
     * @return com.chick.base.R
     **/
    @PostMapping("/insert")
    public R insert(@RequestBody ExamSubject examSubject) {
        if (ObjectUtils.isEmpty(examSubject)){
            return R.failed("考试详情为空");
        }
        return examSubjectService.insert(examSubject);
    }

    /**
     * @Author xkx
     * @Description 更新考试详情
     * @Date 2022-06-17 13:53
     * @Param [examDetail]
     * @return com.chick.base.R
     **/
    @PostMapping("/update")
    public R update(@RequestBody ExamSubject examSubject) {
        if (ObjectUtils.isEmpty(examSubject)){
            return R.failed("考试详情为空");
        }
        return examSubjectService.update(examSubject);
    }


    /**
     * @Author xkx
     * @Description 删除或恢复考试详情
     * @Date 2022-06-17 13:53
     * @Param [examDetail]
     * @return com.chick.base.R
     **/
    @PostMapping("/removeOrRenew")
    public R removeOrRenew(@RequestBody ExamSubject examSubject) {
        if (ObjectUtils.isEmpty(examSubject)){
            return R.failed("考试详情为空");
        }
        return examSubjectService.removeOrRenew(examSubject);
    }
}

