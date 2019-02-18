package com.kmwlyy.doctor.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.InfoEvent;
import com.kmwlyy.doctor.model.SaveServiceSetBean;
import com.kmwlyy.doctor.model.ServiceSetBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getServiceSet_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_saveServiceSet_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.networkbench.agent.impl.NBSAppAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xcj on 2017/6/21.
 */

public class VideoInquirySettingActivity extends BaseActivity{
    static final String TYPE = "TYPE";
    static final String IS_REFRESH = "IS_REFRESH";
    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.tv_center)
    TextView tv_title;
    @ViewInject(R.id.tv_right)
    TextView tv_right;
    @ViewInject(R.id.tv_voice)
    TextView tv_voice;
    @ViewInject(R.id.et_voice)
    EditText et_voice;
    @ViewInject(R.id.switch_voice)
    CheckBox switch_voice;
    @ViewInject(R.id.ll_calendar_voice)
    LinearLayout ll_calendar_voice;

    private int mType = 0;
    private boolean mIsRefresh = false;
    private boolean is_start_video_inquiry;
    private boolean is_start_image_consult;
    private boolean is_start_image_clinic;
    private boolean is_start_family_doctor;
    private boolean is_start_voice_inquiry;
    private double consultPrice = 0;
    private double vodiePrice = 0;

    public static void startVideoInquirySettingActivity(Context context, int type, boolean isRefresh) {
        Intent intent = new Intent(context, VideoInquirySettingActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(IS_REFRESH,isRefresh);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_inquiry_setting);
        ViewUtils.inject(this);
        mType = getIntent().getIntExtra(TYPE, 0);
        mIsRefresh = getIntent().getBooleanExtra(IS_REFRESH, false);
        if(mType == 1){
            tv_title.setText("设置视话问诊");
            tv_voice.setText("视话问诊");
        }else if(mType == 2){
            tv_title.setText("设置图文咨询");
            tv_voice.setText("图文咨询");
            ll_calendar_voice.setVisibility(View.GONE);
        }
        tv_right.setText("保存");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(this);
        btn_left.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(this);
        ll_calendar_voice.setOnClickListener(this);
        if (mIsRefresh){
            getServiceSet();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch(v.getId()){
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_right:

                //调用保存接口
                save();
                break;
            case R.id.ll_calendar_voice:
                startActivity(new Intent(mContext,CalendarActivity.class));
                break;
        }
    }

    private void save(){
        if (TextUtils.isEmpty(et_voice.getText().toString())){
            ToastUtils.showShort(VideoInquirySettingActivity.this,"请输入金额");
            return;
        }
        showLoadDialog(R.string.string_wait_save);
        List<SaveServiceSetBean> beanList = new ArrayList<>();
        if (mType == 1){
            NBSAppAgent.onEvent("服务-视话问诊-保存");
            SaveServiceSetBean voiceBean = new SaveServiceSetBean();
            voiceBean.ServiceSwitch = switch_voice.isChecked()?1:0;
            voiceBean.ServicePrice = Float.valueOf(et_voice.getText().toString());
            voiceBean.ServiceType = 2;
            SaveServiceSetBean videoBean = new SaveServiceSetBean();
            videoBean.ServiceSwitch = switch_voice.isChecked()?1:0;
            videoBean.ServicePrice = Float.valueOf(et_voice.getText().toString());
            videoBean.ServiceType = 3;
            beanList.add(voiceBean);
            beanList.add(videoBean);
        }else if (mType == 2){
            NBSAppAgent.onEvent("服务-图文咨询-保存");
            SaveServiceSetBean imageBean = new SaveServiceSetBean();
            imageBean.ServiceSwitch = switch_voice.isChecked()?1:0;
            imageBean.ServicePrice = Float.valueOf(et_voice.getText().toString());
            imageBean.ServiceType = 1;
            beanList.add(imageBean);
        }
        Http_saveServiceSet_Event event = new Http_saveServiceSet_Event(beanList,new HttpListener<String>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                }
                dismissLoadDialog();
                ToastUtils.showShort(mContext,msg);
            }

            @Override
            public void onSuccess(String msg) {
                if (DebugUtils.debug) {
                }
                dismissLoadDialog();
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_save_success));
                EventBus.getDefault().post(new InfoEvent.SettingSuc());
                finish();
            }
        });

        HttpClient httpClient = NetService.createClient(mContext,HttpClient.DOCTOR_API_URL, event);
        httpClient.start();
    }

    private void getServiceSet() {
        showLoadDialog(R.string.string_wait);
        Http_getServiceSet_Event event = new Http_getServiceSet_Event(new HttpListener<ServiceSetBean>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                }
                dismissLoadDialog();
                ToastUtils.show(mContext, msg, Toast.LENGTH_SHORT);
            }

            @Override
            public void onSuccess(ServiceSetBean bean) {
                if (DebugUtils.debug) {
                }
                dismissLoadDialog();
                //加载服务设置数据
                List<ServiceSetBean.DataBean> list = bean.getData();
                for(int i=0;i<list.size();i++){
                    ServiceSetBean.DataBean dataBean = list.get(i);
                    switch (dataBean.getServiceType()){
                        case 1://图文咨询
                            is_start_image_consult = dataBean.getServiceSwitch()==1?true:false;
                            consultPrice = dataBean.getServicePrice();
                            break;
                        case 2://语音咨询
                            is_start_voice_inquiry = dataBean.getServiceSwitch()==1?true:false;
                            break;
                        case 3://视频咨询
                            is_start_video_inquiry = dataBean.getServiceSwitch()==1?true:false;
                            vodiePrice = dataBean.getServicePrice();
                            break;
                        case 4://家庭医生
                            is_start_family_doctor = dataBean.getServiceSwitch()==1?true:false;
                            break;
                    }
                }
                if (mType == 1){
                    switch_voice.setChecked(is_start_video_inquiry);
                    et_voice.setText(String.valueOf(vodiePrice));
                }else if (mType == 2){
                    switch_voice.setChecked(is_start_image_consult);
                    et_voice.setText(String.valueOf(consultPrice));
                }

            }
        });

        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }
}
