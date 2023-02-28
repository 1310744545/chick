package com.chick.second.hotel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chick.base.R;
import com.chick.second.hotel.dto.HotelDTO;
import com.chick.second.hotel.dto.HotelEditDTO;
import com.chick.second.hotel.dto.HotelSaveDTO;
import com.chick.second.hotel.entity.Hotel;

/**
 * <p>
 * 酒店表 服务类
 * </p>
 *
 * @author xiaokexin
 * @since 2023-02-27
 */
public interface HotelService extends IService<Hotel> {

    R<IPage<Hotel>> getHotel(Page page, HotelDTO hotelDTO);

    R<Hotel> getById(String id);

    R addHotel(HotelSaveDTO hotelSaveDTO);

    R publishOrDown(String hotelId);

    R updateHotel(HotelEditDTO hotelEditDTO);

    R removeByIds(String ids);
}
