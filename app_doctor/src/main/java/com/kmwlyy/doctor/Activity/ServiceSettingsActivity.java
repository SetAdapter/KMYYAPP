package com.kmwlyy.doctor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpCode;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.ServiceSetBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getServiceSet_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_saveServiceSet_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

public class ServiceSettingsActivity extends BaseActivity {
    public static final String TAG = "ServiceSettingsActivity";
    public ServiceSetBean serviceSetBean;
    //服务名称
    @ViewInject(R.id.tv_online)
    TextView tv_online;
    @ViewInject(R.id.tv_voice)
    TextView tv_voice;
    @ViewInject(R.id.tv_video)
    TextView tv_video;
    @ViewInject(R.id.tv_doctor)
    TextView tv_doctor;
    @ViewInject(R.id.tv_long)
    TextView tv_long;
    //服务价格
    @ViewInject(R.id.et_online)
    EditText et_online;
    @ViewInject(R.id.et_voice)
    EditText et_voice;
    @ViewInject(R.id.et_video)
    EditText et_video;
    @ViewInject(R.id.et_doctor)
    EditText et_doctor;
    @ViewInject(R.id.et_long)
    EditText et_long;
    @ViewInject(R.id.et_free)
    EditText et_free;
    //服务开关
    @ViewInject(R.id.switch_online)
    CheckBox switch_online;
    @ViewInject(R.id.switch_voice)
    CheckBox switch_voice;
    @ViewInject(R.id.switch_video)
    CheckBox switch_video;
    @ViewInject(R.id.switch_doctor)
    CheckBox switch_doctor;
    @ViewInject(R.id.switch_long)
    CheckBox switch_long;
    @ViewInject(R.id.switch_free)
    CheckBox switch_free;
    //设置排班
    @ViewInject(R.id.ll_calendar_voice)
    LinearLayout ll_calendar_voice;
    @ViewInject(R.id.ll_calendar_video)
    LinearLayout ll_calendar_video;
    @ViewInject(R.id.ll_calendar_free)
    LinearLayout ll_calendar_free;

    //返回和保存
    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.tv_right)
    TextView tv_right;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_settings);
        ViewUtils.inject(this); //注入view和事件
        //初使化
        init();
        getServiceSet();
    }

    private void init() {
        ((TextView)findViewById(R.id.tv_center)).setText(getResources().getString(R.string.string_service_setting));
        btn_left.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(this);
        btn_left.setText(getResources().getString(R.string.string_back));
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(this);
        tv_right.setText(getResources().getString(R.string.string_save));

        switch_doctor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    switch_online.setChecked(b);
                    switch_voice.setChecked(b);
                    switch_video.setChecked(b);
                }
            }
        });

        ll_calendar_voice.setOnClickListener(this);
        ll_calendar_video.setOnClickListener(this);
        ll_calendar_free.setOnClickListener(this);
    }

    /**
     * 获取服务设置
     */
    private void getServiceSet() {
        showLoadDialog(R.string.string_wait);
        Http_getServiceSet_Event event = new Http_getServiceSet_Event(new HttpListener<ServiceSetBean>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                dismissLoadDialog();
                ToastUtils.show(mContext, msg, Toast.LENGTH_SHORT);
                finish();
            }

            @Override
            public void onSuccess(ServiceSetBean bean) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request success");
                }
                dismissLoadDialog();
                serviceSetBean = bean;
                //加载义诊数据
                et_free.setText(""+serviceSetBean.getFreeClinicSetting().getAcceptCount());
                switch_free.setChecked(serviceSetBean.getFreeClinicSetting().isState());
                //加载服务设置数据
                List<ServiceSetBean.DataBean> list = serviceSetBean.getData();
                for(int i=0;i<list.size();i++){
                    ServiceSetBean.DataBean dataBean = list.get(i);
                    switch (dataBean.getServiceType()){
                        case 1://图文咨询
                            tv_online.setText(dataBean.getServiceTypeName());
                            et_online.setText(""+dataBean.getServicePrice());
                            switch_online.setChecked(dataBean.getServiceSwitch()==1?true:false);
                            break;
                        case 2://语音咨询
                            tv_voice.setText(dataBean.getServiceTypeName());
                            et_voice.setText(""+dataBean.getServicePrice());
                            switch_voice.setChecked(dataBean.getServiceSwitch()==1?true:false);
                            break;
                        case 3://视频咨询
                            tv_video.setText(dataBean.getServiceTypeName());
                            et_video.setText(""+dataBean.getServicePrice());
                            switch_video.setChecked(dataBean.getServiceSwitch()==1?true:false);
                            break;
                        case 4://家庭医生
                            tv_doctor.setText(dataBean.getServiceTypeName());
                            et_doctor.setText(""+dataBean.getServicePrice());
                            switch_doctor.setChecked(dataBean.getServiceSwitch()==1?true:false);
                            break;
                        case 5://远程会诊
                            tv_long.setText(dataBean.getServiceTypeName());
                            et_long.setText(""+dataBean.getServicePrice());
                            switch_long.setChecked(dataBean.getServiceSwitch()==1?true:false);
                            break;
                    }
                }
            }
        });

        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch(v.getId()){
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_right:
                save();
                break;
            case R.id.ll_calendar_video:
            case R.id.ll_calendar_voice:
                startActivity(new Intent(mContext,CalendarActivity.class));
                break;
            case R.id.ll_calendar_free:
                startActivity(new Intent(mContext,FreeCalendarActivity.class));
                break;
        }
    }

    /**
     * 检查家族医生开关 （必需图文语音视频都打开了，才能打开这个）
     */
    public Boolean checkFamilyVaild(){
        if(switch_doctor.isChecked()){
            if(switch_online.isChecked() && switch_voice.isChecked() && switch_video.isChecked()){
                return false;
            }else{
                return true; //有的开关没打开，不让保存
            }
        }
        return false; //检查OK，没有问题
    }

    /**
     * 检查义诊开关 （必需图文打开了，才能打开这个）
     */
    public Boolean checkYiZhenVaild(){
        if(switch_free.isChecked()){
            if(switch_online.isChecked()){
                return false;
            }else{
                return true; //图文开关没打开，不让保存
            }
        }
        return false; //检查OK，没有问题
    }

    /**
     * 发请求保存服务设置
     */
    private void save() {
        if(et_online.getText().toString().isEmpty() || et_voice.getText().toString().isEmpty() || et_video.getText().toString().isEmpty() || et_doctor.getText().toString().isEmpty()|| et_long.getText().toString().isEmpty()){
            ToastUtils.showShort(mContext,getResources().getString(R.string.string_missing_parm));
            return;
        }

        if(checkFamilyVaild()){
            ToastUtils.showShort(mContext,getResources().getString(R.string.string_invaild));
            return;
        }
        if(checkYiZhenVaild()){
            ToastUtils.showShort(mContext,getResources().getString(R.string.string_invaild1));
            return;
        }

        //保存服务设置数据
        List<ServiceSetBean.DataBean> list = serviceSetBean.getData();
        for(int i=0;i<list.size();i++){
            ServiceSetBean.DataBean dataBean = list.get(i);
            switch (dataBean.getServiceType()){
                case 1:
                    list.get(i).setServicePrice(Double.parseDouble(et_online.getText().toString().trim()));
                    list.get(i).setServiceSwitch(switch_online.isChecked()?1:0);
                    break;
                case 2:
                    list.get(i).setServicePrice(Double.parseDouble(et_voice.getText().toString().trim()));
                    list.get(i).setServiceSwitch(switch_voice.isChecked()?1:0);
                    break;
                case 3:
                    list.get(i).setServicePrice(Double.parseDouble(et_video.getText().toString().trim()));
                    list.get(i).setServiceSwitch(switch_video.isChecked()?1:0);
                    break;
                case 4:
                    list.get(i).setServicePrice(Double.parseDouble(et_doctor.getText().toString().trim()));
                    list.get(i).setServiceSwitch(switch_doctor.isChecked()?1:0);
                    break;
                case 5:
                    list.get(i).setServicePrice(Double.parseDouble(et_long.getText().toString().trim()));
                    list.get(i).setServiceSwitch(switch_long.isChecked()?1:0);
                    break;
            }
        }
        serviceSetBean.setData(list);
        //保存义诊数据
        serviceSetBean.getFreeClinicSetting().setState(switch_free.isChecked());
        serviceSetBean.getFreeClinicSetting().setAcceptCount(Integer.parseInt(et_free.getText().toString()));
      /*  showLoadDialog(R.string.string_wait_save);

        Http_saveServiceSet_Event event = new Http_saveServiceSet_Event(serviceSetBean,new HttpListener<String>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                dismissLoadDialog();
                ToastUtils.showShort(mContext,msg);
            }

            @Override
            public void onSuccess(String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request success");
                }
                dismissLoadDialog();
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_save_success));
                finish();
            }
        });

        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
        */
    }


}
