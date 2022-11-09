package com.chick.novel.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chick.base.R;
import com.chick.exam.entity.Exam;
import com.chick.novel.entity.Novel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 小说 服务类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-07-11
 */
public interface NovelService extends IService<Novel> {

    R get(Page<Novel> validPage, String type, String keyword);

    R getThirdPart(Page<Novel> validPage, String thirdPartType, String type, String keyword);
}
