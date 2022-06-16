package com.chick.config;

import com.chick.base.CommonConstants;
import com.chick.common.utils.RedisUtil;
import com.chick.web.config.entity.SysConfig;
import com.chick.web.config.mapper.SysConfigMapper;
import com.chick.web.dictionary.entity.SysDbInfo;
import com.chick.web.dictionary.mapper.SysDbInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

/**
 * @ClassName ChickRunner
 * @Author xiaokexin
 * @Date 2021/12/24 14:58
 * @Description 启动执行
 * @Version 1.0
 */
@Configuration
public class ChickRunner implements ApplicationRunner {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysDbInfoMapper sysDbInfoMapper;
    @Autowired
    private SysConfigMapper sysConfigMapper;
    private static ChickRunner chickRunner;
    @PostConstruct
    public void init() {
        chickRunner = this;
        chickRunner.sysDbInfoMapper = this.sysDbInfoMapper;
        chickRunner.sysConfigMapper = this.sysConfigMapper;
        chickRunner.redisUtil = this.redisUtil;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //加载配置到redis
        loadSysConfig();
        //加载字典到redis
        loadDictionary();

    }

    public static void loadDictionary(){
        List<SysDbInfo> sysDbInfos = chickRunner.sysDbInfoMapper.selectAll();
        //先删除redis中的字典
        Set<String> keys = chickRunner.redisUtil.hashKeysString(CommonConstants.DICTIONARY);
        for (String key : keys){
            chickRunner.redisUtil.delete(CommonConstants.DICTIONARY, key);
        }
        Set<String> keysData = chickRunner.redisUtil.keys(CommonConstants.DICTIONARY + "*");
        for (String key : keysData){
            chickRunner.redisUtil.delete(key);
        }
        for (SysDbInfo sysDbInfo:sysDbInfos){
            chickRunner.redisUtil.set(CommonConstants.DICTIONARY + ":" + sysDbInfo.getDataNum(), sysDbInfo.getDataInfo());
            chickRunner.redisUtil.addObject(CommonConstants.DICTIONARY, sysDbInfo.getDataNum(), sysDbInfo);
        }
    }

    public static void loadSysConfig(){
        List<SysConfig> sysConfigs = chickRunner.sysConfigMapper.selectAll();
        //先删除redis中的字典(hash)
        Set<String> keys = chickRunner.redisUtil.hashKeysString(CommonConstants.CONFIG);
        for (String key : keys){
            chickRunner.redisUtil.delete(CommonConstants.CONFIG, key);
        }
        for (SysConfig sysConfig : sysConfigs){
            chickRunner.redisUtil.set(CommonConstants.CONFIG + ":" + sysConfig.getConfigName(), sysConfig.getConfigValue());
            chickRunner.redisUtil.addObject(CommonConstants.CONFIG, sysConfig.getConfigName(), sysConfig);
        }

        //先删除redis中的字典(String)
    }
}
