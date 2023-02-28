package com.chick.second.hotel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.second.hotel.dto.HotelDTO;
import com.chick.second.hotel.dto.HotelEditDTO;
import com.chick.second.hotel.dto.HotelSaveDTO;
import com.chick.second.hotel.entity.Hotel;
import com.chick.second.hotel.entity.Reservation;
import com.chick.second.hotel.mapper.HotelMapper;
import com.chick.second.hotel.service.HotelService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.chick.common.utils.ChickUtil.DoId;

/**
 * <p>
 * 酒店表 服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2023-02-27
 */
@Service
public class HotelServiceImpl extends ServiceImpl<HotelMapper, Hotel> implements HotelService {
    @Resource
    private HotelMapper hotelMapper;

    @Override
    public R getHotel(Page page, HotelDTO hotelDTO) {
        IPage iPage = hotelMapper.myPage(page, hotelDTO);
        return R.ok(iPage);
    }

    @Override
    public R getById(String id) {
        Hotel hotel = baseMapper.selectOne(Wrappers.<Hotel>lambdaQuery()
                .eq(Hotel::getId, id));
        return R.ok(hotel);
    }

    @Override
    public R addHotel(HotelSaveDTO hotelSaveDTO) {
        Hotel hotel = new Hotel();
        BeanUtils.copyProperties(hotelSaveDTO, hotel);
        hotel.setId(DoId());
        hotel.setDelFlag(CommonConstants.UN_DELETE_FLAG);
        hotel.setStatus(CommonConstants.UN_PUBLISH);
        int insert = baseMapper.insert(hotel);
        if (insert > 0){
            return R.ok("新增成功");
        }
        return R.failed("新增失败");
    }

    @Override
    public R publishOrDown(String hotelId) {
        Hotel hotel = baseMapper.selectOne(Wrappers.<Hotel>lambdaQuery()
                .eq(Hotel::getDelFlag, CommonConstants.UN_DELETE_FLAG)
                .eq(Hotel::getId, hotelId));
        if (ObjectUtils.isEmpty(hotel)) {
            return R.failed("未查到酒店信息");
        }
        if (CommonConstants.PUBLISH.equals(hotel.getStatus())){
            hotel.setStatus(CommonConstants.UN_PUBLISH);
            baseMapper.updateById(hotel);
            return R.ok("下架成功");
        } else {
            hotel.setStatus(CommonConstants.PUBLISH);
            baseMapper.updateById(hotel);
            return R.ok("上架成功");
        }
    }

    @Override
    public R updateHotel(HotelEditDTO hotelEditDTO) {
        Hotel hotel = baseMapper.selectOne(Wrappers.<Hotel>lambdaQuery()
                .eq(Hotel::getId, hotelEditDTO.getId()));
        if (ObjectUtils.isEmpty(hotel)){
            return R.failed("酒店已不存在");
        }
        BeanUtils.copyProperties(hotelEditDTO, hotel);
        int i = baseMapper.updateById(hotel);
        if (i > 0){
            return R.ok("修改成功");
        }
        return R.failed("修改失败");
    }

    @Override
    public R removeByIds(String ids) {
        int update = baseMapper.update(null, Wrappers.<Hotel>lambdaUpdate()
                .in(Hotel::getId, ids.split(","))
                .set(Hotel::getDelFlag, CommonConstants.DELETE_FLAG));
        if (update > 0){
            return R.ok("删除成功");
        }
        return R.failed("删除失败");
    }
}
