package com.chick.jedis;

import com.chick.base.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName RedisTestController
 * @Author xiaokexin
 * @Date 2021/12/13 21:17
 * @Description RedisTestController
 * @Version 1.0
 */
@Controller
@RequestMapping("/studyRedis")
public class RedisTestController {

    private final RedisTemplate redisTemplate;

    @Autowired
    public RedisTestController(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/test")
    @ResponseBody
    public R testRedis(){
        redisTemplate.opsForValue().set("name", "lucky");
        Object name = redisTemplate.opsForValue().get("name");
        return R.ok(name);
    }
}
