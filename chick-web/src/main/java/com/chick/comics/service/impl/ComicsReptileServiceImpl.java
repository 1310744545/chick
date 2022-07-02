package com.chick.comics.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.comics.enent.ComicsReptileEvent;
import com.chick.comics.enent.TencentComicsReptileEvent;
import com.chick.comics.entity.Comics;
import com.chick.comics.entity.ComicsChapter;
import com.chick.comics.entity.ComicsImage;
import com.chick.comics.mapper.ComicsChapterMapper;
import com.chick.comics.mapper.ComicsImageMapper;
import com.chick.comics.mapper.ComicsMapper;
import com.chick.comics.service.ComicsReptileService;
import com.chick.util.MultiPartThreadDownLoad;
import com.chick.util.SoftwareUtil;
import com.chick.util.WatermarkUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.*;

import static com.chick.common.utils.ChickUtil.DoId;

/**
 * @ClassName ComicsReptileServiceImpk
 * @Author xiaokexin
 * @Date 2022-06-27 13:20
 * @Description ComicsReptileServiceImpk
 * @Version 1.0
 */
@Service
@Log4j2
public class ComicsReptileServiceImpl implements ComicsReptileService {

    @Resource
    private ComicsMapper comicsMapper;
    @Resource
    private ComicsChapterMapper comicsChapterMapper;
    @Resource
    private ComicsImageMapper comicsImageMapper;
    @Resource
    private MultiPartThreadDownLoad multiPartThreadDownLoad;
    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(50, 50, 300, TimeUnit.MINUTES, new ArrayBlockingQueue<>(8192), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public R tencentComics(boolean imageScan) {
        ComicsReptileEvent comicsReptileEvent = new TencentComicsReptileEvent();
        return comicsReptile(comicsReptileEvent, imageScan);
    }


    public R comicsReptile(ComicsReptileEvent comicsReptileEvent, boolean imageScan) {
        // 漫画总页数
        int comicsPageTotal = comicsReptileEvent.getComicsPageTotal();

        for (int i = 1; i <= comicsPageTotal; i++) {
            // 解析漫画
            List<Comics> comicsList;
            try {
                comicsList = comicsReptileEvent.getComics(i);
            } catch (Exception e) {
                log.error("第" + i + "页漫画解析错误, 跳过-->" + e);
                continue;
            }

            // 每次处理一页的漫画
            for (Comics comics : comicsList) {

                // 解析篇章
                List<ComicsChapter> comicsChapterList;
                try {
                    // 查询是否存在
                    Comics comicsSelect = comicsMapper.selectOne(Wrappers.<Comics>lambdaQuery()
                            .eq(Comics::getName, comics.getName())
                            .eq(Comics::getAuthor, comics.getAuthor())
                            .eq(Comics::getSource, comics.getSource())
                            .eq(Comics::getDelFlag, CommonConstants.UN_DELETE_FLAG));
                    // 存在时替换为存在的id，并进行更新
                    if (ObjectUtils.isNotEmpty(comicsSelect)) {
                        comics.setId(comicsSelect.getId());
                        comicsMapper.updateById(comics);
                    } else {
                        // 下载封面
                        comics.setCoverLocalPath("D:\\comics\\" + SoftwareUtil.replaceAllIllegalCode(comics.getName()) + "\\0-cover\\" + DoId() + ".jpg");
                        if (!SoftwareUtil.existByPath(comics.getCoverLocalPath())) {
                            multiPartThreadDownLoad.MultiPartDownLoad(comics.getCoverUrl(), comics.getCoverLocalPath());
                        }
                        comicsMapper.insert(comics);
                    }
                } catch (Exception e) {
                    log.error(comics.getName() + "篇章解析错误, 跳过-->" + e);
                    continue;
                }
                int j = 1; //章节标识
                comicsChapterList = comicsReptileEvent.getComicsChapter(comics);
                // 解析图片
                for (ComicsChapter comicsChapter : comicsChapterList) {
                    // 查询是否存在
                    ComicsChapter comicsChapterSelect = comicsChapterMapper.selectOne(Wrappers.<ComicsChapter>lambdaQuery()
                            .eq(ComicsChapter::getName, comicsChapter.getName())
                            .eq(ComicsChapter::getComicsId, comics.getId())
                            .eq(ComicsChapter::getDelFlag, CommonConstants.UN_DELETE_FLAG));
                    // 存在时替换为存在的id，并进行更新
                    if (ObjectUtils.isNotEmpty(comicsChapterSelect)) {
                        // 数据库中存在
                        comicsChapter.setId(comicsChapterSelect.getId());
                        comicsChapter.setComicsId(comics.getId());
                        comicsChapterMapper.updateById(comicsChapter);
                        // 不扫描章节下的图片
                        if (!imageScan){
                            continue;
                        }
                        // 实际存在不存在
                        if (SoftwareUtil.existByPath("D:\\comics\\" + SoftwareUtil.replaceAllIllegalCode(comics.getName()))) {
                            log.info("漫画章节数据库中已存在--实际有------>" + comics.getName() + comicsChapter.getName());
                            continue;
                        } else {
                            log.info("漫画章节数据库中已存在--实际没有--进行图片扫描");
                        }
                    } else {
                        comicsChapterMapper.insert(comicsChapter);
                    }
                    try {
                        List<ComicsImage> comicsImageList = comicsReptileEvent.getComicsImage(comicsChapter);
                        for (ComicsImage comicsImage : comicsImageList) {
                            log.info("正在运行的线程有---    " + threadPool.getActiveCount() + "--- 个线程");
                            log.info("线程队列中还有---    " + threadPool.getQueue().size() + "--- 个线程");
                            // 查询图片和数据库中的数据是否存在，如果存在则不进行下载操作
                            ComicsImage comicsImageSelect = comicsImageMapper.selectOne(Wrappers.<ComicsImage>lambdaQuery()
                                    .eq(ComicsImage::getComicsId, comics.getId())
                                    .eq(ComicsImage::getComicsChapterId, comicsChapter.getId())
                                    .eq(ComicsImage::getImageUrl, comicsImage.getImageUrl()));
                            if (ObjectUtils.isNotEmpty(comicsImageSelect) && SoftwareUtil.existByPath(comicsImageSelect.getImageLocalPath())) {
                                log.info("检测到图片已存在，停止后续下载操作 ---->" + comics.getName() + "----" + comicsChapter.getName() + "-----" + comicsImage.getImageUrl());
                                continue;
                            }
                            // 不存在的添加到下载队列中
                            comicsImage.setImageLocalPath("D:\\comics\\" + SoftwareUtil.replaceAllIllegalCode(comics.getName()) + "\\" + j + "-" + SoftwareUtil.replaceAllIllegalCode(comicsChapter.getName()) + "\\" + comicsImage.getSort() + "-" + DoId() + ".jpg");
                            // 保存
                            if (ObjectUtils.isNotEmpty(comicsImageSelect)) {
                                comicsImage.setId(comicsImageSelect.getId());
                                comicsImageMapper.updateById(comicsImage);
                            } else {
                                comicsImageMapper.insert(comicsImage);
                            }
                            if (threadPool.getQueue().size() > 2000) {
                                log.info("检测到线程等待队列中线程超过4000，暂停一分钟");
                                Thread.sleep(60000);
                            }
                            threadPool.submit(() -> {
                                log.info("线程开始执行---    " + comicsImage.getImageLocalPath() + "---");
                                // 下载图片
//                                multiPartThreadDownLoad.MultiPartDownLoad(comicsImage.getImageUrl(), comicsImage.getImageLocalPath());
                                MultiPartThreadDownLoad.OrdinaryDownLoad(comicsImage.getImageUrl(), comicsImage.getImageLocalPath());
                                // 清除水印
                                WatermarkUtils.convertPath(comicsImage.getImageLocalPath());
                                log.info("线程执行结束---    " + comicsImage.getImageLocalPath() + "--- ");
                            });
                        }
                    } catch (Exception e) {
                        log.error(comicsChapter.getName() + "图片解析错误, 跳过-->" + e);
                    } finally {
                        j++;
                    }
                }
            }
        }
        return R.ok("下载完成");
    }
}
