package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.OrderVoiceBean;

import java.util.HashMap;
import java.util.List;

/**
 * 语音咨询，视频咨询 订单列表使用的接口
 */
public class Http_getOrderVoice_Event extends HttpEvent<List<OrderVoiceBean>> {
	public static String pageSize = "20";

	public Http_getOrderVoice_Event(String CurrentPage, HttpListener listener) {
		super(listener);
		mReqAction = "/UserOPDRegisters/GetDoctorAudVid";
		mReqMethod = HttpClient.GET;
		mReqParams = new HashMap<>();
		mReqParams.put("CurrentPage", CurrentPage);
		mReqParams.put("PageSize", pageSize);

	}


}
