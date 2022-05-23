package com.chick.download.service;

import com.chick.base.R;

/**
 * @ClassName IGitHubService
 * @Author xiaokexin
 * @Date 2022-05-23 11:21
 * @Description IGitHubService
 * @Version 1.0
 */
public interface IGitHubService {

    R nacosDownload();

    R sentinelDownload();

    R dubboDownload();

    R zookeeperDownload();
}
