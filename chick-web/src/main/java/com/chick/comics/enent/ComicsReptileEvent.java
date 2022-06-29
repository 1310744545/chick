package com.chick.comics.enent;

import com.chick.comics.entity.Comics;
import com.chick.comics.entity.ComicsChapter;
import com.chick.comics.entity.ComicsImage;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ComicsReptileEvent
 * @Author xiaokexin
 * @Date 2022-06-27 14:37
 * @Description 漫画网站解析同意模板
 * @Version 1.0
 */
public interface ComicsReptileEvent {

    /**
    * @Author xkx
    * @Description 获取爬取的漫画总页数
    * @Date 2022-06-27 14:40
    * @Param []
    * @return int
    **/
    int getComicsPageTotal();

    /**
    * @Author xkx
    * @Description 获取某一页中的漫画集合
    * @Date 2022-06-27 14:40
    * @Param [pageNum]
    * @return int
    **/
    List<Comics> getComics(int pageNum);

    /**
     * @Author xkx
     * @Description 获取某一个漫画的所有篇章，直接给comics的属性赋值
     * @Date 2022-06-27 14:40
     * @Param [pageNum]
     * @return int
     **/
    List<ComicsChapter> getComicsChapter(Comics comics);

    /**
     * @Author xkx
     * @Description 获取某一个篇章的所有图片
     * @Date 2022-06-27 14:40
     * @Param [pageNum]
     * @return int
     **/
    List<ComicsImage> getComicsImage(ComicsChapter comicsChapter);
}
