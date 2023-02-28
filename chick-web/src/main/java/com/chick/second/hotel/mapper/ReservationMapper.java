package com.chick.second.hotel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.base.R;
import com.chick.second.hotel.dto.ReservationCustomDTO;
import com.chick.second.hotel.dto.ReservationDTO;
import com.chick.second.hotel.entity.Reservation;
import com.chick.second.hotel.vo.ReservationVO;

import java.util.List;

/**
 * <p>
 * 预定表 Mapper 接口
 * </p>
 *
 * @author xiaokexin
 * @since 2023-02-27
 */
public interface ReservationMapper extends BaseMapper<Reservation> {
     IPage<ReservationVO> getReservation(Page<ReservationVO> page, ReservationCustomDTO reservationDTO);
}
