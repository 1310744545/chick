package com.chick.exam.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.controller.BaseController;
import com.chick.exam.entity.Exam;
import com.chick.exam.service.ExamService;
import com.chick.tools.service.IToolsService;
import com.chick.tools.vo.ToolsVO;
import com.chick.utils.PageUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

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

    /**
    * @Author xkx
    * @Description 获取考试列表
    * @Date 2022-06-16 17:28
    * @Param [type, keyword, current, size]
    * @return com.chick.base.R
    **/
    @GetMapping("/getExamList")
    public R getExamList(String type, String keyword, Integer current, Integer size){
        if (StringUtils.isNotBlank(keyword) && keyword.length() > CommonConstants.MAX_NAME_LENGTH) {
            return R.failed("关键字过长");
        }

        return examService.getExamList(PageUtils.validPage(current, size), type, keyword);
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
        return examService.getExamDetailByExamId(examId);
    }

    /**
    * @Author xkx
    * @Description 通过考试详情id和userId获取用户的刷题情况
    * @Date 2022-06-16 17:27
    * @Param [detailId]
    * @return com.chick.base.R
    **/
    @GetMapping("/getExamAnswerHappeningByUserAndDetailId")
    public R getExamAnswerHappeningByUserAndDetailId(String detailId){
        if (StringUtils.isBlank(detailId)) {
            return R.failed("详情id为空");
        }
        return examService.getExamAnswerHappeningByUserAndDetailId(detailId, getUserId());
    }

    /**
    * @Author xkx
    * @Description 查询考试
    * @Date 2022-06-16 17:29
    * @Param [current, size, keyword, delFlag, type]
    * @return com.chick.base.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.chick.tools.vo.ToolsVO>>
    **/
    @GetMapping("/list")
    public R<Page<Exam>> list(Integer current, Integer size, String keyword, String delFlag, String type) {
        if (StringUtils.isNotBlank(keyword) && keyword.length() > CommonConstants.MAX_NAME_LENGTH) {
            return R.failed("关键字过长");
        }
        if (!StringUtils.isNotBlank(delFlag)) {
            return R.failed("是否删除标记为空");
        }
        return examService.list(PageUtils.validPage(current, size), keyword, delFlag, type);
    }

    /**
     * @Author xkx
     * @Description 保存
     * @Date 2022-06-16 17:29
     * @Param [toolId, delFlag]
     * @return com.chick.base.R
     **/
    @PostMapping("/saveExam")
    public R saveExam(@RequestBody Exam exam) {
        if (ObjectUtils.isEmpty(exam)){
            return R.failed("考试为空");
        }
        return examService.saveExam(exam);
    }

    /**
     * @Author xkx
     * @Description 更新
     * @Date 2022-06-16 17:29
     * @Param [toolId, delFlag]
     * @return com.chick.base.R
     **/
    @PostMapping("/update")
    public R update(@RequestBody Exam exam) {
        if (ObjectUtils.isEmpty(exam)){
            return R.failed("考试为空");
        }
        return examService.update(exam);
    }


    /**
    * @Author xkx
    * @Description 删除或恢复考试
    * @Date 2022-06-16 17:29
    * @Param [toolId, delFlag]
    * @return com.chick.base.R
    **/
    @PostMapping("/deleteOrRenew")
    public R deleteOrRenew(@RequestBody Exam exam) {
        if (ObjectUtils.isEmpty(exam)){
            return R.failed("考试为空");
        }
        return examService.deleteOrRenew(exam);
    }
}

