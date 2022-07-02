package com.chick.comics.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.chick.common.domin.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 漫画章节
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ComicsChapter extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
      @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 漫画id
     */
    private String comicsId;

    /**
     * 章节名
     */
    private String name;

    /**
     * 篇章地址
     */
    private String indexUrl;

}
