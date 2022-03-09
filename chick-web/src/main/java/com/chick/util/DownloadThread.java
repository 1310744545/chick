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
@AllArgsConstructor
@NoArgsConstructor
public class DownloadThread implements Callable<R> {

    private SoftwareDetail softwareDetail;
    private CountDownLatch countDownLatch;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SoftwareDetailMapper softwareDetailMapper;
    @Autowired
    private MultiPartThreadDownLoad multiPartThreadDownLoad;

    @Override
    public R call() throws Exception {
        //查询是否存在
        SoftwareDetail softwareDetailResult = softwareDetailMapper.selectOne(Wrappers.<SoftwareDetail>lambdaQuery()
                .eq(SoftwareDetail::getFileOriginalName, softwareDetail.getFileOriginalName())
                .eq(SoftwareDetail::getDelFlag, CommonConstants.NO));
        //判断是否需要去下载
        if (SoftwareUtil.existWinOrLinux(softwareDetailResult)) {
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
                softwareDetailMapper.updateById(softwareDetail);
            }
        }
        countDownLatch.countDown();
        return R.ok();
    }

    public DownloadThread(SoftwareDetail softwareDetail, CountDownLatch countDownLatch) {
        this.softwareDetail = softwareDetail;
        this.countDownLatch = countDownLatch;
    }
}
