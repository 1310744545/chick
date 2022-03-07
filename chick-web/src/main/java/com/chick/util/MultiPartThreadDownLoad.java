package com.chick.util;


import cn.hutool.core.io.FileUtil;
import com.chick.base.R;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;

/**
 * 多线程下载
 * 返回文件大小
 * @author bridge
 */
@Slf4j
public class MultiPartThreadDownLoad {


    public static R MultiPartDownLoad(String serverPath, String localPath){
        FileUtil.mkParentDirs(localPath);
        //次标记用于重复请求，github上的文件请求总是出问题
        int downloadFlag = 10;
        while (downloadFlag > 0) {
            try {
                URL url = new URL(serverPath);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("GET");
                int code = conn.getResponseCode();
                if (code == 200) {
                    log.info("请求文件成功----修改downloadFlag");
                    downloadFlag = 0;
                    //服务器返回的数据的长度，实际上就是文件的长度,单位是字节
                    int length = conn.getContentLength();
                    //应创建的线程数
                    int threadCount = getThreadCountBySoftwareSize(length);
                    //创建线程池
                    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
                    //线程计数器，当其归零之后才继续往下走
                    CountDownLatch latch = new CountDownLatch(threadCount);
                    MultiPartThreadDownLoad m = new MultiPartThreadDownLoad(serverPath, localPath, latch, executor, length, threadCount);
                    long startTime = System.currentTimeMillis();
                    try {
                        m.executeDownLoad();
                        latch.await();
                        executor.shutdown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    long endTime = System.currentTimeMillis();
                    log.info("全部下载结束,共耗时" + (endTime - startTime) / 1000 + "s");
                    return R.ok(length, "下载成功");
                }
            } catch (Exception e) {
                downloadFlag--;
                log.error("请求文件数据出错-->再次请求" + e.getMessage());
            }
        }
        return R.failed("在尝试请求10次该文件后失败，文件链接--->" + serverPath);
    }

    /**
     * 同时下载的线程数
     */
    private int threadCount;
    /**
     * 服务器请求路径
     */
    private String serverPath;
    /**
     * 本地路径
     */
    private String localPath;
    /**
     * 线程计数同步辅助
     */
    private CountDownLatch latch;

    /**
     * 线程池
     */
    private ExecutorService executor;

    /**
     * 线程池
     */
    private int length;

    public MultiPartThreadDownLoad(String serverPath, String localPath, CountDownLatch latch, ExecutorService executor, int length, int threadCount) {
        this.serverPath = serverPath;
        this.localPath = localPath;
        this.latch = latch;
        this.executor = executor;
        this.length = length;
        this.threadCount = threadCount;
    }

    public void executeDownLoad() {
        try {
            RandomAccessFile raf = new RandomAccessFile(localPath, "rwd");
            //指定创建的文件的长度
            raf.setLength(length);
            raf.close();
            //分割文件
            int blockSize = length / threadCount;
            for (int threadId = 1; threadId <= threadCount; threadId++) {
                //第一个线程下载的开始位置
                int startIndex = (threadId - 1) * blockSize;
                int endIndex = startIndex + blockSize - 1;
                if (threadId == threadCount) {
                    //最后一个线程下载的长度稍微长一点
                    endIndex = length;
                }
                System.out.println("线程" + threadId + "下载:" + startIndex + "字节~" + endIndex + "字节");
                executor.submit(new DownLoadThread(threadId, startIndex, endIndex));
            }
        } catch (Exception e) {
            log.error("请求文件数据出错-->再次请求" + e.getMessage());
        }
    }


    /**
     * 内部类用于实现下载
     */
    public class DownLoadThread implements Runnable {
        /**
         * 线程ID
         */
        private int threadId;
        /**
         * 下载起始位置
         */
        private int startIndex;
        /**
         * 下载结束位置
         */
        private int endIndex;

        public DownLoadThread(int threadId, int startIndex, int endIndex) {
            this.threadId = threadId;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        @Override
        public void run() {
            boolean downloadFlag = false;
            while (!downloadFlag) {
                try {
                    System.out.println("线程" + threadId + "正在下载...");
                    URL url = new URL(serverPath);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    //请求服务器下载部分的文件的指定位置
                    conn.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
                    conn.setConnectTimeout(60000);
                    int code = conn.getResponseCode();

                    System.out.println("线程" + threadId + "请求返回code=" + code);

                    InputStream is = conn.getInputStream();//返回资源
                    RandomAccessFile raf = new RandomAccessFile(localPath, "rwd");
                    //随机写文件的时候从哪个位置开始写
                    raf.seek(startIndex);//定位文件

                    int len = 0;
                    byte[] buffer = new byte[1024];
                    while ((len = is.read(buffer)) != -1) {
                        raf.write(buffer, 0, len);
                    }
                    is.close();
                    raf.close();
                    System.out.println("线程" + threadId + "下载完毕");
                    //计数值减一
                    latch.countDown();
                    downloadFlag = true;
                } catch (Exception e) {
                    log.error("分段下载时某段请求数据出错-->再次请求" + e.getMessage());
                }
            }
        }
    }

    public static int getThreadCountBySoftwareSize(int size) {
        int softwareMb = size / 1024 / 1024;
        if (softwareMb < 10) {
            return 3;
        } else if (softwareMb < 20) {
            return 4;
        } else if (softwareMb < 40) {
            return 5;
        } else if (softwareMb < 60) {
            return 6;
        } else if (softwareMb < 80) {
            return 7;
        } else if (softwareMb < 120) {
            return 8;
        } else if (softwareMb < 200) {
            return 9;
        } else if (softwareMb < 300) {
            return 10;
        } else if (softwareMb < 500) {
            return 11;
        } else if (softwareMb < 700) {
            return 12;
        } else if (softwareMb < 1000) {
            return 13;
        } else if (softwareMb < 1500) {
            return 14;
        } else {
            return 15;
        }
    }
}
