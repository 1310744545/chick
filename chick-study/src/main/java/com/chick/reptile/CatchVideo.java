package com.chick.reptile;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;

public class CatchVideo {

    static String infoUrlBase = "http://vv.video.qq.com/getinfo?vids=";

    public static void main(String[] args) {
        String playUrl = "https://v.qq.com/x/cover/mzc00200lo9uiu3/u0041aw7821.html";

        String s = playUrl.split(".html")[0];
        String vid = s.substring(s.lastIndexOf("/")).replace("/", "");

        String infoUrl = infoUrlBase + vid + "&platform=101001&charge=0&otype=json";

        String infoResStr = HttpUtil.get(infoUrl);

        infoResStr = infoResStr.replace("QZOutputJson=", "");
        infoResStr = infoResStr.substring(0, infoResStr.length() - 1);
        JSONObject jsonObject = JSONObject.parseObject(infoResStr);

        JSONObject vi = jsonObject.getJSONObject("vl").getJSONArray("vi").getJSONObject(0);
        //解析fn
        String fn = vi.getString("fn");
        //解析fvkey
        String fvkey = vi.getString("fvkey");
        //解析url
        String url = vi.getJSONObject("ul").getJSONArray("ui").getJSONObject(3).getString("url");


        String realPath = url + fn + "?vkey=" + fvkey;
        System.out.println(realPath);
    }

}
