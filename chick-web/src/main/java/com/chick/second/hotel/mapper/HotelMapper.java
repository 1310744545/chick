package com.chick.second.hotel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.second.hotel.dto.HotelDTO;
import com.chick.second.hotel.entity.Hotel;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 酒店表 Mapper 接口
 * </p>
 *
 * @author xiaokexin
 * @since 2023-02-27
 */
public interface HotelMapper extends BaseMapper<Hotel> {

    IPage<Hotel> myPage(Page<Hotel> page, @Param("hotelDTO") HotelDTO hotelDTO);
}
