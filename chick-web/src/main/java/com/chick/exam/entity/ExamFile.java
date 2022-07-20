package com.chick.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.chick.common.domin.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamFile extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
      @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 考试或答案id
     */
    private String otherId;

    /**
     * 图片远程地址
     */
    private String otherUrl;

    /**
     * 图片本地地址
     */
    private String localUrl;

    /**
     * 图片本地地址
     */
    private String localPath;

    /**
     * 排序
     */
    private Integer sort;


    private String type;


}
