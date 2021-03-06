package com.chick.util;

import com.alibaba.fastjson.JSONObject;
import com.chick.base.CommonConstants;
import com.chick.base.ConfigConstant;
import com.chick.common.utils.ChickUtil;
import com.chick.common.utils.RedisUtil;
import com.chick.software.entity.Software;
import com.chick.software.entity.SoftwareDetail;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;


/**
 * @ClassName GitHubParse
 * @Author xiaokexin
 * @Date 2022/3/7 8:56
 * @Description GitHubParse
 * @Version 1.0
 */
@Slf4j
@Component
public class GitHubParse implements Callable<Document> {

    private String url;
    private int numberOfRequests;
    private CountDownLatch countDownLatch;

    public GitHubParse() {
    }

    public GitHubParse(String url, int numberOfRequests, CountDownLatch countDownLatch) {
        this.url = url;
        this.numberOfRequests = numberOfRequests;
        this.countDownLatch = countDownLatch;
    }

    @Autowired
    private RedisUtil redisUtil;

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // 月日年

    public Software parseHtmlByUrl(String url, String softwareName) {
        //创建线程池 规范：参数自定义，便于其他同学阅读，拒绝策略默认
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(12, 24, 60, TimeUnit.MINUTES, new ArrayBlockingQueue<>(128), Executors.defaultThreadFactory());
        //请求软件基本信息
        Software software = new Software();
        software.setId(ChickUtil.DoId());
        software.setSoftwareName(softwareName);
        software.setCreateDate(new Date());
        software.setDelFlag("0");
        //请求下载信息
        url = url + "/releases";
        //去请求文件页面
        Document doc = getDocumentByUrl(url + "?page=1", 1);
        //下载页面共多少页
        Element pagination = doc.getElementsByClass("pagination").first();
        Integer pageCount = 0;
        Elements a = pagination.getElementsByTag("a");
        for (Element element : a) {
            if (!element.text().equals("Next") && !element.text().equals("Previous")) {
                Integer page = Integer.valueOf(element.text());
                if (pageCount < page) {
                    pageCount = page;
                }
            }
        }
        System.out.println("总页数" + pageCount);
        //线程计数器
        CountDownLatch countDownLatch = new CountDownLatch(pageCount - 1);
        //用于保存所有的页面请求结果
        List<Document> documents = new ArrayList<>();
        //放入第一页
        documents.add(doc);
        ArrayList<Future<Document>> resultDoc = new ArrayList<>();
        for (int i = 2; i <= pageCount; i++) {
            Future<Document> docResult = threadPoolExecutor.submit(new GitHubParse(url + "?page=" + i, 1, countDownLatch));
            resultDoc.add(docResult);
        }
        try {
            countDownLatch.await();
            threadPoolExecutor.shutdown();
            for (Future<Document> future : resultDoc) {
                Document document = future.get();
                documents.add(document);
            }
        } catch (Exception e) {
            log.error("线程等待或关闭线程池出错！！！！！" + e.getMessage());
        }
        //解析
        List<SoftwareDetail> softwareDetails = new ArrayList<>();
        for (Document document : documents) {
            Elements elementsItems = document.getElementsByClass("d-flex flex-column flex-md-row my-5 flex-justify-center");
            softwareDetails.addAll(getSoftwareByElement(elementsItems, software.getId(), softwareName));
        }
        software.setSoftwareDetails(softwareDetails);
        return software;
    }

    //请求页面方法，请求失败后重复请求，知道成功为止
    public Document getDocumentByUrl(String url, int numberOfRequests) {
        Document doc = null;
        try {
            while (ObjectUtils.isEmpty(doc)) {
                try {   //此处加一个try-catch是因为如果出错可能是异常，无法在进行循环，防止进入递归
                    doc = Jsoup.connect(url).get();
                } catch (Exception e) {
                    log.error("请求" + url + "失败，尝试再次请求，第" + numberOfRequests++ + "次");
                }
            }
            return doc;
        } catch (Exception e) {
            log.error("请求" + url + "失败，尝试再次请求，第" + numberOfRequests++ + "次");
            return getDocumentByUrl(url, numberOfRequests);
        }
    }

    //解析页面获取下载链接等等
    public List<SoftwareDetail> getSoftwareByElement(Elements elementsItems, String softwareId, String softwareName) {
        List<SoftwareDetail> softwareList = new ArrayList<>();
        for (Element elementsItem : elementsItems) {
            //不含有Box-body或者Box-footer的跳过
            if (ObjectUtils.isEmpty(elementsItem.getElementsByClass("Box-body")) || ObjectUtils.isEmpty(elementsItem.getElementsByClass("Box-footer"))) {
                continue;
            }
            //获取版本日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // 月日年
            Elements elementsByTag = elementsItem.getElementsByTag("local-time");
            String datetimeParam;
            Date datetime = null;
            if (elementsByTag.size() > 0) {
                datetimeParam = elementsItem.getElementsByTag("local-time").get(0).attr("datetime");
                try {
                    datetime = sdf.parse(datetimeParam.replace("T", " ").replace("Z", " "));
                } catch (ParseException e) {
                    log.error("日期格式化失败，失败原因---->" + e.getMessage());
                }
            }
            //获取版本
            Elements elementsByClass = elementsItem.getElementsByClass("mr-3 mr-md-0 d-flex");
            String version = elementsByClass.get(0).getElementsByTag("span").text();
            Elements elementsBoxRows = elementsItem.getElementsByClass("Box-row");
            for (Element elementBoxRow : elementsBoxRows) {
                SoftwareDetail softwareDetail = new SoftwareDetail(ChickUtil.DoId(), softwareId);
                Elements downloadTagA = elementBoxRow.getElementsByTag("a");
                String href = downloadTagA.attr("href");
                //软件原名
                softwareDetail.setFileOriginalName(CommonConstants.SOURCE_CODE.equals(downloadTagA.text()) ? downloadTagA.text() + SoftwareUtil.getSoftwareNameByUrl(href) : SoftwareUtil.getSoftwareNameByUrl(href));
                //软件名称 (如果是源码，就变成源码+版本)
                softwareDetail.setFileName(CommonConstants.SOURCE_CODE.equals(downloadTagA.text()) ? downloadTagA.text() + SoftwareUtil.getSoftwareNameByUrl(href) : downloadTagA.text());
                //软件版本
                //softwareDetail.setVersion(SoftwareUtil.getVersionBySoftwareName(SoftwareUtil.getSoftwareNameByUrl(href)));
                softwareDetail.setVersion(version);
                //软件操作系统
                softwareDetail.setOperatingSystem(SoftwareUtil.getSoftwareOperationVersionBySoftwareOriginalName(softwareDetail.getFileOriginalName()));
                //软件操作系统版本
                softwareDetail.setOsVersion("");
                //软件最后更新时间
                softwareDetail.setLastModified(ObjectUtils.isEmpty(datetime) ? "" : sdf.format(datetime));
                //软件在windows系统中的本地路径
                softwareDetail.setWindowsPath("\\" + softwareName + "\\" + redisUtil.getString(CommonConstants.DICTIONARY + ":" + softwareDetail.getOperatingSystem()) + "\\" + softwareDetail.getVersion() + "\\" + softwareDetail.getFileOriginalName());
                //软件在linux系统中的本地路径
                softwareDetail.setLinuxPath("/" + softwareName + "/" + redisUtil.getString(CommonConstants.DICTIONARY + ":" + softwareDetail.getOperatingSystem()) + "/" + softwareDetail.getVersion() + "/" + softwareDetail.getFileOriginalName());
                //软件大小单位b
                softwareDetail.setSize("");
                //下载地址
                softwareDetail.setDownloadUrl(redisUtil.getString(CommonConstants.CONFIG + ":" + ConfigConstant.GITHUB_DOWNLOAD_PRE) + href);
                //1：源码 2：编译后的
                softwareDetail.setSourceOrCompile(SoftwareUtil.getSourceOrCompile(softwareDetail.getFileName()));
                //类型
                softwareDetail.setType(SoftwareUtil.getSoftwareTypeByHrefOnAfter(href));
                //备注
                softwareDetail.setRemarks(elementsItem.getElementsByClass("Link--primary").text());
                //删除状态
                softwareDetail.setDelFlag(CommonConstants.NO);

                softwareDetail.setCreateDate(new Date());

                softwareList.add(softwareDetail);
            }
        }
        return softwareList;
    }

    //多线程取请求文档
    @Override
    public Document call() throws Exception {
        Document doc = null;
        try {
            log.info("多线程开始请求页面，请求地址：" + url);
            while (ObjectUtils.isEmpty(doc)) {
                try {   //此处加一个try-catch是因为如果出错可能是异常，无法在进行循环，防止进入递归
                    doc = Jsoup.connect(url).get();
                } catch (Exception e) {
                    log.error("请求" + url + "失败，尝试再次请求，第" + numberOfRequests++ + "次");
                }
            }
            countDownLatch.countDown();
            return doc;
        } catch (Exception e) {
            log.error("请求" + url + "失败，尝试再次请求，第" + numberOfRequests++ + "次");
            return call();
        }
    }
}
