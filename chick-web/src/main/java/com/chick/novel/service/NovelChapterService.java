package com.chick.novel.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.base.R;
import com.chick.novel.entity.Novel;
import com.chick.novel.entity.NovelChapter;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 小说章节 服务类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-07-11
 */
public interface NovelChapterService extends IService<NovelChapter> {
    R get(Page<NovelChapter> validPage, String novelId, String keyword);

    R getThirdPartChapter(String bookId);
    R getContent(String novelChapterId);
    R getThirdPartContent(String bookId, String chapterId);
}
