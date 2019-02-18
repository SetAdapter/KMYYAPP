package com.kmwlyy.core.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.SignatureBean;

/**
 * Created by xcj on 2017/8/23.
 */

public class GetMySignature extends HttpEvent<SignatureBean> {
    public GetMySignature(HttpListener<SignatureBean> mListener) {
        super(mListener);
        mReqMethod = HttpClient.GET;
        mReqAction = "/Signature/GetMySignature";
    }
}
