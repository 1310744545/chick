package com.chick.exam.utils;

import cn.hutool.http.HttpUtil;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

/**
 * @ClassName SoftwareExamUtil
 * @Author xiaokexin
 * @Date 2022-07-03 14:58
 * @Description SoftwareExamUtil
 * @Version 1.0
 */
public class SoftwareExamUtil {
    public static HashMap<String, String> examDetailMap = new HashMap<>();
    public static HashMap<String, String> examTypeMap = new HashMap<>();

    public static HashMap<String, String> examLevel = new HashMap<>();

    static {
        examDetailMap.put("1", "信息系统项目管理师");
        examDetailMap.put("2", "系统集成项目管理工程师");
        examDetailMap.put("3", "系统分析师");
        examDetailMap.put("4", "系统架构设计师");
        examDetailMap.put("5", "网络规划设计师");
        examDetailMap.put("6", "软件设计师");
        examDetailMap.put("7", "网络工程师");
        examDetailMap.put("8", "信息系统监理师");
        examDetailMap.put("9", "信息系统管理工程师");
        examDetailMap.put("10", "数据库系统工程师");
        examDetailMap.put("11", "多媒体应用设计师");
        examDetailMap.put("12", "软件评测师");
        examDetailMap.put("13", "嵌入式系统设计师");
        examDetailMap.put("14", "电子商务设计师");
        examDetailMap.put("15", "程序员");
        examDetailMap.put("16", "网络管理员");
        examDetailMap.put("17", "信息处理技术员");
        examDetailMap.put("18", "信息安全工程师");
        examDetailMap.put("19", "系统规划与管理师");
        examDetailMap.put("20", "信息系统运行管理员");

        /**
         缺失
         subjectMap.put("", "软件过程能力评估师");
         subjectMap.put("", "计算机辅助设计师");
         subjectMap.put("", "计算机硬件工程师");
         subjectMap.put("", "信息技术支持工程师");
         subjectMap.put("", "多媒体应用制作技术员");
         subjectMap.put("", "电子商务技术员");
         subjectMap.put("", "信息系统运行管理员");
         subjectMap.put("", "网页制作员");
         **/
    }

    static {
        examTypeMap.put("1", "高级_信息系统项目管理师");
        examTypeMap.put("2", "中级_系统集成项目管理工程师");
        examTypeMap.put("3", "高级_系统分析师");
        examTypeMap.put("4", "高级_系统架构设计师");
        examTypeMap.put("5", "高级_网络规划设计师");
        examTypeMap.put("6", "中级_软件设计师");
        examTypeMap.put("7", "中级_网络工程师");
        examTypeMap.put("8", "中级_信息系统监理师");
        examTypeMap.put("9", "中级_信息系统管理工程师");
        examTypeMap.put("10", "中级_数据库系统工程师");
        examTypeMap.put("11", "中级_多媒体应用设计师");
        examTypeMap.put("12", "中级_软件评测师");
        examTypeMap.put("13", "中级_嵌入式系统设计师");
        examTypeMap.put("14", "中级_电子商务设计师");
        examTypeMap.put("15", "初级_程序员");
        examTypeMap.put("16", "初级_网络管理员");
        examTypeMap.put("17", "初级_信息处理技术员");
        examTypeMap.put("18", "中级_信息安全工程师");
        examTypeMap.put("19", "高级_系统规划与管理师");
        examTypeMap.put("20", "初级_信息系统运行管理员");

        /**
         缺失
         subjectMap.put("", "软件过程能力评估师");
         subjectMap.put("", "计算机辅助设计师");
         subjectMap.put("", "计算机硬件工程师");
         subjectMap.put("", "信息技术支持工程师");
         subjectMap.put("", "多媒体应用制作技术员");
         subjectMap.put("", "电子商务技术员");
         subjectMap.put("", "信息系统运行管理员");
         subjectMap.put("", "网页制作员");
         **/
    }

    static {
        examLevel.put("信息系统项目管理师", "高级");
        examLevel.put("系统集成项目管理工程师", "中级");
        examLevel.put("系统分析师", "高级");
        examLevel.put("系统架构设计师", "高级");
        examLevel.put("网络规划设计师", "高级");
        examLevel.put("软件设计师", "中级");
        examLevel.put("网络工程师", "中级");
        examLevel.put("信息系统监理师", "中级");
        examLevel.put("信息系统管理工程师", "中级");
        examLevel.put("数据库系统工程师", "中级");
        examLevel.put("多媒体应用设计师", "中级");
        examLevel.put("软件评测师", "中级");
        examLevel.put("嵌入式系统设计师", "中级");
        examLevel.put("电子商务设计师", "中级");
        examLevel.put("程序员", "初级");
        examLevel.put("网络管理员", "初级");
        examLevel.put("信息处理技术员", "初级");
        examLevel.put("信息安全工程师", "中级");
        examLevel.put("系统规划与管理师", "高级");
        examLevel.put("信息系统运行管理员", "初级");

        /**
         缺失
         subjectMap.put("", "软件过程能力评估师");
         subjectMap.put("", "计算机辅助设计师");
         subjectMap.put("", "计算机硬件工程师");
         subjectMap.put("", "信息技术支持工程师");
         subjectMap.put("", "多媒体应用制作技术员");
         subjectMap.put("", "电子商务技术员");
         subjectMap.put("", "信息系统运行管理员");
         subjectMap.put("", "网页制作员");
         **/
    }

    public static final String SOFTWARE_EXAM_NAME = "软考";

    public static final String SOFTWARE_EXAM_DISSERTATION = "论文";
    public static final String SOFTWARE_EXAM_CASE = "案例";
    public static final String SOFTWARE_EXAM_COMPREHENSIVE = "综合知识";

}
