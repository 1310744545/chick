package com.chick.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.exam.entity.ExamType;
import com.chick.exam.mapper.ExamTypeMapper;
import com.chick.exam.service.ExamTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static com.chick.common.utils.ChickUtil.DoId;

/**
 * <p>
 * 考试级别/类别 服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-14
 */
@Service
public class ExamTypeServiceImpl extends ServiceImpl<ExamTypeMapper, ExamType> implements ExamTypeService {

    @Override
    public R list(Page<ExamType> validPage, String keyword, String delFlag, String examId) {
        LambdaQueryWrapper<ExamType> wrapper = Wrappers.<ExamType>lambdaQuery()
                .eq(ExamType::getDelFlag, delFlag)
                .eq(ExamType::getExamId, examId);
        //3.添加关键字
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(wr -> wr.like(ExamType::getTypeName, keyword));
        }
        return R.ok(baseMapper.selectPage(validPage, wrapper));
    }

    @Override
    public R deleteOrRenew(ExamType exam) {
        if (CommonConstants.UN_DELETE_FLAG.equals(exam.getDelFlag())) {
            exam.setDelFlag(CommonConstants.DELETE_FLAG);
        } else {
            exam.setDelFlag(CommonConstants.UN_DELETE_FLAG);
        }
        if (baseMapper.updateById(exam) > 0) {
            return R.ok();
        }
        return R.failed();
    }

    @Override
    public R saveExam(ExamType exam) {
        exam.setId(DoId());
        exam.setDelFlag(CommonConstants.UN_DELETE_FLAG);
        if (baseMapper.insert(exam) > 0) {
            return R.ok("保存成功");
        }
        return R.failed("保存失败");
    }

    @Override
    public R update(ExamType exam) {
        if (baseMapper.updateById(exam) > 0) {
            return R.ok("更新成功");
        }
        return R.failed("更新失败");
    }
}
