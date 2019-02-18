package com.kmwlyy.core.net;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class HttpEvent<T> {
    // 请求数据
    public int mReqMethod;// 请求方法
    public String mReqAction;// 请求动作
    public Map<String, String> mReqParams;// 请求参数\
    public Map<String, File> mImageFiles;
    protected boolean mUseReqParams;
    public boolean noParmName = false;  //true表示POST请求，body里面放一段JSON串，并且没有参数名
    public String mJsonData;
    public String mTotal;
    public boolean mUseErrorData;
    public boolean mUserOriginalData;
    private HttpListener<T> mListener;

    public HttpEvent() {

    }

    public HttpEvent(HttpListener<T> mListener) {
        this.mListener = mListener;
    }

    public void setHttpListener(HttpListener<T> mListener) {
        this.mListener = mListener;
    }

    void onError(int code, String msg) {
        if (mListener != null) {
            mListener.onError(code, msg);
        }
    }

    void onSuccess(T data) {
        if (mListener != null) {
            mListener.onSuccess(data);
        }
    }
}
