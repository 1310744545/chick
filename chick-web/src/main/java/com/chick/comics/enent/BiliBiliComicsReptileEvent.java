package com.chick.comics.enent;

import com.chick.comics.entity.Comics;
import com.chick.comics.entity.ComicsChapter;
import com.chick.comics.entity.ComicsImage;

import java.util.List;
import java.util.Map;

/**
 * @ClassName BilibiliComicsReptileEvent
 * @Author xiaokexin
 * @Date 2022-06-27 15:09
 * @Description BilibiliComicsReptileEvent
 * @Version 1.0
 */
public class BiliBiliComicsReptileEvent implements ComicsReptileEvent{
    @Override
    public int getComicsPageTotal(String flag) {
        return 0;
    }

    @Override
    public List<Comics> getComics(String flag, int pageNum) {
        return null;
    }

    @Override
    public List<ComicsChapter> getComicsChapter(Comics comics) {
        return null;
    }

    @Override
    public List<ComicsImage> getComicsImage(ComicsChapter comicsChapter) {
        return null;
    }
}
