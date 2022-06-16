package com.chick.exam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.exam.entity.ExamSubject;
import com.chick.exam.mapper.ExamSubjectMapper;
import com.chick.exam.service.ExamSubjectService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 考试科目 服务实现类
 * </p>
 *
 * @author xiaokexin
 * @since 2022-06-15
 */
@Service
public class ExamSubjectServiceImpl extends ServiceImpl<ExamSubjectMapper, ExamSubject> implements ExamSubjectService {

}
