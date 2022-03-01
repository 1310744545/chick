package com.chick.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @ClassName JedisPoolUtil
 * @Author xiaokexin
 * @Date 2021/12/14 22:37
 * @Description 此方法是针对没有用springboot的情况
 * @Version 1.0
 */
public class JedisPoolUtil {
    private static volatile JedisPool jedisPool = null;

    private JedisPoolUtil(){

    }

    public static JedisPool getJedisPoolInstance(){
        if (jedisPool == null){
            synchronized (JedisPoolUtil.class){
                if (jedisPool == null){
                    JedisPoolConfig poolConfig = new JedisPoolConfig();
                    poolConfig.setMaxTotal(200);
                    poolConfig.setMaxIdle(32);
                    poolConfig.setMaxWaitMillis(100 * 1000);
                    poolConfig.setBlockWhenExhausted(true);
                    poolConfig.setTestOnBorrow(true);

                    jedisPool = new JedisPool(poolConfig, "47.93.35.215", 6379, 60000, "qq10086..");
                }
            }
        }
        return jedisPool;
    }

    public static void release(JedisPool jedisPool, Jedis jedis){
        if (jedis != null){
            jedisPool.returnResource(jedis);
        }
    }
}
