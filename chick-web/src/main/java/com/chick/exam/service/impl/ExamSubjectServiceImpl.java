package com.chick.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.exam.entity.ExamDetail;
import com.chick.exam.entity.ExamSubject;
import com.chick.exam.mapper.ExamSubjectMapper;
import com.chick.exam.service.ExamSubjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static com.chick.common.utils.ChickUtil.DoId;

/**
 * <p>
 * 考试科目 服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-15
 */
@Service
public class ExamSubjectServiceImpl extends ServiceImpl<ExamSubjectMapper, ExamSubject> implements ExamSubjectService {

    @Override
    public R getList(Page<ExamSubject> validPage, String keyword, String delFlag, String examId, String detailId) {
        LambdaQueryWrapper<ExamSubject> wrapper = Wrappers.<ExamSubject>lambdaQuery()
                .eq(ExamSubject::getDelFlag, delFlag);

        // 添加考试id
        if (StringUtils.isNotBlank(examId)) {
            wrapper.eq(ExamSubject::getExamId, examId);
        }
        // 添加考试详情id
        if (StringUtils.isNotBlank(detailId)) {
            wrapper.eq(ExamSubject::getDetailId, detailId);
        }
        //3.添加关键字
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(wr -> wr.like(ExamSubject::getName, keyword));
        }
        return R.ok(baseMapper.selectPage(validPage, wrapper));
    }
    /**
     * @return com.chick.base.R
     * @Author xkx
     * @Description 保存考试详情
     * @Date 2022-06-17 13:54
     * @Param [examDetail]
     **/
    @Override
    public R insert(ExamSubject examSubject) {
        examSubject.setId(DoId());
        if (baseMapper.insert(examSubject) > 0) {
            return R.ok("保存成功");
        }
        return R.failed("保存失败");
    }

    /**
     * @return com.chick.base.R
     * @Author xkx
     * @Description 更新考试详情
     * @Date 2022-06-17 13:53
     * @Param [examDetail]
     **/
    @Override
    public R update(ExamSubject examSubject) {
        if (baseMapper.updateById(examSubject) > 0) {
            return R.ok("更新成功");
        }
        return R.failed("更新失败");
    }

    /**
     * @return com.chick.base.R
     * @Author xkx
     * @Description 删除或恢复考试详情
     * @Date 2022-06-17 13:53
     * @Param [examDetail]
     **/
    @Override
    public R removeOrRenew(ExamSubject examSubject) {
        examSubject.setDelFlag(CommonConstants.DELETE_FLAG);
        if (baseMapper.updateById(examSubject) > 0) {
            return R.ok("删除成功");
        }
        return R.failed("更新失败");
    }
}
