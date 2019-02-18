package com.kmwlyy.doctor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.View.MonthDateView;
import com.kmwlyy.doctor.model.FreeCalendarBean;
import com.kmwlyy.doctor.model.InfoEvent;
import com.kmwlyy.doctor.model.httpEvent.Http_getFreeCalender_Event;
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
 * Created by xcj on 2017/7/10.
 */

public class FreeClinicShowActivity extends BaseActivity{
    public static final String TAG = "FreeClinicShowActivity";
    public static final int LAST_MONTH = -1,NEXT_MONTH = 1;
    public String currentDate = "";
    public FreeCalendarBean freeCalendarBean;
    public int Month = 0; //请求的月份， 0表示当前月份， 1表示下个月 -1表示上个月
    public List<Integer> clinicList = new ArrayList<>();//存放当月设置义诊的日期
    //返回和保存
    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.tv_date)
    TextView tv_date;
    @ViewInject(R.id.tv_right)
    TextView tv_right;
    @ViewInject(R.id.monthDateView)
    MonthDateView monthDateView;
    @ViewInject(R.id.iv_left_arrow)
    ImageView iv_left;
    @ViewInject(R.id.iv_right_arrow)
    ImageView iv_right;
    @ViewInject(R.id.tv_count)
    TextView tv_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_clinic_show);
        ViewUtils.inject(this); //注入view和事件
        //初使化
        init();
        EventBus.getDefault().register(this);
    }

    private void init() {
        ((TextView) findViewById(R.id.tv_center)).setText("图文义诊");
        btn_left.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(this);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("设置");
        tv_right.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        iv_right.setOnClickListener(this);
        monthDateView.setTextView(tv_date);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_left://退出
                finish();
                break;
            case R.id.iv_left_arrow://上个月
                Month = Month - 1;
                loadData(MyUtils.getYearMonth(Month),LAST_MONTH);
                break;
            case R.id.iv_right_arrow://下个月
                Month = Month + 1;
                loadData(MyUtils.getYearMonth(Month),NEXT_MONTH);
                break;
            case R.id.tv_right:
                NBSAppAgent.onEvent("服务-图文义诊-设置");
                //跳转到设置页面
                Intent intent = new Intent(FreeClinicShowActivity.this, FreeClinicSettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(MyUtils.getYearMonth(Month),0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshData(InfoEvent.SettingSuc refreshData) {
        finish();
    }

    /**
     *
     * @param monthStr
     * type = 1 表示向后移一个月日历  -1表示向前移一个月
     */
    public void loadData(String monthStr,final int type){
        showLoadDialog(R.string.string_wait);
        Http_getFreeCalender_Event event = new Http_getFreeCalender_Event(monthStr,new HttpListener<FreeCalendarBean>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                if(type == LAST_MONTH){
                    Month = Month + 1;
                }
                else if(type == NEXT_MONTH){
                    Month = Month - 1;
                }
                dismissLoadDialog();
                ToastUtils.showShort(mContext,msg);
            }

            @Override
            public void onSuccess(FreeCalendarBean bean) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request success");
                }
                dismissLoadDialog();
                freeCalendarBean = bean;
                currentDate = bean.getClinicMonth();
                clinicList.clear();

                if(bean.getClinicDates() == null){//当月没有任何排班

                }else{
                    List<String> list= Arrays.asList(bean.getClinicDates() .split(","));
                    for(int i=0;i<list.size();i++){
                        clinicList.add(Integer.parseInt(list.get(i)));
                    }
                }

                monthDateView.setClinicList(clinicList);
                tv_count.setText(String.valueOf(bean.getAcceptCount()));
                if(type == LAST_MONTH){
                    monthDateView.onLeftClick();
                }
                else if(type == NEXT_MONTH){
                    monthDateView.onRightClick();
                }
                updateUI();
            }
        });

        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

    public void updateUI(){
        if(Month == 1){
            iv_right.setImageResource(R.mipmap.icon_next_week2);
            iv_right.setClickable(false);
        }else {
            iv_right.setImageResource(R.mipmap.icon_next_week1);
            iv_right.setClickable(true);
        }

        if(Month == 0){
            iv_left.setImageResource(R.mipmap.icon_previous_week2);
            iv_left.setClickable(false);
        }else {
            iv_left.setImageResource(R.mipmap.icon_previous_week1);
            iv_left.setClickable(true);
        }
    }
}
