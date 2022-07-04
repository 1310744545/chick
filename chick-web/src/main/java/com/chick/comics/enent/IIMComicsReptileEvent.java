package com.chick.comics.enent;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.chick.comics.entity.Comics;
import com.chick.comics.entity.ComicsChapter;
import com.chick.comics.entity.ComicsImage;
import com.chick.comics.utils.IIMHComicsUtil;
import com.chick.comics.utils.TencentComicsUtil;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

import static com.chick.common.utils.ChickUtil.DoId;

/**
 * @ClassName IIMComicsReptileEvent
 * @Author xiaokexin
 * @Date 2022-07-03 21:41
 * @Description IIMComicsReptileEvent
 * @Version 1.0
 */
@Log4j2
public class IIMComicsReptileEvent implements ComicsReptileEvent {

    public static final String FILE_NAME = "IIMH";
    protected static String source = "IIMHComics";
    protected static IIMHComicsUtil iimhComicsUtil = new IIMHComicsUtil();

    public static void main(String[] args) {
        Document document = null;
        try {
            document = Jsoup.connect("http://www.iimanhua.cc/comic/993/index.html").get();
        } catch (IOException e) {
            log.error("getComicsChapter获取章节错误--->");
        }
        Comics comics = new Comics();
        // 解析漫画是不能解析的
        comics.setDescription(document.getElementById("intro1").text());
        comics.setPopularity(document.getElementsByClass("detailInfo").get(0).getElementsByTag("li").get(7).text().replace("人气：", ""));
        comics.setLabel(document.getElementsByClass("detailInfo").get(0).getElementsByTag("li").get(6).text().replace("关键词：", ""));
        comics.setCoverUrl(document.getElementsByClass("info_cover").get(0).getElementsByTag("img").get(0).attr("src"));

        ArrayList<ComicsChapter> comicsChapters = new ArrayList<>();
        Elements worksChapterItem = document.getElementsByClass("plist").get(0).getElementsByTag("li");
        for (Element element : worksChapterItem) {
            ComicsChapter comicsChapter = new ComicsChapter();
            comicsChapter.setId(DoId());
            comicsChapter.setComicsId(comics.getId());
            comicsChapter.setName(element.text());
            comicsChapter.setIndexUrl("http://www.iimanhua.cc/" + element.getElementsByTag("a").get(0).attr("href"));
            comicsChapters.add(comicsChapter);
        }
    }

    @Override
    public int getComicsPageTotal(String flag) {
        Document document = null;
        try {
            document = Jsoup.connect("http://www.iimanhua.cc/mh/" + flag).get();
        } catch (IOException e) {
            log.error("请求页面失败--> http://www.iimanhua.cc/mh/" + flag);
        }
        if (ObjectUtils.isEmpty(document)) {
            return -1;
        }
        Elements elementsByTag = document.getElementById("pagerH").getElementsByTag("strong");
        int all = Integer.parseInt(elementsByTag.get(0).text());
        int pageCount = Integer.parseInt(elementsByTag.get(1).text());
        return all / pageCount + 1;
    }

    @Override
    public List<Comics> getComics(String flag, int pageNum) {
        Document document = null;
        try {
            document = Jsoup.connect("http://www.iimanhua.cc/mh/" + flag + "/index" + (pageNum == 1 ? "" : "_" + pageNum) + ".html").get();
        } catch (IOException e) {
            log.error("请求页面失败--> http://www.iimanhua.cc/mh/" + flag + "/index" + (pageNum == 1 ? "" : "_" + pageNum) + ".html");
        }
        Elements li = document.getElementsByClass("dmList clearfix").get(0).getElementsByTag("li");
        ArrayList<Comics> comicsList = new ArrayList<>();
        for (Element element : li) {
            Element a = element.getElementsByTag("dl").get(0).getElementsByTag("a").get(0);
            Element p = element.getElementsByTag("dl").get(0).getElementsByTag("p").get(2);
            Comics comics = new Comics();
            comics.setId(DoId());
            // 名字
            comics.setName(a.text());
            comics.setDescription(""); // 此处不全，待解析篇章的时候填写
            // 作者
            comics.setAuthor(p.text().replace("作　者：", ""));
            // 人气
            comics.setPopularity(""); // 此处不全，待解析篇章的时候填写
            // 标签
            comics.setLabel(""); // 此处不全，待解析篇章的时候填写
            // 点赞数
            comics.setLikes(0);
            // 收藏数
            comics.setFavorites(0);
            // 是否展示 默认隐藏
            comics.setOpen("1");
            // 来源
            comics.setSource(source);
            // 漫画首页地址
            comics.setIndexUrl("http://www.iimanhua.cc" + a.attr("href"));
            // 封面地址
            comics.setCoverUrl(""); // 此处不全，待解析篇章的时候填写
            // 封面本地路径
            comics.setCoverLocalPath(""); // 后面填写
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
            log.error("getComicsChapter获取章节错误--->");
            return new ArrayList<>();
        }
        // 解析漫画是不能解析的
        comics.setDescription(document.getElementsByClass("introduction").get(0).text());
        comics.setPopularity(document.getElementsByClass("detailInfo").get(0).getElementsByTag("li").get(7).text().replace("人气：", ""));
        comics.setLabel(document.getElementsByClass("detailInfo").get(0).getElementsByTag("li").get(6).text().replace("关键词：", ""));
        comics.setCoverUrl(document.getElementsByClass("info_cover").get(0).getElementsByTag("img").get(0).attr("src"));

        ArrayList<ComicsChapter> comicsChapters = new ArrayList<>();
        Elements worksChapterItem = document.getElementsByClass("plist").get(0).getElementsByTag("li");
        for (Element element : worksChapterItem) {
            ComicsChapter comicsChapter = new ComicsChapter();
            comicsChapter.setId(DoId());
            comicsChapter.setComicsId(comics.getId());
            comicsChapter.setName(element.text());
            comicsChapter.setIndexUrl("http://www.iimanhua.cc" + element.getElementsByTag("a").get(0).attr("href"));
            comicsChapters.add(comicsChapter);
        }
        Collections.reverse(comicsChapters);
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
        Map<Integer, String> pics = iimhComicsUtil.getPics(document.toString());
        ArrayList<ComicsImage> comicsImages = new ArrayList<>();
        int sort = 1;
        for (Integer key : pics.keySet()) {
            ComicsImage comicsImage = new ComicsImage();
            comicsImage.setId(DoId());
            comicsImage.setComicsId(comicsChapter.getComicsId());
            comicsImage.setComicsChapterId(comicsChapter.getId());
            comicsImage.setImageUrl("https://res.img.96youhuiquan.com" + pics.get(key));
            comicsImage.setSort(key);
            sort++;
            comicsImages.add(comicsImage);
        }
        return comicsImages;
    }
}
