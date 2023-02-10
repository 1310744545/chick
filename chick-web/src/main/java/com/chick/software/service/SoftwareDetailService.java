package com.chick.software.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chick.base.R;
import com.chick.software.entity.Software;
import com.chick.software.entity.SoftwareDetail;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-03-03
 */
public interface SoftwareDetailService extends IService<SoftwareDetail> {

    R getSoftwareDetailList(Page<SoftwareDetail> validPage, String type, String keyword);
}
