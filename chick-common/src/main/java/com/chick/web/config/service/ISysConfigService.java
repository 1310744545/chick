package com.chick.web.config.service;

import com.chick.base.R;
import com.chick.web.config.entity.SysConfig;
import com.chick.web.dictionary.entity.SysDbInfo;

/**
 * @ClassName ISysDbInfoService
 * @Author xiaokexin
 * @Date 2022/2/11 16:50
 * @Description ISysDbInfoService
 * @Version 1.0
 */
public interface ISysConfigService {

    R save(SysConfig sysConfig);

    R update(SysConfig sysConfig);

    R remove(SysConfig sysConfig);

    R getAllConfigInfo();
}
