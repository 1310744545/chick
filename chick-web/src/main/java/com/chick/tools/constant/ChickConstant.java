package com.chick.tools.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @Author 肖可欣
 * @Description Chick常量
 * @Create 2021-01-21 10:10
 */
public interface ChickConstant {
    /**
     *小写字母
     */
    List<String> LOWERCASE = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
            "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");


    /**
     *大写字母
     */
    List<String> UPPERCASE = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
    /**
     *数字
     */
    List<String> NUMBER = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9");

    /**
     *特殊字符
     */
    List<String> SPECIAL_CHARACTER = Arrays.asList("!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "-", "+", "=", ",", "<", ".",
            ">", "/", "?", ";", ":", "'", "[", "]", "{", "}", "|", "~");

    /**
     *不易识别字符
     */
    List<String> RUBBISH_CHARACTER = Arrays.asList("1", "i", "I", "0", "o", "O");


    String COMMON_TYPE = "00000000-0000-0000-0000-000000000000";

}
