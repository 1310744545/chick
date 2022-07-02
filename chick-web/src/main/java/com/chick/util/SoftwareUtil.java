package com.chick.util;

import cn.hutool.core.io.FileUtil;
import com.chick.base.CommonConstants;
import com.chick.base.ConfigConstant;
import com.chick.base.DictionaryConstants;
import com.chick.common.utils.RedisUtil;
import com.chick.software.entity.SoftwareDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SoftwateUtil
 * @Author xiaokexin
 * @Date 2022/3/3 13:41
 * @Description SoftwateUtil
 * @Version 1.0
 */
@Component
public class SoftwareUtil {

    //    public static String I_SLASH_I = "\\";
//    public static String I_BACK_SLASH_I = "/";
//    public static String I_COLON_I = ":";
//    public static String I_ASTERISK_I = "*";
//    public static String I_QUESTION_MARK_I = "?";
//    public static String I_DOUBLE_QUOTES_I = "\"";
//    public static String I_LEFT_I = "<";
//    public static String I_RIGHT_I = ">";
//    public static String I_VERTICAL_BAR_I = "|";
    public static String I_SLASH_I = "I_SLASH_I";
    public static String I_BACK_SLASH_I = "I_BACK_SLASH_I";
    public static String I_COLON_I = "I_COLON_I";
    public static String I_ASTERISK_I = "I_ASTERISK_I";
    public static String I_QUESTION_MARK_I = "I_QUESTION_MARK_I";
    public static String I_DOUBLE_QUOTES_I = "I_DOUBLE_QUOTES_I";
    public static String I_LEFT_I = "I_LEFT_I";
    public static String I_RIGHT_I = "I_RIGHT_I";
    public static String I_VERTICAL_BAR_I = "I_VERTICAL_BAR_I";


    @Autowired
    private RedisUtil redisUtil;
    private static SoftwareUtil softwareUtil;

    @PostConstruct
    public void init() {
        softwareUtil = this;
        softwareUtil.redisUtil = this.redisUtil;
    }

    /**
     * @return java.lang.String
     * @Author xiaokexin
     * @Description 通过下载链接获取文件名
     * @Date 2022/3/3 13:39
     * @Param [url]
     **/
    public static String getSoftwareNameByUrl(String url) {
        return url.substring(url.lastIndexOf("/")).replace("/", "");
    }

    /**
     * @return java.lang.String
     * @Author xiaokexin
     * @Description 通过真实文件名获取文件名
     * @Date 2022/3/3 13:39
     * @Param [url]
     **/
    public static String getSoftwareNameBySoftwareName(String softwareName) {
        return softwareName.replace(".tar.gz", "").replace(".zip", "");
    }

    /**
     * @return boolean
     * @Author xiaokexin
     * @Description 判断文件是否存在
     * @Date 2022/3/3 13:58
     * @Param [softwareDetailResult]
     **/
    public static boolean existWinOrLinux(SoftwareDetail softwareDetailResult) {
        //为空 不存在
        if (ObjectUtils.isEmpty(softwareDetailResult)) {
            return false;
        }
        if (FileUtil.isWindows() && !FileUtil.exist(softwareUtil.redisUtil.getString(CommonConstants.CONFIG + ":" + ConfigConstant.WINDOWS_FILE_PRO) + softwareDetailResult.getWindowsPath())) {
            //服务器为window
            return false;
        }
        //服务器为linux
        if (!FileUtil.isWindows() && !FileUtil.exist(softwareUtil.redisUtil.getString(ConfigConstant.LINUX_FILE_PRO) + softwareDetailResult.getLinuxPath())) {
            return false;
        }
        //存在
        return true;
    }

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 6, 300, TimeUnit.MINUTES, new ArrayBlockingQueue<>(8192), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        for (int i = 0; i < 10; i++) {
            System.out.println("任务数"+threadPoolExecutor.getTaskCount());
            System.out.println("活跃数"+threadPoolExecutor.getActiveCount());
            threadPoolExecutor.submit(() -> {
                String a = "https://manhua.acimg.cn/manhua_detail/0/09_20_40_a7356ca75ca9d8a3a6837a14f86d1c010_119748505.jpg/0";
                MultiPartThreadDownLoad multiPartThreadDownLoad = new MultiPartThreadDownLoad();
                multiPartThreadDownLoad.MultiPartDownLoad(a, "D:\\comics\\aaa.jpg");
            });
        }
    }

    /**
     * @return boolean
     * @Author xiaokexin
     * @Description 通过路径判断文件是否存在
     * @Date 2022/3/3 13:58
     * @Param [softwareDetailResult]
     **/
    public static boolean existByPath(String path) {
        // 不存在
        if (StringUtils.isEmpty(path) || !FileUtil.exist(path)) {
            return false;
        }
        //存在
        return true;
    }

    /**
     * @return java.lang.String
     * @Author xiaokexin
     * @Description 获取文件版本，针对文件版本在文件名最后的
     * @Date 2022/3/3 14:00
     * @Param [text]
     **/
    public static String getSoftwareVersionByTextOnAfter(String text) {
        return text.substring(text.lastIndexOf("-")).replace("-", "");
    }


    /**
     * @return java.lang.String
     * @Author xiaokexin
     * @Description 获取文件类型 针对href中文件类型在最后的情况
     * @Date 2022/3/3 14:14
     * @Param [href]
     **/
    public static String getSoftwareTypeByHrefOnAfter(String href) {
        if (href.endsWith("tar.gz")) {
            return "tar.gz";
        }
        return "zip";
    }

    /**
     * @return java.lang.String
     * @Author xiaokexin
     * @Description 获取系统类型
     * @Date 2022/3/3 15:41
     * @Param [SoftwareOriginalName]
     **/
    public static String getSoftwareOperationVersionBySoftwareOriginalName(String SoftwareOriginalName) {
        if (SoftwareOriginalName.contains("tar.gz")) {
            return DictionaryConstants.LINUX;
        }
        return DictionaryConstants.WINDOWS;
    }

    /**
     * @return java.lang.String
     * @Author xiaokexin
     * @Description 获取代码类型源码or编译后的
     * @Date 2022/3/3 15:41
     * @Param [SoftwareOriginalName]
     **/
    public static String getSourceOrCompile(String fileName) {
        if (fileName.contains("Source code")) {
            return "1";
        }
        return "2";
    }

    public static String replaceAllIllegalCode(String fileName) {
        return fileName.replace("\\", I_SLASH_I).replace("/", I_BACK_SLASH_I).replace(":", I_COLON_I).
                replace("*", I_ASTERISK_I).replace("?", I_QUESTION_MARK_I).replace("\"", I_DOUBLE_QUOTES_I).
                replace("<", I_LEFT_I).replace(">", I_RIGHT_I).replace("|", I_VERTICAL_BAR_I)
                .replace(".", "");
    }

    /**
     * @return java.lang.String
     * @Author xiaokexin
     * @Description 根据软件名获取版本
     * @Date 2022/3/7 13:50
     * @Param [software]
     **/
    public static String getVersionBySoftwareName(String softwareName) {
        String version = "";
        for (int i = 0; i < softwareName.length(); i++) {
            char c = softwareName.charAt(i);
            if ((c <= 67 && c >= 48) || c == 46) {
                if (version.endsWith(".") && c == 46) {
                    break;
                }
                version += c;
            }
        }
        return version.substring(0, version.length() - 1);
    }
}
