package com.chick.second.hotel.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.controller.BaseController;
import com.chick.second.hotel.dto.*;
import com.chick.second.hotel.entity.Hotel;
import com.chick.second.hotel.service.CollectService;
import com.chick.second.hotel.service.HotelService;
import com.chick.second.hotel.service.ReservationService;
import com.chick.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @ClassName CustomController
 * @Author xiaokexin
 * @Date 2023-02-27 15:19
 * @Description CustomController
 * @Version 1.0
 */
@Api(tags = "客户端相关接口")
@RequestMapping("/hotelCustom")
@RestController
public class CustomController extends BaseController {
    @Resource
    private HotelService hotelService;
    @Resource
    private ReservationService reservationService;
    @Resource
    private CollectService collectService;

    /**
     * 酒店分页查询
     */
    @ApiOperation(value = "酒店分页查询", httpMethod = "GET")
    @GetMapping("/getHotel")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", required = true),
            @ApiImplicitParam(paramType = "query", name = "size", value = "分页数量", required = true),
            @ApiImplicitParam(paramType = "query", name = "hotelDTO", value = "查询信息"),
    })
    public R<IPage<Hotel>> getHotel(int current, int size, HotelDTO hotelDTO) {
        hotelDTO.setStatus(CommonConstants.PUBLISH);
        return hotelService.getHotel(PageUtils.validPage(current, size), hotelDTO);
    }


    /**
     * 预约酒店
     *
     * @param reservationHotelDTO 预约酒店
     * @return R
     */
    @ApiOperation(value = "预约酒店 ", httpMethod = "POST")
    @PostMapping("/reservationHotel")
    public R reservationHotel(@Valid @RequestBody ReservationHotelDTO reservationHotelDTO) {
        if (StringUtils.isBlank(reservationHotelDTO.getHotelId())) {
            return R.failed("酒店id不能为空");
        }
        if (StringUtils.isBlank(reservationHotelDTO.getTime())) {
            return R.failed("预定酒店时间不能为空");
        }
        return reservationService.reservationHotel(reservationHotelDTO, getUserId());
    }

    /**
     * 收藏/取消收藏酒店
     *
     * @param hotelId 收藏/取消收藏酒店
     * @return R
     */
    @ApiOperation(value = "收藏/取消收藏酒店", httpMethod = "POST")
    @PostMapping("/collectHotel")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "hotelId", value = "酒店id"),
    })
    public R collectHotel(String hotelId) {
        if (StringUtils.isBlank(hotelId)) {
            return R.failed("酒店id不能为空");
        }
        return collectService.collectHotel(hotelId, getUserId());
    }

    /**
     * 查询收藏列表
     *
     * @param current 查询 收藏列表
     * @return R
     */
    @ApiOperation(value = "查询收藏列表", httpMethod = "GET")
    @GetMapping("/getCollect")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", required = true),
            @ApiImplicitParam(paramType = "query", name = "size", value = "分页数量", required = true)
    })
    public R getCollect(int current, int size) {
        return collectService.getCollect(PageUtils.validPage(current, size), getUserId());
    }

    /**
     * 查询订单
     *
     * @param reservationCustomDTO 查询订单
     * @return R
     */
    @ApiOperation(value = "查询订单", httpMethod = "GET")
    @GetMapping("/getReservation")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", required = true),
            @ApiImplicitParam(paramType = "query", name = "size", value = "分页数量", required = true),
            @ApiImplicitParam(paramType = "query", name = "reservationDTO", value = "查询信息"),
    })
    public R getReservation(int current, int size, ReservationCustomDTO reservationCustomDTO) {

        return reservationService.getReservationCustom(PageUtils.validPage(current, size), reservationCustomDTO);
    }
}
