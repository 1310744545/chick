package com.chick.second.hotel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 酒店表
 * </p>
 *
 * @author xiaokexin
 * @since 2023-02-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Hotel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 所属用户id
     */
    private String userId;

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
