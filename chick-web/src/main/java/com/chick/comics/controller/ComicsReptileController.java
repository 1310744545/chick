package com.chick.comics.controller;

import com.chick.base.R;
import com.chick.comics.service.ComicsReptileService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @ClassName getController
 * @Author xiaokexin
 * @Date 2022-06-26 11:17
 * @Description getController
 * @Version 1.0
 */
@Log4j2
@RestController
@RequestMapping("/comics")
public class ComicsReptileController {

    @Resource
    private ComicsReptileService comicsReptileService;

    @GetMapping("/tencentComics")
    public R TencentComics(boolean imageScan) {
        return comicsReptileService.tencentComics(imageScan);
    }

    @GetMapping("/IIMHComics")
    public R IIMHComics(boolean imageScan) {
        return comicsReptileService.IIMHComics(imageScan);
    }
}
