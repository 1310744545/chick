package com.chick.exam.controller;


import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.controller.BaseController;
import com.chick.exam.service.ExamRecordService;
import com.chick.exam.vo.CreateRecordVO;
import com.chick.utils.PageUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

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
public class ExamRecordController extends BaseController {
    @Resource
    private ExamRecordService examRecordService;

    /**
     * @Author xkx
     * @Description 查询记录中的已做题
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

    /**
     * @Author xkx
     * @Description 查询做题记录
     * @Date 2023-02-10 14:16
     * @Param [current, size, keyword, delFlag, examId, subjectId]
     * @return com.chick.base.R
     **/
    @GetMapping("/getExaminationRecord")
    public R getExaminationRecord(Integer current, Integer size, String keyword, String delFlag, String examId,String subjectId,String detailId) {
        if (StringUtils.isNotBlank(keyword) && keyword.length() > CommonConstants.MAX_NAME_LENGTH) {
            return R.failed("关键字过长");
        }
        if (!StringUtils.isNotBlank(delFlag)) {
            return R.failed("是否删除标记为空");
        }
        return examRecordService.getExaminationRecord(PageUtils.validPage(current, size), keyword, delFlag, examId, subjectId, detailId, getUserId());
    }

    /**
     * @Author xkx
     * @Description 创建做题记录
     * @Date 2023-02-15 13:24
     * @Param [realId, type]
     * @return com.chick.base.R
     **/
    @PostMapping("/createRecord")
    public R createRecord(@RequestBody CreateRecordVO createRecordVO) {
        if (ObjectUtils.isEmpty(createRecordVO)){
            return R.failed("id不能为空");
        }
        return examRecordService.createRecord(createRecordVO, getUserId());
    }

    /**
     * @Author xkx
     * @Description 获取真题
     * @Date 2023-02-10 14:16
     * @Param [current, size, keyword, delFlag, examId, subjectId]
     * @return com.chick.base.R
     **/
    @GetMapping("/getQuestionByRecordId")
    public R getQuestionByRecordId(String recordId) {
        if (StringUtils.isEmpty(recordId)){
            return R.failed("id不能为空");
        }
        return examRecordService.getQuestionByRecordId(recordId, getUserId());
    }
}

