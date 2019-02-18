package com.kmwlyy.core.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.UserAccount;
import com.kmwlyy.core.net.bean.UserData;

import java.util.HashMap;

/**
 * Created by Winson on 2016/8/13.
 */
public interface HttpUser {

    // sms type
    int MSG_TYPE_REGISTER = 1;
    int MSG_TYPE_FORGET_PWD = 2;

    // user type
    int PATIENT = 1;
    int DOCTOR = 2;

    class Login extends HttpEvent<UserData> {

        public Login(String mobile, String pwd, int userType, String JRegisterID, HttpListener<UserData> mListener) {
            super(mListener);

            mReqMethod = HttpClient.POST;

            mReqAction = "/users/login";

            mReqParams = new HashMap<>();
            mReqParams.put("Mobile", mobile);
            mReqParams.put("Password", pwd);
            mReqParams.put("UserType", "" + userType);
            mReqParams.put("JRegisterID", JRegisterID);
            mReqParams.put("ClientName","com.kmwlyy.familydoctor.gzpy");
        }
    }

    class Register extends HttpEvent {

        public Register(String mobile, String pwd, String msgVerifyCode, int userType, HttpListener mListener) {
            super(mListener);

            mReqMethod = HttpClient.POST;

            mReqAction = "/users/userRegister";

            mReqParams = new HashMap<>();
            mReqParams.put("Mobile", mobile);
            mReqParams.put("MsgType", "" + 1);
            mReqParams.put("Password", pwd);
            mReqParams.put("MsgVerifyCode", msgVerifyCode);
            mReqParams.put("UserType", "" + userType);

        }
    }

    class ModifyPassword extends HttpEvent {

        public ModifyPassword(String oldPwd, String newPwd, String confirmPwd, HttpListener mListener) {
            super(mListener);

            mReqMethod = HttpClient.POST;
            mReqAction = "/users/changePassword";

            mReqParams = new HashMap();
            mReqParams.put("OldPassword", oldPwd);
            mReqParams.put("NewPassword", newPwd);
            mReqParams.put("ConfirmPassword", confirmPwd);

        }
    }

    class FindPassword extends HttpEvent {

        public FindPassword(String mobile, String password, String msgVerifyCode, int type, HttpListener mListener) {
            super(mListener);

            mReqMethod = HttpClient.POST;
            mReqAction = "/users/userFindPwd";

            mReqParams = new HashMap();
            mReqParams.put("Mobile", mobile);
            mReqParams.put("Password", password);
            mReqParams.put("MsgType", "" + MSG_TYPE_FORGET_PWD);
            mReqParams.put("MsgVerifyCode", msgVerifyCode);
            mReqParams.put("UserType",type+"");

        }
    }

    class SendSmsCode extends HttpEvent {

        private String mMobile;
        private int mMsgType;

        public SendSmsCode(String mobile, int msgType, HttpListener mListener) {
            super(mListener);
            mReqMethod = HttpClient.POST;

            mReqAction = "/users/sendSmsCode";

            mMobile = mobile;
            mMsgType = msgType;

            mReqParams = new HashMap<>();
            mReqParams.put("Mobile", mMobile);
            mReqParams.put("MsgType", "" + mMsgType);
        }
    }

    class Logout extends HttpEvent {

        public Logout(HttpListener mListener) {
            super(mListener);
            mReqMethod = HttpClient.POST;
            mReqAction = "/users/logout";
            mReqParams = new HashMap<>();
            mReqParams.put("ClientName","com.kmwlyy.familydoctor.gzpy");
        }
    }

    class IsNeedInitMemberInfo extends HttpEvent {

        public IsNeedInitMemberInfo(HttpListener mListener) {
            super(mListener);
            mReqMethod = HttpClient.POST;

            mReqAction = "/users/isNeedInitMemberInfo";
        }
    }

    class IsLogin extends HttpEvent {

        public IsLogin(HttpListener mListener) {
            super(mListener);
            mReqMethod = HttpClient.POST;

            mReqAction = "/users/isLogin";
        }
    }

    class GetUserAccount extends HttpEvent<UserAccount> {

        public GetUserAccount(HttpListener<UserAccount> mListener) {
            super(mListener);
            mReqMethod = HttpClient.GET;

            mReqAction = "/userAccount/getUserAccount";
        }
    }

}
