package com.chick.software.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.base.R;
import com.chick.software.entity.Software;
import com.chick.software.entity.SoftwareDetail;
import com.chick.software.mapper.SoftwareDetailMapper;
import com.chick.software.service.SoftwareDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-03-03
 */
@Service
public class SoftwareDetailServiceImpl extends ServiceImpl<SoftwareDetailMapper, SoftwareDetail> implements SoftwareDetailService {

    @Override
    public R getSoftwareDetailList(Page<SoftwareDetail> validPage, String type, String keyword) {
        LambdaQueryWrapper<SoftwareDetail> wrapper = Wrappers.lambdaQuery();
        //2.添加类型
        if (StringUtils.isNotBlank(type)) {
            wrapper.and(wr -> wr.eq(SoftwareDetail::getType, type));
        }
        //3.添加关键字
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(wr -> wr.like(SoftwareDetail::getFileName, keyword));
        }
        return R.ok(baseMapper.selectPage(validPage, wrapper));
    }
}
