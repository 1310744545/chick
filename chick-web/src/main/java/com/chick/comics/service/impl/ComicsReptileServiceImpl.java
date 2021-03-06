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
import com.chick.util.WatermarkUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.concurrent.*;
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
        return R.ok("??????");
    }

    @Override
    public R IIMHComicsByIndex(boolean imageScan, int pageNum, String letter) {
        ComicsReptileEvent comicsReptileEvent = new IIMComicsReptileEvent();
        comicsReptile(comicsReptileEvent, imageScan, IIMComicsReptileEvent.FILE_NAME, letter, pageNum);
        return R.ok("??????");
    }

    public R comicsReptile(ComicsReptileEvent comicsReptileEvent, boolean imageScan, String fileName, String flag, int pageNum) {
        // ???????????????
        int comicsPageTotal = comicsReptileEvent.getComicsPageTotal(flag);

        for (int i = pageNum; i <= comicsPageTotal; i++) {
            // ????????????
            List<Comics> comicsList;
            try {
                comicsList = comicsReptileEvent.getComics(flag, i);
            } catch (Exception e) {
                log.error("???" + i + "?????????????????????, ??????-->" + e);
                continue;
            }
            // ???????????????????????????
            for (Comics comics : comicsList) {
                // ????????????
                List<ComicsChapter> comicsChapterList = comicsReptileEvent.getComicsChapter(comics);
                if (comicsChapterList.size() < 1) {
                    log.info("???????????????????????????????????? ----->" + comics.getName());
                }
                // ??????????????? ?????????????????? ?????????????????????????????????????????????
                File fileIndex = new File(disk + ":\\comics\\" + fileName + "\\" + SoftwareUtil.replaceAllIllegalCode(comics.getName()) + "\\0-cover");
                String[] indexFiles = fileIndex.list((dir, name) -> name.endsWith(".jpg"));
                if (ObjectUtils.isNotEmpty(indexFiles) && indexFiles.length > 0) {
                    log.info("?????????????????????  ??????---->" + indexFiles[0]);
                    comics.setCoverLocalPath(null);
                } else {
                    // ?????????????????? ?????????
                    comics.setCoverLocalPath(disk + ":\\comics\\" + fileName + "\\" + SoftwareUtil.replaceAllIllegalCode(comics.getName()) + "\\0-cover\\" + DoId() + ".jpg");
                    if (StringUtils.isNotBlank(comics.getCoverUrl())) {
                        MultiPartThreadDownLoad.OrdinaryDownLoad(comics.getCoverUrl(), comics.getCoverLocalPath());
                    }
                }
                // ??????????????????
                Comics comicsSelect = comicsMapper.selectOne(Wrappers.<Comics>lambdaQuery()
                        .eq(Comics::getName, comics.getName())
                        .eq(Comics::getAuthor, comics.getAuthor())
                        .eq(Comics::getSource, comics.getSource())
                        .eq(Comics::getDelFlag, CommonConstants.UN_DELETE_FLAG));
                // ??????????????????????????????id??????????????????
                if (ObjectUtils.isNotEmpty(comicsSelect)) {
                    comics.setId(comicsSelect.getId());
                    comicsMapper.updateById(comics);
                } else {
                    comicsMapper.insert(comics);
                }
                // ????????????
                for (ComicsChapter comicsChapter : comicsChapterList) {
                    // ??????????????????
                    ComicsChapter comicsChapterSelect = comicsChapterMapper.selectOne(Wrappers.<ComicsChapter>lambdaQuery()
                            .eq(ComicsChapter::getName, comicsChapter.getName())
                            .eq(ComicsChapter::getComicsId, comics.getId())
                            .eq(ComicsChapter::getDelFlag, CommonConstants.UN_DELETE_FLAG));
                    // ???????????????????????????id??????????????????
                    if (ObjectUtils.isNotEmpty(comicsChapterSelect)) {
                        // ??????????????????
                        comicsChapter.setId(comicsChapterSelect.getId());
                        comicsChapter.setComicsId(comics.getId());
                        comicsChapter.setLocalPath(comicsChapterSelect.getLocalPath());
                        comicsChapterMapper.updateById(comicsChapter);
                        // ?????????????????????
                        if (SoftwareUtil.existByPath(comicsChapterSelect.getLocalPath())) {
                            log.info("?????????????????????????????????--?????????------>" + comics.getName() + "-->" + comicsChapter.getName());
                            if (!imageScan) {
                                continue;
                            }
                        } else {
                            log.info("?????????????????????????????????--????????????????????????????????????--??????????????????");
                            comicsChapter.setLocalPath(disk + ":\\comics\\" + fileName + "\\" + SoftwareUtil.replaceAllIllegalCode(comics.getName()) + "\\" + comicsChapter.getSort() + "-" + SoftwareUtil.replaceAllIllegalCode(comicsChapter.getName()));
                            comicsChapterMapper.updateById(comicsChapter);
                        }
                    } else {
                        comicsChapter.setLocalPath(disk + ":\\comics\\" + fileName + "\\" + SoftwareUtil.replaceAllIllegalCode(comics.getName()) + "\\" + comicsChapter.getSort() + "-" + SoftwareUtil.replaceAllIllegalCode(comicsChapter.getName()));
                        comicsChapterMapper.insert(comicsChapter);
                    }
                    try {
                        // ????????? ????????????????????????
                        File file = new File(comicsChapter.getLocalPath());
                        List<ComicsImage> comicsImageList = comicsReptileEvent.getComicsImage(comicsChapter);
                        for (ComicsImage comicsImage : comicsImageList) {
                            log.info("????????????????????????---  " + threadPool.getActiveCount() + "--- ?????????");
                            log.info("?????????????????????---    " + threadPool.getQueue().size() + "--- ?????????");
                            String[] files = file.list((dir, name) -> name.startsWith(comicsImage.getSort().toString() + "-"));
                            if (ObjectUtils.isNotEmpty(files) && files.length > 0) {
                                log.info("???????????????  ??????---->" + files[0]);
                                continue;
                            }
                            comicsImage.setImageLocalPath(comicsChapter.getLocalPath() + "\\" + comicsImage.getSort() + "-" + DoId() + ".jpg");
                            if (threadPool.getQueue().size() > 2000) {
                                log.info("??????????????????????????????????????????2000??????????????????");
                                Thread.sleep(60000);
                            }
                            threadPool.submit(() -> {
                                try {
                                    log.info("??????????????????---    " + comicsImage.getImageLocalPath() + "---");
                                    // ????????????
                                    MultiPartThreadDownLoad.OrdinaryDownLoad(comicsImage.getImageUrl(), comicsImage.getImageLocalPath());
                                    // ????????????
                                    log.info("??????????????????---    " + comicsImage.getImageLocalPath() + "--- ");
                                    lock.lock();
                                    comicsImageMapper.insert(comicsImage);
                                    log.info("???????????????????????????--" + comicsImage.getImageLocalPath() + "???");
                                } catch (Exception e) {

                                } finally {
                                    lock.unlock();
                                }
                            });
                        }
                    } catch (Exception e) {
                        log.error(comicsChapter.getName() + "??????????????????, ??????-->" + e);
                    }
                }
            }
        }
        return R.ok("????????????");
    }
}
