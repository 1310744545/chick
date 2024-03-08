package com.chick.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;


/**
 * @ClassName CaptchaConfig阿斯顿
 * @Author xiaokexin
 * @Date 2022-05-27 16:07阿斯顿
 * @Description CaptchaConfig
 * @Version 1.0
 */
@Component
public class CaptchaConfig {

    @Bean(name = "kaptchaProuder")
    public DefaultKaptcha getDefaultKaptcha(){
        //验证码生成器
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        //配置
        Properties properties = new Properties();
        //是否有边框
        properties.setProperty("kaptcha.border", "no");
        //设置边框颜色
//        properties.setProperty("kaptcha.border.color", "blue");
        //设置边框粗细度 默认1
        //properties.setProperty("kaptcha.border.thickness", "1");
        //验证码
        properties.setProperty("kaptcha.session.key", "code");
        //验证码文本字符颜色 默认为黑色
        properties.setProperty("kaptcha.textproducer.font.color", "blue");
        //设置字体样式
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        //字体大小,默认40
        properties.setProperty("kaptcha.textproducer.font.size", "30");
        //验证码文本字符内容范围,默认abcded2345678gfynmpwx
        //properties.setProperty("kaptcha.textproducer.char.string", "");
        //字符长度 默认5
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        //字符间距 默认2
        properties.setProperty("kaptcha.textproducer.font.space", "7");
        //验证码图片宽度 默认200
        properties.setProperty("kaptcha.image.width", "100");
        //验证码图片高度 默认40
        properties.setProperty("kaptcha.image.height", "38");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }
}
