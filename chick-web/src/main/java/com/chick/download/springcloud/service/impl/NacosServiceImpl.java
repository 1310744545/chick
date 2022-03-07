package com.chick.download.springcloud.service.impl;

import com.chick.base.R;
import com.chick.download.springcloud.service.INacosService;
import com.chick.software.entity.Software;
import com.chick.util.GitHubParse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public R download() {
        Software nacos = gitHubParse.parseHtmlByUrl("https://github.com/alibaba/nacos", "Nacos");
        StringBuffer stringBuffer = new StringBuffer();
        System.out.println(nacos);
        return null;
    }
}
