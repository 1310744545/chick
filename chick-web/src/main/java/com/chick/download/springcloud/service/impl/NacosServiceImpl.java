package com.chick.download.springcloud.service.impl;

import cn.hutool.core.io.FileUtil;
import com.chick.base.CommonConstants;
import com.chick.base.ConfigConstant;
import com.chick.base.R;
import com.chick.common.utils.RedisUtil;
import com.chick.download.springcloud.service.INacosService;
import com.chick.software.entity.Software;
import com.chick.util.GitHubParse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.chick.util.MultiPartThreadDownLoad.MultiPartDownLoadBySoftware;

/**
 * @ClassName NacosServiceImpl
 * @Author xiaokexin
 * @Date 2022/3/7 16:36
 * @Description NacosServiceImpl
 * @Version 1.0
 */
@Service
public class NacosServiceImpl implements INacosService {

    @Autowired
    private GitHubParse gitHubParse;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public R download() {
        Software nacos = gitHubParse.parseHtmlByUrl(redisUtil.getString(CommonConstants.CONFIG + ":" + ConfigConstant.NACOS_INDEX), "nacos");
        return MultiPartDownLoadBySoftware(nacos);
    }
}
