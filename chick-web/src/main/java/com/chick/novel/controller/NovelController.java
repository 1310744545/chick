package com.chick.novel.controller;


import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.controller.BaseController;
import com.chick.novel.service.NovelService;
import com.chick.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 小说 前端控制器
 * </p>
 *
 * @author xiaokexin
 * @since 2022-07-11
 */
@RestController
@RequestMapping("/novel")
public class NovelController extends BaseController {

    @Autowired
    private NovelService novelService;

    @GetMapping("")
    public R get(String type, String keyword, Integer current, Integer size) {
        if (StringUtils.isNotBlank(keyword) && keyword.length() > CommonConstants.MAX_NAME_LENGTH) {
            return R.failed("关键字过长");
        }
        return novelService.get(PageUtils.validPage(current, size), type, keyword);
    }

    @GetMapping("/thirdPart")
    public R getThirdPart(String type, String keyword, String thirdPartType, Integer current, Integer size) {
        if (StringUtils.isNotBlank(keyword) && keyword.length() > CommonConstants.MAX_NAME_LENGTH) {
            return R.failed("关键字过长");
        }
        return novelService.getThirdPart(PageUtils.validPage(current, size), thirdPartType, type, keyword);
    }
}

