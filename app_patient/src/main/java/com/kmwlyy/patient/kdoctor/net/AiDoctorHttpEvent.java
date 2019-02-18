package com.kmwlyy.patient.kdoctor.net;

import android.text.TextUtils;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.patient.kdoctor.BatLocationUtils;

import java.util.HashMap;

import static com.kmwlyy.patient.kdoctor.net.ConstantURLs.K_DOCTOR_REPLY;

/**
 * @Description描述:
 * @Author作者: hx
 * @Date日期: 2017/7/31
 */

public class AiDoctorHttpEvent extends HttpEvent {
    public AiDoctorHttpEvent(String url, String keyword, String maxWords) {
        mUserOriginalData = true;
        mReqParams = new HashMap();

        if (TextUtils.isEmpty(url)) {
            mReqMethod = HttpClient.POST;
            mReqAction = BaseConstants.SEARCH_SERVER_URL + K_DOCTOR_REPLY;
            mReqParams.put("keyword", keyword);
        } else {
            mReqMethod = HttpClient.GET;
            mReqAction = url;
        }
        mReqParams.put("resultLength", maxWords);
        mReqParams.put("userDeviceId", BaseApplication.getInstance().getUniqueID());
        mReqParams.put("lat", BatLocationUtils.latitude + "");
        mReqParams.put("lon", BatLocationUtils.longitude + "");
    }

    public AiDoctorHttpEvent() {
        mUserOriginalData = true;
        mReqParams = new HashMap();
        mReqAction = BaseConstants.SEARCH_SERVER_URL + "elasticsearch/DrKang/getIntroductionNew";
        mReqMethod = HttpClient.GET;
    }

    /**
     * 请求康博士服务
     * 智能随访（首次）：followup_firstTime 智能随访：followup
     */
    public void requestDrKangServe(String serviceType,String sessionId){
        mUserOriginalData = true;
        mReqParams = new HashMap();
        mReqMethod = HttpClient.POST;
        mReqAction = BaseConstants.SEARCH_SERVER_URL + "elasticsearch/DrKang/requestDrKangServer";
        mReqParams.put("serviceType", serviceType);
        mReqParams.put("userDeviceId", BaseApplication.getInstance().getUniqueID());
        mReqParams.put("sessionId", "followup_firstTime".equals(serviceType)?"followup_firstTime_"+sessionId:sessionId);
    }
    /**
     * 获取康博士回复
     */
    public void pull(String sessionId){
        mUserOriginalData = true;
        mReqParams = new HashMap();
        mReqMethod = HttpClient.POST;
        mReqAction = BaseConstants.SEARCH_SERVER_URL + "elasticsearch/DrKang/pull";
        mReqParams.put("userDeviceId", BaseApplication.getInstance().getUniqueID());
        mReqParams.put("sessionId", sessionId);
    }
    /**
     * 回复康博士
     */
    public void push(String text,String sessionId,String respId){
        mUserOriginalData = true;
        mReqParams = new HashMap();
        mReqMethod = HttpClient.POST;
        mReqAction = BaseConstants.SEARCH_SERVER_URL + "elasticsearch/DrKang/push";
        mReqParams.put("userDeviceId", BaseApplication.getInstance().getUniqueID());
        mReqParams.put("sessionId", sessionId);
        mReqParams.put("text", text);
        mReqParams.put("respId", respId);
    }
}
