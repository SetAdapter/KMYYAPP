package com.kmwlyy.core.net;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.internal.$Gson$Types;
import com.kmwlyy.core.R;
import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.NetworkUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class HttpClient {
    public static final String TAG = "MyHttpClient";


    /**
     * 正式测试
     */
//    public static String KMYY_URL = "https://api.kmwlyy.com";
//    public static String KMYY_URL_2 = "https://commonapi.kmwlyy.com";
//    public static String DOCTOR_API_URL = "https://doctorapi.kmwlyy.com";
//    public static String IMAGE_URL = "https://store.kmwlyy.com";

    /**
     * 测试环境 （测试专用，开发人员尽量不用！）
     */
    public static String KMYY_URL = "https://tapi.kmwlyy.com:8015";
    public static String IMAGE_URL = "https://tstore.kmwlyy.com:8027";
    public static String DOCTOR_API_URL = "https://tdoctor.kmwlyy.com:8134";
    public static String FAMILY_URL = "https://tfamilyapi.kmwlyy.com:8063";



    /**        v
     * 开发环境
     */
//    public static String KMYY_URL = "http://10.2.21.216:80";
    public static String KMYY_URL_2 = "http://10.2.21.216:8200";
//    public static String DOCTOR_API_URL = "http://10.2.21.216:8101";
//    public static String IMAGE_URL = "https://tstore.kmwlyy.com:8027";

    /**
     * 预发布环境
     */
//    public static String KMYY_URL = "http://test.api.kmwlyy.com";
//    public static String IMAGE_URL = "http://test.store.kmwlyy.com";
//    public static String DOCTOR_API_URL = "http://test.doctor.kmwlyy.com";
//    public static String FAMILY_URL = "http://test.familyapi.kmwlyy.com";
    /**
     * 调试地址
     */
//    public static String KMYY_URL = "http://121.15.153.63:8016";//开发环境
//    public static String IMAGE_URL = "http://121.15.153.63:8028";

//    public static String KMYY_URL = "http://10.2.29.123";//调试地址 许光丽
//    public static String IMAGE_URL = "http://121.15.153.63:8028";

//    public static String KMYY_URL = "http://10.2.20.214:80/";//调试地址 曾璐
//    public static String IMAGE_URL = "http://121.15.153.63:8028";

//    public static String KMYY_URL = "http://10.2.21.52:8020";//调试地址 唐鸿鸣
//    public static String IMAGE_URL = "http://121.15.153.63:8028";



//    public static String KMYY_URL = "http://10.2.29.118:8002";//调试地址 郭明
//    public static String IMAGE_URL = "http://121.15.153.63:8028";


    // 常用字段
    public static final String TAG_CODE = "Status";// 返回编码

    public static final String TAG_MSG = "Msg";// 返回信息
    public static final String TAG_DATA = "Data";// 返回数据
    public static final String TAG_TOTAL = "Total";// 总条数

    public static final String HEADER_SIGN = "sign";
    public static final String HEADER_NONESTR = "noncestr";
    public static final String HEADER_APPTOKEN = "apptoken";
    public static final String HEADER_USERTOKEN = "usertoken";

    // 请求方法
    public static final int POST = 0;
    public static final int GET = 1;
    public static final int PUT = 2;
    public static final int DELETE = 3;

    // 主要属性
    private Context mContext;
    private HttpEvent mHttpEvent;
    private HttpFilter mHttpFilter;
    private HttpHandler mHttpHandler;
    private String mUrl;
    private int mRetryCount;

    public void hitRetryCount() {
        mRetryCount++;
    }

    public boolean overRetryCount() {
        return mRetryCount >= 5;
    }

    public HttpClient(Context context, HttpEvent event) {
        this(context, event, null);
    }

    public HttpClient(Context context, HttpEvent event, HttpFilter filter) {
        this(context, event, null, filter);
    }

    public HttpClient(Context context, HttpEvent event, String replaceUrl, HttpFilter filter) {
        this.mContext = context;
        this.mHttpEvent = event;
        this.mHttpFilter = filter;
        this.mUrl = replaceUrl;
        if (this.mHttpFilter != null) {
            this.mHttpFilter.setHttpClient(this);
        }
    }

    private HttpMethod getMethod(int method) {
        switch (method) {
            case POST:
                return HttpMethod.POST;
            case GET:
                return HttpMethod.GET;
            case PUT:
                return HttpMethod.PUT;
            case DELETE:
                return HttpMethod.DELETE;
            default:
                return HttpMethod.POST;
        }
    }

    private RequestParams getParams(Map<String, String> map) {

        if (map == null) {
            map = new HashMap<>();
        }

        List<String> list = new ArrayList<>();
        RequestParams params = new RequestParams();

        // 请求头部
        String noneStr = CommonUtils.createRandomString();
        String appToken = BaseApplication.instance.getAppToken();
        String userToken = BaseApplication.instance.getUserData() == null ? null : BaseApplication.instance.getUserData().mUserToken;
        String sign = CommonUtils.createSignString(noneStr, appToken, userToken);

        params.addHeader(HEADER_SIGN, sign);
        params.addHeader(HEADER_NONESTR, noneStr);
        params.addHeader(HEADER_APPTOKEN, appToken);
        params.addHeader(HEADER_USERTOKEN, userToken);

        LogUtils.i(TAG, "请求头部：" + "appToken=" + appToken + ",userToken=" + userToken + ",noneStr=" + noneStr + ",sign=" + sign);


        //当POST 请求一段JSON数据时，没有参数名，用这里面的。
        if (mHttpEvent.noParmName) {
            params.addHeader("Content-Type", "application/json");
            try {
                params.setBodyEntity(new StringEntity(mHttpEvent.mJsonData, "utf-8"));
                LogUtils.i(TAG, mHttpEvent.mJsonData);
            } catch (UnsupportedEncodingException e) {
            }
        }
        // 请求主体
        for (String key : map.keySet()) {
            String value = map.get(key);
            list.add(key + "=" + value);
            if (HttpClient.GET == mHttpEvent.mReqMethod || mHttpEvent.mUseReqParams) {
                params.addQueryStringParameter(key, value);
            } else {
                params.addBodyParameter(key, value);
            }
        }

        //add other params
        if (HttpClient.POST == mHttpEvent.mReqMethod && mHttpEvent.mImageFiles != null) {
            for (Object key : mHttpEvent.mImageFiles.keySet()) {
                params.addBodyParameter(key.toString(), (File) mHttpEvent.mImageFiles.get(key));

            }
        }

        LogUtils.i(TAG, "请求参数：" + list);
        return params;
    }

    public void start() {
        if (!NetworkUtils.isConnectedToNetwork(mContext)) {// 检查网络

            //ToastUtils.showShort(mContext, "网络连接失败");
            mHttpEvent.onError(HttpCode.RESPONSE_FAIL, mContext.getResources().getString(R.string.string_net_error));
            LogUtils.i(TAG, "网络连接失败");
            return;
        }

        // HTTP请求
        String headUrl = mUrl == null ? KMYY_URL : mUrl;
        String mAction = headUrl + mHttpEvent.mReqAction;// 请求地址
        LogUtils.i(TAG, "请求地址：" + mAction);
        HttpMethod mMethod = getMethod(mHttpEvent.mReqMethod);//请求方法
        RequestParams mParams = getParams(mHttpEvent.mReqParams);// 请求参数

        // 发送请求
        HttpUtils httpUtils = new HttpUtils(10000);// 设置超时
        httpUtils.configCurrentHttpCacheExpiry(0);
        httpUtils.configDefaultHttpCacheExpiry(0);
        httpUtils.configRequestRetryCount(0);
        mHttpHandler = httpUtils.send(mMethod, mAction, mParams, mCallBack);

    }

    public void startNewApi() {
        if (!NetworkUtils.isConnectedToNetwork(mContext)) {// 检查网络

            //ToastUtils.showShort(mContext, "网络连接失败");
            mHttpEvent.onError(HttpCode.RESPONSE_FAIL, mContext.getResources().getString(R.string.string_net_error));
            LogUtils.i(TAG, "网络连接失败");
            return;
        }

        // HTTP请求
        String headUrl = mUrl == null ? DOCTOR_API_URL : mUrl;
        String mAction = headUrl + mHttpEvent.mReqAction;// 请求地址
        LogUtils.i(TAG, "请求地址：" + mAction);
        HttpMethod mMethod = getMethod(mHttpEvent.mReqMethod);//请求方法
        RequestParams mParams = getParams(mHttpEvent.mReqParams);// 请求参数

        // 发送请求
        HttpUtils httpUtils = new HttpUtils(10000);// 设置超时
        httpUtils.configCurrentHttpCacheExpiry(0);
        httpUtils.configDefaultHttpCacheExpiry(0);
        httpUtils.configRequestRetryCount(0);
        mHttpHandler = httpUtils.send(mMethod, mAction, mParams, mCallBack);

    }

    public void imageStart() {
        if (!NetworkUtils.isConnectedToNetwork(mContext)) {// 检查网络
            //ToastUtils.showShort(mContext, "网络连接失败");
            mHttpEvent.onError(HttpCode.RESPONSE_FAIL, mContext.getResources().getString(R.string.string_net_error));
            LogUtils.i(TAG, "网络连接失败");
            return;
        }

        // HTTP请求
        String mAction = IMAGE_URL + mHttpEvent.mReqAction;// 请求地址
        LogUtils.i(TAG, "请求地址：" + mAction);
        HttpMethod mMethod = getMethod(mHttpEvent.mReqMethod);//请求方法
        RequestParams mParams = getParams(mHttpEvent.mReqParams);// 请求参数

        // 发送请求
        HttpUtils httpUtils = new HttpUtils(10000);// 设置超时
        httpUtils.configCurrentHttpCacheExpiry(0);
        httpUtils.configDefaultHttpCacheExpiry(0);
        mHttpHandler = httpUtils.send(mMethod, mAction, mParams, mCallBack);

    }

    public void cancel() {
        if (mHttpHandler != null) {
            mHttpHandler.cancel();
        }
    }

    private boolean filter(int code) {
        if (mHttpFilter != null) {
            switch (code) {
                case HttpCode.TOKEN_FAIL:
                    mHttpFilter.onTokenExpire();
                    return false;
                case HttpCode.TOKEN_EXPIRED:
                    mHttpFilter.onAppTokenExpire();
                    return false;
                case HttpCode.USER_NOT_LOGIN:
                    mHttpFilter.onNoneUserLogin();
                    return false;
            }
        }
        return true;
    }

    private Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            return null;
            //throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    private RequestCallBack<String> mCallBack = new RequestCallBack<String>() {

        @Override
        public void onFailure(HttpException arg0, String arg1) {
            LogUtils.i(TAG, "数据获取失败:" + arg1);
            mHttpEvent.onError(HttpCode.RESPONSE_FAIL, mContext.getResources().getString(R.string.string_try_later));
        }

        @Override
        public void onSuccess(ResponseInfo<String> arg0) {
            // TODO Auto-generated method stub
            LogUtils.i(TAG, "数据获取成功:" + arg0.result);

            try {
                JSONObject jsonObject = new JSONObject(arg0.result);
                if (mHttpEvent.mUserOriginalData) {
                    mHttpEvent.onSuccess(jsonObject.toString());
                    return;
                }
                int code = jsonObject.optInt(TAG_CODE);
                String msg = jsonObject.optString(TAG_MSG);

                if (code == HttpCode.RESPONSE_SUCCESS) {
                    mHttpEvent.mTotal = jsonObject.optString(TAG_TOTAL);
                    String data = jsonObject.optString(TAG_DATA);
                    Type type = getSuperclassTypeParameter(mHttpEvent.getClass());
                    if (type == null || type.equals(String.class)) {
                        mHttpEvent.onSuccess(data);
                    } else {
                        mHttpEvent.onSuccess(new GsonBuilder().create().fromJson(data, type));
                    }
                } else {
                    if (filter(code)) {
                        if (mHttpEvent.mUseErrorData) {
                            mHttpEvent.onError(code, jsonObject.getString(TAG_DATA));
                        } else {
                            mHttpEvent.onError(code, msg);
                        }
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogUtils.i(TAG, "数据解析失败");
                mHttpEvent.onError(HttpCode.RESPONSE_FAIL, mContext.getResources().getString(R.string.string_analysis_failed));
            } catch (Exception e) {
                e.printStackTrace();
                mHttpEvent.onError(HttpCode.RESPONSE_FAIL, "error on response!");
            }
        }
    };
}
