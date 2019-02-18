package com.kmwlyy.patient.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.event.HttpUser;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.SPUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.winson.ui.widget.RateLayout;

import butterknife.BindView;

/**
 * Created by Winson on 2016/8/17.
 */
public class ModifyPasswordActivity extends BaseActivity implements View.OnClickListener {

    private static final java.lang.String TAG = ModifyPasswordActivity.class.getSimpleName();


    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.old_password)
    EditText mOldPassword;
    @BindView(R.id.new_password)
    EditText mNewPassword;
    @BindView(R.id.confirm_password)
    EditText mConfirmPassword;
    @BindView(R.id.modify)
    Button mModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_password);
        butterknife.ButterKnife.bind(this);

        tv_center.setText(R.string.modify_password);
        tv_left.setOnClickListener(this);
        mModify.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_left:
                onBackPressed();
                break;
            case R.id.modify:
                modifyPassword();
                break;
        }
    }

    private void modifyPassword() {

        final String pwd = mOldPassword.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.show(this, R.string.please_input_pwd, Toast.LENGTH_SHORT);
            return;
        }

        final String newPassword = mNewPassword.getText().toString();
        if (TextUtils.isEmpty(newPassword)) {
            ToastUtils.show(this, R.string.please_input_new_pwd, Toast.LENGTH_SHORT);
            return;
        }

        if (!CommonUtils.checkPwd(newPassword)) {
            ToastUtils.show(this, R.string.p_please_input_true_pwd, Toast.LENGTH_SHORT);
            ToastUtils.show(this, R.string.p_true_pwd_format, Toast.LENGTH_SHORT);
            return;
        }

        final String confirPassowrd = mConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(confirPassowrd)) {
            ToastUtils.show(this, R.string.please_input_confirm_pwd, Toast.LENGTH_SHORT);
            return;
        }
        if (!confirPassowrd.equals(confirPassowrd)) {
            ToastUtils.show(this, R.string.new_pwd_not_eq_confirm, Toast.LENGTH_SHORT);
            return;
        }

        HttpUser.ModifyPassword modifyPassword = new HttpUser.ModifyPassword(pwd, newPassword, confirPassowrd, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                LogUtils.d(TAG, "modify password error , code : " + code + " , msg : " + msg);
                if (!TextUtils.isEmpty(msg)){
                    ToastUtils.show(ModifyPasswordActivity.this, msg, Toast.LENGTH_SHORT);
                } else {
                    ToastUtils.show(ModifyPasswordActivity.this, R.string.modify_password_failed,
                        Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onSuccess(Object o) {
                LogUtils.d(TAG, "modify password success");
                ToastUtils.show(ModifyPasswordActivity.this, R.string.modify_password_success, Toast.LENGTH_SHORT);

                SPUtils.LoginInfo loginInfo = SPUtils.getLoginInfo(ModifyPasswordActivity.this);
                SPUtils.saveLoginInfo(ModifyPasswordActivity.this, loginInfo.userName, newPassword);

//                PApplication.getPApplication(ModifyPasswordActivity.this).logout(true);
                finish();
            }
        });

        HttpClient client = NetService.createClient(this, modifyPassword);
        client.start();
    }
}
