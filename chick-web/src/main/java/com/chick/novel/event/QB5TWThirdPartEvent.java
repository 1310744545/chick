package com.chick.novel.event;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.base.R;
import com.chick.novel.entity.Novel;
import com.chick.novel.entity.NovelChapter;
import com.chick.novel.service.impl.NovelReptileServiceImpl;
import com.chick.novel.util.GZIPUtils;
import com.chick.novel.util.QB5TWUtil;
import com.chick.novel.vo.NovelContentVO;
import com.chick.novel.vo.NovelVO;
import com.chick.util.SoftwareUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.chick.common.utils.ChickUtil.DoId;

/**
 * @ClassName QB5TWThirdPartEvent
 * @Author xiaokexin
 * @Date 2022-11-09 9:55
 * @Description QB5TWThirdPartEvent
 * @Version 1.0
 */
@Log4j2
@Component
public class QB5TWThirdPartEvent implements NovelThirdPartEvent {

    public static final String source = "全本小说网";

    @Override
    public R getNovelList(Page<Novel> validPage, String type, String keyword) {
        Document parse;
        try {
            String htmlStr = HttpUtil.createGet("https://www.qb5.tw/top/allvisit/" + validPage.getCurrent() + ".html").timeout(10000).execute().toString();
            parse = Jsoup.parse(htmlStr);
        } catch (Exception e) {
            log.error("解析页数错误");
            return R.failed("系统错误");
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
            novel.setIndexUrl(getBookId(element.getElementsByTag("a").get(0).attr("href")));
            // 解析章节的封面url
            novel.setImageUrl(getCoverPath(element.getElementsByTag("a").get(0).attr("href")));
            novel.setImageLocalPath("");
            novel.setAuthor(element.getElementsByTag("span").get(2).text());
            novel.setSource(source);
            novel.setType(element.getElementsByTag("span").get(0).text());
            novel.setWordCount(element.getElementsByTag("span").get(5).text());
            novels.add(novel);
        }
        Long last = Long.parseLong(parse.getElementsByClass("last").get(0).text());
        validPage.setTotal(last * 40);
        validPage.setRecords(novels);
        validPage.setSize(novels.size());
        return R.ok(validPage);
    }

    @Override
    public R getChapterList(String bookId) {
        // 获取一部小说的章节
        Document parse;
        ArrayList<NovelChapter> novelChapters = new ArrayList<>();
        NovelVO novel = new NovelVO();
        try {
            String htmlStr = HttpUtil.createGet("https://www.qb5.tw/book_" + bookId + "/").timeout(10000).execute().toString();
            parse = Jsoup.parse(htmlStr);
            // 设置封面地址和本地地址
            novel.setImageUrl(parse.getElementById("picbox").getElementsByTag("img").get(0).attr("src")); // 封面地址
            novel.setDescribtion(parse.getElementById("intro").html().replace(" ", "").replace("&nbsp;", ""));
            // 书名
            Element bookName = parse.getElementsByTag("h1").get(0);
            novel.setName(bookName.text());
            // 作者
            novel.setAuthor(bookName.getElementsByTag("a").get(0).text());
            // 来源
            novel.setSource(source);
            Elements zjlist = parse.getElementsByClass("zjlist");
            if (ObjectUtils.isEmpty(zjlist) || zjlist.size() == 0) {
                log.error("总章节为空");
                return R.failed("系统错误");
            }
            Elements dds = zjlist.get(0).getElementsByTag("dd");
            if (ObjectUtils.isEmpty(dds) || dds.size() == 0) {
                log.error("章节dd为空");
                return R.failed("系统错误");
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
                novelChapter.setSort(dds.indexOf(element));
                novelChapter.setIndexUrl(bookId + "-" + element.getElementsByTag("a").get(0).attr("href").split("\\.")[0]);
                novelChapter.setName(element.getElementsByTag("a").get(0).text());
                novelChapters.add(novelChapter);
            }
            novel.setNovelChapters(novelChapters);
        } catch (Exception e) {
            log.error("解析页数错误");
            return R.failed("系统错误");
        }
        return R.ok(novel);
    }

    @Override
    public R getContent(String bookId, String chapterId) {
        Document parse;
        try {
            String htmlStr = HttpUtil.createGet("https://www.qb5.tw/book_" + bookId + "/" + chapterId + ".html").timeout(10000).execute().toString();
            parse = Jsoup.parse(htmlStr);
        } catch (Exception e) {
            log.error("解析页数错误");
            return R.failed("系统错误");
        }
        String content = parse.getElementById("content").html().split("</a>最新章节！\n" +
                "<br>\n" +
                "<br> ")[1];
        NovelContentVO novelContentVO = new NovelContentVO();
        novelContentVO.setContent(content);
        novelContentVO.setTitle(parse.getElementsByTag("h1").get(0).text());
        novelContentVO.setBookId(bookId);
        if (ObjectUtils.isNotEmpty(parse.getElementById("link-preview")) && parse.getElementById("link-preview").attr("href").split("book_" + bookId + "/").length > 1){
            novelContentVO.setPre(parse.getElementById("link-preview").attr("href").split("book_" + bookId + "/")[1].split("\\.")[0]);
        }
        if (ObjectUtils.isNotEmpty(parse.getElementById("link-next")) && parse.getElementById("link-next").attr("href").split("book_" + bookId + "/").length > 1){
            novelContentVO.setNext(parse.getElementById("link-next").attr("href").split("book_" + bookId + "/")[1].split("\\.")[0]);
        }
        return R.ok(novelContentVO);
    }

    private static String getCoverPath(String indexStr) {
        try {
            String coverUrl = "https://www.qb5.tw/files/article/image/";
            String bookStr = indexStr.split("book_")[1];
            String book = bookStr.split("/")[0];
            if (book.length() == 3) {
                return coverUrl + "0/" + book + "/" + book + "s.jpg";
            }
            if (book.length() == 4) {
                return coverUrl + book.charAt(0) + "/" + book + "/" + book + "s.jpg";
            }
            if (book.length() == 5) {
                return coverUrl + book.substring(0, 2) + "/" + book + "/" + book + "s.jpg";
            }
            if (book.length() == 6) {
                return coverUrl + book.substring(0, 3) + "/" + book + "/" + book + "s.jpg";
            }
        } catch (Exception e) {
            return "https://www.qb5.tw/modules/article/images/nocover.jpg";
        }
        return "https://www.qb5.tw/modules/article/images/nocover.jpg";
    }

    private static String getBookId(String indexStr) {
        try {
            String bookStr = indexStr.split("book_")[1];
            String book = bookStr.split("/")[0];
            return book;
        } catch (Exception e) {
            return "https://www.qb5.tw/modules/article/images/nocover.jpg";
        }
    }
}
