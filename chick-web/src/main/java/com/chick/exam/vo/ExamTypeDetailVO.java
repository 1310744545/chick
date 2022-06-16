package com.chick.exam.vo;

import com.chick.exam.entity.ExamDetail;
import lombok.Data;

import java.util.List;

/**
 * @ClassName ExamTypeDetailVO
 * @Author xiaokexin
 * @Date 2022-06-14 16:03
 * @Description ExamTypeDetailVO
 * @Version 1.0
 */
@Data
public class ExamTypeDetailVO {
    /**
     * 所属详情id
     */
    private String typeId;

    /**
     * 所属考试id
     */
    private String examId;

    /**
     * 分类名，例：初级、中级、高级
     */
    private String typeName;

    /**
     * 详情列
     */
    private List<ExamDetail> examDetailList;
}
