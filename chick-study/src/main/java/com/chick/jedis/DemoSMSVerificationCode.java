package com.chick.jedis;

import com.chick.base.R;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.Calendar;
import java.util.Random;

/**
 * @ClassName demoSMSVerificationCode
 * @Author xiaokexin
 * @Date 2021/12/12 23:03
 * @Description 一天发送三次验证码示例
 * @Version 1.0
 */
public class DemoSMSVerificationCode {
    private static Jedis jedis;
    static {
        Jedis jedisParam = new Jedis("47.93.35.215", 6379);
        jedisParam.auth("qq10086..");
        jedis = jedisParam;
    }

    public static void main(String[] args) {
        R r = pushSMSVerificationCode("13685368864");
        System.out.println(r.getMsg());
        R smsVerificationCode = getSMSVerificationCode("13685368864");
        System.out.println(smsVerificationCode.getMsg());
    }

    public static R pushSMSVerificationCode(String phone) {
        //次数key
        String countKey = "verifyCode" + phone + ":count";
        //验证码Key
        String codeKey = "verifyCode" + phone + ":code";

        //发送次数
        String count = jedis.get(countKey);
        if (StringUtils.isBlank(count)){
            //没有发送过，设置发送次数是1
            jedis.setex(countKey, getSecondsNextEarlyMorning(), "1");
        } else if (Integer.parseInt(count) <= 2){
            jedis.incrBy(countKey, 1);
        } else {
            return R.failed("今日已发送三次验证码，无法继续发送");
        }
        jedis.setex(codeKey, 120L, getCode());
        return R.ok("发送成功");
    }

    //验证验证码
    public static R getSMSVerificationCode(String phone) {
        //验证码Key
        String codeKey = "verifyCode" + phone + ":code";
        String s = jedis.get(codeKey);
        if (StringUtils.isBlank(s)){
            return R.failed("验证码为空或已过期");
        }
        return R.ok(s);
    }

    //获取验证码
    public static String getCode(){
        StringBuffer code = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < 6; i++){
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    //获取当前时间到凌晨12点的秒数
    public static Long getSecondsNextEarlyMorning() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }
}
