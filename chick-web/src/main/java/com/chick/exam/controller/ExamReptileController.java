package com.chick.exam.controller;

import com.chick.base.R;
import com.chick.exam.service.ExamReptileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName ExamReptileController
 * @Author xiaokexin
 * @Date 2022-07-02 18:56
 * @Description ExamReptileController
 * @Version 1.0
 */
@RestController
@RequestMapping("examReptile")
public class ExamReptileController {

    @Resource
    private ExamReptileService examReptileService;

    @GetMapping("rKPassReptile")
    public R rKPassReptile(Integer page){
        return examReptileService.rKPassReptile(page);
    }
}
