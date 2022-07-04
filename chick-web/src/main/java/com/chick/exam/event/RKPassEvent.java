package com.chick.exam.event;

import cn.hutool.http.HttpUtil;
import com.chick.exam.vo.ExamRealVO;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName RKPassEvent
 * @Author xiaokexin
 * @Date 2022-07-02 19:08
 * @Description RKPassEvent
 * @Version 1.0
 */
@Log4j2
public class RKPassEvent implements ExamReptileEvent {

    @Override
    public int getExamPageTotal(String cookie) {
        Document document = null;
        try {
            document = Jsoup.parse(HttpUtil.createGet("http://www.rkpass.cn//tk_index.jsp")
                    .header("Cookie", cookie).execute().toString());
        } catch (Exception e) {
            log.error("请求页面失败");
            return -1;
        }
        Elements texts = document.getElementsByClass("shisi_text_hui");
        String page = "";
        for (Element element : texts) {
            if (element.text().contains("/")) {
                page = element.text().replace("页", "");
            }
        }
        if (StringUtils.isNotBlank(page)) {
            return Integer.parseInt(page.split("/")[1]);
        }
        return -1;
    }

    @Override
    public List<ExamRealVO> getExamReals(int pageNum, String cookie) {
        Document document = null;
        try {
            document = Jsoup.parse(HttpUtil.createGet("http://www.rkpass.cn//tk_index.jsp?CurrentPage=" + pageNum)
                    .header("Cookie", cookie).execute().toString());
        } catch (Exception e) {
            log.error("请求考试页失败");
        }
        Elements subjects = document.getElementsByAttributeValue("color", "#000000");
        System.out.println(subjects);
        ArrayList<ExamRealVO> softRealVOS = new ArrayList<>();
        for (Element element : subjects){
            ExamRealVO softRealVO = new ExamRealVO();
            softRealVO.setUrl("http://www.rkpass.cn/" + element.getElementsByTag("a").get(0).attr("href"));
            softRealVO.setYear(element.getElementsByTag("a").get(0).text().split(" ")[0]);
            softRealVO.setName(element.getElementsByTag("a").get(0).text().split(" ")[1]);
            softRealVO.setTestPaperType(element.getElementsByTag("a").get(0).text().split(" ")[2]);
            softRealVO.setExamType(element.getElementsByTag("a").get(0).text().split(" ")[3]);
            softRealVOS.add(softRealVO);
        }
        return softRealVOS;
    }

    public static void main(String[] args) {
        Document document = null;
        try {
            document = Jsoup.parse(HttpUtil.createGet("http://www.rkpass.cn//tk_index.jsp?CurrentPage=" + 1)
                    .header("Cookie", "JSESSIONID=71D681859A35BBF7F6A21DF0CC82EEBC").execute().toString());
        } catch (Exception e) {
            log.error("请求考试页失败");
        }
        Elements subjects = document.getElementsByAttributeValue("color", "#000000");
        System.out.println(subjects);
        ArrayList<ExamRealVO> softRealVOS = new ArrayList<>();
        for (Element element : subjects){
            ExamRealVO softRealVO = new ExamRealVO();
            softRealVO.setUrl("http://www.rkpass.cn/" + element.getElementsByTag("a").get(0).attr("href"));
            softRealVO.setYear(element.getElementsByTag("a").get(0).text().split(" ")[0]);
            softRealVO.setName(element.getElementsByTag("a").get(0).text());
            softRealVOS.add(softRealVO);
        }


    }
}
