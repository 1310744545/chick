package com.chick.second.hotel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel("酒店编辑实体")
public class HotelEditDTO implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "id", required = true, example = "id", dataType = "java.lang.String")
    private String id;

    /**
     * 酒店名
     */
    @ApiModelProperty(name = "酒店名", value = "酒店名", required = true, example = "酒店名", dataType = "java.lang.String")
    private String name;

    /**
     * 价格
     */
    @ApiModelProperty(name = "价格", value = "价格", required = true, example = "100", dataType = "java.lang.Integer")
    private Integer price;

    /**
     * 图片base64编码
     */
    @ApiModelProperty(name = "图片base64编码", value = "图片base64编码", required = true, example = "base;123213", dataType = "java.lang.String")
    private String imgUrl;

    /**
     * 电话
     */
    @ApiModelProperty(name = "电话", value = "电话", required = true, example = "12345", dataType = "java.lang.String")
    private String phone;

    /**
     * 省
     */
    @ApiModelProperty(name = "省", value = "省", required = true, example = "浙江省", dataType = "java.lang.String")
    private String province;

    /**
     * 市
     */
    @ApiModelProperty(name = "市", value = "市", required = true, example = "杭州市", dataType = "java.lang.String")
    private String city;

    /**
     * 区
     */
    @ApiModelProperty(name = "区", value = "区", required = true, example = "西湖区", dataType = "java.lang.String")
    private String district;

    /**
     * 镇
     */
    @ApiModelProperty(name = "镇", value = "镇", required = true, example = "三墩镇", dataType = "java.lang.String")
    private String town;

    /**
     * 详细地址
     */
    @ApiModelProperty(name = "详细地址", value = "详细地址", required = true, example = "某某街道某某路某某号", dataType = "java.lang.String")
    private String detailed;

}
