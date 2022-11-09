package com.chick.novel.vo;

import lombok.Data;

/**
 * @ClassName NovelContentVO
 * @Author xiaokexin
 * @Date 2022-11-09 16:43
 * @Description NovelContentVO
 * @Version 1.0
 */
@Data
public class NovelContentVO {
    private String title;
    private String content;

    private String bookId;
    private String pre;
    private String next;
}
