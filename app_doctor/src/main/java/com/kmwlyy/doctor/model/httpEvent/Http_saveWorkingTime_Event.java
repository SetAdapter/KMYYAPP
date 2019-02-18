package com.kmwlyy.doctor.model.httpEvent;

import com.alibaba.fastjson.JSON;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.HomeSettingBean;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by TFeng on 2017/7/10.
 */

public class Http_saveWorkingTime_Event extends HttpEvent<String> {
    public Http_saveWorkingTime_Event(List<HomeSettingBean.WorkingTimeBean> bean, HttpListener listener){
        super(listener);
        mReqAction = "/DoctorFamily/WorkingTimeSetting";
        mReqMethod = HttpClient.POST;
        noParmName = true;
        mReqParams = new HashMap<>();
        String str = JSON.toJSONString(bean);
        try {
            mJsonData = new String(str.getBytes(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
