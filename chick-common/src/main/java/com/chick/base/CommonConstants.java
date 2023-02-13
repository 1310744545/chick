package com.chick.base;

/**
 * @ClassName CommonConstant
 * @Author 肖可欣
 * @Descrition 公共常量
 * @Create 2022-05-27 10:57
 */
public interface CommonConstants {
    /**
     *是否
     */
    String YES = "1";
    String NO = "0";

    /**
     * 成功标记，失败标记
     */
    Integer SUCCESS = 0;
    Integer FAIL = 1;

    /**
     * 系统
     */
    String DICTIONARY = "dictionary";
    String CONFIG = "config";

    String SYS_ROLE_PATH = "SYS:ROLE";

    //源码
    String SOURCE_CODE = "Source code (tar.gz)";

    /**
     * 用户返回信息
     */
    String ACCESS_IS_DENIED = "权限不足";
    String UNAUTHORIZED = "用户未登录,需登录";

    /**
     * 最大姓名数量
     */
    int MAX_NAME_LENGTH = 50;

    /**
     * 锁定标记
     */
    String LOCK_FLAG = "0";
    String UNLOCK_FLAG = "1";

    /**
     * 禁用标记
     */
    String ENABLED_FLAG = "0";
    String UN_ENABLED_FLAG = "1";

    /**
     * 删除标记
     */
    String UN_DELETE_FLAG = "0";
    String DELETE_FLAG = "1";

    /**
     * 题目正确性标记
     */
    String EXAM_ANSWER_RIGHT = "0";
    String EXAM_ANSWER_FAIL = "1";

    /**
     * 阿里云key与secret的名字
     */
    String aliKey = "accessKeyId";
    String aliSecret = "accessKeySecret";

    /* 角色相关 */
    /**
     * 运营管理
     */
    //String ROLE_ADMIN = "ROLE_ADMIN";
    String HAS_ROLE_ADMIN = "hasRole('ROLE_ADMIN')";


    /* 类型相关 */
    /**
     * 1、后台菜单
     * 2、接口分类
     * 3、接口
     */
    String MENU_TYPE_BACKSTAGE = "1";
    String MENU_TYPE_INTERFACE_TYPE = "2";
    String MENU_TYPE_INTERFACE = "3";

    String USER_CHAPTER_CODE_REGISTER = "user:verification:code:register:";
    String USER_CHAPTER_CODE_LOGIN = "user:verification:code:login:";


    /**
     * 1、其他方式注册默认的用户名密码
     */
    String DEFAULT_USERNAME = "xkx_default_uid_";
    String DEFAULT_PASSWORD = "cRJWl*:kwxrslET'Kf3-7Y29mR56Gf";

    /**
     * 考试记录类型
     */
    String REAL = "1";
    String SIMULATION_TEST = "2";
    String INTELLIGENT_EXERCISE = "3";

    /**
     * 是否作答
     */
    String ANSWERED = "0";
    String UN_ANSWERED = "1";

    /**
     * 角色类型
     */
    String ROLE_COMMON = "4";
    String ROLE_VIP = "3";
    String ROLE_MANAGER = "2";
    String ROLE_ADMIN = "1";
}
