package com.chick.second.hotel.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.second.hotel.entity.Collect;
import com.chick.second.hotel.mapper.CollectMapper;
import com.chick.second.hotel.service.CollectService;
import org.springframework.stereotype.Service;

import static com.chick.common.utils.ChickUtil.DoId;

/**
 * <p>
 * 收藏表 服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2023-02-27
 */
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {

    @Override
    public R collectHotel(String hotelId, String userId) {
        Collect collect = baseMapper.selectOne(Wrappers.<Collect>lambdaQuery()
                .eq(Collect::getDelFlag, CommonConstants.UN_DELETE_FLAG)
                .eq(Collect::getHotelId, hotelId)
                .eq(Collect::getUserId, userId));
        if (ObjectUtils.isEmpty(collect)) {
            collect = new Collect();
            collect.setId(DoId());
            collect.setDelFlag(CommonConstants.UN_DELETE_FLAG);
            collect.setHotelId(hotelId);
            collect.setUserId(userId);
            baseMapper.insert(collect);
            return R.ok("收藏成功");
        } else {
            collect.setDelFlag(CommonConstants.DELETE_FLAG);
            baseMapper.updateById(collect);
            return R.ok("取消收藏成功");
        }
    }

    @Override
    public R getCollect(Page<Collect> page, String userId) {
        return R.ok(baseMapper.getCollect(page, userId));
    }
}
