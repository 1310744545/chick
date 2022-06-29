package com.chick.exam.controller;


import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.controller.BaseController;
import com.chick.exam.entity.ExamDetail;
import com.chick.exam.entity.ExamType;
import com.chick.exam.service.ExamDetailService;
import com.chick.exam.service.ExamTypeService;
import com.chick.utils.PageUtils;
import com.chick.web.dictionary.service.ISysDbInfoService;
import com.chick.web.dictionary.service.Impl.SysDbInfoServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 考试详情 前端控制器
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-14
 */
@RestController
@RequestMapping("/examDetail")
public class ExamDetailController extends BaseController {


    @Resource
    private ExamDetailService examDetailService;
    @Resource
    private ISysDbInfoService sysDbInfoService;

    /**
    * @Author xkx
    * @Description 查询考试详情
    * @Date 2022-06-17 13:54
    * @Param [current, size, keyword, delFlag, examId]
    * @return com.chick.base.R
    **/
    @GetMapping("/getList")
    public R getList(Integer current, Integer size, String keyword, String delFlag, String examId, String examTypeId, String types) {
        if (StringUtils.isNotBlank(keyword) && keyword.length() > CommonConstants.MAX_NAME_LENGTH) {
            return R.failed("关键字过长");
        }
        if (!StringUtils.isNotBlank(delFlag)) {
            return R.failed("是否删除标记为空");
        }
        return examDetailService.getList(PageUtils.validPage(current, size), keyword, delFlag, examId, examTypeId, types);
    }


    /**
    * @Author xkx
    * @Description 查询考试
    * @Date 2022-06-17 14:33
    * @Param []
    * @return com.chick.base.R
    **/
    @GetMapping("/getExamList")
    public R getExamList() {
        return examDetailService.getExamList();
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
        return examDetailService.getExamDetailByExamId(examId);
    }

    /**
     * @Author xkx
     * @Description 通过考试id、详情id、科目id获取知识点
     * @Date 2022-06-16 17:28
     * @Param [examId]
     * @return com.chick.base.R
     **/
    @GetMapping("/getExamQuestionTypeByExamId")
    public R getExamQuestionTypeByExamId(Integer current, Integer size, String keyword, String delFlag, String examId, String detailId, String subjectId){
        if (StringUtils.isBlank(examId)) {
            return R.failed("考试id为空");
        }
        if (StringUtils.isBlank(detailId)) {
            return R.failed("详情id为空");
        }
        if (StringUtils.isBlank(subjectId)) {
            return R.failed("科目id为空");
        }
        return examDetailService.getExamQuestionTypeByExamId(PageUtils.validPage(current, size), keyword, delFlag, examId, detailId, subjectId);
    }

    /**
     * @Author xkx
     * @Description 根据字典ket获取子项
     * @Date 2022-06-10 13:36
     * @Param [sysDbInfo]
     * @return com.chick.base.R
     **/
    @GetMapping("/getChildrenDataForManager")
    public R getChildrenDataForManager(String key, Boolean childrenOfChildren){
        if(ObjectUtils.isEmpty(key)){
            return R.failed("参数错误");
        }
        return sysDbInfoService.getChildrenDataForManager(key, false, childrenOfChildren);
    }

    /**
    * @Author xkx
    * @Description 保存考试详情
    * @Date 2022-06-17 13:54
    * @Param [examDetail]
    * @return com.chick.base.R
    **/
    @PostMapping("/insert")
    public R insert(@RequestBody ExamDetail examDetail) {
        if (ObjectUtils.isEmpty(examDetail)){
            return R.failed("考试详情为空");
        }
        return examDetailService.insert(examDetail);
    }

    /**
    * @Author xkx
    * @Description 更新考试详情
    * @Date 2022-06-17 13:53
    * @Param [examDetail]
    * @return com.chick.base.R
    **/
    @PostMapping("/update")
    public R update(@RequestBody ExamDetail examDetail) {
        if (ObjectUtils.isEmpty(examDetail)){
            return R.failed("考试详情为空");
        }
        return examDetailService.update(examDetail);
    }


    /**
    * @Author xkx
    * @Description 删除或恢复考试详情
    * @Date 2022-06-17 13:53
    * @Param [examDetail]
    * @return com.chick.base.R
    **/
    @PostMapping("/removeOrRenew")
    public R removeOrRenew(@RequestBody ExamDetail examDetail) {
        if (ObjectUtils.isEmpty(examDetail)){
            return R.failed("考试详情为空");
        }
        return examDetailService.removeOrRenew(examDetail);
    }
}

