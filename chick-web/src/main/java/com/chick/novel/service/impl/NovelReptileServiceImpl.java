package com.chick.novel.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chick.base.R;
import com.chick.comics.entity.Comics;
import com.chick.novel.entity.Novel;
import com.chick.novel.entity.NovelChapter;
import com.chick.novel.event.NovelReptileEvent;
import com.chick.novel.event.QB5TWEvent;
import com.chick.novel.mapper.NovelChapterMapper;
import com.chick.novel.mapper.NovelMapper;
import com.chick.novel.service.NovelReptileService;
import com.chick.novel.util.GZIPUtils;
import com.chick.novel.util.QB5TWUtil;
import com.chick.util.MultiPartThreadDownLoad;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName NovelReptileServiceImpl
 * @Author xiaokexin
 * @Date 2022-07-21 16:02
 * @Description NovelReptileServiceImpl
 * @Version 1.0
 */
@Service
@Log4j2
public class NovelReptileServiceImpl implements NovelReptileService {

    @Resource
    private NovelMapper novelMapper;
    @Resource
    private NovelChapterMapper novelChapterMapper;
    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(20, 20, 600, TimeUnit.MINUTES, new ArrayBlockingQueue<>(8192), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

    public static final String disk = "E";

    @Override
    public R qb5tw(int page) {
        QB5TWEvent qb5TWEvent = new QB5TWEvent();
        int novelPage = qb5TWEvent.getNovelPage();
        NovelReptile(qb5TWEvent, page, novelPage);
        //new Thread(()->{
        //    NovelReptile(qb5TWEvent, 1, novelPage);
        //}).start();
//        for (int i = 1; i <= 3; i++) {
//            int finalI = i;
//            new Thread(()->{
//                NovelReptile(qb5TWEvent, (novelPage * (finalI - 1)/ 3 +1), (novelPage * finalI / 3));
//            }).start();
//        }
        return R.ok();
    }

    @Override
    public R getContent(String id) {
        NovelChapter novelChapter = novelChapterMapper.selectOne(Wrappers.<NovelChapter>lambdaQuery()
                .eq(NovelChapter::getId, id));
        String content = GZIPUtils.uncompressToString(novelChapter.getContent());
        content = content.replaceAll(QB5TWUtil.NEWLINE2_AND_SPACE4_REPLACE, QB5TWUtil.NEWLINE2_AND_SPACE4).replaceAll(QB5TWUtil.SPACE4_REPLACE, QB5TWUtil.SPACE4);
        return R.ok(content);
    }

    R NovelReptile(NovelReptileEvent novelReptileEvent, int pageStart, int pageEnd) {
        for (int i = pageStart; i <= pageEnd; i++) {
            // 获取本页所有小说
            List<Novel> novelList = novelReptileEvent.getNovelList(i);
            // 遍历本页小说
            for (Novel novel : novelList) {
                // 查看小说如果有就插入没有就更新
                Novel novelSelect = novelMapper.selectOne(Wrappers.<Novel>lambdaQuery()
                        .eq(Novel::getName, novel.getName())
                        .eq(Novel::getIndexUrl, novel.getIndexUrl())
                        .eq(Novel::getSource, novel.getSource()));
                // 获取小说章节
                List<NovelChapter> novelChapterList = novelReptileEvent.getNovelChapterList(novel);
                if (ObjectUtils.isNotEmpty(novelSelect)) {
                    novel.setId(novelSelect.getId());
                    novel.setImageLocalPath(novelSelect.getImageLocalPath());
                    novelMapper.updateById(novel);
                    continue;
                } else {
                    //MultiPartThreadDownLoad.OrdinaryDownLoad(novel.getImageUrl(), novel.getImageLocalPath());
                    novelMapper.insert(novel);
                }
/*                // 遍历小说章节
                for (NovelChapter novelChapter : novelChapterList) {
                    log.info("正在运行的线程有---  " + threadPool.getActiveCount() + "--- 个线程");
                    log.info("线程队列中还有---    " + threadPool.getQueue().size() + "--- 个线程");
                    if (threadPool.getQueue().size() > 4000) {
                        log.info("检测到线程等待队列中线程超过4000，暂停二十秒");
                        try {
                            Thread.sleep(20000);
                        } catch (InterruptedException e) {
                            log.error("线程暂停失败");
                        }
                    }
                    // 查看章节是否存在，不存在就插入
//                    NovelChapter novelChapterSelect = novelChapterMapper.selectOne(Wrappers.<NovelChapter>lambdaQuery()
//                            .eq(NovelChapter::getName, novelChapter.getName())
//                            .eq(NovelChapter::getNovelId, novel.getId())
//                            .eq(NovelChapter::getIndexUrl, novelChapter.getIndexUrl()));
//                    if (ObjectUtils.isNotEmpty(novelChapterSelect)){
//                        log.info("章节已存在，继续下一章" + novel.getName() + "---" + novelChapter.getName());
//                        continue;
//                    }
                    threadPool.submit(() -> {
                        log.info("开始解析章节---" + novel.getName() + "---" + novelChapter.getName());
                        novelReptileEvent.getChapterContent(novelChapter);
                        //novelChapterMapper.insertCompress(novelChapter);
                        novelChapterMapper.insert(novelChapter);
                        log.info("结束解析章节---" + novel.getName() + "---" + novelChapter.getName());
                    });
                }*/
            }
        }
        return R.ok("爬取完成");
    }

}
