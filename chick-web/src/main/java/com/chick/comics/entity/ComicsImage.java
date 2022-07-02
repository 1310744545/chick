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
 * 漫画图片
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ComicsImage extends BaseEntity implements Serializable {

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
     * 漫画章节id
     */
    private String comicsChapterId;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 本地图片路径
     */
    private String imageLocalPath;

    /**
     * 排序
     */
    private Integer sort;

}
