package com.chick.novel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.chick.common.domin.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 小说章节
 * </p>
 *
 * @author xiaokexin
 * @since 2022-07-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NovelChapter extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
      @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 小说id
     */
    private String novelId;

    /**
     * 章节名
     */
    private String name;

    /**
     * 篇章地址(id)
     */
    private String indexUrl;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 内容
     */
    private byte[] content;

    /**
     * 1、html 2、纯汉字
     */
    private String type;

}
