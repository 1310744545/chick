package com.chick.comics.enent;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.chick.comics.entity.Comics;
import com.chick.comics.entity.ComicsChapter;
import com.chick.comics.entity.ComicsImage;
import com.chick.comics.utils.IIMHComicsUtil;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
    public static String source = "IIMHComics";
    protected static IIMHComicsUtil iimhComicsUtil = new IIMHComicsUtil();

    @Override
    public int getComicsPageTotal(String flag) {
        Document document = null;
        String html;
        try {
            html = HttpUtil.get("http://www.iimh.net/mh/" + flag + "/", 60000);
            document = Jsoup.parse(html);
            //document = Jsoup.connect("http://www.iimh.cc/mh/" + flag).get();
        } catch (Exception e) {
            log.error("请求页面失败--> http://www.iimh.net/mh/" + flag);
            return 0;
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
        String html;
        try {
            html = HttpUtil.get("http://www.iimh.net/mh/" + flag + "/index" + (pageNum == 1 ? "" : "_" + pageNum) + ".html", 60000);
            document = Jsoup.parse(html);
            //document = Jsoup.connect("http://www.iimanhua.cc/mh/" + flag + "/index" + (pageNum == 1 ? "" : "_" + pageNum) + ".html").get();
        } catch (Exception e) {
            log.error("请求页面失败--> http://www.iimh.net/mh/" + flag + "/index" + (pageNum == 1 ? "" : "_" + pageNum) + ".html");
            return new ArrayList<>();
        }
        Elements li = document.getElementsByClass("dmList clearfix").get(0).getElementsByTag("li");
        ArrayList<Comics> comicsList = new ArrayList<>();
        for (Element element : li) {
            if (ObjectUtils.isEmpty(element.getElementsByTag("dl")) || element.getElementsByTag("dl").size() == 0){
                continue;
            }
            if (ObjectUtils.isEmpty(element.getElementsByTag("dl").get(0).getElementsByTag("a")) || element.getElementsByTag("dl").get(0).getElementsByTag("a").size() == 0){
                continue;
            }
            if (ObjectUtils.isEmpty(element.getElementsByTag("dl").get(0).getElementsByTag("p")) || element.getElementsByTag("dl").get(0).getElementsByTag("p").size() < 2){
                continue;
            }
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
            comics.setIndexUrl("http://www.iimh.net" + a.attr("href"));
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
        String html;
        try {
            html = HttpUtil.get(comics.getIndexUrl(), 60000);
            document = Jsoup.parse(html);
        } catch (Exception e) {
            log.error("getComicsChapter获取章节错误--->" + comics.getName());
            return new ArrayList<>();
        }
        // 简介不为空
        if (ObjectUtils.isNotEmpty(document.getElementById("intro"))){
            comics.setDescription(document.getElementById("intro").text());
        }
//        comics.setPopularity(document.getElementsByClass("detailInfo").get(0).getElementsByTag("li").get(7).text().replace("人气：", ""));
//        comics.setLabel(document.getElementsByClass("detailInfo").get(0).getElementsByTag("li").get(6).text().replace("关键词：", ""));
        if (ObjectUtils.isNotEmpty(document.getElementsByClass("info_cover")) && document.getElementsByClass("info_cover").size() > 0){
            Elements img = document.getElementsByClass("info_cover").get(0).getElementsByTag("img");
            if (ObjectUtils.isNotEmpty(img) && img.size() > 0){
                comics.setCoverUrl(document.getElementsByClass("info_cover").get(0).getElementsByTag("img").get(0).attr("src"));
            }
        }
        ArrayList<ComicsChapter> comicsChapters = new ArrayList<>();
        if (ObjectUtils.isEmpty(document.getElementsByClass("plist")) || document.getElementsByClass("plist").size() == 0){
            log.error("章节列表为空");
            return new ArrayList<>();
        }
        Elements worksChapterItem = document.getElementsByClass("plist").get(0).getElementsByTag("li");
        if (ObjectUtils.isEmpty(worksChapterItem) || worksChapterItem.size() == 0){
            log.error("worksChapterItem列表为空");
            return new ArrayList<>();
        }
        Collections.reverse(worksChapterItem);
        for (Element element : worksChapterItem) {
            ComicsChapter comicsChapter = new ComicsChapter();
            comicsChapter.setId(DoId());
            comicsChapter.setComicsId(comics.getId());
            comicsChapter.setName(element.text());
            comicsChapter.setSort(worksChapterItem.indexOf(element) + 1);
            comicsChapter.setIndexUrl("http://www.iimh.net" + element.getElementsByTag("a").get(0).attr("href"));
            comicsChapters.add(comicsChapter);
        }
        Collections.reverse(comicsChapters);
        return comicsChapters;
    }

    @Override
    public List<ComicsImage> getComicsImage(ComicsChapter comicsChapter) {
        String html;
        try {
            html = HttpUtil.get(comicsChapter.getIndexUrl(), 60000);
        } catch (Exception e) {
            log.error("请求图片出错--->" + comicsChapter.getName());
            return new ArrayList<>();
        }
        Map<Integer, String> pics = iimhComicsUtil.getPics(html);
        ArrayList<ComicsImage> comicsImages = new ArrayList<>();
        int sort = 1;
        for (Integer key : pics.keySet()) {
            ComicsImage comicsImage = new ComicsImage();
            comicsImage.setId(DoId());
            comicsImage.setComicsId(comicsChapter.getComicsId());
            comicsImage.setComicsChapterId(comicsChapter.getId());
            comicsImage.setImageUrl("https://res.img.96youhuiquan.com/" + pics.get(key));
            comicsImage.setSort(key);
            sort++;
            comicsImages.add(comicsImage);
        }
        return comicsImages;
    }
}
