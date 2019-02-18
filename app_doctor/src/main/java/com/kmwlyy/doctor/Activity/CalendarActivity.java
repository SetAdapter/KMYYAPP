package com.kmwlyy.doctor.Activity;

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
import com.kmwlyy.doctor.adapter.DayAdapter;
import com.kmwlyy.doctor.model.CalendarBean;
import com.kmwlyy.doctor.model.DateDay;
import com.kmwlyy.doctor.model.Schedule;
import com.kmwlyy.doctor.model.httpEvent.Http_getCalendar_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_saveCalendar_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.networkbench.agent.impl.NBSAppAgent;
import com.winson.ui.widget.ResizeGridView;

import java.util.ArrayList;
import java.util.List;

public class CalendarActivity extends BaseActivity {
    public static final String TAG = "CalendarActivity";
    public String currentDate,oldDate;
    public DayAdapter dayAdapter;
    public CalendarBean calendarBean;
    public List<Schedule> dayList;
    public String today;

    //返回和保存
    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.tv_right)
    TextView tv_right;

    //上周，下周
    @ViewInject(R.id.iv_last_week)
    ImageView btn_last_week;
    @ViewInject(R.id.iv_next_week)
    ImageView btn_next_week;

    //开始结束时间
    @ViewInject(R.id.tv_start_date)
    TextView tv_start_date;
    @ViewInject(R.id.tv_end_date)
    TextView tv_end_date;

    //排班日期
    @ViewInject(R.id.grid_day)
    ResizeGridView grid_day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ViewUtils.inject(this); //注入view和事件
        //初使化
        init();
        getCalendar(currentDate);
    }

    private void init() {
        ((TextView)findViewById(R.id.tv_center)).setText(getResources().getString(R.string.string_calendar1));
        btn_left.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(this);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(this);
        tv_right.setText(getResources().getString(R.string.string_save));

        btn_last_week.setOnClickListener(this);
        btn_next_week.setOnClickListener(this);

        currentDate = MyUtils.getCurrentDate("yyyy-MM-dd");
        today = MyUtils.getCurrentDate("yyyy-MM-dd");
        setDateTitle();

    }

    /**
     * 设置 时间文字
     */
    private void setDateTitle() {
        tv_start_date.setText(currentDate);
        tv_end_date.setText(MyUtils.getFutureDate(currentDate,"yyyy-MM-dd",6));

        if(currentDate.equals(today)){
            btn_last_week.setImageResource(R.mipmap.icon_previous_week2);
            btn_last_week.setClickable(false);
        }else{
            btn_last_week.setImageResource(R.mipmap.icon_previous_week1);
            btn_last_week.setClickable(true);
        }
        if (currentDate.equals(MyUtils.getFutureDate(today,"yyyy-MM-dd",7))){
            btn_next_week.setImageResource(R.mipmap.icon_next_week2);
            btn_next_week.setClickable(false);
        }else{
            btn_next_week.setImageResource(R.mipmap.icon_next_week1);
            btn_next_week.setClickable(true);
        }
    }

    /**
     * 获取排班
     * @param date
     */
    private void getCalendar(String date) {
        showLoadDialog(R.string.string_wait);
        Http_getCalendar_Event event = new Http_getCalendar_Event(date,new HttpListener<CalendarBean>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                dismissLoadDialog();
                if(oldDate != null){
                    currentDate = oldDate;
                    setDateTitle();
                }
                ToastUtils.showShort(mContext,msg);
            }

            @Override
            public void onSuccess(CalendarBean data) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request success");
                }
                dismissLoadDialog();

                //排班的日期
                dayList = new ArrayList<>();
                calendarBean = new CalendarBean();
                calendarBean = data;
                DateDay week;
                Schedule schedule;
                for(int i=0;i<calendarBean.ScheduleList.size();i++){
                    week = calendarBean.ScheduleList.get(i);
                    for(int j=0;j<week.DoctorSchedule.size();j++){
                        schedule = week.DoctorSchedule.get(j);
                        schedule.AppointmentCounts = null;
                        dayList.add(schedule);
                    }
                }
//                if(dayList.size() == 42){
                    //数据必须完整（6行7列 = 42个Schedule），适配器才能画出排班。 不然会闪退
                    dayAdapter = new DayAdapter(mContext,dayList,data.DateWeekList);
                    grid_day.setAdapter(dayAdapter);
//                }
            }
        });

        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

    /**
     * 保存排班
     */
    private void saveCalendar(List<DateDay> list,String date) {
        if(list.size() == 0){
            return;
        }

        showLoadDialog(R.string.string_wait);
        Http_saveCalendar_Event event = new Http_saveCalendar_Event(list,date,new HttpListener<String>(
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
            public void onSuccess(String str) {
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
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.iv_last_week://上一周
                oldDate = currentDate;
                currentDate = MyUtils.getFutureDate(currentDate,"yyyy-MM-dd",-7);
                setDateTitle();
                getCalendar(currentDate);
                break;
            case R.id.iv_next_week://下一周
                oldDate = currentDate;
                currentDate = MyUtils.getFutureDate(currentDate,"yyyy-MM-dd",7);
                setDateTitle();
                getCalendar(currentDate);
                break;
            case R.id.tv_left://退出
                finish();
                break;
            case R.id.tv_right://保存
                NBSAppAgent.onEvent("服务—排班保存");
                if(null == dayAdapter){
                    //防止因断网没有请求到数据的情况
                    ToastUtils.showShort(mContext,getResources().getString(R.string.string_calendar_invaild));
                }else{
                    dayList = dayAdapter.getData();
                    List<DateDay> list = new ArrayList<>();
                    if(calendarBean != null){
                        list.addAll(calendarBean.ScheduleList);
                        for(int i=0;i<list.size();i++){
                            list.get(i).DoctorSchedule.clear();
                            for(int j=0;j<7;j++){
                                list.get(i).DoctorSchedule.add(dayList.get(7 * i + j));
                            }
                        }
                    }

                    String date = tv_start_date.getText().toString();
                    saveCalendar(list,date);
                }

                break;
        }
    }
}
