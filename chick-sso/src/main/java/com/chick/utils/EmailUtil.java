package com.chick.utils;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import groovy.util.logging.Slf4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName UserEmailUtil
 * @Author xiaokexin
 * @Date 2022-10-18 8:43
 * @Description UserEmailUtil
 * @Version 1.0
 */
@Component
@Log4j2
public class EmailUtil {
    private static MailAccount mailAccount;
    @Autowired
    private void setSwitchConfig(MailAccount mailAccount) {
        EmailUtil.mailAccount = mailAccount;
    }

    public static boolean sendVerificationCode(String email, String code){
        try {
            MailUtil.send(mailAccount, email, "小鸡的one piece", "您的验证码为" + code + "，请在五分钟内使用。", false);
            return true;
        } catch (Exception e){
            log.error("验证码发送错误 -->" + e.getMessage());
            return false;
        }
    }
}
