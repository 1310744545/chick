package com.chick.jedis;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisTestLockController
 * @Author xiaokexin
 * @Date 2021/12/20 0:13
 * @Description redis锁操作
 * @Version 1.0
 */
@RestController
@RequestMapping("/testLock")
public class RedisTestLockController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/testLock")
    public void testLock(){
        //生成uuid
        String uuid = UUID.randomUUID().toString();
        //1、获取锁，setne
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 3, TimeUnit.SECONDS);
        //2、获取锁成功，查询num的值
        if (lock){
            String num = redisTemplate.opsForValue().get("num") + "";
            //2.1、判断num为空return
            if (StringUtils.isBlank(num)){
                return;
            }
            //2.2、有值就转成int
            int i = Integer.parseInt(num);
            //2.3、把redis的num加1
            redisTemplate.opsForValue().set("num", ++i);
            //2.4、释放锁，del
            String lockUUid = redisTemplate.opsForValue().get("lock") + "";
            if (uuid.equals(lockUUid)){
                redisTemplate.delete("lock");
            }
        } else {
            //3、获取锁失败、每隔0.1秒再获取
            try {
                Thread.sleep(100);
                testLock();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
