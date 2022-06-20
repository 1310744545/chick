package com.chick.exam.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.controller.BaseController;
import com.chick.exam.entity.ExamType;
import com.chick.exam.entity.ExamType;
import com.chick.exam.service.ExamTypeService;
import com.chick.utils.PageUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 考试级别/类别 前端控制器
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-14
 */
@RestController
@RequestMapping("/examType")
public class ExamTypeController extends BaseController {

    @Resource
    private ExamTypeService examTypeService;

    /**
     * @Author xkx
     * @Description 查询考试类型
     * @Date 2022-06-16 17:29
     * @Param [current, size, keyword, delFlag, type]
     * @return com.chick.base.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.chick.tools.vo.ToolsVO>>
     **/
    @GetMapping("/list")
    public R list(Integer current, Integer size, String keyword, String delFlag, String examId) {
        if (StringUtils.isNotBlank(keyword) && keyword.length() > CommonConstants.MAX_NAME_LENGTH) {
            return R.failed("关键字过长");
        }
        if (!StringUtils.isNotBlank(delFlag)) {
            return R.failed("是否删除标记为空");
        }
        return examTypeService.list(PageUtils.validPage(current, size), keyword, delFlag, examId);
    }

    /**
     * @Author xkx
     * @Description 保存考试类型
     * @Date 2022-06-16 17:29
     * @Param [toolId, delFlag]
     * @return com.chick.base.R
     **/
    @PostMapping("/saveExam")
    public R saveExam(@RequestBody ExamType examType) {
        if (ObjectUtils.isEmpty(examType)){
            return R.failed("考试为空");
        }
        return examTypeService.saveExam(examType);
    }

    /**
     * @Author xkx
     * @Description 更新考试类型
     * @Date 2022-06-16 17:29
     * @Param [toolId, delFlag]
     * @return com.chick.base.R
     **/
    @PostMapping("/update")
    public R update(@RequestBody ExamType examType) {
        if (ObjectUtils.isEmpty(examType)){
            return R.failed("考试为空");
        }
        return examTypeService.update(examType);
    }


    /**
     * @Author xkx
     * @Description 删除或恢复考试类型
     * @Date 2022-06-16 17:29
     * @Param [toolId, delFlag]
     * @return com.chick.base.R
     **/
    @PostMapping("/deleteOrRenew")
    public R deleteOrRenew(@RequestBody ExamType examType) {
        if (ObjectUtils.isEmpty(examType)){
            return R.failed("考试为空");
        }
        return examTypeService.deleteOrRenew(examType);
    }

}

