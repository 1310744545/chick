package com.chick.software.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.base.R;
import com.chick.exam.entity.Exam;
import com.chick.software.entity.Software;
import com.chick.software.mapper.SoftwareMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.software.service.SoftwareService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-03-01
 */
@Service
public class SoftwareServiceImpl extends ServiceImpl<SoftwareMapper, Software> implements SoftwareService {

    @Override
    public R getSoftwareList(Page<Software> validPage, String type, String keyword) {
        LambdaQueryWrapper<Software> wrapper = Wrappers.lambdaQuery();
        //2.添加类型
        if (StringUtils.isNotBlank(type)) {
            wrapper.and(wr -> wr.eq(Software::getType, type));
        }
        //3.添加关键字
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(wr -> wr.like(Software::getSoftwareName, keyword));
        }
        return R.ok(baseMapper.selectPage(validPage, wrapper));
    }
}
