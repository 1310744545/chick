package com.chick.second.hotel.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 酒店表
 * </p>
 *
 * @author xiaokexin
 * @since 2023-02-27
 */
@Data
public class HotelCollectionVO implements Serializable {


    /**
     * ID
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 酒店id
     */
    private String hotelId;

    /**
     * 酒店名
     */
    private String name;

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

    /**
     * 0下架，1发布
     */
    private String status;

}
