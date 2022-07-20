package com.chick.novel.service.impl;

import com.chick.novel.entity.Novel;
import com.chick.novel.mapper.NovelMapper;
import com.chick.novel.service.NovelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 小说 服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-07-11
 */
@Service
public class NovelServiceImpl extends ServiceImpl<NovelMapper, Novel> implements NovelService {

}
