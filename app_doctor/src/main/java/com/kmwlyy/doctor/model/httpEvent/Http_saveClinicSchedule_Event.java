package com.kmwlyy.doctor.model.httpEvent;

import com.alibaba.fastjson.JSON;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.SaveClinicScheduleBean;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * 保存服务设置使用的接口
 */
public class Http_saveClinicSchedule_Event extends HttpEvent<String> {

	public Http_saveClinicSchedule_Event(List<SaveClinicScheduleBean> bean, HttpListener listener) {
		super(listener);
		mReqAction = "/DoctorService/ClinicSettings";
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
