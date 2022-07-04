package com.chick.exam.service.impl;

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
import com.chick.exam.vo.ExamRealVO;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.map.HashedMap;
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

    @Override
    public R rKPassReptile() {
        // 获取随机cookie
        HttpResponse cookieHtml = HttpUtil.createGet("http://www.rkpass.cn//login.jsp?formlogin_back_url=tk_index.jsp___CurrentPage(((0---year(((---px_type(((0---kemu_id(((2")
                .execute();
        String header = cookieHtml.header("Set-Cookie");

        // 登录
        HashedMap<String, Object> param = new HashedMap<>();
        param.put("formlogin_back_url", "tk_index.jsp___CurrentPage(((0---year(((---px_type(((0---kemu_id(((0");
        param.put("username_1", "1310744545@qq.com");
        param.put("password", "qq10086..");
        param.put("x", "41");
        param.put("y", "16");
        HttpUtil.createGet("http://www.rkpass.cn//dealwithcenter.jsp?action=login&zhudenglu=1").form(param).execute();
        String cookie = header.split(";")[0];
        System.out.println(cookie);
        //考试类别 软考
        Exam exam = examMapper.selectOne(Wrappers.<Exam>lambdaQuery()
                .eq(Exam::getName, SoftwareExamUtil.SOFTWARE_EXAM_NAME));

        RKPassEvent rkPassEvent = new RKPassEvent();
        for (String key : SoftwareExamUtil.examDetailMap.keySet()) {
            // 切换考试科目
            HashMap<String, Object> paramSubject = new HashMap<>();
            paramSubject.put("user_id", "382549");
            paramSubject.put("study_kemu_bg", SoftwareExamUtil.examTypeMap.get(key));
            HttpUtil.createGet("http://www.rkpass.cn/dealwithcenter.jsp?action=bgkm").form(paramSubject).header("Cookie", cookie).execute();
            // 然后解析
            examReptile(rkPassEvent, cookie, exam, SoftwareExamUtil.examTypeMap.get(key).split("_")[0]);
        }

        return R.ok();
    }

    private R examReptile(ExamReptileEvent examReptileEvent, String cookie, Exam exam, String examTypeName) {
        int examPageTotal = examReptileEvent.getExamPageTotal(cookie);
        if (examPageTotal == -1) {
            log.error("页数解析失败");
            return R.failed("解析失败");
        }
        for (int i = 0; i < examPageTotal; examPageTotal++) {
            List<ExamRealVO> examReals = examReptileEvent.getExamReals(i, cookie);
            ExamType examTypeSelect = examTypeMapper.selectOne(Wrappers.<ExamType>lambdaQuery()
                    .eq(ExamType::getExamId, exam.getId())
                    .eq(ExamType::getTypeName, examTypeName)
                    .eq(ExamType::getDelFlag, CommonConstants.UN_DELETE_FLAG));
            if (ObjectUtils.isEmpty(examTypeSelect)) {
                ExamType examType = new ExamType();
                examType.setId(DoId());
                examType.setExamId(exam.getId());
                examType.setTypeName(examTypeName);
                examTypeSelect = examType;
                examTypeMapper.insert(examType);
                return R.failed("考试类别匹配错误");
            }
            for (ExamRealVO examRealVO : examReals) {
                // 查询考试详情
                ExamDetail examDetail = examDetailMapper.selectOne(Wrappers.<ExamDetail>lambdaQuery()
                        .eq(ExamDetail::getExamId, exam.getId())
                        .eq(ExamDetail::getExamTypeId, examTypeSelect.getId())
                        .eq(ExamDetail::getDelFlag, CommonConstants.UN_DELETE_FLAG));
                // 获取科目
                ExamSubject examSubjectSelect = examSubjectMapper.selectOne(Wrappers.<ExamSubject>lambdaQuery()
                        .eq(ExamSubject::getExamId, exam.getId())
                        .eq(ExamSubject::getDetailId, examDetail.getExamId())
                        .eq(ExamSubject::getName, examRealVO.getName())
                        .eq(ExamSubject::getDelFlag, CommonConstants.UN_DELETE_FLAG));
                if (ObjectUtils.isEmpty(examSubjectSelect)) {
                    log.error("科目为空，插入");
                    ExamSubject examSubject = new ExamSubject();
                    examSubject.setId(DoId());
                    examSubject.setExamId(exam.getId());
                    examSubject.setDetailId(examDetail.getId());
                    examSubject.setName(examRealVO.getName());
                    examSubject.setSort("");
                    examSubject.setDelFlag(CommonConstants.UN_DELETE_FLAG);
                    examSubjectSelect = examSubject;
                    examSubjectMapper.insert(examSubject);
                }
                // 获取试题类型(所属知识点)
                ExamQuestionType examQuestionType = examQuestionTypeMapper.selectOne(Wrappers.<ExamQuestionType>lambdaQuery()
                        .eq(ExamQuestionType::getExamId, exam.getId())
                        .eq(ExamQuestionType::getDetailId, examDetail.getId())
                        .eq(ExamQuestionType::getSubjectId, examSubjectSelect.getId())
                        .eq(ExamQuestionType::getDelFlag, CommonConstants.UN_DELETE_FLAG));

            }
        }
        return null;
    }
}
