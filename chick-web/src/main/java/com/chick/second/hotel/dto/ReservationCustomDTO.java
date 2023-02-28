package com.chick.second.hotel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 预定表
 * </p>
 *
 * @author xiaokexin
 * @since 2023-02-27
 */
@Data
@ApiModel("管理端预定信息查询")
public class ReservationCustomDTO implements Serializable {

    /**
     * 酒店id
     */
    @ApiModelProperty(name = "酒店id", value = "酒店id", example = "12321312", dataType = "java.lang.String")
    private String hotelId;

    /**
     * 预定日期
     */
    @ApiModelProperty(name = "预定日期", value = "预定日期", example = "2022-11-11", dataType = "java.lang.String")
    private String time;

    /**
     * 预定日期
     */
    @ApiModelProperty(hidden = true)
    private String userId;

    /**
     * 状态（0，预约状态 1，已确认）
     */
    @ApiModelProperty(name = "状态（0，预约中 1，已确认）", value = "状态（0，预约中 1，已确认）", example = "2022-11-11", dataType = "java.lang.String")
    private String status;

}
