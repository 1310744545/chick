package com.chick.util;

import java.util.*;

/**
 * @ClassName ExamUtils
 * @Author xiaokexin
 * @Date 2023-02-16 15:25
 * @Description ExamUtils
 * @Version 1.0
 */
public class ExamUtils {
    /**
     * 随机取值
     *
     * @param list   随机取值对象
     * @param length 取值个数
     * @return
     */
    public static List<String> randomValues(List<String> list, int length) {
        Set<String> randomSource = new HashSet<String>(list);
        //重复数据防越界
        if (randomSource.size() <= length) {
            return new ArrayList<String>(randomSource);
        }
        List<String> randomResult = new ArrayList<>(randomSource);

        Set<String> randomList = new HashSet<String>();
        Random random = new Random();
        while (randomList.size() < length) {
            randomList.add(randomResult.get(random.nextInt(randomResult.size())));
        }
        return new ArrayList<>(randomList);
    }
}
