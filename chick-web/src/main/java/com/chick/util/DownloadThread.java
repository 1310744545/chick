package com.chick.util;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chick.base.CommonConstants;
import com.chick.base.ConfigConstant;
import com.chick.base.R;
import com.chick.common.utils.RedisUtil;
import com.chick.software.entity.Software;
import com.chick.software.entity.SoftwareDetail;
import com.chick.software.mapper.SoftwareDetailMapper;
import com.chick.software.mapper.SoftwareMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName DownloadThread
 * @Author xiaokexin
 * @Date 2022/3/8 16:42
 * @Description DownloadThread
 * @Version 1.0
 */
@Slf4j
@NoArgsConstructor
@Component
public class DownloadThread implements Callable<R> {

    private SoftwareDetail softwareDetail;
    private CountDownLatch countDownLatch;


    private RedisUtil redisUtil;

    private SoftwareDetailMapper softwareDetailMapper;

    private MultiPartThreadDownLoad multiPartThreadDownLoad;

    @Override
    public R call() throws Exception {
        //查询是否存在
        SoftwareDetail softwareDetailResult = softwareDetailMapper.selectOne(Wrappers.<SoftwareDetail>lambdaQuery()
                .eq(SoftwareDetail::getFileOriginalName, softwareDetail.getFileOriginalName())
                .eq(SoftwareDetail::getDelFlag, CommonConstants.NO));
        //判断是否需要去下载
        if (SoftwareUtil.existWinOrLinux(softwareDetailResult)) {
            log.info("当前文件已下载，停止下载--文件名{}",softwareDetailResult.getFileOriginalName());
            countDownLatch.countDown();
            return R.failed("已下载");
        }
        String windowsPathPre = redisUtil.getString(CommonConstants.CONFIG + ":" + ConfigConstant.WINDOWS_FILE_PRO);
        String linuxPathPre = redisUtil.getString(CommonConstants.CONFIG + ":" + ConfigConstant.LINUX_FILE_PRO);
        String localPath = FileUtil.isWindows() ? windowsPathPre + softwareDetail.getWindowsPath() : linuxPathPre + softwareDetail.getLinuxPath();
        FileUtil.mkParentDirs(localPath);
        //下载
        R r = multiPartThreadDownLoad.MultiPartDownLoad(softwareDetail.getDownloadUrl(), localPath);
        if (r.getCode() == 0) {
            //成功
            if (ObjectUtils.isEmpty(softwareDetailResult)) {
                softwareDetailMapper.insert(softwareDetail);
            } else {
                softwareDetail.setId(softwareDetailResult.getId());
                softwareDetail.setUpdateDate(new Date());
                softwareDetailMapper.updateById(softwareDetail);
            }
        }
        countDownLatch.countDown();
        return R.ok();
    }

    public DownloadThread(SoftwareDetail softwareDetail, CountDownLatch countDownLatch, RedisUtil redisUtil, SoftwareDetailMapper softwareDetailMapper, MultiPartThreadDownLoad multiPartThreadDownLoad) {
        this.softwareDetail = softwareDetail;
        this.countDownLatch = countDownLatch;
        this.redisUtil = redisUtil;
        this.softwareDetailMapper = softwareDetailMapper;
        this.multiPartThreadDownLoad = multiPartThreadDownLoad;
    }
}
