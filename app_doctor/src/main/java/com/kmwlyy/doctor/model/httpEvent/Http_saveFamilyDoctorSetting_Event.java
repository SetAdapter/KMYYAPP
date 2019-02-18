package com.kmwlyy.doctor.model.httpEvent;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.HomeSettingBean;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by TFeng on 2017/7/17.
 */

public class Http_saveFamilyDoctorSetting_Event extends HttpEvent {
    public Http_saveFamilyDoctorSetting_Event(int IsFreeCoupon , String OfflineAddress ,String MapAddress,
                                              String MaxSaleCount , String AutoReplyContent,boolean isOpen,List<HomeSettingBean.DoctorPackageBean> PackageList,HttpListener mListener) {
        super(mListener);
        mReqMethod = HttpClient.POST;
        mReqAction ="/DoctorFamily/ContractSetting";
        noParmName = true;
        HashMap<String,Object> params = new HashMap<>();
        params.put("IsFreeCoupon",IsFreeCoupon);
        params.put("OfflineAddress",OfflineAddress);
        params.put("MapAddress",MapAddress);
        params.put("MaxSaleCount",MaxSaleCount);
        params.put("AutoReplyContent",AutoReplyContent);
        params.put("PackageList",PackageList);
        params.put("Enable",isOpen);
        mJsonData = new Gson().toJson(params);

    }
}
