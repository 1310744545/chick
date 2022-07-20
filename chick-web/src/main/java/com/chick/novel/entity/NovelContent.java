package com.chick.novel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 小说内容
 * </p>
 *
 * @author xiaokexin
 * @since 2022-07-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NovelContent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
      @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 章节id
     */
    private String chapterId;

    /**
     * 内容
     */
    private String content;

    /**
     * 1、html 2、纯汉字
     */
    private String type;

    /**
     * 是否逻辑删除 1是0否
     */
    private String delFlag;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateDate;


}
