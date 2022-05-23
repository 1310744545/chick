package com.chick.download.service.impl;

import com.chick.base.CommonConstants;
import com.chick.base.ConfigConstant;
import com.chick.base.FileName;
import com.chick.base.R;
import com.chick.common.utils.RedisUtil;
import com.chick.download.service.IGitHubService;
import com.chick.software.entity.Software;
import com.chick.util.GitHubParse;
import com.chick.util.MultiPartThreadDownLoad;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.OffsetDateTime;

/**
 * @ClassName GitHubServiceImpl
 * @Author xiaokexin
 * @Date 2022-05-23 11:21
 * @Description GitHubServiceImpl
 * @Version 1.0
 */
@Service
public class GitHubServiceImpl implements IGitHubService {

    @Resource
    private GitHubParse gitHubParse;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private MultiPartThreadDownLoad multiPartThreadDownLoad;


    @Override
    public R nacosDownload() {
        String url = redisUtil.getString(CommonConstants.CONFIG + ":" + FileName.NACOS + ConfigConstant.INDEX);
        if (StringUtils.isBlank(url)){
            R.failed("配置地址为空，请文件配置首页地址");
        }
        Software nacos = gitHubParse.parseHtmlByUrl(url, FileName.NACOS);
        return multiPartThreadDownLoad.MultiPartDownLoadBySoftware(nacos);
    }

    @Override
    public R sentinelDownload() {
        String url = redisUtil.getString(CommonConstants.CONFIG + ":" + FileName.SENTINEL + ConfigConstant.INDEX);
        if (StringUtils.isBlank(url)){
            R.failed("配置地址为空，请文件配置首页地址");
        }
        Software sentinel = gitHubParse.parseHtmlByUrl(url, FileName.SENTINEL);
        return multiPartThreadDownLoad.MultiPartDownLoadBySoftware(sentinel);
    }

    @Override
    public R dubboDownload() {
        String url = redisUtil.getString(CommonConstants.CONFIG + ":" + FileName.DUBBO + ConfigConstant.INDEX);
        if (StringUtils.isBlank(url)){
            R.failed("配置地址为空，请文件配置首页地址");
        }
        Software dubbo = gitHubParse.parseHtmlByUrl(url, FileName.DUBBO);
        return multiPartThreadDownLoad.MultiPartDownLoadBySoftware(dubbo);
    }

    @Override
    public R zookeeperDownload() {
        String url = redisUtil.getString(CommonConstants.CONFIG + ":" + FileName.ZOOKEEPER + ConfigConstant.INDEX);
        if (StringUtils.isBlank(url)){
            R.failed("配置地址为空，请文件配置首页地址");
        }
        Software zookeeper = gitHubParse.parseHtmlByUrl(url, FileName.ZOOKEEPER);
        return multiPartThreadDownLoad.MultiPartDownLoadBySoftware(zookeeper);
    }
}
