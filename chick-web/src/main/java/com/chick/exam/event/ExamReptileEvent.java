package com.chick.exam.event;

import com.chick.exam.entity.Exam;
import com.chick.exam.entity.ExamDetail;
import com.chick.exam.vo.ExamQuestionDetailVO;
import com.chick.exam.vo.ExamQuestionVO;
import com.chick.exam.vo.ExamRealVO;

import java.util.List;

/**
 * @ClassName ExamReptileEvent
 * @Author xiaokexin
 * @Date 2022-07-02 18:58
 * @Description ExamReptileEvent
 * @Version 1.0
 */
public interface ExamReptileEvent {

    /**
     * @Author xkx
     * @Description 解析页面的总页数
     * @Date 2022-06-27 14:40
     * @Param [cookie] 可以不用
     * @return int
     **/
    int getExamPageTotal(String cookie);

    /**
     * @Author xkx
     * @Description 获取某一页中的试题集合
     * @Date 2022-06-27 14:40
     * @Param [pageNum]
     * @return int
     **/
    List<ExamRealVO> getExamReals(int pageNum, String cookie);

    /**
     * @Author xkx
     * @Description 获取试题集合中的所有试题
     * @Date 2022-06-27 14:40
     * @Param [pageNum]
     * @return int
     **/
    List<ExamQuestionVO> getExamQuestions(String url, String cookie);

    /**
    * @Author xkx
    * @Description 获取试题详情
    * @Date 2022-07-05 13:17
    * @Param [url]
    * @return com.chick.exam.vo.ExamQuestionDetailVO
    **/
    ExamQuestionDetailVO getExamQuestionDetail(String url, String cookie);

}
