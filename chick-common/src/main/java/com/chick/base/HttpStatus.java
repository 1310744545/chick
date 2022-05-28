package com.chick.base;

/**
 * 返回状态码
 *
 * @author Sky
 */
public interface HttpStatus {
    /**
     * 操作成功
     */
    public static final int SUCCESS = 200;

    /**
     * 对象创建成功
     */
    public static final int CREATED = 201;

    /**
     * 请求已经被接受
     */
    public static final int ACCEPTED = 202;

    /**
     * 操作已经执行成功，但是没有返回数据
     */
    public static final int NO_CONTENT = 204;

    /**
     * 资源已被移除
     */
    public static final int MOVED_PERM = 301;

    /**
     * 重定向
     */
    public static final int SEE_OTHER = 303;

    /**
     * 资源没有被修改
     */
    public static final int NOT_MODIFIED = 304;

    /**
     * 参数列表错误（缺少，格式不匹配）
     */
    public static final int BAD_REQUEST = 400;

    /**
     * 未授权
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 访问受限，授权过期
     */
    public static final int FORBIDDEN = 403;

    /**
     * 资源，服务未找到
     */
    public static final int NOT_FOUND = 404;

    /**
     * 不允许的http方法
     */
    public static final int BAD_METHOD = 405;

    /**
     * 资源冲突，或者资源被锁
     */
    public static final int CONFLICT = 409;

    /**
     * 不支持的数据，媒体类型
     */
    public static final int UNSUPPORTED_TYPE = 415;

    /**
     * 系统内部错误
     */
    public static final int ERROR = 500;

    /**
     * 接口未实现
     */
    public static final int NOT_IMPLEMENTED = 501;

    /**
     * 系统错误码定义
     * 例：101101
     * 101表示系统管理的用户管理， 1（0校验1查询2新增3修改4删除5其他） 01用户列表报错
     */
    /**
     * -----用户管理相关-----
     */
    /**
     * 校验密码复杂度
     */
    public static final int SYS_USER_CHECK_PWD = 101001;
    /**
     * 校验用户
     */
    public static final int SYS_USER_CHECK_USER = 101002;
    /**
     * 新增用户
     */
    public static final int SYS_USER_INSERT_USER = 101201;
    /**
     * 修改用户
     */
    public static final int SYS_USER_EDIT_USER = 101301;
    /**
     * 重置密码
     */
    public static final int SYS_USER_EDIT_PWD = 101302;
    /**
     * -----字典管理相关-----
     */
    /**
     * 校验字典
     */
    public static final int SYS_DICT_CHECK_DICT = 102001;
    /**
     * 新增字典
     */
    public static final int SYS_DICT_INSERT_DICT = 102201;
    /**
     * 修改字典
     */
    public static final int SYS_DICT_EDIT_DICT = 102301;
    /**
     * -----机构管理相关-----
     */
    /**
     * 机构重复校验
     */
    public static final int SYS_DEPT_CHECK_DEPT = 103001;
    /**
     * 新增机构
     */
    public static final int SYS_DEPT_INSERT_DEPT = 103201;
    /**
     * 修改机构
     */
    public static final int SYS_DEPT_EDIT_DEPT = 103301;
    /**
     * 删除机构
     */
    public static final int SYS_DEPT_DELETE_DEPT = 103401;
    /**
     * -----岗位管理相关-----
     */
    /**
     * 岗位重复校验
     */
    public static final int SYS_POST_CHECK_POST = 104001;
    /**
     * 新增岗位
     */
    public static final int SYS_POST_INSERT_POST = 104201;
    /**
     * 修改岗位
     */
    public static final int SYS_POST_EDIT_POST = 104301;
    /**
     * 删除岗位
     */
    public static final int SYS_POST_DELETE_POST = 104401;
    /**
     * -----职务管理相关-----
     */
    /**
     * 职务重复校验
     */
    public static final int SYS_DUTY_CHECK_DUTY = 105001;
    /**
     * 新增职务
     */
    public static final int SYS_DUTY_INSERT_DUTY = 105201;
    /**
     * 修改职务
     */
    public static final int SYS_DUTY_EDIT_DUTY = 105301;
    /**
     * 删除职务
     */
    public static final int SYS_DUTY_DELETE_DUTY = 105401;

    /**
     * -----菜单管理相关-----
     */
    /**
     * 菜单重复校验
     */
    public static final int SYS_MENU_CHECK_MENU = 106001;
    /**
     * 新增菜单
     */
    public static final int SYS_MENU_INSERT_MENU = 106201;
    /**
     * 修改菜单
     */
    public static final int SYS_MENU_EDIT_MENU = 106301;
    /**
     * 删除菜单
     */
    public static final int SYS_MENU_DELETE_MENU = 106401;
    /**
     * -----角色管理相关-----
     */
    /**
     * 角色重复校验
     */
    public static final int SYS_ROLE_CHECK_ROLE = 107001;
    /**
     * 新增角色
     */
    public static final int SYS_ROLE_INSERT_ROLE = 107201;
    /**
     * 修改角色
     */
    public static final int SYS_ROLE_EDIT_ROLE = 107301;
    /**
     * 删除菜单
     */
    public static final int SYS_ROLE_DELETE_ROLE = 107401;
    /**
     * -----安全管理相关-----
     */

    /**
     * -----参数设置相关-----
     */
    /**
     * 参数配置检查
     */
    public static final int SYS_CONFIG_CHECK_CONFIG = 108001;
    /**
     * 新增参数配置
     */
    public static final int SYS_CONFIG_INSERT_CONFIG = 108201;
    /**
     * 修改参数配置
     */
    public static final int SYS_CONFIG_EDIT_CONFIG = 108301;
    /**
     * 删除参数配置
     */
    public static final int SYS_CONFIG_DELETE_CONFIG = 108401;
    /**
     * -----操作日志相关-----
     */

    /**
     * -----在线用户相关-----
     */

    /**
     * -----登录日志相关-----
     */

    /**
     * -----账号密码登录相关-----
     */
    /**
     * 登录出错
     */
    public static final int SYS_USERNAME_LOGIN_ERROR = 200001;


    /**
     * -----手机号登录相关-----
     *
     */
    /**
     * 短信登录出错
     */
    public static final int SYS_SMS_LOGIN_ERROR = 200002;

    /**
     * -----首页常用菜单相关-----
     */
    /**
     * -----首页卡片相关-----
     */

    /**
     * -----首页自定义目录相关-----
     */

    /**
     * -----首页个人中心相关-----
     */


    /**
     * -----系统工具-代码生成相关-----
     */

}
