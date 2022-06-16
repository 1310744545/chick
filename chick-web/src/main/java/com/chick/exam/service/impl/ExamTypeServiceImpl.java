package com.chick.exam.service.impl;

import com.chick.exam.entity.ExamType;
import com.chick.exam.mapper.ExamTypeMapper;
import com.chick.exam.service.ExamTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 考试级别/类别 服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-14
 */
@Service
public class ExamTypeServiceImpl extends ServiceImpl<ExamTypeMapper, ExamType> implements ExamTypeService {

}
