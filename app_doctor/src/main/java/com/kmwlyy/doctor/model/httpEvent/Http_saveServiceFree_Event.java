package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.FreeCalendarBean;

import java.util.HashMap;

/**
 * 保存服务设置使用的接口
 */
public class Http_saveServiceFree_Event extends HttpEvent<String> {

	public Http_saveServiceFree_Event(FreeCalendarBean bean, HttpListener listener) {
		super(listener);
		mReqAction = "/DoctorClinics";
		mReqMethod = HttpClient.POST;

		mReqParams = new HashMap<>();
		mReqParams.put("ClinicMonth", bean.getClinicMonth());
		mReqParams.put("AcceptCount", ""+bean.getAcceptCount());
		mReqParams.put("ClinicDates", ""+bean.getClinicDates());
		mReqParams.put("State", ""+bean.isState());

	}

}
