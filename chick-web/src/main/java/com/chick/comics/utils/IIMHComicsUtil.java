package com.chick.comics.utils;

import cn.hutool.core.codec.Base64Decoder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName IIMHComicsUtil
 * @Author xiaokexin
 * @Date 2022-07-04 9:40
 * @Description IIMHComicsUtil
 * @Version 1.0
 */
@Log4j2
public class IIMHComicsUtil {
    /**
    * @Author xkx
    * @Description 通过首页url获取图片地址（不带域名）
    * @Date 2022-07-04 10:01
    * @Param [url]
    * @return java.util.Map<java.lang.Integer,java.lang.String>
    **/
    public Map<Integer, String> getPics(String htmlContent){
        Document document = null;
        try {
            document = Jsoup.parse(htmlContent);
        } catch (Exception e) {
            return new HashedMap<>();
        }
        Elements scripts = document.getElementsByTag("script");
        String data = "";
        for (Element script : scripts) {
            if (StringUtils.contains(script.html(), "packed")) {
                data = StringUtils.substringBetween(script.html(), "packed=\"", "\";");
                break;
            }
        }
        String sourceStrings = executeJS(Base64Decoder.decodeStr(data).substring(4));
        HashMap<Integer, String> imgMap = new HashMap<>();
        int i = 1;
        for (String sourceStr : sourceStrings.split(";")) {
            imgMap.put(i, StringUtils.substringBetween(sourceStr, "\"", "\""));
            i++;
        }
        return imgMap;
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
            return "";
        }
    }
}
