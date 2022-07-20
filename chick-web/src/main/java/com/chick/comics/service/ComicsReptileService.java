package com.chick.comics.service;

import com.chick.base.R;

/**
 * @ClassName ComicsReptileService
 * @Author xiaokexin
 * @Date 2022-06-27 13:19
 * @Description ComicsReptileService
 * @Version 1.0
 */
public interface ComicsReptileService {

    R tencentComics(boolean imageScan, int pageNum);

    R IIMHComics(boolean imageScan, int pageNum, String letter);

    R IIMHComicsByIndex(boolean imageScan, int pageNum, String letter);
}
