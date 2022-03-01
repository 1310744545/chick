package com.chick.jedis;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName JedisClusterDemo
 * @Author xiaokexin
 * @Date 2021/12/19 0:16
 * @Description redis集群操作
 * @Version 1.0
 */
public class JedisClusterDemo {
    @Autowired
    private JedisCluster jedisCluster;

    public static void main(String[] args) {
        Set<HostAndPort> set = new HashSet<>();
        set.add(new HostAndPort("192.168.31.211", 6379));
        JedisCluster jedisCluster = new JedisCluster(set);
        jedisCluster.set("k1", "v1");
        System.out.println(jedisCluster.get("k1"));
    }
}
