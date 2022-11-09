package com.chick.novel.event;

import com.chick.novel.entity.Novel;
import com.chick.novel.entity.NovelChapter;

import java.util.List;

/**
 * @ClassName NovelReptileEvent
 * @Author xiaokexin
 * @Date 2022-07-21 16:14
 * @Description NovelReptileEvent
 * @Version 1.0
 */
public interface NovelReptileEvent {

    // 获取总页数
    int getNovelPage();

    // 获取每页中的小说集合
    List<Novel> getNovelList(int page);
    // 获取一部小说的章节
    List<NovelChapter> getNovelChapterList(Novel novel);
    // 获取章节内容
    void getChapterContent(NovelChapter novelChapter);
}
