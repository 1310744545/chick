package com.chick.second.hotel.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chick.base.R;
import com.chick.second.hotel.dto.ReservationDTO;
import com.chick.second.hotel.entity.Collect;
import com.chick.second.hotel.entity.Reservation;

/**
 * <p>
 * 收藏表 服务类
 * </p>
 *
 * @author xiaokexin
 * @since 2023-02-27
 */
public interface CollectService extends IService<Collect> {

    R collectHotel(String hotelId, String userId);

    R getCollect(Page<Collect> page, String userId);

}
