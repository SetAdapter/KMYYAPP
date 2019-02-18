package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;

/**
 * Created by xcj on 2016/10/28.
 */
public class Http_submitVerificationInfo_Event extends HttpEvent {
    public Http_submitVerificationInfo_Event(String DoctorName, String HospitalName,
                                   String DepartmentName,String Title,String Intro,String Specialty,String IDNumber,String CertificateNo,String IDURL,
                                             String HandheldIDURL,String CertificateURL, HttpListener listener) {
        super(listener);
        mReqAction = "/DoctorVerifications/SubmitVerificationInfo";
        mReqMethod = HttpClient.POST;

        mReqParams = new HashMap<>();
        mReqParams.put("DoctorName", DoctorName);
        mReqParams.put("HospitalName", HospitalName);
        mReqParams.put("DepartmentName", DepartmentName);
        mReqParams.put("Title", Title);
        mReqParams.put("Intro", Intro);
        mReqParams.put("Specialty", Specialty);
        mReqParams.put("IDNumber", IDNumber);
        mReqParams.put("CertificateNo", CertificateNo);
        mReqParams.put("IDURL", IDURL);
        mReqParams.put("HandheldIDURL", HandheldIDURL);
        mReqParams.put("CertificateURL", CertificateURL);

    }
}
