package com.chick.jedis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @ClassName jedisDemo01
 * @Author xiaokexin
 * @Date 2021/12/12 21:14
 * @Description jedis基础示例
 * @Version 1.0
 */
public class JedisDemoBase {
    private static Jedis jedis;

    static {
        Jedis jedisParam = new Jedis("47.93.35.215", 6379);
        jedisParam.auth("qq10086..");
        jedis = jedisParam;
    }


    public static void main(String[] args) {
        Jedis jedis = new Jedis("47.93.35.215", 6379);
        jedis.auth("qq10086..");
        String ping = jedis.ping();
        System.out.println(ping);
    }

    //操作key String
    @Test
    public void demoString() {
        //set
        jedis.set("xkx", "goodMan");
        //get
        String xkx = jedis.get("xkx");
        System.out.println(xkx);
        //设置多个key-value
        jedis.mset("k1", "v1", "k2", "v2");
        List<String> mGet = jedis.mget("k1", "k2");
        System.out.println(mGet);
        //获取所有key
        Set<String> keys = jedis.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }
    }

    //操作list
    @Test
    public void demoList() {
        jedis.lpush("key1", "java", "c++", "c", "c#");
        List<String> values = jedis.lrange("key1", 0, -1);
        System.out.println(values);
    }

    //操作set
    @Test
    public void demoSet() {
        jedis.sadd("keySet", "java", "c++", "c", "c#");
        Set<String> valueSet = jedis.smembers("keySet");
        System.out.println(valueSet);
    }

    //操作hash
    @Test
    public void demoHash() {
        jedis.hset("hash1", "userName", "xkx");
        System.out.println(jedis.hget("hash1", "userName"));
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("userName", "lisi");
        paramMap.put("password", "qq10086..");
        paramMap.put("age", "18");
        jedis.hmset("hash2", paramMap);
        List<String> hashMGets = jedis.hmget("hash2", "password", "age");
        for (String hashMGet : hashMGets){
            System.out.println(hashMGet);
        }
    }

    //操作ZSet
    @Test
    public void demoZSet() {
        jedis.zadd("ZSet1", 100d, "shanghai");
        jedis.zadd("ZSet1", 90d, "beijing");
        jedis.zadd("ZSet1", 80d, "anda");
        jedis.zadd("ZSet1", 70d, "shandong");
        Set<String> zSet1 = jedis.zrange("ZSet1", 0, -1);
        for (String e : zSet1){
            System.out.println(e);
        }
    }
}
