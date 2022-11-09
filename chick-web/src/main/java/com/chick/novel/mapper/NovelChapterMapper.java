package com.chick.novel.mapper;

import com.chick.novel.entity.NovelChapter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 小说章节 Mapper 接口
 * </p>
 *
 * @author xiaokexin
 * @since 2022-07-11
 */
public interface NovelChapterMapper extends BaseMapper<NovelChapter> {

    int insertCompress(NovelChapter novelChapter);

}
