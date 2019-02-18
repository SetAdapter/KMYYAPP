package com.kmwlyy.core.net;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import com.google.gson.GsonBuilder;
import com.google.gson.internal.$Gson$Types;
import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.LogUtils;

import com.kmwlyy.core.util.MResource;
import com.kmwlyy.core.util.NetworkUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

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

/**
 * Created by xcj on 2017/8/9.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NewHttpClient {
    public static final String TAG = "MyHttpClient";
    // 常用字段
    public static final String TAG_CODE = "resultCode";// 返回编码

    public static final String TAG_MSG = "Msg";// 返回信息
    public static final String TAG_DATA = "resultData";// 返回数据

    public static String KMYY_URL = "http://fd.kmwlyy.com:8080";
//  public static String KMYY_URL = "http://192.168.100.110:8080/kmhc-familydoctor-web";

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
    private NewHttpFilter mHttpFilter;
    private HttpHandler mHttpHandler;
    private String mUrl;

    public NewHttpClient(Context context, HttpEvent event) {
        this.mContext = context;
        this.mHttpEvent = event;
        this.mHttpFilter = BaseApplication.instance.getNewHttpFilter();
        if (this.mHttpFilter != null) {
            this.mHttpFilter.setHttpClient(this);
        }
    }

    public void onError(int code) {
        mHttpEvent.onError(code, "");
    }

    private HttpRequest.HttpMethod getMethod(int method) {
        switch (method) {
            case POST:
                return HttpRequest.HttpMethod.POST;
            case GET:
                return HttpRequest.HttpMethod.GET;
            case PUT:
                return HttpRequest.HttpMethod.PUT;
            case DELETE:
                return HttpRequest.HttpMethod.DELETE;
            default:
                return HttpRequest.HttpMethod.POST;
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
            params.addHeader("Content-Type", "application/json;charse=UTF-8");
            params.addHeader("Accept", "application/json");
            params.addHeader("dataType", "json");

            try {
                params.setBodyEntity(new StringEntity(mHttpEvent.mJsonData, "utf-8"));
                LogUtils.i(TAG, mHttpEvent.mJsonData);
            } catch (UnsupportedEncodingException e) {
            }
        }
        // 请求主体
//        for (String key : map.keySet()) {
//            String value = map.get(key);
//            list.add(key + ":" + value);
//            if (HttpClient.GET == mHttpEvent.mReqMethod || mHttpEvent.mUseReqParams) {
//                params.addQueryStringParameter(key, value);
//            } else {
//                params.addBodyParameter(key, value);
//            }
//        }

        //add other params
        if (HttpClient.POST == mHttpEvent.mReqMethod && mHttpEvent.mImageFiles != null) {
//            params.addHeader("Content-Type", "multipart/form-data");
            params.addHeader("Accept", "application/json");
            params.addHeader("dataType", "json");
            for (Object key : mHttpEvent.mImageFiles.keySet()) {
                params.addBodyParameter(key.toString(), (File) mHttpEvent.mImageFiles.get(key));

            }
        }
//        LogUtils.i(TAG, "请求参数：" + params);
        LogUtils.i(TAG, "请求参数：" + list);
        return params;
    }

    public void start() {
        start(null);
    }

    public void start(String specificUrl) {
        if (!NetworkUtils.isConnectedToNetwork(mContext)) {// 检查网络
            //ToastUtils.showShort(mContext, "网络连接失败");
            mHttpEvent.onError(HttpCode.RESPONSE_FAIL, mContext.getResources().getString(MResource.getIdByName(mContext, "string", "string_net_error")));
            LogUtils.i(TAG, "网络连接失败");
            return;
        }

        // HTTP请求
        String headUrl = KMYY_URL;
        String mAction = specificUrl == null ? headUrl + mHttpEvent.mReqAction : specificUrl + mHttpEvent.mReqAction;// 请求地址
        LogUtils.i(TAG, "请求地址：" + mAction);
        HttpRequest.HttpMethod mMethod = getMethod(mHttpEvent.mReqMethod);//请求方法
        RequestParams mParams = getParams(mHttpEvent.mReqParams);// 请求参数

        // 发送请求
        HttpUtils httpUtils = new HttpUtils(10000);// 设置超时
        httpUtils.configCurrentHttpCacheExpiry(0);
        httpUtils.configDefaultHttpCacheExpiry(0);
        httpUtils.configRequestRetryCount(0);
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
            mHttpEvent.onError(HttpCode.RESPONSE_FAIL, mContext.getResources().getString(MResource.getIdByName(mContext, "string", "string_try_later")));
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
                    String data = jsonObject.optString(TAG_DATA);
                    Type type = getSuperclassTypeParameter(mHttpEvent.getClass());

                    if (type == null || type.equals(String.class)) {
                        mHttpEvent.onSuccess(data);
                    } else {
                        mHttpEvent.onSuccess(new GsonBuilder().create().fromJson(arg0.result, type));
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
                mHttpEvent.onError(HttpCode.RESPONSE_FAIL, "数据解析失败");
            } catch (Exception e) {
                e.printStackTrace();
                mHttpEvent.onError(HttpCode.RESPONSE_FAIL, "error on response!");
            }
        }
    };
}
