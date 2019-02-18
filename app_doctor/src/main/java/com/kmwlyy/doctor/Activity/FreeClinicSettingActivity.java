package com.kmwlyy.doctor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.FreeCalendarBean;
import com.kmwlyy.doctor.model.InfoEvent;
import com.kmwlyy.doctor.model.SaveClinicScheduleBean;
import com.kmwlyy.doctor.model.SaveServiceSetBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getFreeCalender_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_saveClinicSchedule_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_saveServiceSet_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.networkbench.agent.impl.NBSAppAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xcj on 2017/6/27.
 */

public class FreeClinicSettingActivity extends BaseActivity {
    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.tv_center)
    TextView tv_title;
    @ViewInject(R.id.tv_right)
    TextView tv_right;
    @ViewInject(R.id.switch_free)
    CheckBox switch_free;
    @ViewInject(R.id.et_free)
    EditText et_free;
    @ViewInject(R.id.ll_calendar_free)
    LinearLayout ll_calendar_free;

    private List<Integer> clinicList = new ArrayList<>();
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_clinic_setting);
        ViewUtils.inject(this);
        tv_title.setText("设置图文义诊");
        tv_right.setText("保存");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(this);
        btn_left.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(this);
        ll_calendar_free.setOnClickListener(this);
        EventBus.getDefault().register(this);
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshData(InfoEvent.FreeCalendar refreshData) {
        clinicList = refreshData.calendarlist;
        currentDate = refreshData.currentDate;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch(v.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_right:
                NBSAppAgent.onEvent("服务—图文义诊—保存");
                //调用保存接口
                save();
                break;
            case R.id.ll_calendar_free:
                startActivity(new Intent(mContext,FreeCalendarActivity.class));
                break;
        }
    }

    private void save(){
        String count = et_free.getText().toString();
        if (TextUtils.isEmpty(count)){
            ToastUtils.showShort(FreeClinicSettingActivity.this,"请输入次数");
            return;
        }
        if (switch_free.isChecked()){
            if (clinicList.size() == 0){
                ToastUtils.showShort(FreeClinicSettingActivity.this,"请设置排班");
                return;
            }
        }
        if (TextUtils.isEmpty(currentDate)){
            currentDate =  MyUtils.getYearMonth(0);
        }
        showLoadDialog(R.string.string_wait_save);

        List<SaveClinicScheduleBean> list = new ArrayList<>();
        SaveClinicScheduleBean bean = new SaveClinicScheduleBean();
        bean.AcceptCount = count;
        bean.ClinicMonth = currentDate;
        bean.ClinicDates = parseString(clinicList);
        bean.State = switch_free.isChecked();
        list.add(bean);

        Http_saveClinicSchedule_Event event = new Http_saveClinicSchedule_Event(list, new HttpListener<String>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                }
                dismissLoadDialog();
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_save_failed));
            }

            @Override
            public void onSuccess(String msg) {
                if (DebugUtils.debug) {
                }
                dismissLoadDialog();
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_save_success));
                finish();
                EventBus.getDefault().post(new InfoEvent.SettingSuc());
            }
        });

        HttpClient httpClient = NetService.createClient(mContext,HttpClient.DOCTOR_API_URL, event);
        httpClient.start();
    }

    private String parseString(List<Integer> mList){
        String str = "";
        if(null != mList){
            for(int i=0;i<mList.size();i++){
                if(i!= (mList.size() -1) ){//小于10的前面补个0. 如1 用 01表示
                    str = str + (mList.get(i)<10?("0"+mList.get(i)):mList.get(i)) + ",";
                }else{
                    str = str + (mList.get(i)<10?("0"+mList.get(i)):mList.get(i));
                }
            }
        }
        return str;
    }

    private void loadData(){
        String monthStr = MyUtils.getYearMonth(0);
        showLoadDialog(R.string.string_wait);
        Http_getFreeCalender_Event event = new Http_getFreeCalender_Event(monthStr,new HttpListener<FreeCalendarBean>(
        ) {
            @Override
            public void onError(int code, String msg) {
                dismissLoadDialog();
                ToastUtils.showShort(mContext,msg);
            }

            @Override
            public void onSuccess(FreeCalendarBean bean) {
                if (DebugUtils.debug) {
                }
                dismissLoadDialog();
                switch_free.setChecked(bean.isState());
                et_free.setText(bean.getAcceptCount()+"");
            }
        });

        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }
}
