package com.chick.novel.event;

import cn.hutool.http.HttpUtil;
import com.chick.novel.entity.Novel;
import com.chick.novel.entity.NovelChapter;
import com.chick.novel.service.impl.NovelReptileServiceImpl;
import com.chick.novel.util.GZIPUtils;
import com.chick.novel.util.QB5TWUtil;
import com.chick.util.SoftwareUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.chick.common.utils.ChickUtil.DoId;

/**
 * @ClassName QB5TWEvent
 * @Author xiaokexin
 * @Date 2022-07-22 10:51
 * @Description QB5TWEvent
 * @Version 1.0
 */
@Log4j2
public class QB5TWEvent implements NovelReptileEvent {

    public static final String source = "QB5TW";

    @Override
    public int getNovelPage() {
        Document parse;
        try {
            String htmlStr = HttpUtil.createGet("https://www.qb5.tw/top/allvisit/1.html").timeout(10000).execute().toString();
            parse = Jsoup.parse(htmlStr);
            return Integer.parseInt(parse.getElementById("pagestats").text().split("/")[1]);
        } catch (Exception e) {
            log.error("解析页数错误");
            return 0;
        }
    }

    @Override
    public List<Novel> getNovelList(int page) {
        Document parse;
        try {
            String htmlStr = HttpUtil.createGet("https://www.qb5.tw/top/allvisit/" + page + ".html").timeout(10000).execute().toString();
            parse = Jsoup.parse(htmlStr);
        } catch (Exception e) {
            log.error("解析页数错误");
            return new ArrayList<>();
        }
        Elements elementsLi = parse.getElementById("articlelist").getElementsByTag("ul").get(1).getElementsByTag("li");
        List<Novel> novels = new ArrayList<>();
        for (Element element : elementsLi) {
            if (elementsLi.equals(element)) {
                continue;//跳过标题
            }
            Novel novel = new Novel();
            novel.setId(DoId());
            novel.setName(element.getElementsByTag("a").get(0).text());
            novel.setIndexUrl(element.getElementsByTag("a").get(0).attr("href"));
            novel.setImageUrl(""); // 章节解析时赋值
            novel.setImageLocalPath("");
            novel.setAuthor(element.getElementsByTag("span").get(2).text());
            novel.setSource(source);
            novel.setType(element.getElementsByTag("span").get(0).text());
            novel.setWordCount(element.getElementsByTag("span").get(5).text());
            novels.add(novel);
        }
        return novels;
    }

    @Override
    public List<NovelChapter> getNovelChapterList(Novel novel) {
        Document parse;
        ArrayList<NovelChapter> novelChapters = new ArrayList<>();
        try {
            String htmlStr = HttpUtil.createGet(novel.getIndexUrl()).timeout(10000).execute().toString();
            parse = Jsoup.parse(htmlStr);
            // 设置封面地址和本地地址
            novel.setImageUrl(parse.getElementById("picbox").getElementsByTag("img").get(0).attr("src")); // 封面地址
            novel.setImageLocalPath(NovelReptileServiceImpl.disk + ":\\novel\\qb5tw\\" + SoftwareUtil.replaceAllIllegalCode(novel.getName()) + DoId().substring(1, 10) + ".jpg");
            novel.setDescribtion(parse.getElementById("intro").html().replace(" ", "").replace("&nbsp;", ""));

            Elements zjlist = parse.getElementsByClass("zjlist");
            if (ObjectUtils.isEmpty(zjlist) || zjlist.size() == 0) {
                log.error("总章节为空");
                return new ArrayList<>();
            }
            Elements dds = zjlist.get(0).getElementsByTag("dd");
            if (ObjectUtils.isEmpty(dds) || dds.size() == 0) {
                log.error("章节dd为空");
                return new ArrayList<>();
            }
            // 去除无效的章节
            Elements ddsReal = new Elements();
            for (Element element : dds) {
                Elements a = element.getElementsByTag("a");
                if (ObjectUtils.isEmpty(a) || a.size() == 0) {
                    continue;
                }
                ddsReal.add(element);
            }
            for (Element element : ddsReal) {
                NovelChapter novelChapter = new NovelChapter();
                novelChapter.setId(DoId());
                novelChapter.setNovelId(novel.getId());
                novelChapter.setSort(dds.indexOf(element));
                novelChapter.setIndexUrl(novel.getIndexUrl() + element.getElementsByTag("a").get(0).attr("href"));
                novelChapter.setName(element.getElementsByTag("a").get(0).text());
                novelChapters.add(novelChapter);
            }
        } catch (Exception e) {
            log.error("解析页数错误");
            return new ArrayList<>();
        }

        return novelChapters;
    }


    @Override
    public void getChapterContent(NovelChapter novelChapter) {
        Document parse;
        try {
            String htmlStr = HttpUtil.createGet(novelChapter.getIndexUrl()).timeout(10000).execute().toString();
            parse = Jsoup.parse(htmlStr);
        } catch (Exception e) {
            log.error("解析页数错误");
            return;
        }
        String content = parse.getElementById("content").html().split("</a>最新章节！\n" +
                "<br>\n" +
                "<br> ")[1];
        content = content.replaceAll(QB5TWUtil.NEWLINE2_AND_SPACE4, QB5TWUtil.NEWLINE2_AND_SPACE4_REPLACE).replaceAll(QB5TWUtil.SPACE4, QB5TWUtil.SPACE4_REPLACE);
        // 压缩内存为byte[]
        novelChapter.setContent(GZIPUtils.compress(content));
        novelChapter.setType("1");
    }
}
