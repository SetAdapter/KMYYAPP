package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;

/**
 * 退出登录 使用的接口
 */
public class Http_logout_Event extends HttpEvent<String> {

	public Http_logout_Event(HttpListener listener) {
		super(listener);
		mReqAction = "/users/logout";
	}
}
