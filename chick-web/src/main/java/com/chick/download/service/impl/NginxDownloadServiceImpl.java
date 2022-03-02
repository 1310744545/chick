package com.chick.download.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.system.SystemUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.common.utils.RedisUtil;
import com.chick.download.service.INginxDownloadService;
import com.chick.software.entity.Software;
import com.chick.software.entity.SoftwareDetail;
import com.chick.software.mapper.SoftwareDetailMapper;
import com.chick.software.mapper.SoftwareMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;

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
        String url = "http://nginx.org/en/download.html";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取所有可下载的a标签
        Elements links = doc.getElementsByTag("a");
        for (Element element : links){
            //获取所有可下载的版本a标签
            if (element.attr("href").contains("/download/nginx") && !element.attr("href").contains("asc")){
                //查询是否存在
                SoftwareDetail softwareDetail = softwareDetailMapper.selectOne(Wrappers.<SoftwareDetail>lambdaQuery()
                        .eq(SoftwareDetail::getFileOriginalName, element.text())
                        .eq(SoftwareDetail::getDelFlag, CommonConstants.NO));
                if (ObjectUtils.isEmpty(softwareDetail)){
                    //不存在去下载并保存
                    if (FileUtil.isWindows()){
                        redisUtil.getString(CommonConstants.WINDOWS_FILE_PRO);
                    }
                }
            }
        }
        return R.ok();
    }

    public static void main(String[] args) {
        System.out.println(SystemUtil.OS_NAME);
    }
}
