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
 * 小说
 * </p>
 *
 * @author xiaokexin
 * @since 2022-07-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Novel extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
      @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 小说名
     */
    private String name;

    /**
     * 首页地址
     */
    private String indexUrl;

    /**
     * 封面地址
     */
    private String imageUrl;

    /**
     * 封面本地路径
     */
    private String imageLocalPath;

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

}
