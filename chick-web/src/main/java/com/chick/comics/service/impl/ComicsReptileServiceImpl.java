package com.chick.comics.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.comics.enent.ComicsReptileEvent;
import com.chick.comics.enent.IIMComicsReptileEvent;
import com.chick.comics.enent.TencentComicsReptileEvent;
import com.chick.comics.entity.Comics;
import com.chick.comics.entity.ComicsChapter;
import com.chick.comics.entity.ComicsImage;
import com.chick.comics.mapper.ComicsChapterMapper;
import com.chick.comics.mapper.ComicsImageMapper;
import com.chick.comics.mapper.ComicsMapper;
import com.chick.comics.service.ComicsReptileService;
import com.chick.comics.utils.TencentComicsUtil;
import com.chick.util.MultiPartThreadDownLoad;
import com.chick.util.SoftwareUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    public static final String disk = "E";
    @Resource
    private ComicsMapper comicsMapper;
    @Resource
    private ComicsChapterMapper comicsChapterMapper;
    @Resource
    private ComicsImageMapper comicsImageMapper;
    private static final Lock lock = new ReentrantLock();

    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(40, 40, 300, TimeUnit.MINUTES, new ArrayBlockingQueue<>(8192), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
    private static ThreadPoolExecutor threadPoolIIMC = new ThreadPoolExecutor(26, 26, 300, TimeUnit.MINUTES, new ArrayBlockingQueue<>(4), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public R tencentComics(boolean imageScan, int pageNum) {
        ComicsReptileEvent comicsReptileEvent = new TencentComicsReptileEvent();
        return comicsReptile(comicsReptileEvent, imageScan, TencentComicsUtil.FILE_NAME, null, pageNum);
    }

    public R IIMHComics(boolean imageScan, int pageNum, String letter) {
        String[] index = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        ComicsReptileEvent comicsReptileEvent = new IIMComicsReptileEvent();
        boolean flag = false;
        for (String i : index) {
            if (i.equals(letter)) {
                flag = true;
            }
            if (flag) {
                comicsReptile(comicsReptileEvent, imageScan, IIMComicsReptileEvent.FILE_NAME, i, pageNum);
            }
        }
        return R.ok("成功");
    }

    @Override
    public R IIMHComicsByIndex(boolean imageScan, int pageNum, String letter) {
        String[] letters = letter.split(",");
        ComicsReptileEvent comicsReptileEvent = new IIMComicsReptileEvent();
        for (String lat : letters) {
            threadPoolIIMC.submit(() -> {
                comicsReptile(comicsReptileEvent, imageScan, IIMComicsReptileEvent.FILE_NAME, lat, pageNum);
            }, lat);
        }

        new Thread(() -> {
            while (true){
                log.info("活跃的线程数-->" + threadPoolIIMC.getActiveCount());
                log.info("等待的线程数-->" + threadPoolIIMC.getQueue().size());
                try {
                    if (threadPoolIIMC.getActiveCount() == 0)
                        Thread.interrupted();
                    Thread.sleep(5000);
                } catch (Exception e) {
                    log.error("出错了e：{}", e.getMessage());
                }
            }
        }).start();

        return R.ok();
    }

    public R comicsReptile(ComicsReptileEvent comicsReptileEvent, boolean imageScan, String fileName, String flag, int pageNum) {
        // 漫画总页数
        int comicsPageTotal = comicsReptileEvent.getComicsPageTotal(flag);

        for (int i = pageNum; i <= comicsPageTotal; i++) {
            log.info("开始解析" + flag + "的漫画------第" + i + "页");
            // 解析漫画
            List<Comics> comicsList;
            try {
                comicsList = comicsReptileEvent.getComics(flag, i);
            } catch (Exception e) {
                log.error("第" + i + "页漫画解析错误, 跳过-->" + e);
                continue;
            }
            // 每次处理一页的漫画
            for (Comics comics : comicsList) {
                // 解析篇章
                List<ComicsChapter> comicsChapterList = comicsReptileEvent.getComicsChapter(comics);
                if (comicsChapterList.size() < 1) {
                    log.info("篇章解析出错，继续下一个 ----->" + comics.getName());
                }
                // 下载封面， 移动到这里了 因为有些要到章节里才能加载封面
                File fileIndex = new File(disk + ":\\comics\\" + fileName + "\\" + SoftwareUtil.replaceAllIllegalCode(comics.getName()) + "\\0-cover");
                String[] indexFiles = fileIndex.list((dir, name) -> name.endsWith(".jpg"));
                if (ObjectUtils.isNotEmpty(indexFiles) && indexFiles.length > 0) {
                    log.info("封面图片已存在  路径---->" + indexFiles[0]);
                    comics.setCoverLocalPath(null);
                } else {
                    // 设置封面地址 并下载
                    comics.setCoverLocalPath(disk + ":\\comics\\" + fileName + "\\" + SoftwareUtil.replaceAllIllegalCode(comics.getName()) + "\\0-cover\\" + DoId() + ".jpg");
                    if (StringUtils.isNotBlank(comics.getCoverUrl())) {
                        MultiPartThreadDownLoad.OrdinaryDownLoad(comics.getCoverUrl(), comics.getCoverLocalPath());
                    }
                }
                // 查询是否存在
                Comics comicsSelect = comicsMapper.selectOne(Wrappers.<Comics>lambdaQuery()
                        .eq(Comics::getName, comics.getName())
                        .eq(Comics::getAuthor, comics.getAuthor())
                        .eq(Comics::getSource, comics.getSource())
                        .eq(Comics::getDelFlag, CommonConstants.UN_DELETE_FLAG));
                // 存在赋值替换为存在的id，并进行更新
                if (ObjectUtils.isNotEmpty(comicsSelect)) {
                    comics.setId(comicsSelect.getId());
                    comicsMapper.updateById(comics);
                } else {
                    comicsMapper.insert(comics);
                }
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
                        comicsChapter.setLocalPath(comicsChapterSelect.getLocalPath());
                        comicsChapterMapper.updateById(comicsChapter);
                        // 实际存在不存在
                        if (SoftwareUtil.existByPath(comicsChapterSelect.getLocalPath())) {
                            log.info("漫画章节数据库中已存在--实际有------>" + comics.getName() + "-->" + comicsChapter.getName());
                            if (!imageScan) {
                                continue;
                            }
                        } else {
                            log.info("漫画章节数据库中已存在--实际没有设置新的本地路径--进行图片扫描");
                            comicsChapter.setLocalPath(disk + ":\\comics\\" + fileName + "\\" + SoftwareUtil.replaceAllIllegalCode(comics.getName()) + "\\" + comicsChapter.getSort() + "-" + SoftwareUtil.replaceAllIllegalCode(comicsChapter.getName()));
                            comicsChapterMapper.updateById(comicsChapter);
                        }
                    } else {
                        comicsChapter.setLocalPath(disk + ":\\comics\\" + fileName + "\\" + SoftwareUtil.replaceAllIllegalCode(comics.getName()) + "\\" + comicsChapter.getSort() + "-" + SoftwareUtil.replaceAllIllegalCode(comicsChapter.getName()));
                        comicsChapterMapper.insert(comicsChapter);
                    }
                    try {
                        // 文件夹 用于检测是否存在
                        File file = new File(comicsChapter.getLocalPath());
                        List<ComicsImage> comicsImageList = comicsReptileEvent.getComicsImage(comicsChapter);
                        for (ComicsImage comicsImage : comicsImageList) {
                            log.info("正在运行的线程有---  " + threadPool.getActiveCount() + "--- 个线程");
                            log.info("线程队列中还有---    " + threadPool.getQueue().size() + "--- 个线程");
                            String[] files = file.list((dir, name) -> name.startsWith(comicsImage.getSort().toString() + "-"));
                            if (ObjectUtils.isNotEmpty(files) && files.length > 0) {
                                log.info("图片已存在  路径---->" + files[0]);
                                continue;
                            }
                            comicsImage.setImageLocalPath(comicsChapter.getLocalPath() + "\\" + comicsImage.getSort() + "-" + DoId() + ".jpg");
                            if (threadPool.getQueue().size() > 2000) {
                                log.info("检测到线程等待队列中线程超过2000，暂停一分钟");
                                Thread.sleep(60000);
                            }
                            threadPool.submit(() -> {
                                try {
                                    // 下载图片
                                    MultiPartThreadDownLoad.OrdinaryDownLoad(comicsImage.getImageUrl(), comicsImage.getImageLocalPath());
                                    lock.lock();
                                    comicsImageMapper.insert(comicsImage);
                                    log.info("图片插入数据库成功--" + comicsImage.getImageLocalPath() + "✌");
                                } catch (Exception e) {

                                } finally {
                                    lock.unlock();
                                }
                            });
                        }
                    } catch (Exception e) {
                        log.error(comicsChapter.getName() + "图片解析错误, 跳过-->" + e);
                    }
                }
            }
        }
        return R.ok("下载完成");
    }
}
