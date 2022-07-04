package com.chick.comics.enent;

import com.chick.comics.entity.Comics;
import com.chick.comics.entity.ComicsChapter;
import com.chick.comics.entity.ComicsImage;
import com.chick.comics.utils.TencentComicsUtil;
import com.chick.common.utils.RedisUtil;
import com.chick.util.MultiPartThreadDownLoad;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static com.chick.common.utils.ChickUtil.DoId;

/**
 * @ClassName TencentComicsReptileEvent
 * @Author xiaokexin
 * @Date 2022-06-27 14:55
 * @Description TencentComicsReptileEvent
 * @Version 1.0
 */
@Log4j2
public class TencentComicsReptileEvent implements ComicsReptileEvent {

    protected static String source = "TencentComics";
    protected static TencentComicsUtil tencentComicsUtil = new TencentComicsUtil();
    private RedisUtil redisUtil;

    @Override
    public int getComicsPageTotal(String flag) {
        Document document = null;
        try {
            document = Jsoup.connect("https://ac.qq.com/Comic/index").get();
        } catch (IOException e) {
            log.error("请求页面失败--> https://ac.qq.com/Comic/index");
            return 0;
        }
        // 获取总数
        int total = Integer.parseInt(document.getElementsByClass("ret-result-num").get(0).getElementsByTag("em").get(0).text());

        Elements elementsByClass = document.getElementsByClass("ret-search-item clearfix");
        // 获取每页个数
        int size = elementsByClass.size();
        // 总页数
        return (total / size + 1);
    }

    @Override
    public List<Comics> getComics(String flag, int pageNum) {
        Document document = null;
        try {
            document = Jsoup.connect("https://ac.qq.com/Comic/all/page/" + pageNum).get();
        } catch (IOException e) {
            log.error("腾讯漫画解析失败--->第" + pageNum + "页" + e);
            return new ArrayList<>();
        }
        Elements retSearchItem = document.getElementsByClass("ret-search-item clearfix");
        ArrayList<Comics> comicsList = new ArrayList<>();
        for (Element element : retSearchItem) {
            Comics comics = new Comics();
            comics.setId(DoId());
            // 名字
            Element modCoverListThumb = element.getElementsByClass("mod-cover-list-thumb").get(0);
            comics.setName(modCoverListThumb.attr("title"));
            comics.setDescription(""); // 此处不全，待解析篇章的时候填写
            // 作者
            comics.setAuthor(element.getElementsByClass("ret-works-author").get(0).text());
            // 人气
            comics.setPopularity(element.getElementsByClass("ret-works-tags").get(0).getElementsByTag("em").get(0).text());
            // 标签
            Elements spanElements = element.getElementsByClass("ret-works-tags").get(0).getElementsByTag("span");
            StringJoiner stringJoiner = new StringJoiner(",");
            for (Element elementSpan : spanElements) {
                if (elementSpan.hasAttr("target")) {
                    stringJoiner.add(elementSpan.text());
                }
            }
            comics.setLabel(stringJoiner.toString());
            // 点赞数
            comics.setLikes(0);
            // 收藏数
            comics.setFavorites(0);
            // 是否展示 默认隐藏
            comics.setOpen("1");
            // 来源
            comics.setSource(source);
            // 漫画首页地址
            comics.setIndexUrl("https://ac.qq.com" + modCoverListThumb.attr("href"));
            // 封面地址
            comics.setCoverUrl(modCoverListThumb.getElementsByTag("img").attr("data-original"));
            // 封面本地路径
            comics.setCoverLocalPath("");

            comicsList.add(comics);
        }
        return comicsList;
    }

    @Override
    public List<ComicsChapter> getComicsChapter(Comics comics) {
        Document document = null;
        try {
            document = Jsoup.connect(comics.getIndexUrl()).get();
        } catch (IOException e) {
            log.error("getComicsChapter获取章节错误--->" + comics.getName());
            return new ArrayList<>();
        }
        // 解析漫画是不能解析的
        comics.setDescription(document.getElementsByClass("works-intro-short").get(0).text());

        ArrayList<ComicsChapter> comicsChapters = new ArrayList<>();
        Elements worksChapterItem = document.getElementsByClass("works-chapter-item");
        for (Element element : worksChapterItem) {
            ComicsChapter comicsChapter = new ComicsChapter();
            comicsChapter.setId(DoId());
            comicsChapter.setComicsId(comics.getId());
            comicsChapter.setName(element.text());
            comicsChapter.setIndexUrl("https://ac.qq.com" + element.getElementsByTag("a").get(0).attr("href"));
            comicsChapters.add(comicsChapter);
        }
        return comicsChapters;
    }

    @Override
    public List<ComicsImage> getComicsImage(ComicsChapter comicsChapter) {
        Document document;
        try {
            document = Jsoup.connect(comicsChapter.getIndexUrl()).get();
        } catch (IOException e) {
            log.error("请求图片出错--->" + comicsChapter.getName());
            return new ArrayList<>();
        }
        List<String> pics = tencentComicsUtil.getPics(document.toString());
        ArrayList<ComicsImage> comicsImages = new ArrayList<>();
        int sort = 1;
        for (String url : pics) {
            ComicsImage comicsImage = new ComicsImage();
            comicsImage.setId(DoId());
            comicsImage.setComicsId(comicsChapter.getComicsId());
            comicsImage.setComicsChapterId(comicsChapter.getId());
            comicsImage.setImageUrl(url);
            comicsImage.setSort(sort);
            sort++;
            comicsImages.add(comicsImage);
        }
        return comicsImages;
    }
}
