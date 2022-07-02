package com.chick.comics.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import com.chick.common.domin.BaseEntity;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 漫画
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Comics extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
      @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 名字
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 作者
     */
    private String author;

    /**
     * 人气
     */
    private String popularity;

    /**
     * 标签
     */
    private String label;

    /**
     * 点赞数
     */
    private Integer likes;

    /**
     * 收藏数
     */
    private Integer favorites;

    /**
     * 是否展示
     */
    private String open;

    /**
     * 来源
     */
    private String source;

    /**
     * 漫画首页url
     */
    private String indexUrl;

    /**
     * 封面图片url
     */
    private String coverUrl;

    /**
     * 封面本地路径
     */
    private String coverLocalPath;

    /**
     * 篇章
     */
    @TableField(exist = false)
    private List<ComicsChapter> comicsChapters;

}
