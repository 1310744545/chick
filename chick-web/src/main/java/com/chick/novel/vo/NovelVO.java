package com.chick.novel.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.chick.common.domin.BaseEntity;
import com.chick.novel.entity.NovelChapter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 小说
 * </p>
 *
 * @author xiaokexin
 * @since 2022-07-11
 */
@Data
public class NovelVO extends BaseEntity implements Serializable {

    /**
     * 小说名
     */
    private String name;

    /**
     * 首页地址(bookId)
     */
    private String indexUrl;

    /**
     * 封面地址
     */
    private String imageUrl;

    /**
     * 作者
     */
    private String author;

    /**
     * 来源
     */
    private String source;

    /**
     * 类型
     */
    private String type;

    /**
     * 字数
     */
    private String wordCount;

    /**
     * 描述
     */
    private String describtion;

    private List<NovelChapter> novelChapters;

}
