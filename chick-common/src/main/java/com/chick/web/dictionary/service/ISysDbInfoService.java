package com.chick.web.dictionary.service;

import com.chick.base.R;
import com.chick.web.dictionary.entity.SysDbInfo;

import java.util.List;

/**
 * @ClassName ISysDbInfoService
 * @Author xiaokexin
 * @Date 2022/2/11 16:50
 * @Description ISysDbInfoService
 * @Version 1.0
 */
public interface ISysDbInfoService {

    R save(SysDbInfo sysDbInfo);

    R update(SysDbInfo sysDbInfo);

    R removeByDataNum(SysDbInfo sysDbInfo);

    R getAllDbInfo();
}
