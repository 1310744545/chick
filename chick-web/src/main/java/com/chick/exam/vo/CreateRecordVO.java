package com.chick.exam.vo;

import lombok.Data;

/**
 * @ClassName CreateRecordVO
 * @Author xiaokexin
 * @Date 2023-02-15 13:25
 * @Description CreateRecordVO
 * @Version 1.0
 */
@Data
public class CreateRecordVO {
    private String type;
    private String realId;
    private String examId;
    private String subjectId;
    private String detailId;
    private String answerType; //1、不限，2、未做题，3、已做题
    private Integer count;
}
