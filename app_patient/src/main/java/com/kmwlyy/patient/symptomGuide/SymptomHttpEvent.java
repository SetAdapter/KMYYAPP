package com.kmwlyy.patient.symptomGuide;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;

import java.util.HashMap;

/**
 * @Description描述:
 * @Author作者: hx
 * @Date日期: 2017/7/31
 */

public class SymptomHttpEvent extends HttpEvent {
    public SymptomHttpEvent(String id) {
        mReqMethod = HttpClient.GET;
        mReqAction = "/api/Symptom/GetSymptomDetail";
        mUserOriginalData = true;
        mReqParams = new HashMap();
        mReqParams.put("ID", id);
    }
}
