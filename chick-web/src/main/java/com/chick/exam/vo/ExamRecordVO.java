package com.chick.exam.vo;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName ExamRecordVO
 * @Author xiaokexin
 * @Date 2023-02-13 22:14
 * @Description ExamRecordVO
 * @Version 1.0
 */
@Data
public class ExamRecordVO {
    private String id;
    private String realId;
    private String type;
    private String realName;
    private Integer countAll; //总题数
    private Integer countDo;  //已做题数
    private Integer countRight;  //正确数
    private String allQuestion;
    private String doQuestion;
    private Date createDate;
    private Date updateDate;
}
