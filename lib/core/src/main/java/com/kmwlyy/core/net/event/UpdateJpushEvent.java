package com.kmwlyy.core.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;

/** 更新极光推送ID
 * Created by Administrator on 2017/3/23.
 */
public interface UpdateJpushEvent {

    public class UpdateJpush extends HttpEvent<String> {

        public UpdateJpush(String jpushRegisterId, HttpListener mListener) {
            super(mListener);
            mReqMethod = HttpClient.GET;

            mReqAction = "/users/UpdateJRegisterID";
            mReqParams = new HashMap<>();
            mReqParams.put("registerId", jpushRegisterId);
            mReqParams.put("ClientName","com.kmwlyy.familydoctor.gzpy");

        }
    }
}