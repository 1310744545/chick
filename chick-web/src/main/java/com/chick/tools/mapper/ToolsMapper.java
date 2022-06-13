package com.chick.tools.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.tools.entity.Tools;
import com.chick.tools.vo.ToolsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 在线工具表 Mapper 接口
 * </p>
 *
 * @author 肖可欣
 * @since 2021-02-28
 */
@Mapper
public interface ToolsMapper extends BaseMapper<Tools> {

    Page<ToolsVO> getList(Page<ToolsVO> validPage, @Param("keyword") String keyword, @Param("delFlag")String delFlag, @Param("type")String type);

}
