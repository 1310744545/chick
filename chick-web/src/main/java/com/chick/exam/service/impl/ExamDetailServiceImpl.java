package com.chick.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.exam.entity.Exam;
import com.chick.exam.entity.ExamDetail;
import com.chick.exam.entity.ExamType;
import com.chick.exam.mapper.ExamDetailMapper;
import com.chick.exam.mapper.ExamMapper;
import com.chick.exam.mapper.ExamTypeMapper;
import com.chick.exam.service.ExamDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

import static com.chick.common.utils.ChickUtil.DoId;

/**
 * <p>
 * 考试详情 服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-14
 */
@Service
public class ExamDetailServiceImpl extends ServiceImpl<ExamDetailMapper, ExamDetail> implements ExamDetailService {

    @Resource
    private ExamMapper examMapper;
    @Resource
    private ExamTypeMapper examTypeMapper;
    /**
     * @return com.chick.base.R
     * @Author xkx
     * @Description 查询考试详情
     * @Date 2022-06-17 13:54
     * @Param [current, size, keyword, delFlag, examId]
     **/
    @Override
    public R getList(Page<ExamDetail> validPage, String keyword, String delFlag, String examId, String examTypeId, String types) {
        LambdaQueryWrapper<ExamDetail> wrapper = Wrappers.<ExamDetail>lambdaQuery()
                .eq(ExamDetail::getDelFlag, delFlag);
        if (StringUtils.isNotBlank(types)){
            wrapper.and(w -> {
                for (String type : types.split(",")){
                    w.or().like(ExamDetail::getType, type);
                }
            });
        }
        // 添加考试id
        if (StringUtils.isNotBlank(examId)) {
            wrapper.eq(ExamDetail::getExamId, examId);
        }
        // 添加考试类型id
        if (StringUtils.isNotBlank(examTypeId)) {
            wrapper.eq(ExamDetail::getExamTypeId, examTypeId);
        }
        //3.添加关键字
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(wr -> wr.like(ExamDetail::getDetailName, keyword));
        }
        return R.ok(baseMapper.selectPage(validPage, wrapper));
    }

    /**
    * @Author xkx
    * @Description 查询考试
    * @Date 2022-06-17 14:42
    * @Param []
    * @return com.chick.base.R
    **/
    @Override
    public R getExamList() {
        List<Exam> exams = examMapper.selectList(Wrappers.<Exam>lambdaQuery()
                .eq(Exam::getDelFlag, CommonConstants.UN_DELETE_FLAG));
        return R.ok(exams);
    }

    @Override
    public R getExamDetailByExamId(String examId) {
        List<ExamType> examTypes = examTypeMapper.selectList(Wrappers.<ExamType>lambdaQuery()
                .eq(ExamType::getExamId, examId)
                .eq(ExamType::getDelFlag, CommonConstants.UN_DELETE_FLAG)
                .orderByAsc(ExamType::getSort));
        return R.ok(examTypes);
    }

    /**
     * @return com.chick.base.R
     * @Author xkx
     * @Description 保存考试详情
     * @Date 2022-06-17 13:54
     * @Param [examDetail]
     **/
    @Override
    public R insert(ExamDetail examDetail) {
        examDetail.setId(DoId());
        if (CommonConstants.UN_DELETE_FLAG.equals(examDetail.getDelFlag())) {
            examDetail.setDelFlag(CommonConstants.DELETE_FLAG);
        } else {
            examDetail.setDelFlag(CommonConstants.UN_DELETE_FLAG);
        }
        if (baseMapper.insert(examDetail) > 0) {
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
    public R update(ExamDetail examDetail) {
        if (baseMapper.updateById(examDetail) > 0) {
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
    public R removeOrRenew(ExamDetail examDetail) {
        examDetail.setDelFlag(CommonConstants.DELETE_FLAG);
        if (baseMapper.updateById(examDetail) > 0) {
            return R.ok("删除成功");
        }
        return R.failed("更新失败");
    }
}
