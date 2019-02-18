package com.kmwlyy.patient.center;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.event.FeedbackEvent;
import com.kmwlyy.core.net.event.UpdateJpushEvent;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.SPUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.PApplication;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.account.ModifyPasswordActivity;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.main.AboutUsActivity;
import com.winson.ui.widget.AlterDialogView;

public class SettingActivity extends BaseActivity {

    private static final String TAG = SettingActivity.class.getSimpleName();

    @BindView(R.id.tv_center)
    TextView tv_center;

    @BindView(R.id.switchCb)
    CheckBox switchCb;
    @BindView(R.id.tv_loginOut)
    TextView tv_loginOut;
    @BindView(R.id.line_loginOut)
    View line_loginOut;

    boolean switchCanListener = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        butterknife.ButterKnife.bind(this);
        tv_center.setText(R.string.settings);

        if (BaseApplication.getInstance().getUserToken() != null) {
            tv_loginOut.setVisibility(View.VISIBLE);
            line_loginOut.setVisibility(View.VISIBLE);
        }else{
            tv_loginOut.setVisibility(View.GONE);
            line_loginOut.setVisibility(View.GONE);
        }

        if ((boolean) SPUtils.get(this,SPUtils.JPUSH_STATE,Boolean.valueOf(true)))
        {
            switchCb.setChecked(true);
        }
        else{
            switchCb.setChecked(false);
        }

        switchCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchCanListener) {
                    if (isChecked) {
                        resumeJPush();
                    } else {
                        stopJPush();
                    }
                }else {
                    switchCanListener = !switchCanListener;
                }
            }
        });
    }

    //修改密码
    @OnClick(R.id.modify_password)
    public void jumpModifyPassword() {
        if (PUtils.checkHaveUser(this)) {
            CommonUtils.startActivity(this, ModifyPasswordActivity.class);
        }

    }
    //关于我们
    @OnClick(R.id.about_us)
    public void jumpAboutUs() {
        CommonUtils.startActivity(this, AboutUsActivity.class);
    }

    //意见反馈
    @OnClick(R.id.suggest_feedback)
    public void jumpFeedback() {
        CommonUtils.startActivity(this, FeedBackActivity.class);
    }
    //跳转应用市场
    @OnClick(R.id.grade)
    public void jumpAppStore() {
        CommonUtils.goToMarket(this);
    }

    //退出登陆
    @OnClick(R.id.tv_loginOut)
    public void loginOut() {
        AlterDialogView.Builder builder = new AlterDialogView.Builder(SettingActivity.this);
        builder.setTitle(getResources().getString(R.string.string_dialog));
        builder.setMessage(getResources().getString(R.string.string_dialog_content));
        builder.setNegativeButton(getResources().getString(R.string.string_exit_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.string_exit_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                PApplication.getPApplication(SettingActivity.this).logout(true);
            }
        });
        builder.create().show();
    }

    private void stopJPush() {
//        JPushInterface.stopPush(SettingActivity.this);
        showLoadDialog(R.string.close_push);
        UpdateJpushEvent.UpdateJpush updateJpush = new UpdateJpushEvent.UpdateJpush("",
                new HttpListener() {
                    @Override
                    public void onError(int code, String msg) {
                        dismissLoadDialog();
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("updateJpush", code, msg));
                        }
                        ToastUtils.showLong(SettingActivity.this, R.string.close_push_fail);
                        switchCanListener = false;
                        switchCb.setChecked(true);

                    }

                    @Override
                    public void onSuccess(Object o) {
                        dismissLoadDialog();
                        SPUtils.put(SettingActivity.this,SPUtils.JPUSH_STATE,Boolean.valueOf(false));
                    }
                });

        HttpClient client = NetService.createClient(this, updateJpush);
        client.start();
    }

    private void resumeJPush(){
//        JPushInterface.resumePush(SettingActivity.this);
        showLoadDialog(R.string.open_push);
        UpdateJpushEvent.UpdateJpush updateJpush = new UpdateJpushEvent.UpdateJpush(BaseApplication.instance.getJpushRegisterID(),
                new HttpListener() {
                    @Override
                    public void onError(int code, String msg) {
                        dismissLoadDialog();
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("updateJpush", code, msg));
                        }
                        ToastUtils.showLong(SettingActivity.this, R.string.open_push_fail);
                        switchCanListener = false;
                        switchCb.setChecked(false);
                    }

                    @Override
                    public void onSuccess(Object o) {
                        dismissLoadDialog();
                        SPUtils.put(SettingActivity.this,SPUtils.JPUSH_STATE,Boolean.valueOf(true));
                    }
                });

        HttpClient client = NetService.createClient(this, updateJpush);
        client.start();
    }

}
