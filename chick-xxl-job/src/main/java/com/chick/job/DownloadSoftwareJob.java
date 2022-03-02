package com.chick.job;

import com.chick.config.XxlJobConfig;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName DownloadSoftwareJob
 * @Author xiaokexin
 * @Date 2022/3/1 14:56
 * @Description 定时巡检并下载未下载的软件们
 * @Version 1.0
 */
@Component
public class DownloadSoftwareJob {
    private Logger logger = LoggerFactory.getLogger(XxlJobConfig.class);

    /**
    * @Author xiaokexin
    * @Description 巡检并下载Nginx定时任务
    * @Date 2022/3/1 14:57
    * @Param []
    * @return void
    **/
    @XxlJob("downloadNginxJob")
    public void demoJobHandler() throws Exception {
        logger.info("Nginx下载巡检任务开始，查看是否有新版本未下载");
        XxlJobHelper.log("XXL-JOB, Hello World.");

        for (int i = 0; i < 5; i++) {
            XxlJobHelper.log("beat at:" + i);
            TimeUnit.SECONDS.sleep(2);
        }
        // default success
    }
}
