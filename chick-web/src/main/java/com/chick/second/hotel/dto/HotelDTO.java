package com.chick.second.hotel.dto;

import io.swagger.annotations.ApiModelProperty;
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
@EqualsAndHashCode(callSuper = false)
public class HotelDTO implements Serializable {

    /**
     * 酒店名
     */
    @ApiModelProperty(name = "酒店名", value = "酒店名", example = "id", dataType = "java.lang.String")
    private String name;

    /**
     * 价格上限
     */
    @ApiModelProperty(name = "价格上限", value = "价格上限", example = "100", dataType = "java.lang.String")
    private Integer priceMax;

    /**
     * 价格下限
     */
    @ApiModelProperty(name = "价格上限", value = "价格上限",example = "50", dataType = "java.lang.String")
    private Integer priceMin;

    /**
     * 省
     */
    @ApiModelProperty(name = "省", value = "省", example = "浙江省", dataType = "java.lang.String")
    private String province;

    /**
     * 市
     */
    @ApiModelProperty(name = "市", value = "市", example = "杭州市", dataType = "java.lang.String")
    private String city;

    /**
     * 区
     */
    @ApiModelProperty(name = "区", value = "区",example = "西湖区", dataType = "java.lang.String")
    private String district;

    /**
     * 镇
     */
    @ApiModelProperty(name = "镇", value = "镇",example = "三墩镇", dataType = "java.lang.String")
    private String town;

    /**
     * 0下架，1发布
     */
    @ApiModelProperty(name = "0下架，1发布", value = "0下架，1发布", example = "0", dataType = "java.lang.String")
    private String status;

}
