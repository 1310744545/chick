package com.chick.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.base.R;
import com.chick.novel.entity.Novel;
import com.chick.novel.entity.NovelChapter;
import com.chick.novel.event.NovelThirdPartEvent;
import com.chick.novel.mapper.NovelChapterMapper;
import com.chick.novel.service.NovelChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 小说章节 服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-07-11
 */
@Service
public class NovelChapterServiceImpl extends ServiceImpl<NovelChapterMapper, NovelChapter> implements NovelChapterService {

    @Resource
    private NovelThirdPartEvent novelThirdPartEvent;

    @Override
    public R get(Page<NovelChapter> validPage, String novelId, String keyword) {
        LambdaQueryWrapper<NovelChapter> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NovelChapter::getNovelId, novelId);
        //3.添加关键字
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(wr -> wr.like(NovelChapter::getName, keyword));
        }
        return R.ok(baseMapper.selectPage(validPage, wrapper.select(NovelChapter::getName).orderByAsc(NovelChapter::getSort)));
    }

    @Override
    public R getThirdPartChapter(String bookId) {
        return novelThirdPartEvent.getChapterList(bookId);
    }

    @Override
    public R getContent(String novelChapterId) {
        return null;
    }

    @Override
    public R getThirdPartContent(String bookId, String chapterId) {
        return novelThirdPartEvent.getContent(bookId, chapterId);
    }
}
