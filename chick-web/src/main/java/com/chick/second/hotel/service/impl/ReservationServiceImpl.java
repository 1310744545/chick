package com.chick.second.hotel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.second.hotel.dto.ReservationCustomDTO;
import com.chick.second.hotel.dto.ReservationDTO;
import com.chick.second.hotel.dto.ReservationHotelDTO;
import com.chick.second.hotel.entity.Hotel;
import com.chick.second.hotel.entity.Reservation;
import com.chick.second.hotel.mapper.ReservationMapper;
import com.chick.second.hotel.service.ReservationService;
import com.chick.second.hotel.vo.ReservationVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.chick.common.utils.ChickUtil.DoId;

/**
 * <p>
 * 预定表 服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2023-02-27
 */
@Service
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation> implements ReservationService {

    @Override
    public R<IPage<Reservation>> getReservation(Page<Reservation> page, ReservationDTO reservationDTO) {
        Page<Reservation> reservationPage = baseMapper.selectPage(page, Wrappers.<Reservation>lambdaQuery()
                .eq(Reservation::getDelFlag, CommonConstants.UN_DELETE_FLAG));
        return R.ok(reservationPage);
    }

    @Override
    public R<IPage<ReservationVO>> getReservationCustom(Page<ReservationVO> page, ReservationCustomDTO reservationCustomDTO) {
        IPage<ReservationVO> reservation = baseMapper.getReservation(page, reservationCustomDTO);
        return R.ok(reservation);
    }

    @Override
    public R reservationOk(String id) {
        Reservation reservation = baseMapper.selectOne(Wrappers.<Reservation>lambdaQuery()
                .eq(Reservation::getDelFlag, CommonConstants.UN_DELETE_FLAG)
                .eq(Reservation::getId, id));
        if (ObjectUtils.isEmpty(reservation)) {
            return R.failed("未查到预定信息");
        }
        if (CommonConstants.RESERVATION_ED.equals(reservation.getStatus())) {
            return R.failed("预定信息已确认");
        }
        reservation.setStatus(CommonConstants.RESERVATION_ED);
        baseMapper.updateById(reservation);
        return R.ok("预定信息确认成功");
    }

    @Override
    public R reservationHotel(ReservationHotelDTO reservationHotelDTO, String userId) {
        Reservation reservation;
        reservation = baseMapper.selectOne(Wrappers.<Reservation>lambdaQuery()
                .eq(Reservation::getUserId, userId)
                .eq(Reservation::getHotelId, reservationHotelDTO.getHotelId())
                .eq(Reservation::getTime, reservationHotelDTO.getTime()));
        if (ObjectUtils.isNotEmpty(reservation)) {
            return R.failed("重复预定");
        }
        reservation = baseMapper.selectOne(Wrappers.<Reservation>lambdaQuery()
                .eq(Reservation::getUserId, userId)
                .eq(Reservation::getTime, reservationHotelDTO.getTime()));
        if (ObjectUtils.isNotEmpty(reservation)) {
            return R.failed("此日期已有其他预定信息，请检查行程");
        }
        reservation = new Reservation();
        BeanUtils.copyProperties(reservationHotelDTO, reservation);
        reservation.setId(DoId());
        reservation.setDelFlag(CommonConstants.UN_DELETE_FLAG);
        reservation.setUserId(userId);
        reservation.setStatus(CommonConstants.RESERVATION_ING);
        baseMapper.insert(reservation);
        return R.ok("预定成功");
    }
}
