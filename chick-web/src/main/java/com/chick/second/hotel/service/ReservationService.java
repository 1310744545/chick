package com.chick.second.hotel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chick.base.R;
import com.chick.second.hotel.dto.HotelDTO;
import com.chick.second.hotel.dto.ReservationCustomDTO;
import com.chick.second.hotel.dto.ReservationDTO;
import com.chick.second.hotel.dto.ReservationHotelDTO;
import com.chick.second.hotel.entity.Hotel;
import com.chick.second.hotel.entity.Reservation;
import com.chick.second.hotel.vo.ReservationVO;

/**
 * <p>
 * 预定表 服务类
 * </p>
 *
 * @author xiaokexin
 * @since 2023-02-27
 */
public interface ReservationService extends IService<Reservation> {

    R<IPage<Reservation>>  getReservation(Page<Reservation> page, ReservationDTO reservationDTO);

    R<IPage<ReservationVO>> getReservationCustom(Page<ReservationVO> page, ReservationCustomDTO reservationCustomDTO);

    R  reservationOk(String id);

    R  reservationHotel(ReservationHotelDTO reservationHotelDTO, String userId);
}
