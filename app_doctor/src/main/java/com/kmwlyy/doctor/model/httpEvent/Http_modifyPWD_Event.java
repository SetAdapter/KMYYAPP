package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;

/**
 * 修改密码使用的接口
 */
public class Http_modifyPWD_Event extends HttpEvent<String> {

	public Http_modifyPWD_Event(String old_pwd, String new_pwd, HttpListener listener) {
		super(listener);
		mReqAction = "/users/changePassword";

		mReqParams = new HashMap<>();
		mReqParams.put("OldPassword", old_pwd);
		mReqParams.put("NewPassword", new_pwd);
		mReqParams.put("ConfirmPassword", new_pwd);
	}
}
