package com.chick.comics.service.impl;

import com.chick.comics.entity.Comics;
import com.chick.comics.mapper.ComicsMapper;
import com.chick.comics.service.ComicsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 漫画 服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-27
 */
@Service
public class ComicsServiceImpl extends ServiceImpl<ComicsMapper, Comics> implements ComicsService {

}
