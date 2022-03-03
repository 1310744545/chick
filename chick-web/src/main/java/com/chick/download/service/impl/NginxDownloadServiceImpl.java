package com.chick.download.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chick.base.CommonConstants;
import com.chick.base.ConfigConstant;
import com.chick.base.R;
import com.chick.common.utils.ChickUtil;
import com.chick.common.utils.RedisUtil;
import com.chick.util.MultiPartThreadDownLoad;
import com.chick.util.SoftwareUtil;
import com.chick.download.service.INginxDownloadService;
import com.chick.software.entity.Software;
import com.chick.software.entity.SoftwareDetail;
import com.chick.software.mapper.SoftwareDetailMapper;
import com.chick.software.mapper.SoftwareMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.Date;

/**
 * @ClassName NginxDownloadServiceImpl
 * @Author xiaokexin
 * @Date 2021/12/24 14:40
 * @Description NginxDownloadServiceImpl
 * @Version 1.0
 */
@Slf4j
@Service
public class NginxDownloadServiceImpl implements INginxDownloadService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SoftwareMapper softwareMapper;
    @Autowired
    private SoftwareDetailMapper softwareDetailMapper;

    @Override
    public R download() {
        log.info("Nginx下载开始");
        Software nginx = softwareMapper.selectOne(Wrappers.<Software>lambdaQuery()
                .eq(Software::getSoftwareName, "Nginx"));
        if (ObjectUtils.isEmpty(nginx)) {
            nginx = new Software(ChickUtil.DoId(), "Nginx", "伊戈尔·赛索耶夫为", "ginx (engine x) 是一个高性能的HTTP和反向代理web服务器，同时也提供了IMAP/POP3/SMTP服务。Nginx是由伊戈尔·赛索耶夫为俄罗斯访问量第二的Rambler.ru站点（俄文：Рамблер）开发的，第一个公开版本0.1.0发布于2004年10月4日。", "");
            softwareMapper.insert(nginx);
        }
        String url = redisUtil.getString(CommonConstants.CONFIG + ":" + ConfigConstant.NGINX_DOWNLOAD_URL);
        if (StringUtils.isBlank(url)) {
            log.error("下载错误，utl为空，请维护该文件的url---->配置名：" + ConfigConstant.NGINX_DOWNLOAD_URL);
            return R.failed("下载错误，utl为空，请维护该文件的url---->配置名：" + ConfigConstant.NGINX_DOWNLOAD_URL);
        }
        //去请求文件页面
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取所有可下载的a标签
        Elements links = doc.getElementsByTag("a");
        for (Element element : links) {
            //获取所有可下载的版本a标签
            if (element.attr("href").contains("/download/nginx") && !element.attr("href").contains("asc")) {
                //查询是否存在
                SoftwareDetail softwareDetailResult = softwareDetailMapper.selectOne(Wrappers.<SoftwareDetail>lambdaQuery()
                        .eq(SoftwareDetail::getFileOriginalName, element.text())
                        .eq(SoftwareDetail::getDelFlag, CommonConstants.NO));
                //判断是否需要去下载
                if (SoftwareUtil.existWinOrLinux(softwareDetailResult)) {
                    continue;
                }
                //不存在去下载并保存/更新
                SoftwareDetail softwareDetail = creatSoftwareDetailByElement(element, nginx.getId());
                R r;
                if (FileUtil.isWindows()) {
                    r = MultiPartThreadDownLoad.MultiPartDownLoad(softwareDetail.getDownloadUrl(), redisUtil.getString(CommonConstants.CONFIG + ":" + ConfigConstant.WINDOWS_FILE_PRO) + softwareDetail.getWindowsPath());
                } else {
                    r = MultiPartThreadDownLoad.MultiPartDownLoad(softwareDetail.getDownloadUrl(), redisUtil.getString(CommonConstants.CONFIG + ":" + ConfigConstant.LINUX_FILE_PRO) + softwareDetail.getLinuxPath());
                }
                if (r.getCode() == 0) {
                    if (ObjectUtils.isEmpty(softwareDetailResult)) {
                        softwareDetailMapper.insert(softwareDetail);
                    } else {
                        softwareDetail.setId(softwareDetailResult.getId());
                        softwareDetailMapper.updateById(softwareDetail);
                    }
                } else {
                    log.error("下载文件出错，文件名：" + softwareDetailResult.getFileOriginalName());
                }
            }
        }
        return R.ok();
    }


    private SoftwareDetail creatSoftwareDetailByElement(Element element, String softwareId) {
        SoftwareDetail softwareDetail = new SoftwareDetail();
        softwareDetail.setId(ChickUtil.DoId());
        softwareDetail.setSoftwareId(softwareId);
        softwareDetail.setFileName(element.text());
        softwareDetail.setFileOriginalName(SoftwareUtil.getSoftWareNameByUrl(element.attr("href")));
        softwareDetail.setVersion(SoftwareUtil.getSoftwareVersionByTextOnAfter(element.text()));
        softwareDetail.setOperatingSystem(SoftwareUtil.getSoftwareOperationVersionBySoftwareOriginalName(softwareDetail.getFileOriginalName()));
        softwareDetail.setOsVersion("");
        softwareDetail.setLastModified(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        softwareDetail.setType(SoftwareUtil.getSoftwareTypeByHrefOnAfter(element.attr("href")));
        softwareDetail.setWindowsPath("\\Nginx\\" + redisUtil.getString(CommonConstants.DICTIONARY + ":" + softwareDetail.getOperatingSystem()) + "\\" + softwareDetail.getVersion() + "\\" + softwareDetail.getFileOriginalName());
        softwareDetail.setLinuxPath("/Nginx/" + redisUtil.getString(CommonConstants.DICTIONARY + ":" + softwareDetail.getOperatingSystem()) + "/" + softwareDetail.getVersion() + "/" + softwareDetail.getFileOriginalName());
        softwareDetail.setSize("");
        softwareDetail.setRemarks("");
        softwareDetail.setCreateDate(new Date());
        softwareDetail.setDownloadUrl(redisUtil.getString(CommonConstants.CONFIG + ":" + ConfigConstant.NGINX_DOWNLOAD_PRE) + element.attr("href"));
        return softwareDetail;
    }
}
