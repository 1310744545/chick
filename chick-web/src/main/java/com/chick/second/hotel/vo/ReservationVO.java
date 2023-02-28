package com.chick.second.hotel.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

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
public class ReservationVO implements Serializable {

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
     * 价格
     */
    private Integer price;

    /**
     * 图片地址
     */
    private String imgUrl;

    /**
     * 电话
     */
    private String phone;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 镇
     */
    private String town;

    /**
     * 详细地址
     */
    private String detailed;

}
