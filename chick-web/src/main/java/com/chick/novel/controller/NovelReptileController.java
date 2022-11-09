package com.chick.novel.controller;

import com.chick.base.R;
import com.chick.controller.BaseController;
import com.chick.novel.service.NovelReptileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName MovelReptileController
 * @Author xiaokexin
 * @Date 2022-07-21 16:01
 * @Description MovelReptileController
 * @Version 1.0
 */
@RestController
@RequestMapping("/novelReptile")
public class NovelReptileController extends BaseController {

    @Resource
    private NovelReptileService novelReptileService;

    @GetMapping("/qb5-tw")
    public R qb5tw(int page){
        return novelReptileService.qb5tw(page);
    }

    @GetMapping("/getContent")
    public R getContent(String id){
        return novelReptileService.getContent(id);
    }
}
