package com.chick.second.hotel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ReservationHotelDTO
 * @Author xiaokexin
 * @Date 2023-02-28 10:56
 * @Description ReservationHotelDTO
 * @Version 1.0
 */
@Data
@ApiModel("客户端预定酒店实体")
public class ReservationHotelDTO implements Serializable {

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

}
