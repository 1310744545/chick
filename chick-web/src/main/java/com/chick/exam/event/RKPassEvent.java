package com.chick.exam.event;

import cn.hutool.http.HttpUtil;
import com.chick.exam.entity.ExamAnswer;
import com.chick.exam.entity.ExamFile;
import com.chick.exam.entity.ExamKnowledge;
import com.chick.exam.entity.ExamQuestion;
import com.chick.exam.utils.SoftwareExamUtil;
import com.chick.exam.vo.ExamQuestionDetailVO;
import com.chick.exam.vo.ExamQuestionVO;
import com.chick.exam.vo.ExamRealVO;
import com.sun.org.apache.xerces.internal.xs.StringList;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

import static com.chick.common.utils.ChickUtil.DoId;

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
        if (document == null) {
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
            document = Jsoup.parse(HttpUtil.createGet("http://www.rkpass.cn//tk_index.jsp?CurrentPage=" + pageNum).timeout(9999999)
                    .header("Cookie", cookie).execute().toString());
        } catch (Exception e) {
            log.error("请求考试页失败");
        }
        if (document == null) {
            return new ArrayList<>();
        }
        Elements subjects = document.getElementsByAttributeValue("color", "#000000");
        ArrayList<ExamRealVO> softRealVOS = new ArrayList<>();
        for (Element element : subjects) {
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

    @Override
    public List<ExamQuestionVO> getExamQuestions(String url, String cookie) {
        Document document = null;
        try {
            document = Jsoup.parse(HttpUtil.createGet(url).timeout(9999999)
                    .header("Cookie", cookie).execute().toString());
        } catch (Exception e) {
            log.error("请求试题集合页失败");
            return new ArrayList<>();
        }
        if (document == null) {
            return new ArrayList<>();
        }
        Elements div = document.getElementsByTag("div");
        ArrayList<ExamQuestionVO> examQuestionVOS = new ArrayList<>();
        int flag = 1;
        for (Element element : div) {
            if (element.hasAttr("id") && element.hasAttr("style") && "height:25px;line-height:25px;".equals(element.attr("style"))) {
                ExamQuestionVO examQuestionVO = new ExamQuestionVO();
                examQuestionVO.setName(element.getElementsByTag("a").text());
                examQuestionVO.setUrl(element.getElementsByTag("a").attr("href"));
                examQuestionVO.setSort(flag + "");
                flag++;
                examQuestionVOS.add(examQuestionVO);
            }
        }
        return examQuestionVOS;
    }

    @Override
    public ExamQuestionDetailVO getExamQuestionDetail(String url, String cookie) {
        Document document = null;
        try {
            document = Jsoup.parse(HttpUtil.createGet(url).timeout(99999999)
                    .header("Cookie", cookie.split("_")[0]).execute().toString());
        } catch (Exception e) {
            log.error("请求试题页失败");
            return null;
        }
        // 返回对象，包含所有信息
        ExamQuestionDetailVO examQuestionDetailVO = new ExamQuestionDetailVO();
        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.setId(DoId());
        ArrayList<ExamFile> examFiles = new ArrayList<>();
        // 问题
        Element questionElement = document.getElementsByAttributeValue("background", "../image/logo_rkpass_mini_bg.jpg").get(0);
        Elements questionElements = questionElement.getElementsByClass("shiwu_text_v2");

        // 解析
        Elements scripts = document.getElementsByTag("script");
        String productId = "";
        for (Element element : scripts) {
            if (element.html().contains("show_analysis_user_answer")) {
                productId = element.html().split("'")[1];
            }
        }
        String[] split = url.split("/");
        String s1 = split[split.length - 1];
        String tixing = s1.split("_")[3].replace(".html","");
        String paperId = s1.split("_")[1];
        String tihao = s1.split("_")[2];
        String parseHtml = HttpUtil.get("http://www.rkpass.cn//tk_jiexi.jsp?product_id=" + productId + "&tixing=" + tixing + "&answer=A&paper_id=" + paperId + "&tihao=" + tihao + "&usertklevel=tkmianfei&cache=false", 999999);
        Document parseDocument = Jsoup.parse(parseHtml);
        Elements parses = parseDocument.getElementsByClass("shiwu_text_v2");
        Elements parseAnswerHtml = parseDocument.getElementsByClass("shisi_text");
        String parse = "";
        for (Element element : parses){
            parse = parse + element.html() + "</br>";
        }
        if (StringUtils.isBlank(parse)){
            parse = "暂无";
        }
        String parseAnswer = parseAnswerHtml.text().replace("答案： ","");
        // 设置解析
        examQuestion.setParse(parse);


        StringJoiner questionHtmlJoiner = new StringJoiner("\n");
        for (Element element : questionElements) {
            if (ObjectUtils.isNotEmpty(element.previousElementSibling()) && element.previousElementSibling().hasAttr("name")) {
                continue;
            }
            questionHtmlJoiner.add(element.html());
        }
        // 问题html
        String questionHtml = questionHtmlJoiner.toString();

        // 判断是否是一个题多个空
        if (SoftwareExamUtil.SOFTWARE_EXAM_COMPREHENSIVE.equals(cookie.split("_")[2])) {
//            ArrayList<String> stringArrayList = new ArrayList<>();
//            if (StringUtils.substringsBetween(questionHtml, "（", "）") != null) {
//                stringArrayList.addAll(Arrays.asList(StringUtils.substringsBetween(questionHtml, "（", "）")));
//            }
//            if (StringUtils.substringsBetween(questionHtml, "(", "）") != null) {
//                stringArrayList.addAll(Arrays.asList(StringUtils.substringsBetween(questionHtml, "(", "）")));
//            }
//            if (StringUtils.substringsBetween(questionHtml, "（", ")") != null) {
//                stringArrayList.addAll(Arrays.asList(StringUtils.substringsBetween(questionHtml, "（", ")")));
//            }
//            if (StringUtils.substringsBetween(questionHtml, "(", ")") != null) {
//                stringArrayList.addAll(Arrays.asList(StringUtils.substringsBetween(questionHtml, "(", ")")));
//            }

            int flag = 0;
//            StringJoiner questionNum = new StringJoiner("~");
//            ArrayList<String> questionList = new ArrayList<>();
//            String endStr = "";
//            if (ObjectUtils.isNotEmpty(stringArrayList) && stringArrayList.size() > 0) {
//                for (String s : stringArrayList) {
//                    try {
//                        int i = Integer.parseInt(s);
//                        questionList.add(i + "");
//                        if (flag == 0) {
//                            questionNum.add(s);
//                        } else {
//                            endStr = s;
//                        }
//                        flag++;
//                    } catch (Exception e) {
//                    }
//                }
//            }
//            if (flag > 1) {
                //questionNum.add(endStr);
                //examQuestionDetailVO.setQuestionNum(questionNum.toString());
                //examQuestion.setTakeUp(flag);
//            } else {
                examQuestionDetailVO.setQuestionNum(document.getElementsByClass("word_title_v2").get(0).text().replace("第", "").replace("题", ""));
                examQuestion.setTakeUp(1);
//            }
            examQuestion.setType("QUESTION_01");

            // 答案
            Elements elementsByAttributeValue = document.child(0).getElementsByAttributeValue("name", "answer_option_user");
            int sort = 1;
            ArrayList<ExamAnswer> examAnswers = new ArrayList<>();
            for (Element element : elementsByAttributeValue) {
                ExamAnswer examAnswer = new ExamAnswer();
                examAnswer.setId(DoId());
                // 替换答案中的图片
                Elements as3 = element.nextElementSibling().getElementsByTag("a");
                String answerStr = element.nextElementSibling().html().replace("&nbsp;", "");
                for (Element a : as3) {
                    Elements imgs = a.getElementsByTag("img");
                    if (ObjectUtils.isEmpty(imgs)) {
                        // 可能包含连接文字，替换掉
                        answerStr = answerStr.replace(a.toString(), a.text());
                        continue;
                    }
                    Element img = imgs.get(0);
                    ExamFile examFile = new ExamFile();
                    examFile.setId(DoId());
                    examFile.setOtherId(examAnswer.getId());
                    examFile.setType("2");
                    examFile.setOtherUrl(img.attr("src"));
                    String doId = DoId();
                    examFile.setLocalPath("E:\\exam\\software\\" + cookie.split("_")[1] + "\\" + cookie.split("_")[2] + "\\" + cookie.split("_")[3] + "\\" + doId + ".jpg");
                    examFile.setLocalUrl("http://124.221.239.221/file/exam/software/" + cookie.split("_")[1] + "/" + cookie.split("_")[2] + "/" + cookie.split("_")[3] + "/" + doId + ".jpg");
                    String replaceImg = img.toString().replace(examFile.getOtherUrl(), examFile.getLocalUrl());
                    answerStr = answerStr.replace(a.toString(), replaceImg);
                    examFiles.add(examFile);
                }

                examAnswer.setName(answerStr);
                examAnswer.setSort(sort + "");
                examAnswer.setCorrect(1); // 先设置为错误答案
                //Collections.sort(questionList);
                //int i = questionList.indexOf(document.getElementsByClass("word_title_v2").get(0).text().replace("第", "").replace("题", ""));
                //examAnswer.setTakeUpSort(i == -1 ? 0 : (i + 1)); //  先设置只有是的一个空
                examAnswers.add(examAnswer);
                sort++;
            }
            // 判断正确答案是哪个
            for (ExamAnswer examAnswer : examAnswers){
                if (examAnswer.getName().startsWith(parseAnswer)){
                    // 设置为正确答案
                    examAnswer.setCorrect(0);
                }
            }
            examQuestionDetailVO.setExamAnswers(examAnswers);

            // 设置 题目排序
            examQuestionDetailVO.setSort(document.getElementsByClass("word_title_v2").get(0).text().replace("第", "").replace("题", ""));
        }


        // 处理问题的图片
        Elements as = questionElement.getElementsByTag("a");
        for (Element a : as) {
            Elements imgs = a.getElementsByTag("img");
            if (ObjectUtils.isEmpty(imgs)) {
                // 可能包含连接文字，替换掉
                questionHtml = questionHtml.replace(a.toString(), a.text());
                continue;
            }
            Element img = imgs.get(0);
            ExamFile examFile = new ExamFile();
            examFile.setId(DoId());
            examFile.setOtherId(examQuestion.getId());
            examFile.setOtherUrl(img.attr("src"));
            examFile.setType("1");
            examFile.setLocalPath("E:\\exam\\software\\" + cookie.split("_")[1] + "\\" + cookie.split("_")[2] + "\\" + cookie.split("_")[3] + "\\" + DoId() + ".jpg");
            examFile.setLocalUrl("http://124.221.239.221/file/exam/software/" + cookie.split("_")[1] + "/" + cookie.split("_")[2] + "/" + cookie.split("_")[3] + DoId() + ".jpg");
            String replaceImg = img.toString().replace(examFile.getOtherUrl(), examFile.getLocalUrl());
            questionHtml = questionHtml.replace(a.toString(), replaceImg);
            examFiles.add(examFile);
        }

        // 问题赋值
        examQuestion.setName(questionHtml);

        // 案例
        if (SoftwareExamUtil.SOFTWARE_EXAM_CASE.equals(cookie.split("_")[2])) {
            examQuestionDetailVO.setQuestionNum(document.getElementsByClass("word_title_v2").get(0).text().replace("第", "").replace("题", ""));
            examQuestion.setType("QUESTION_05");
        }
        // 论文
        if (SoftwareExamUtil.SOFTWARE_EXAM_DISSERTATION.equals(cookie.split("_")[2])) {
            examQuestionDetailVO.setQuestionNum(document.getElementsByClass("word_title_v2").get(0).text().replace("第", "").replace("题", ""));
            examQuestion.setType("QUESTION_06");
        }
        examQuestionDetailVO.setExamQuestion(examQuestion);

        // 知识点
        ArrayList<ExamKnowledge> examKnowledges = new ArrayList<>();
        Elements knowledges = document.getElementsByClass("search_kemu");
        for (Element element : knowledges) {
            ExamKnowledge examKnowledge = new ExamKnowledge();
            examKnowledge.setId(DoId());
            Elements trs = element.getElementsByTag("tr");
            StringBuilder knowledge = new StringBuilder();
            int flag = 0;
            for (Element elementTr : trs) {
                if (flag % 3 == 0 && flag != 3) {
                    if (flag == 0) {
                        examKnowledge.setName(elementTr.text());
                    } else {
                        Elements shiwu_text_v2 = elementTr.getElementsByClass("shiwu_text_v2");
                        if (ObjectUtils.isEmpty(shiwu_text_v2) || shiwu_text_v2.size() == 0){
                            continue;
                        }
                        Elements as2 = elementTr.getElementsByClass("shiwu_text_v2").get(0).getElementsByTag("a");
                        String knowledgeStr = elementTr.getElementsByClass("shiwu_text_v2").get(0).html().replace("&nbsp;", "");
                        for (Element a : as2) {
                            Elements imgs = a.getElementsByTag("img");
                            if (ObjectUtils.isEmpty(imgs)) {
                                // 可能包含连接文字，替换掉
                                knowledgeStr = knowledgeStr.replace(a.toString(), a.text());
                                continue;
                            }
                            Element img = imgs.get(0);
                            ExamFile examFile = new ExamFile();
                            examFile.setId(DoId());
                            examFile.setOtherId(examKnowledge.getId());
                            examFile.setType("3");
                            examFile.setOtherUrl(img.attr("src"));
                            examFile.setLocalPath("E:\\exam\\software\\" + cookie.split("_")[1] + "\\" + cookie.split("_")[2] + "\\" + cookie.split("_")[3] + "\\" + DoId() + ".jpg");
                            examFile.setLocalUrl("http://124.221.239.221/file/exam/software/" + cookie.split("_")[1] + "/" + cookie.split("_")[2] + "/" + cookie.split("_")[3] + DoId() + ".jpg");
                            String replaceImg = img.toString().replace(examFile.getOtherUrl(), examFile.getLocalUrl());
                            knowledgeStr = knowledgeStr.replace(a.toString(), replaceImg);
                            examFiles.add(examFile);
                        }
                        knowledge.append(knowledgeStr + "</br>");
                    }
                }
                flag++;
            }
            examKnowledge.setKnowledge(knowledge.toString());
            examKnowledges.add(examKnowledge);
        }

        // 问题类型
        Elements questionType = document.getElementsByClass("red-v2");
        if (ObjectUtils.isNotEmpty(questionType) && questionType.size() > 0) {
            examQuestionDetailVO.setQuestionType(questionType.get(0).text());
        } else {
            examQuestionDetailVO.setQuestionType("无");
        }
        examQuestionDetailVO.setExamFiles(examFiles);
        examQuestionDetailVO.setExamKnowledges(examKnowledges);
        return examQuestionDetailVO;
    }
}

