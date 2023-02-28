package com.chick.second.hotel.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chick.base.R;
import com.chick.second.hotel.dto.HotelDTO;
import com.chick.second.hotel.dto.HotelEditDTO;
import com.chick.second.hotel.dto.HotelSaveDTO;
import com.chick.second.hotel.dto.ReservationDTO;
import com.chick.second.hotel.entity.Hotel;
import com.chick.second.hotel.entity.Reservation;
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
 * @ClassName ManagerController
 * @Author xiaokexin
 * @Date 2023-02-27 15:19
 * @Description ManagerController
 * @Version 1.0
 */
@RestController
@Api(tags = "管理端相关接口")
@RequestMapping("/hotelManager")
public class ManagerController {

    @Resource
    private HotelService hotelService;
    @Resource
    private ReservationService reservationService;

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

        return hotelService.getHotel(PageUtils.validPage(current, size), hotelDTO);
    }

    /**
     * 根据酒店ID查询酒店信息
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "根据酒店ID查询酒店信息", httpMethod = "GET")
    @GetMapping("/{id}")
    public R<Hotel> getById(@PathVariable("id") String id) {
        return hotelService.getById(id);
    }


    /**
     * 新增酒店
     *
     * @param hotelSaveDTO 新增酒店
     * @return R
     */
    @ApiOperation(value = "新增酒店", httpMethod = "POST")
    @PostMapping("/add")
    public R save(@Valid @RequestBody HotelSaveDTO hotelSaveDTO) {
        return hotelService.addHotel(hotelSaveDTO);
    }

    /**
     * 上架或下架酒店
     *
     * @param hotelId 上架或下架酒店
     * @return R
     */
    @ApiOperation(value = "上架或下架酒店", httpMethod = "POST")
    @PostMapping("/publishOrDown")
    public R publishOrDown(String hotelId) {
        return hotelService.publishOrDown(hotelId);
    }


    /**
     * 修改酒店
     *
     * @param hotelEditDTO 修改酒店
     * @return R
     */
    @ApiOperation(value = "修改酒店 ", httpMethod = "POST")
    @PostMapping("/modify")
    public R updateHotel(@Valid @RequestBody HotelEditDTO hotelEditDTO) {
        if (StringUtils.isBlank(hotelEditDTO.getId())) {
            return R.failed("酒店id不能为空");
        }
        return hotelService.updateHotel(hotelEditDTO);
    }


    @ApiOperation(value = "删除酒店", httpMethod = "POST")
    @PostMapping("/remove")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id数组对象", paramType = "query"),
    })
    public R removeByIds(String ids) {
        if (StringUtils.isBlank(ids)) {
            return R.failed("选择不能为空");
        }
        return hotelService.removeByIds(ids);
    }

    /**
     * 预定分页查询
     */
    @ApiOperation(value = "预定信息分页查询", httpMethod = "GET")
    @GetMapping("/getReservation")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", required = true),
            @ApiImplicitParam(paramType = "query", name = "size", value = "分页数量", required = true),
            @ApiImplicitParam(paramType = "query", name = "hotel", value = "查询信息"),
    })
    public R<IPage<Reservation>> getReservation(int current, int size, ReservationDTO reservationDTO) {
        return reservationService.getReservation(PageUtils.validPage(current, size), reservationDTO);
    }


    /**
     * 预定信息确认
     */
    @ApiOperation(value = "预定信息确认", httpMethod = "GET")
    @GetMapping("/reservationOk")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "预定信息id"),
    })
    public R reservationOk(String id) {
        return reservationService.reservationOk(id);
    }
}
