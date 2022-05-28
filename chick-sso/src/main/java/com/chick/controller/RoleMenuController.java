package com.chick.controller;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 角色菜单配置表  前端控制器
 * </p>
 *
 * @author 肖可欣
 * @since 2022-05-27
 */
@RestController
@RequestMapping("/sys/roleMenu")
public class RoleMenuController extends BaseController {

//    @RequestMapping("/test")
//    public R upload(@RequestParam("file") MultipartFile multipartFile) throws Exception {
//        ImportParams params = new ImportParams();
//        params.setHeadRows(8);
//        List<question> result = ExcelImportUtil.importExcel(multipartFile.getInputStream(),
//                question.class, params);
//        return R.ok();
//    }
//    @RequestMapping("/test2")
//    public R upload1(@RequestParam("file") MultipartFile multipartFile) throws Exception {
//        ImportParams params = new ImportParams();
//        params.setHeadRows(8);
//        List<question> result = ExcelImportUtil.importExcel(multipartFile.getInputStream(),
//                question.class, params);
//        return R.ok();
//    }
//    @RequestMapping("/test3")
//    public R upload2(@RequestParam("file") MultipartFile multipartFile) throws Exception {
//        ImportParams params = new ImportParams();
//        params.setHeadRows(8);
//        List<question> result = ExcelImportUtil.importExcel(multipartFile.getInputStream(),
//                question.class, params);
//        return R.ok();
//    }
}
