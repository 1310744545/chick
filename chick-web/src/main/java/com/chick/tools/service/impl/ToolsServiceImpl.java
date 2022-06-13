package com.chick.tools.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.tools.constant.ChickConstant;
import com.chick.tools.entity.Tools;
import com.chick.tools.mapper.ToolsMapper;
import com.chick.tools.service.IToolsService;
import com.chick.tools.vo.ToolsVO;
import com.chick.util.QRCodeUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


/**
 * <p>
 * 在线工具表 服务实现类
 * </p>
 *
 * @author 肖可欣
 * @since 2021-02-28
 */
@Service
public class ToolsServiceImpl extends ServiceImpl<ToolsMapper, Tools> implements IToolsService {

    @Resource
    private ToolsMapper toolsMapper;
    /**
     * 获取在线工具列表
     *
     * @param validPage 分页信息
     * @param keyword   关键字
     * @param delFlag   删除标记
     * @return 分页列表
     */
    @Override
    public Page<ToolsVO> list(Page<ToolsVO> validPage, String keyword, String delFlag, String type) {
        Page<ToolsVO> list = toolsMapper.getList(validPage, keyword, delFlag, type);
        return toolsMapper.getList(validPage, keyword, delFlag, type);
//        LambdaQueryWrapper<Tools> wrapper = Wrappers.<Tools>lambdaQuery()
//                .eq(Tools::getDelFlag, delFlag)
//                .orderByAsc(Tools::getCreateDate);
//        //3.添加关键字
//        if (StringUtils.isNotBlank(keyword)) {
//            wrapper.and(wr -> wr.like(Tools::getName, keyword));
//        }
//        return baseMapper.selectPage(validPage, wrapper);
    }

    /**
     * 删除或恢复工具
     *
     * @param toolId 文件id
     * @return R
     */
    @Override
    public R deleteOrRenew(String toolId, String delFlag) {
        int update = baseMapper.update(null, Wrappers.<Tools>lambdaUpdate()
                .eq(Tools::getId, toolId)
                .set(Tools::getDelFlag, CommonConstants.DELETE_FLAG.equals(delFlag) ? CommonConstants.UN_DELETE_FLAG:CommonConstants.DELETE_FLAG));
        if (update > 0 && CommonConstants.DELETE_FLAG.equals(delFlag)){
            return R.ok("删除成功");
        }else if (update > 0 && CommonConstants.UN_DELETE_FLAG.equals(delFlag)){
            return R.ok("恢复成功");
        }else {
            return R.failed("系统错误,请联系站长");
        }
    }

    /**
     * 生成UUID
     *
     * @param count 生成个数
     * @return UUID
     */
    @Override
    public List<String> generateUUID(Integer count) {
        ArrayList<String> UUIDList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            UUIDList.add(UUID.randomUUID().toString());
        }
        return UUIDList;
    }

    /**
     * 生成随机密码
     *
     * @param count            生成个数
     * @param numberCount      密码位数
     * @param smallLetter      小写字母
     * @param bigLetter        大写字母
     * @param number           数字
     * @param specialCharacter 特殊字符
     * @param rubbishCharacter 去除不易识别字符
     * @return 随机密码
     */
    @Override
    public List<String> generateRandomCipher(Integer count, Integer numberCount, Boolean smallLetter, Boolean bigLetter, Boolean number, Boolean specialCharacter, Boolean rubbishCharacter) {
        //存储用户选择的字符
        List<String> characterList = new ArrayList<>();
        if (smallLetter) {
            characterList.addAll(ChickConstant.LOWERCASE);
        }
        if (bigLetter) {
            characterList.addAll(ChickConstant.UPPERCASE);
        }
        if (number) {
            characterList.addAll(ChickConstant.NUMBER);
        }
        if (specialCharacter) {
            characterList.addAll(ChickConstant.SPECIAL_CHARACTER);
        }
        //是否去除不易识别字符
        if (rubbishCharacter) {
            for (String c : ChickConstant.RUBBISH_CHARACTER) {
                if (characterList.contains(c)) {
                    characterList.remove(c);
                }
            }
        }
        //计算结果
        List<String> resultList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            StringBuffer resultString = new StringBuffer();
            for (int j = 0; j < numberCount; j++) {
                int n = random.nextInt(characterList.size());
                resultString.append(characterList.get(n));
            }
            resultList.add(resultString.toString());
        }
        return resultList;
    }

    /**
     * base64编码/解码
     *
     * @param code base64编码/解码的字符
     * @param flag 0编码 1解码
     * @return 编码/解码后的字符
     */
    @Override
    public R base64EncodeOrDecode(String code, String flag) {
        if ("0".equals(flag)) {
            BASE64Encoder encoder = new BASE64Encoder();
            return R.ok(encoder.encode(code.getBytes(StandardCharsets.UTF_8)), "编码成功");
        }
        if ("1".equals(flag)) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                return R.ok(new String(decoder.decodeBuffer(code), StandardCharsets.UTF_8), "解码成功");
            } catch (IOException e) {
                e.printStackTrace();
                R.failed("请输入正确的编码过的base64字符");
            }
        }
        return R.failed("系统错误");
    }

    /**
     * 生成二维码
     *
     * @param textarea 内容
     */
    @Override
    public void createQRCode(String textarea, HttpServletRequest request, HttpServletResponse response) {
        byte[] captcha;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            BufferedImage bi = QRCodeUtil.encode(textarea);
            ImageIO.write(bi, "jpg", out);

            captcha = out.toByteArray();
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            ServletOutputStream sout = response.getOutputStream();
            sout.write(captcha);
            sout.flush();
            sout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 识别二维码
     *
     * @param file 二维码
     */
    @Override
    public R distinguishQRCode(MultipartFile file) {
        File toFile = null;
        String content ;
        try {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
            content = QRCodeUtil.decode(toFile);
        } catch (Exception e) {
            return R.failed("请上传正确的二维码");
        }
        ArrayList<String> list = new ArrayList<>();
        list.add(content);
        return R.ok(list);
    }


    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
