package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.VersionBean;

import java.util.HashMap;

/**
 * Created by KM on 2017/6/16.
 * Request Version API
 *
 */

public class Http_getVersionCode_Event extends HttpEvent<VersionBean> {
    //doctor client type


    /*
    * @param versionNo
    * @param versionType
    *
    * */
    public Http_getVersionCode_Event(int versionNo,int versionType,HttpListener<VersionBean> mListener) {
        super(mListener);

        mReqAction = "/DownLoad/UpdateAndroidApp";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("VersionNo",versionNo+"");
        mReqParams.put("VersionType",versionType+"");
    }
}
