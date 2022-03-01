package com.chick.web.config.service.impl;

import cn.hutool.core.lang.tree.Tree;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.common.utils.RedisUtil;
import com.chick.web.config.entity.SysConfig;
import com.chick.web.config.mapper.SysConfigMapper;
import com.chick.web.config.service.ISysConfigService;
import com.chick.web.dictionary.entity.SysDbInfo;
import com.chick.web.dictionary.mapper.SysDbInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

import static com.chick.common.utils.ChickUtil.DoId;
import static com.chick.config.ChickRunner.loadSysConfig;

/**
 * @ClassName SysConfigServiceImpl
 * @Author xiaokexin
 * @Date 2022/2/25 10:49
 * @Description SysConfigServiceImpl
 * @Version 1.0
 */
@Service
public class SysConfigServiceImpl implements ISysConfigService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Override
    public R save(SysConfig sysConfig) {
        if (StringUtils.isBlank(sysConfig.getId())){
            sysConfig.setId(DoId());
            if (sysConfigMapper.insertSelective(sysConfig) == 1){
                loadSysConfig();
                return R.ok("新增配置成功");
            }
        } else {
            if (sysConfigMapper.updateByPrimaryKeySelective(sysConfig) == 1){
                loadSysConfig();
                return R.ok("修改配置成功");
            }
        }
        return R.failed();
    }

    @Override
    public R update(SysConfig sysConfig) {
        return null;
    }

    @Override
    public R remove(SysConfig sysConfig) {
        if(sysConfigMapper.updateByPrimaryKeySelective(sysConfig) == 1){
            loadSysConfig();
            return R.ok("删除成功");
        }
        return R.failed();
    }

    @Override
    public R getAllConfigInfo() {
        ArrayList<SysConfig> configList = new ArrayList<SysConfig>(redisUtil.getHashEntries(CommonConstants.CONFIG).values());
        return R.ok(configList);
    }
}
