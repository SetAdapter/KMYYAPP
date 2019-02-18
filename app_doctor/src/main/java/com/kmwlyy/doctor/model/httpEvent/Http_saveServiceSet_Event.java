package com.kmwlyy.doctor.model.httpEvent;

import com.alibaba.fastjson.JSON;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.SaveServiceSetBean;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * 保存服务设置使用的接口
 */
public class Http_saveServiceSet_Event extends HttpEvent<String> {

	public Http_saveServiceSet_Event(List<SaveServiceSetBean> bean, HttpListener listener) {
		super(listener);
		mReqAction = "/DoctorService/ServiceSettings";
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
