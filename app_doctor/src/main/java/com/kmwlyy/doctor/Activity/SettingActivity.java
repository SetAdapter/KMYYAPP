package com.kmwlyy.doctor.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.event.UpdateJpushEvent;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.SPUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.MyApplication;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.VersionBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getVersionCode_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.networkbench.agent.impl.NBSAppAgent;
import com.winson.ui.widget.AlterDialogView;

import cn.jpush.android.api.JPushInterface;

/**
 * @Description描述:
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/12
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener{
    public static final String TAG = SettingActivity.class.getSimpleName();

    /* 标题栏 */
    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.tv_center)
    TextView tv_title;


    @ViewInject(R.id.switchCb)
    CheckBox switchCb;
    @ViewInject(R.id.modify_password)
    LinearLayout modify_password;
    @ViewInject(R.id.suggest_feedback)
    LinearLayout suggest_feedback;
    @ViewInject(R.id.about_us)
    LinearLayout about_us;
    @ViewInject(R.id.grade)
    LinearLayout grade;
//    检查更新
    @ViewInject(R.id.update)
    LinearLayout update;

    @ViewInject(R.id.tv_loginOut)
    TextView tv_loginOut;

    boolean switchCanListener = true;
    private String versionName;
    private int versionCode;
    private boolean isUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ViewUtils.inject(this);

        initView();
        initListener();
    }


    private void initView(){
        tv_title.setText(R.string.string_setting);
        btn_left.setVisibility(View.VISIBLE);

        if ((boolean) SPUtils.get(this,SPUtils.JPUSH_STATE,Boolean.valueOf(true)))
        {
            switchCb.setChecked(true);
        }
        else{
            switchCb.setChecked(false);
        }

    }

    private void initListener(){
        btn_left.setOnClickListener(this);
        modify_password.setOnClickListener(this);
        suggest_feedback.setOnClickListener(this);
        about_us.setOnClickListener(this);
        grade.setOnClickListener(this);
        tv_loginOut.setOnClickListener(this);
        update.setOnClickListener(this);


        switchCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchCanListener) {
                    if (isChecked) {
                        NBSAppAgent.onEvent("个人中心-点击设置-开启极光推送");
                        resumeJPush();
                    } else {
                        NBSAppAgent.onEvent("个人中心-点击设置-关闭极光推送");
                        stopJPush();
                    }
                }else {
                    switchCanListener = !switchCanListener;
                }
            }
        });

    }



    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()){
            case R.id.tv_left:
                finish();
                break;
            case R.id.modify_password:
                startActivity(new Intent(mContext,ModifyPwdActivity.class));
                break;
            case R.id.suggest_feedback:
                CommonUtils.startActivity(this, FeedBackActivity.class);
                break;
            case R.id.about_us:
                CommonUtils.startActivity(this, AboutUsActivity.class);
                break;
            case R.id.grade:
                CommonUtils.goToMarket(this);
                break;
            case R.id.tv_loginOut:
                AlterDialogView.Builder builder = new AlterDialogView.Builder(mContext);
                builder.setTitle(getResources().getString(R.string.string_dialog));
                builder.setMessage(getResources().getString(R.string.string_dialog_content));
                builder.setNegativeButton(getResources().getString(R.string.string_exit_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NBSAppAgent.onEvent("个人中心-点击设置-取消退出");
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton(getResources().getString(R.string.string_exit_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NBSAppAgent.onEvent("个人中心-点击设置-确定退出");
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        MyApplication.getApplication(SettingActivity.this).logout();
                        SPUtils.logout(mContext);

                    }
                });
                builder.create().show();
                break;
            case R.id.update:
                try {
                    checkUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void checkUpdate() throws Exception {
//        ToastUtils.showShort(SettingActivity.this,"Check Update");

        /*获取本地版本*/

        versionName = getVersionName();
        versionCode = getVersionCode();

//        ToastUtils.showShort(SettingActivity.this,"Check Update"+"versionName"+versionName+"versionCode"+versionCode);
        //判断是否开启网络

        if(MyUtils.isNetAvailable(this)){
//            ToastUtils.showShort(SettingActivity.this,"Network is connected");
//            升级对话框
//            是否需要升级
            getNetVersion();

        }else{
            ToastUtils.showShort(SettingActivity.this,"Please check your network!!");

        }

    }

    private void getNetVersion() {

     Http_getVersionCode_Event getVersion = new Http_getVersionCode_Event(versionCode,2, new HttpListener<VersionBean>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(SettingActivity.this,code+msg);

            }

            @Override
            public void onSuccess(VersionBean versionBean) {

                if(versionBean != null){
                        boolean needUpdate = versionBean.isNeedUpdate();
                        if(needUpdate) {
                            showUpdateDialog();
                        }else{
                            ToastUtils.showShort(SettingActivity.this,"已经是最新版不用升级");
                        }


                }else{

                }





            }

        });
        HttpClient client = NetService.createClient(this,getVersion);
        client.start();




    }

    private void showUpdateDialog() {

        AlterDialogView.Builder builder = new AlterDialogView.Builder(mContext);
        builder.setTitle(getResources().getString(R.string.string_dialog));
        builder.setMessage(getResources().getString(R.string.string_dialog_update));
        builder.setNegativeButton(getResources().getString(R.string.string_exit_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastUtils.showShort(SettingActivity.this,"不升级");

                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.string_exit_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastUtils.showShort(SettingActivity.this,"升级");
                // TODO: 2017/6/16 jump to android market

                goToMarket(mContext,getPackageName());
                
              
                dialog.dismiss();

            }
        });
        builder.create().show();



    }

    private void goToMarket(Context context,String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private int getVersionCode() throws Exception{
        PackageManager pm = getPackageManager();
        PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
        return packageInfo.versionCode;


    }

    private String getVersionName() throws Exception {
        PackageManager pm = getPackageManager();
        PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
        return packageInfo.versionName;

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
