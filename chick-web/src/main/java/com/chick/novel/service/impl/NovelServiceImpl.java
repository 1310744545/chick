package com.chick.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.base.R;
import com.chick.exam.entity.Exam;
import com.chick.novel.entity.Novel;
import com.chick.novel.event.NovelThirdPartEvent;
import com.chick.novel.mapper.NovelMapper;
import com.chick.novel.service.NovelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 小说 服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-07-11
 */
@Service
public class NovelServiceImpl extends ServiceImpl<NovelMapper, Novel> implements NovelService {

    @Resource
    private NovelThirdPartEvent novelThirdPartEvent;
    @Override
    public R get(Page<Novel> validPage, String type, String keyword) {
        LambdaQueryWrapper<Novel> wrapper = Wrappers.lambdaQuery();
        //2.添加类型
        if (StringUtils.isNotBlank(type)) {
            wrapper.and(wr -> wr.eq(Novel::getType, type));
        }
        //3.添加关键字
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(wr -> wr.like(Novel::getName, keyword));
        }
        return R.ok(baseMapper.selectPage(validPage, wrapper));
    }

    @Override
    public R getThirdPart(Page<Novel> validPage, String thirdPartType, String type, String keyword) {

        return novelThirdPartEvent.getNovelList(validPage, type, keyword);
    }
}
