package com.chick.novel.event;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.base.R;
import com.chick.novel.entity.Novel;

import java.util.List;

/**
 * @ClassName NovelThirdPartEvent
 * @Author xiaokexin
 * @Date 2022-11-09 9:53
 * @Description NovelThirdPartEvent
 * @Version 1.0
 */
public interface NovelThirdPartEvent {

    // 获取每页中的小说集合
    R getNovelList(Page<Novel> validPage, String type, String keyword);

    // 获取章节集合
    R getChapterList(String bookId);

    // 获取章节内容
    R getContent(String bookId, String chapterId);
}
