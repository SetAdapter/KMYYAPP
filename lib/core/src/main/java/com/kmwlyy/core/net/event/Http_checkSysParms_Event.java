package com.kmwlyy.core.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.SysParms;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 检查更新
 */
public class Http_checkSysParms_Event extends HttpEvent<SysParms> {

    public Http_checkSysParms_Event(String VersionNo,String VersionType,HttpListener<SysParms> mListener) {
        super(mListener);
        mReqAction = "/DownLoad/UpdateAndroidAPP";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("VersionNo", VersionNo);
        mReqParams.put("VersionType", VersionType);
    }

}
