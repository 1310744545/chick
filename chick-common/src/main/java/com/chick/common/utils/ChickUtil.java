package com.chick.common.utils;

import java.util.UUID;

/**
 * @ClassName ChickUtil
 * @Author xiaokexin
 * @Date 2022/2/24 17:21
 * @Description ChickUtil
 * @Version 1.0
 */
public class ChickUtil {

    /**
    * @Author xiaokexin
    * @Description 生成无-的id
    * @Date 2022/2/24 17:23
    * @Param []
    * @return java.lang.String
    **/
    public static String DoId(){
        return UUID.randomUUID().toString().replace("-","");
    }


}
