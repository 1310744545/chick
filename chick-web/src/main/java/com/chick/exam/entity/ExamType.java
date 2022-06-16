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
 * 考试级别/类别
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamType extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
      @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 所属考试id
     */
    private String examId;

    /**
     * 分类名，例：初级、中级、高级
     */
    private String typeName;

    /**
     * 排序
     */
    private String sort;

}
