package com.kmwlyy.core.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.SysDict;

import java.util.ArrayList;

/**
 * Created by Winson on 2016/8/13.
 */
public class HttpSysDicts extends HttpEvent<ArrayList<SysDict>> {

    public HttpSysDicts(HttpListener<ArrayList<SysDict>> mListener) {
        super(mListener);

        mReqMethod = HttpClient.GET;

        mReqAction = "/SysDicts";
    }

}
