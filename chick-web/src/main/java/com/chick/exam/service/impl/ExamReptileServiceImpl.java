package com.chick.exam.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.exam.entity.*;
import com.chick.exam.event.ExamReptileEvent;
import com.chick.exam.event.RKPassEvent;
import com.chick.exam.mapper.*;
import com.chick.exam.service.ExamReptileService;
import com.chick.exam.utils.SoftwareExamUtil;
import com.chick.exam.vo.ExamQuestionDetailVO;
import com.chick.exam.vo.ExamQuestionVO;
import com.chick.exam.vo.ExamRealVO;
import com.chick.util.MultiPartThreadDownLoad;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

import static com.chick.common.utils.ChickUtil.DoId;

/**
 * @ClassName ExamReptileServiceImpl
 * @Author xiaokexin
 * @Date 2022-07-02 18:57
 * @Description ExamReptileServiceImpl
 * @Version 1.0
 */
@Log4j2
@Service
public class ExamReptileServiceImpl implements ExamReptileService {

    @Resource
    private ExamMapper examMapper;
    @Resource
    private ExamTypeMapper examTypeMapper;
    @Resource
    private ExamDetailMapper examDetailMapper;
    @Resource
    private ExamSubjectMapper examSubjectMapper;
    @Resource
    private ExamQuestionTypeMapper examQuestionTypeMapper;
    @Resource
    private ExamRealMapper examRealMapper;
    @Resource
    private ExamQuestionMapper examQuestionMapper;
    @Resource
    private ExamRealQuestionMapper examRealQuestionMapper;
    @Resource
    private ExamKnowledgeMapper examKnowledgeMapper;
    @Resource
    private ExamQuestionKnowledgeMapper examQuestionKnowledgeMapper;
    @Resource
    private ExamFileMapper examFileMapper;
    @Resource
    private ExamAnswerMapper examAnswerMapper;

    @Override
    public R rKPassReptile() {

        //???????????? ??????
        Exam exam = examMapper.selectOne(Wrappers.<Exam>lambdaQuery()
                .eq(Exam::getName, SoftwareExamUtil.SOFTWARE_EXAM_NAME));
        RKPassEvent rkPassEvent = new RKPassEvent();
        examReptile(rkPassEvent, "", exam);

        return R.ok();
    }


    private R examReptile(ExamReptileEvent examReptileEvent, String cookie, Exam exam) {
        int examPageTotal = examReptileEvent.getExamPageTotal(cookie);
        if (examPageTotal == -1) {
            log.error("??????????????????");
            return R.failed("????????????");
        }
        // ???????????????
        for (int i = 0; i < examPageTotal; i++) {

            // ????????????????????????????????????
            List<ExamRealVO> examReals = examReptileEvent.getExamReals(i, cookie);

            for (ExamRealVO examRealVO : examReals) {

                // ????????????
                ExamType examTypeSelect = examTypeMapper.selectOne(Wrappers.<ExamType>lambdaQuery()
                        .eq(ExamType::getExamId, exam.getId())
                        .eq(ExamType::getTypeName, SoftwareExamUtil.examLevel.get(examRealVO.getName()))
                        .eq(ExamType::getDelFlag, CommonConstants.UN_DELETE_FLAG));
                if (ObjectUtils.isEmpty(examTypeSelect)) {
                    ExamType examType = new ExamType();
                    examType.setId(DoId());
                    examType.setExamId(exam.getId());
                    examType.setTypeName(SoftwareExamUtil.examLevel.get(examRealVO.getName()));
                    examTypeSelect = examType;
                    examTypeMapper.insert(examType);
                }


                // ??????????????????
                ExamDetail examDetailSelect = examDetailMapper.selectOne(Wrappers.<ExamDetail>lambdaQuery()
                        .eq(ExamDetail::getExamId, exam.getId())
                        .eq(ExamDetail::getExamTypeId, examTypeSelect.getId())
                        .eq(ExamDetail::getDetailName, examRealVO.getName())
                        .eq(ExamDetail::getDelFlag, CommonConstants.UN_DELETE_FLAG));
                if (ObjectUtils.isEmpty(examDetailSelect)) {
                    log.info("?????????????????????");
                    ExamDetail examDetail = new ExamDetail();
                    examDetail.setId(DoId());
                    examDetail.setExamId(exam.getId());
                    examDetail.setExamTypeId(examTypeSelect.getId());
                    examDetail.setDetailName(examRealVO.getName());
                    examDetail.setOpen("1");
                    examDetail.setDelFlag(CommonConstants.UN_DELETE_FLAG);
                    examDetailSelect = examDetail;
                    examDetailMapper.insert(examDetail);
                }

                // ????????????
                ExamSubject examSubjectSelect = examSubjectMapper.selectOne(Wrappers.<ExamSubject>lambdaQuery()
                        .eq(ExamSubject::getExamId, exam.getId())
                        .eq(ExamSubject::getDetailId, examDetailSelect.getId())
                        .eq(ExamSubject::getName, examRealVO.getExamType())
                        .eq(ExamSubject::getDelFlag, CommonConstants.UN_DELETE_FLAG));
                if (ObjectUtils.isEmpty(examSubjectSelect)) {
                    log.info("?????????????????????");
                    ExamSubject examSubject = new ExamSubject();
                    examSubject.setId(DoId());
                    examSubject.setExamId(exam.getId());
                    examSubject.setDetailId(examDetailSelect.getId());
                    examSubject.setName(examRealVO.getExamType());
                    examSubject.setSort("");
                    examSubject.setDelFlag(CommonConstants.UN_DELETE_FLAG);
                    examSubjectSelect = examSubject;
                    examSubjectMapper.insert(examSubject);
                }

                // ??????
                ExamReal examRealSelect = examRealMapper.selectOne(Wrappers.<ExamReal>lambdaQuery()
                        .eq(ExamReal::getExamId, exam.getId())
                        .eq(ExamReal::getSubjectId, examSubjectSelect.getId())
                        .eq(ExamReal::getYear, examRealVO.getYear())
                        .eq(ExamReal::getDelFlag, CommonConstants.UN_DELETE_FLAG));
                if (ObjectUtils.isEmpty(examRealSelect)) {
                    log.info("?????????????????????");
                    ExamReal examReal = new ExamReal();
                    examReal.setId(DoId());
                    examReal.setExamId(exam.getId());
                    examReal.setSubjectId(examSubjectSelect.getId());
                    examReal.setYear(examRealVO.getYear());
                    examReal.setDelFlag(CommonConstants.UN_DELETE_FLAG);
                    examRealSelect = examReal;
                    examRealMapper.insert(examReal);
                }


                // ????????????
                List<ExamQuestionVO> examQuestions = examReptileEvent.getExamQuestions(examRealVO.getUrl(), cookie);

                //??????????????????
                for (ExamQuestionVO examQuestionVO : examQuestions) {

                    // ???????????????
                    ExamQuestionDetailVO examQuestionDetail = examReptileEvent.getExamQuestionDetail(examQuestionVO.getUrl(), cookie + "_" + examRealVO.getName() + "_" + examSubjectSelect.getName() + "_" + examQuestionVO.getName() + "_" + examRealVO.getYear());

                    ExamQuestionType examQuestionType;

                    // ??????????????????(???????????????)
                    List<ExamQuestionType> examQuestionTypes = examQuestionTypeMapper.selectList(Wrappers.<ExamQuestionType>lambdaQuery()
                            .eq(ExamQuestionType::getExamId, exam.getId())
                            .eq(ExamQuestionType::getDetailId, examDetailSelect.getId())
                            .eq(ExamQuestionType::getSubjectId, examSubjectSelect.getId())
                            .eq(ExamQuestionType::getDelFlag, CommonConstants.UN_DELETE_FLAG));
                    examQuestionType = null;
                    for (ExamQuestionType eqt : examQuestionTypes) {
                        if (eqt.getName().equals(examQuestionDetail.getQuestionType())) {
                            examQuestionType = eqt;
                            break;
                        }
                    }
                    if (ObjectUtils.isEmpty(examQuestionType)) {
                        ExamQuestionType examQuestionTypeDTO = new ExamQuestionType();
                        examQuestionTypeDTO.setId(DoId());
                        examQuestionTypeDTO.setExamId(exam.getId());
                        examQuestionTypeDTO.setName(examQuestionDetail.getQuestionType());
                        examQuestionTypeDTO.setDetailId(examDetailSelect.getId());
                        examQuestionTypeDTO.setSubjectId(examSubjectSelect.getId());
                        examQuestionType = examQuestionTypeDTO;
                        examQuestionTypeMapper.insert(examQuestionType);
                    }


                    // ????????????
                    ExamQuestion examQuestionSelect = examQuestionMapper.selectOne(Wrappers.<ExamQuestion>lambdaQuery()
                            .eq(ExamQuestion::getName, examQuestionDetail.getExamQuestion().getName()));
                    if (ObjectUtils.isEmpty(examQuestionSelect)) {
                        examQuestionDetail.getExamQuestion().setExamId(exam.getId());
                        examQuestionDetail.getExamQuestion().setDetailId(examDetailSelect.getId());
                        examQuestionDetail.getExamQuestion().setQuestionTypeId(examQuestionType.getId());
                        examQuestionDetail.getExamQuestion().setSubjectId(examSubjectSelect.getId());
                        examQuestionSelect = examQuestionDetail.getExamQuestion();
                        examQuestionMapper.insert(examQuestionDetail.getExamQuestion());
                    }

                    // ?????? ???????????????
                    if (SoftwareExamUtil.SOFTWARE_EXAM_COMPREHENSIVE.equals(examSubjectSelect.getName())) {
                        // ????????????
                        for (ExamAnswer examAnswer : examQuestionDetail.getExamAnswers()) {
                            ExamAnswer examAnswerSelect = examAnswerMapper.selectOne(Wrappers.<ExamAnswer>lambdaQuery()
                                    .eq(ExamAnswer::getName, examQuestionDetail.getExamQuestion().getName())
                                    .eq(ExamAnswer::getQuestionId, examQuestionSelect.getId()));
                            if (ObjectUtils.isEmpty(examAnswerSelect)) {
                                examAnswer.setQuestionId(examQuestionSelect.getId());
                                examAnswerMapper.insert(examAnswer);
                            }
                        }
                    }
                    // ??????
                    if (SoftwareExamUtil.SOFTWARE_EXAM_CASE.equals(examSubjectSelect.getName())) {

                    }
                    // ??????
                    if (SoftwareExamUtil.SOFTWARE_EXAM_DISSERTATION.equals(examSubjectSelect.getName())) {

                    }

                    // ????????????????????????????????????
                    ExamRealQuestion examRealQuestionSelect = examRealQuestionMapper.selectOne(Wrappers.<ExamRealQuestion>lambdaQuery()
                            .eq(ExamRealQuestion::getExamRealId, examRealSelect.getId())
                            .eq(ExamRealQuestion::getExamId, exam.getId())
                            .eq(ExamRealQuestion::getName, examQuestionVO.getName()));
                    if (ObjectUtils.isEmpty(examRealQuestionSelect)) {
                        ExamRealQuestion examRealQuestion = new ExamRealQuestion();
                        examRealQuestion.setId(DoId());
                        examRealQuestion.setExamId(exam.getId());
                        examRealQuestion.setExamRealId(examRealSelect.getId());
                        examRealQuestion.setSort(examQuestionDetail.getQuestionNum());
                        examRealQuestion.setName("???" + examQuestionDetail.getQuestionNum() + "???");
                        examRealQuestion.setExamQuestionId(examQuestionSelect.getId());
                        examRealQuestionMapper.insert(examRealQuestion);
                    }

                    // ?????????
                    for (ExamKnowledge examKnowledge : examQuestionDetail.getExamKnowledges()) {
                        // ???????????????
                        ExamKnowledge examKnowledgeSelect = examKnowledgeMapper.selectOne(Wrappers.<ExamKnowledge>lambdaQuery()
                                .eq(ExamKnowledge::getExamId, exam.getId())
                                .eq(ExamKnowledge::getName, examKnowledge.getName()));
                        if (ObjectUtils.isEmpty(examKnowledgeSelect)) {
                            examKnowledge.setExamId(exam.getId());
                            examKnowledgeMapper.insert(examKnowledge);
                        }

                        //?????????????????????????????????
                        ExamQuestionKnowledge examQuestionKnowledgeSelect = examQuestionKnowledgeMapper.selectOne(Wrappers.<ExamQuestionKnowledge>lambdaQuery()
                                .eq(ExamQuestionKnowledge::getKnowledgeId, examKnowledge.getId())
                                .eq(ExamQuestionKnowledge::getQuestionId, examQuestionSelect.getId()));
                        if (ObjectUtils.isEmpty(examQuestionKnowledgeSelect)) {
                            ExamQuestionKnowledge examQuestionKnowledge = new ExamQuestionKnowledge();
                            examQuestionKnowledge.setKnowledgeId(examKnowledge.getId());
                            examQuestionKnowledge.setQuestionId(examQuestionSelect.getId());
                            examQuestionKnowledgeMapper.insert(examQuestionKnowledge);
                        }

                    }

                    // ????????????
                    for (ExamFile examFile : examQuestionDetail.getExamFiles()) {
                        MultiPartThreadDownLoad.OrdinaryDownLoad(examFile.getOtherUrl(), examFile.getLocalPath());
                        // ????????????
                        ExamFile examFileSelect = examFileMapper.selectOne(Wrappers.<ExamFile>lambdaQuery()
                                .eq(ExamFile::getOtherUrl, examFile.getOtherUrl()));
                        if (ObjectUtils.isEmpty(examFileSelect)) {
                            examFileMapper.insert(examFile);
                        }
                    }
                }
            }
        }
        return null;
    }
}
