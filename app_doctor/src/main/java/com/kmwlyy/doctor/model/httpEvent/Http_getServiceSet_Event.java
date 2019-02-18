package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.ServiceSetBean;

import java.util.List;

/**
 * 获取服务设置使用的接口
 */
public class Http_getServiceSet_Event extends HttpEvent<ServiceSetBean> {

	public Http_getServiceSet_Event(HttpListener listener) {
		super(listener);
		mReqAction = "/doctorPrice/GetServiceSettings";
		mReqMethod = HttpClient.GET;
	}
}
