package com.chick.comics.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @ClassName TencentComicsUtil
 * @Author xiaokexin
 * @Date 2022-06-27 10:18
 * @Description 腾讯漫画爬取工具类
 * @Version 1.0
 */
@Log4j2
@Component
public class TencentComicsUtil {

    public static void main(String[] args) {
        String s = Long.toBinaryString(Long.parseLong("4848448498494"));
        String substring = s.substring(s.length() - 8);
        Integer integer = Integer.valueOf(substring, 2);
        System.out.println(integer);
//        TencentComicsUtil tencentComicsUtil = new TencentComicsUtil();
//        Document document = null;
//        try {
//            document = Jsoup.connect("https://ac.qq.com/ComicView/index/id/635980/cid/10").get();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        List<String> pics = tencentComicsUtil.getPics(document.toString());
//        System.out.println(pics);
    }

    /**
     * @param htmlContent 章节url的html文本
     * @return TencentChapter 每章内容信息
     */
    public List<String> getPics(String htmlContent) {
        Document document = Jsoup.parse(htmlContent);
        Elements scripts = document.getElementsByTag("script");
        String data = "";
        String nonce = "";
        for (Element script : scripts) {
            //获取DATA
            if (StringUtils.contains(script.html(), "DATA")) {
                data = StringUtils.substringBetween(script.html(), "'", "'");
            }
            //获取nonce
            if (StringUtils.containsAny(script.html(), "window\\[.*?\\].*?eval")) {
                String str = StringUtils.substringBefore(script.html(), " = ");
                str = StringUtils.replace(str, "\"+\"", "");
                if (StringUtils.equalsIgnoreCase(str, "window[\"nonce\"]")) {
                    nonce = StringUtils.substringBetween(script.html(), "] = ", ";");
                    nonce = StringUtils.replace(nonce, "!!document.getElementsByTagName('html')", "true");
                    nonce = StringUtils.replace(nonce, "(!window.Array)", "false");
                    nonce = StringUtils.replace(nonce, "!!document.children", "true");
                    nonce = executeJS(nonce);
                }

            }
        }

        if (StringUtils.isAnyBlank(data, nonce)) {
            return null;
        }

        //通过data和nonce计算出网站返回的json(其中包含图片)
        String jsonData = jsDecode(data, nonce);

        JSONObject sourceJson = JSON.parseObject(jsonData);
        JSONArray pictures = sourceJson.getJSONArray("picture");
        return pictures.stream().map(j -> ((JSONObject) j).getString("url")).collect(Collectors.toList());
    }

    private String executeJS(String jsContent) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            CompiledScript script = ((Compilable) engine).compile(jsContent);
            Object object = script.eval();
            return object.toString();
        } catch (Exception e) {
            log.error("jsContent:{}", jsContent);
            e.printStackTrace();
        }
        return "";
    }

    private String jsDecode(String data, String nonce) {
        List<String> dataList = converter(data);

        List<String> nonceList = matchNonce(nonce);
        for (int i = nonceList.size() - 1; i >= 0; i--) {
            String non = nonceList.get(i);
            int locate = getNumbers(non) & 255;
            String str = non.replaceAll("\\d+", "");
            dataList.subList(locate, locate + str.length()).clear();
        }


        data = dataList.stream().collect(Collectors.joining(""));
        String decode = decode(data);
        return decode;
    }

    private String decode(String source) {
        source = source.replaceAll("[^A-Za-z0-9\\+\\/\\=]", "");
        String keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        String a = "";
        int b, d, h, f, g, e = 0;

        for (int i = 0; i < source.length() && e < source.length(); i++) {
            b = keyStr.indexOf(source.charAt(e));
            e++;
            d = keyStr.indexOf(source.charAt(e));
            e++;
            f = keyStr.indexOf(source.charAt(e));
            e++;
            g = keyStr.indexOf(source.charAt(e));
            e++;

            b = b << 2 | d >> 4;
            d = (d & 15) << 4 | f >> 2;
            h = (f & 3) << 6 | g;

            a += (char) b;
            if (64 != f) {
                a += (char) d;
            }
            if (64 != g) {
                a += (char) h;
            }
        }
        return a;
    }

    private List<String> matchNonce(String nonce) {
        List<String> allMatches = new ArrayList<>();
        Matcher m = Pattern.compile("\\d+[a-zA-Z]+").matcher(nonce);
        while (m.find()) {
            allMatches.add(m.group());
        }
        return allMatches;
    }

    private int getNumbers(String s) {
        String[] n = s.split(""); //array of strings
        StringBuffer f = new StringBuffer(); // buffer to store numbers
        for (int i = 0; i < n.length; i++) {
            if ((n[i].matches("[0-9]+"))) {// validating numbers
                f.append(n[i]); //appending
            } else {
                //parsing to int and returning value
                if (f.toString().length() > 7){
                    String longString = Long.toBinaryString(Long.parseLong(f.toString()));
                    String substring = longString.substring(longString.length() - 8);
                    return Integer.valueOf(substring, 2);
                }
                return Integer.parseInt(f.toString());
            }
        }
        return 0;
    }


    private List<String> converter(String data) {
        char[] chars = data.toCharArray();
        List<String> result = new ArrayList<>();
        for (char aChar : chars) {
            result.add(Character.toString(aChar));
        }
        return result;
    }
}
