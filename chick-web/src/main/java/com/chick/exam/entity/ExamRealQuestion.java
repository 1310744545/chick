package com.chick.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiaokexin
 * @since 2022-07-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamRealQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
      @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 考试id
     */
    private String examId;

    /**
     * 真题id
     */
    private String examRealId;

    /**
     * 试题id
     */
    private String examQuestionId;

    /**
     * 例：第一题
     */
    private String name;

    /**
     * 排序
     */
    private String sort;

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
