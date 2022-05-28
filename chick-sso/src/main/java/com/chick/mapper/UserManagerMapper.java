package com.chick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chick.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author 肖可欣
 * @since 2022-05-27 16:07
 */
@Mapper
@Repository
public interface UserManagerMapper extends BaseMapper<User> {

}
