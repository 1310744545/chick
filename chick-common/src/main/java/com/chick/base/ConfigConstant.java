package com.chick.base;

/**
 * @ClassName ConfigConstant
 * @Author xiaokexin
 * @Date 2022/3/3 14:32
 * @Description ConfigConstant
 * @Version 1.0
 */
public interface ConfigConstant {
    /**
     * 配置
     */
    //nginx下载地址
    String NGINX_DOWNLOAD_URL = "nginxDownloadUrl";
    //nginx下载前缀
    String NGINX_DOWNLOAD_PRE = "nginxDownloadPre";
    //软件在windows系统中的前缀
    String WINDOWS_FILE_PRO = "windowsFilePro";
    //软件在linux系统中的前缀
    String LINUX_FILE_PRO = "linuxFilePro";

    String INDEX = "Index";

    //软件在linux系统中的前缀
    String GITHUB_DOWNLOAD_PRE = "githubDownloadPre";
}
