package com.chick.software.controller;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.chick.base.R;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaokexin
 * @since 2022-03-01
 */
@RestController
@RequestMapping("/software")
public class SoftwareController {

    @RequestMapping("/test")
    public R upload(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        ImportParams params = new ImportParams();
        params.setHeadRows(8);
        List<question> result = ExcelImportUtil.importExcel(multipartFile.getInputStream(),
                question.class, params);
        return R.ok();
    }
}

