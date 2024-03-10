package com.chick.config;

import cn.hutool.extra.mail.MailAccount;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @ClassName MailConfig
 * @Author xiaokexin
 * @Date 2022-10-18 9:37
 * @Description 邮件发送配置类阿斯顿阿三大苏打现场
 * @Version 1.0
 */
@Component
@Data
@ConfigurationProperties(prefix = "email")
public class MailConfig {

    private String host;
    private int port;
    private String from;
    private String user;
    private String pass;

    @Bean
    public MailAccount getMailAccount(){
        MailAccount account = new MailAccount();
        account.setHost(host);
        account.setPort(port);
        account.setFrom(from);
        account.setUser(user);
        account.setPass(pass);
        return account;
    }
}
