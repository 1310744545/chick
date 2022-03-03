package com.chick.util;

import cn.hutool.core.io.FileUtil;
import com.chick.base.CommonConstants;
import com.chick.base.ConfigConstant;
import com.chick.base.DictionaryConstants;
import com.chick.common.utils.RedisUtil;
import com.chick.software.entity.SoftwareDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;

/**
 * @ClassName SoftwateUtil
 * @Author xiaokexin
 * @Date 2022/3/3 13:41
 * @Description SoftwateUtil
 * @Version 1.0
 */
public class SoftwareUtil {


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
    public static String getSoftWareNameByUrl(String url) {
        return url.substring(url.lastIndexOf("/")).replace("/", "");
    }

    /**
    * @Author xiaokexin
    * @Description 判断文件是否存在
    * @Date 2022/3/3 13:58
    * @Param [softwareDetailResult]
    * @return boolean
    **/
    public static boolean existWinOrLinux(SoftwareDetail softwareDetailResult) {
        //为空 不存在
        if (ObjectUtils.isEmpty(softwareDetailResult)) {
            return false;
        }
        //服务器为window
        if (FileUtil.isWindows() && !FileUtil.exist(softwareUtil.redisUtil.getString(ConfigConstant.WINDOWS_FILE_PRO) + softwareDetailResult.getWindowsPath())) {
            return false;
        }
        //服务器为linux
        if (!FileUtil.exist(softwareUtil.redisUtil.getString(ConfigConstant.LINUX_FILE_PRO) + softwareDetailResult.getLinuxPath())) {
            return false;
        }
        //存在
        return true;
    }

    /**
    * @Author xiaokexin
    * @Description 获取文件版本，针对文件版本在文件名最后的
    * @Date 2022/3/3 14:00
    * @Param [text]
    * @return java.lang.String
    **/
    public static String getSoftwareVersionByTextOnAfter(String text) {
        return text.substring(text.lastIndexOf("-")).replace("-", "");
    }


    /**
    * @Author xiaokexin
    * @Description 获取文件类型 针对href中文件类型在最后的情况
    * @Date 2022/3/3 14:14
    * @Param [href]
    * @return java.lang.String
    **/
    public static String getSoftwareTypeByHrefOnAfter(String href) {
        if (href.endsWith("tar.gz")){
            return "tar.gz";
        }
        return "zip";
    }

    /**
    * @Author xiaokexin
    * @Description 获取系统类型
    * @Date 2022/3/3 15:41
    * @Param [SoftwareOriginalName]
    * @return java.lang.String
    **/
    public static String getSoftwareOperationVersionBySoftwareOriginalName(String SoftwareOriginalName) {
        if (SoftwareOriginalName.contains("tar.gz")){
            return DictionaryConstants.LINUX;
        }
        return DictionaryConstants.WINDOWS;
    }
}
