package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;

/**
 * Created by Winson on 2016/10/28.
 */
public class Http_acceptQuestion_Event extends HttpEvent {

    public Http_acceptQuestion_Event(String consultId, HttpListener listener) {
        super(listener);
        mReqAction = "/UserConsults/AcceptQuestion";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("ID", consultId);
    }

}
