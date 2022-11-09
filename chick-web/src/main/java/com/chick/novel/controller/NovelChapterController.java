package com.chick.novel.controller;


import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.controller.BaseController;
import com.chick.novel.entity.NovelChapter;
import com.chick.novel.service.NovelChapterService;
import com.chick.novel.service.NovelService;
import com.chick.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 小说章节 前端控制器
 * </p>
 *
 * @author xiaokexin
 * @since 2022-07-11
 */
@RestController
@RequestMapping("/novelChapter")
public class NovelChapterController extends BaseController {

    @Autowired
    private NovelChapterService novelChapterService;
    @GetMapping("")
    public R get(String novelId, String keyword, Integer current, Integer size){
        if (StringUtils.isBlank(novelId)) {
            return R.failed("小说id为空");
        }
        if (StringUtils.isNotBlank(keyword) && keyword.length() > CommonConstants.MAX_NAME_LENGTH) {
            return R.failed("关键字过长");
        }
        return novelChapterService.get(PageUtils.validPage(current, size), novelId, keyword);
    }

    @GetMapping("/getThirdPartChapter")
    public R getThirdPartChapter(String bookId){
        if (StringUtils.isBlank(bookId)) {
            return R.failed("小说id为空");
        }
        return novelChapterService.getThirdPartChapter(bookId);
    }

    @GetMapping("/getContent")
    public R getContent(String novelChapterId){
        if (StringUtils.isBlank(novelChapterId)) {
            return R.failed("小说id为空");
        }
        return novelChapterService.getContent(novelChapterId);
    }

    @GetMapping("/getThirdPartContent")
    public R getThirdPartContent(String bookId, String chapterId){
        if (StringUtils.isBlank(bookId)) {
            return R.failed("小说id为空");
        }
        if (StringUtils.isBlank(chapterId)) {
            return R.failed("章节id为空");
        }
        return novelChapterService.getThirdPartContent(bookId, chapterId);
    }
}

