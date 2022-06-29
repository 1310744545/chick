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
import com.chick.common.utils.RedisUtil;
import com.chick.util.MultiPartThreadDownLoad;
import com.chick.util.SoftwareUtil;
import com.chick.util.WatermarkUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
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
    private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 10, 300, TimeUnit.MINUTES, new ArrayBlockingQueue<>(8192), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());


    @Override
    public R tencentComics() {
        ComicsReptileEvent comicsReptileEvent = new TencentComicsReptileEvent();
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
                    comicsChapterList = comicsReptileEvent.getComicsChapter(comics);
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
                        comicsMapper.insert(comics);
                    }
                } catch (Exception e) {
                    log.error(comics.getName() + "篇章解析错误, 跳过-->" + e);
                    continue;
                }
                int j = 1; //章节标识
                // 解析图片
                for (ComicsChapter comicsChapter : comicsChapterList) {
                    // 查询是否存在
                    ComicsChapter comicsChapterSelect = comicsChapterMapper.selectOne(Wrappers.<ComicsChapter>lambdaQuery()
                            .eq(ComicsChapter::getName, comicsChapter.getName())
                            .eq(ComicsChapter::getComicsId, comics.getId())
                            .eq(ComicsChapter::getDelFlag, CommonConstants.UN_DELETE_FLAG));
                    // 存在时替换为存在的id，并进行更新
                    if (ObjectUtils.isNotEmpty(comicsChapterSelect)) {
                        comicsChapter.setId(comicsChapterSelect.getId());
                        comicsChapter.setComicsId(comics.getId());
                        comicsChapterMapper.updateById(comicsChapter);
                    } else {
                        // 下载封面
                        comics.setCoverLocalPath("D:\\comics\\" + comics.getName() + "\\0-cover\\" + DoId() + ".jpg");
                        if (!SoftwareUtil.existByPath(comics.getCoverLocalPath())){
                            multiPartThreadDownLoad.MultiPartDownLoad(comics.getCoverUrl(), comics.getCoverLocalPath());
                        }
                        comicsChapterMapper.insert(comicsChapter);
                    }
                    try {
                        List<ComicsImage> comicsImageList = comicsReptileEvent.getComicsImage(comicsChapter);
                        for (ComicsImage comicsImage : comicsImageList) {
                            // 查询图片和数据库中的数据是否存在，如果存在则不进行下载操作
                            ComicsImage comicsImageSelect = comicsImageMapper.selectOne(Wrappers.<ComicsImage>lambdaQuery()
                                    .eq(ComicsImage::getComicsId, comics.getId())
                                    .eq(ComicsImage::getComicsChapterId, comicsChapter.getId())
                                    .eq(ComicsImage::getImageUrl, comicsImage.getImageUrl()));
                            if (ObjectUtils.isNotEmpty(comicsImageSelect) && SoftwareUtil.existByPath(comicsImageSelect.getImageLocalPath())) {
                                continue;
                            }
                            // 不存在的添加到下载队列中
                            comicsImage.setImageLocalPath("D:\\comics\\" + comics.getName() + "\\" + j + "-" + comicsChapter.getName() + "\\" + comicsImage.getSort() + "-" + DoId() + ".jpg");
                            // 保存
                            if (ObjectUtils.isNotEmpty(comicsImageSelect)){
                                comicsImage.setId(comicsImageSelect.getId());
                                comicsImageMapper.updateById(comicsImage);
                            } else {
                                comicsImageMapper.insert(comicsImage);
                            }
                            threadPool.submit(() -> {
                                // 下载图片
                                log.info(comics.getName() + "-->" + comicsChapter.getName() + "-->" + comicsImage.getSort() + "----开始下载");
                                multiPartThreadDownLoad.MultiPartDownLoad(comicsImage.getImageUrl(), comicsImage.getImageLocalPath());
                                // 清除水印
                                WatermarkUtils.convertPath(comicsImage.getImageLocalPath());
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
