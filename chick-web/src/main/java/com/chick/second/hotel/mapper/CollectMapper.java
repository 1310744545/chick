package com.chick.second.hotel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.exam.vo.ExamRecordVO;
import com.chick.second.hotel.entity.Collect;
import com.chick.second.hotel.vo.HotelCollectionVO;

import java.util.List;

/**
 * <p>
 * 收藏表 Mapper 接口
 * </p>
 *
 * @author xiaokexin
 * @since 2023-02-27
 */
public interface CollectMapper extends BaseMapper<Collect> {
    List<HotelCollectionVO> getCollect(Page validPage, String userId);
}
