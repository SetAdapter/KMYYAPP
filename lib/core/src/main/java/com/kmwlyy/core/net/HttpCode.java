package com.kmwlyy.core.net;

/**
 * Created by Administrator on 2016/8/5.
 */
public class HttpCode {

    public static String APP_ID = "Andfamilyplatform";
    public static String APP_KEY = "platform@km2017_Test";
    public static String APP_SECRET = "platsforme@ckm2017_Test";

    // 返回编码
    /**
     * 操作成功
     */
    public static final int RESPONSE_SUCCESS = 0;

    /**
     * 异常
     */
    public static final int RESPONSE_FAIL = 1;
    /**
     * 获取token时验证失败
     */
    public static final int TOKEN_FAIL = 2;
    /**
     * 非法请求
     */
    public static final int REQUEST_INVAILD = 3;
    /**
     * appToken失效或过期
     */
    public static final int TOKEN_EXPIRED = 5;
    /**
     * 用户未登录或登录过期
     */
    public static final int USER_NOT_LOGIN = 6;
    /**
     * 登录用户无权限访问该接口
     */
    public static final int USER_NO_AUTHORITY = 7;



    /**
     * 系统错误
     */
    public static final int ERROR_SYSTEM = 10001;

    /**
     * 暂停服务
     */
    public static final int ERROR_OUT_OF_SERVICE = 10002;

    /**
     * 处理超时
     */
    public static final int ERROR_TIME_OUT = 10003;

    /**
     * 非法请求
     */
    public static final int ERROR_REQUEST = 10004;

    /**
     * 用户授权不可为空
     */
    public static final int ERROR_TOKEN_NOT_NULL = 10005;

    /**
     * 用户授权过期
     */
    public static final int ERROR_TOKEN_TIMEOUT = 10006;

    /**
     * 请求参数错误
     */
    public static final int ERROR_PARAMETER_ERROR = 10007;

    /**
     * 业务异常
     */
    public static final int ERROR_BISNUESS_EXCEPTION = 10008;

    /**
     * 请求的客户端版本号为空
     */
    public static final int ERROR_VERSION_NULL = 10009;

    /**
     * 当前有新版本，需要更新才能使用
     */
    public static final int ERROR_VERSION_UPDATE = 10010;
}
