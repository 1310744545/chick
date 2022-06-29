package com.chick.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.exam.entity.ExamQuestion;
import com.chick.exam.entity.ExamQuestionType;
import com.chick.exam.mapper.ExamQuestionTypeMapper;
import com.chick.exam.service.ExamQuestionTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static com.chick.common.utils.ChickUtil.DoId;

/**
 * <p>
 * 知识点分类 服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-15
 */
@Service
public class ExamQuestionTypeServiceImpl extends ServiceImpl<ExamQuestionTypeMapper, ExamQuestionType> implements ExamQuestionTypeService {

    @Override
    public R getList(Page<ExamQuestionType> validPage, String keyword, String delFlag, String examId, String detailId) {
        LambdaQueryWrapper<ExamQuestionType> wrapper = Wrappers.<ExamQuestionType>lambdaQuery()
                .eq(ExamQuestionType::getDelFlag, delFlag);

        // 添加考试id
        if (StringUtils.isNotBlank(examId)) {
            wrapper.eq(ExamQuestionType::getExamId, examId);
        }
        // 添加考试详情id
        if (StringUtils.isNotBlank(detailId)) {
            wrapper.eq(ExamQuestionType::getDetailId, detailId);
        }
        //3.添加关键字
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(wr -> wr.like(ExamQuestionType::getName, keyword));
        }
        return R.ok(baseMapper.selectPage(validPage, wrapper));
    }

    @Override
    public R insert(ExamQuestionType examQuestionType) {
        examQuestionType.setId(DoId());
        if (baseMapper.insert(examQuestionType) > 0) {
            return R.ok("保存成功");
        }
        return R.failed("保存失败");
    }

    @Override
    public R update(ExamQuestionType examQuestionType) {
        if (baseMapper.updateById(examQuestionType) > 0) {
            return R.ok("更新成功");
        }
        return R.failed("更新失败");
    }

    @Override
    public R removeOrRenew(ExamQuestionType examQuestionType) {
        if (CommonConstants.UN_DELETE_FLAG.equals(examQuestionType.getDelFlag())) {
            examQuestionType.setDelFlag(CommonConstants.DELETE_FLAG);
        } else {
            examQuestionType.setDelFlag(CommonConstants.UN_DELETE_FLAG);
        }
        if (baseMapper.updateById(examQuestionType) > 0) {
            return R.ok("删除成功");
        }
        return R.failed("更新失败");
    }
}
