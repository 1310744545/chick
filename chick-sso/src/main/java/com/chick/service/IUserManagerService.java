package com.chick.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chick.base.R;
import com.chick.pojo.entity.User;


/**
 * @ClassName IUserManagerService
 * @Author xiaokexin
 * @Date 2022-05-27 19:53
 * @Description IUserManagerService
 * @Version 1.0
 */
public interface IUserManagerService extends IService<User> {
    /**
     * 获取用户列表
     *
     * @param validPage 分页信息
     * @param keyword   关键字
     * @param delFlag   删除标记
     * @return 分页列表
     */
    Page<User> list(Page<User> validPage, String keyword, String delFlag);

    /**
     * 锁定或解锁
     *
     * @param userId 用户id
     * @param lockFlag 锁定标记
     * @return R
     */
    R luckOrUnlock(String userId, String lockFlag);

    /**
     * 禁用与解禁
     *
     * @param userId 用户id
     * @param enabledFlag 禁用标记
     * @return R
     */
    R enabledOrUnEnabled(String userId, String enabledFlag);

    /**
     * 删除或恢复用户
     *
     * @param userId 用户id
     * @return R
     */
    R deleteOrRenew(String userId, String delFlag);
}
