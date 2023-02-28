package com.chick.second.hotel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 预定表
 * </p>
 *
 * @author xiaokexin
 * @since 2023-02-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
      @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 酒店id
     */
    private String hotelId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 预定日期
     */
    private String time;

    /**
     * 0已预约 1已消费 成为历史
     */
    private String status;

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
