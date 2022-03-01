package com.chick.jedis;

import com.chick.base.R;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName SpikeDemo
 * @Author xiaokexin
 * @Date 2021/12/14 21:26
 * @Description 秒杀案例
 * @Version 1.0
 */
@RestController
@RequestMapping("/SpikeDemo")
public class SpikeDemoController {

    @Autowired
    private RedisTemplate redisTemplate;
    static String SpikeLUAString = "local userid = KEYS[1];\n" +
            "local prodid = KEYS[2];\n" +
            "local qtkey= \"sk:\"..prodid..\":qt\";\n" +
            "local usersKey= \"sk:\"..prodid..\":usr\";\n" +
            "local userExists=redis.call(\"sismember'\",usersKey,userid);\n" +
            "if tonumber(userExists)==1 then\n" +
            "return 2;\n" +
            "end\n" +
            "local num= rediscall(\"get\" ,qtkey);\n" +
            "if tonumber(num)<=0 then\n" +
            "return 0;\n" +
            "else\n" +
            "redis.call(\"decr\",qtkey);\n" +
            "redis.call(\"sadd\",usersKey,userid);\n" +
            "end\n" +
            "return 1";


    //基本的秒杀，存在超卖和超时问题
    @GetMapping("/Spike")
    public R SpikeDemoTest(String userId, String goodId){
        userId = UUID.randomUUID().toString();
        return SpikeGoods(userId, goodId);
    }

    //加了乐观锁的秒杀，存在库存遗留的问题
    @GetMapping("/SpikeWithOptimisticLock")
    public R SpikeDemoTestWithOptimisticLock(String userId, String goodId){
        userId = UUID.randomUUID().toString();
        return SpikeGoodsWithOptimisticLock(userId, goodId);
    }

    //通过LUA脚本
    @GetMapping("/SpikeWithLUA")
    public R SpikeDemoTestWithLUA(String userId, String goodId){
        userId = UUID.randomUUID().toString();
        return SpikeGoodsWithLUA(userId, goodId);
    }

    @GetMapping("/addKc")
    public R addKc(String goodId, int count){
        return addKcs(goodId, count);
    }

    //添加商品
    public R addKcs(String goodId, int count){
        String kcKey = "sk:" + goodId + ":qt";
        redisTemplate.opsForValue().set(kcKey, count);
        return R.ok("添加商品成功");
    }


    //存在超卖问题，库存变为负的了        超时问题，redis一次处理不了很多个请求
    //1、判空
    //2、链接redis
    //3、拼接key
    //3.1、库存key
    //3.2、秒杀成功用户key
    //4、获取库存，如果为空，秒杀还没有开始
    //5、判断用户是否重复秒杀操作
    //6、判断如果商品数量、库存数量小于1，秒杀结束
    //7、秒杀过程
    //7.1 库存-1
    //7.2 把秒杀成功用户调价清单里面
    public R SpikeGoods(String userId, String goodId){
        //1、判空
        if (StringUtils.isAnyBlank(userId, goodId)){
            return R.failed("参数缺失");
        }
        //2、链接redis
        //3、拼接key
        //3.1、库存key
        String kcKey = "sk:" + goodId + ":qt";
        //3.2、秒杀成功用户key
        String userKey = "sk" + goodId + ":user";
        //4、获取库存，如果为空，秒杀还没有开始
        Object kc = redisTemplate.opsForValue().get(kcKey);
        if (ObjectUtils.isEmpty(kc)){
            System.out.println("秒杀还未开始，请等待");
            return R.failed("秒杀还未开始，请等待");
        }
        //5、判断用户是否重复秒杀操作
        if (redisTemplate.opsForSet().isMember(userKey, userId)){
            System.out.println("已秒杀成功，请勿重复秒杀");
            return R.failed("已秒杀成功，请勿重复秒杀");
        }
        //6、判断如果商品数量、库存数量小于1，秒杀结束
        if (ObjectUtils.isNotEmpty(kc) && Integer.parseInt(kc.toString()) < 1){
            System.out.println("已被抢空，秒杀已结束");
            return R.failed("已被抢空，秒杀已结束");
        }
        //7、秒杀过程
        //7.1 库存-1
        redisTemplate.opsForValue().decrement(kcKey);
        //7.2 把秒杀成功用户调价清单里面
        redisTemplate.opsForSet().add(userKey, userId);
        System.out.println("秒杀成功");
        return R.ok("秒杀成功");
    }


    public R SpikeGoodsWithOptimisticLock(String userId, String goodId){
        if (StringUtils.isAnyBlank(userId, goodId)){
            return R.failed("参数缺失");
        }
        String kcKey = "sk:" + goodId + ":qt";
        String userKey = "sk" + goodId + ":user";
        //开启事务
        redisTemplate.setEnableTransactionSupport(true);
        //监视库存
        redisTemplate.watch(kcKey);
        Object kc = redisTemplate.opsForValue().get(kcKey);
        if (ObjectUtils.isEmpty(kc)){
            System.out.println("秒杀还未开始，请等待");
            return R.failed("秒杀还未开始，请等待");
        }
        if (redisTemplate.opsForSet().isMember(userKey, userId)){
            System.out.println("已秒杀成功，请勿重复秒杀");
            return R.failed("已秒杀成功，请勿重复秒杀");
        }
        if (ObjectUtils.isNotEmpty(kc) && Integer.parseInt(kc.toString()) < 1){
            System.out.println("已被抢空，秒杀已结束");
            return R.failed("已被抢空，秒杀已结束");
        }

//        redisTemplate.boundValueOps(kcKey).decrement(1L);
//        redisTemplate.opsForSet().add(userKey, userId);

        //秒杀过程 组队
        redisTemplate.multi();
        redisTemplate.boundValueOps(kcKey).decrement(1L);
        redisTemplate.opsForSet().add(userKey, userId);
        List exec = redisTemplate.exec();
        if(exec == null || exec.size()==0) {
            System.out.println("秒杀失败了....");
            return R.failed("秒杀失败了....");
        }
        System.out.println("秒杀成功");
        return R.ok("秒杀成功");
    }

    public R SpikeGoodsWithLUA(String userId, String goodId){
        String kcKey = "sk:" + goodId + ":qt";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(SpikeLUAString, Long.class);
        Object result = redisTemplate.execute(redisScript, Collections.singletonList(kcKey), userId, goodId);
        System.out.println(result);
        return null;
    }
}
