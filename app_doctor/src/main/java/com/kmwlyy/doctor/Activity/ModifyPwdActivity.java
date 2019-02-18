package com.kmwlyy.doctor.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.SPUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.httpEvent.Http_modifyPWD_Event;

public class ModifyPwdActivity extends BaseActivity {

    public static final String TAG = "ModifyPwdActivity";
    private EditText old_pwd;//旧密码输入框
    private EditText new_pwd;//新密码输入框
    private EditText new2_pwd;//第二次输入新密码

    private String oldPwd;//原始密码
    private String newPwd;//新密码
    private String newPwd2;//第二次输入的新密码


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);

        ((TextView)findViewById(R.id.tv_center)).setText(getResources().getString(R.string.string_modify_pwd));
        mLeftBtn = (TextView) findViewById(R.id.tv_left);
        mLeftBtn.setVisibility(View.VISIBLE);
        old_pwd = (EditText) findViewById(R.id.et_old);
        new_pwd = (EditText) findViewById(R.id.et_new);
        new2_pwd = (EditText) findViewById(R.id.et_new2);

        findViewById(R.id.btn_commit).setOnClickListener(this);
        findViewById(R.id.tv_left).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_left://返回
                finish();
                break;
            case R.id.btn_commit://提交
                oldPwd = old_pwd.getText().toString();
                newPwd = new_pwd.getText().toString();
                newPwd2 = new2_pwd.getText().toString();
                if (TextUtils.isEmpty(oldPwd)) {
                    Toast.makeText(mContext, getResources().getString(R.string.string_pwd_msg1), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(newPwd)) {
                    Toast.makeText(mContext, getResources().getString(R.string.string_pwd_msg2), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newPwd.equals(oldPwd)){
                    Toast.makeText(mContext, getResources().getString(R.string.string_pwd_msg3), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newPwd2.length() < 6 || newPwd2.length() > 18){
                    Toast.makeText(mContext, getResources().getString(R.string.string_pwd_msg5), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newPwd.equals(newPwd2)) {
                    Toast.makeText(mContext, getResources().getString(R.string.string_pwd_msg4), Toast.LENGTH_SHORT).show();
                    return;
                }

                modifyPwd(oldPwd, newPwd);
                break;
        }
    }

    private void modifyPwd(String oldPwd, final String newPwd) {
        Http_modifyPWD_Event event = new Http_modifyPWD_Event(oldPwd, newPwd, new HttpListener<String>(

        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "login error , code : " + code + " , msg : " + msg);
                }
                dismissLoadDialog();
                ToastUtils.show(mContext, msg, Toast.LENGTH_SHORT);

            }

            @Override
            public void onSuccess(String data) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "modify pwd success");
                }
                dismissLoadDialog();
                ToastUtils.show(mContext, R.string.string_modify_success, Toast.LENGTH_SHORT);

                SPUtils.LoginInfo loginInfo = SPUtils.getLoginInfo(ModifyPwdActivity.this);
                SPUtils.saveLoginInfo(ModifyPwdActivity.this, loginInfo.userName, newPwd);

                finish();
            }
        });
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }


}
