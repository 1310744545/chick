package com.chick.software.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.base.R;
import com.chick.exam.entity.Exam;
import com.chick.software.entity.Software;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-03-01
 */
public interface SoftwareService extends IService<Software> {

    R getSoftwareList(Page<Software> validPage, String type, String keyword);
}
