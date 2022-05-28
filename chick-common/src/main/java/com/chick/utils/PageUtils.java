package com.chick.utils;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.experimental.UtilityClass;

/**
 * @ClassName PageUtils
 * @Author xiaokexin
 * @Date 2022-05-27 19:21
 * @Description PageUtils
 * @Version 1.0
 */
@UtilityClass
public class PageUtils {
    private final int MAX_PAGE_SIZE = 20;

    public <T> Page<T> validPage(Integer current, Integer size) {
        if (ObjectUtil.isNull(size) || size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        if (size == 0) {
            size = MAX_PAGE_SIZE;
        }
        if (ObjectUtil.isNull(current)) {
            current = 1;
        }
        return new Page<T>(current, size);
    }
}
