package com.kmwlyy.doctor.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.View.SelectDayPopupWindow;
import com.kmwlyy.doctor.adapter.StopScheduleDayAdapter;
import com.kmwlyy.doctor.model.CalendarBean;
import com.kmwlyy.doctor.model.DateDay;
import com.kmwlyy.doctor.model.DateWeek;
import com.kmwlyy.doctor.model.InfoEvent;
import com.kmwlyy.doctor.model.Schedule;
import com.kmwlyy.doctor.model.WorkTimeBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getCalendar_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_getWorkingTimeBase_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_stopDiagnosis_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.networkbench.agent.impl.NBSAppAgent;
import com.winson.ui.widget.AlterDialogView;
import com.winson.ui.widget.ResizeGridView;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.qqtheme.framework.picker.OptionPicker;

/**
 * Created by xcj on 2017/6/29.
 */

public class IssueStopDiagnoseActivity extends BaseActivity {
    public String currentDate,oldDate;
    public StopScheduleDayAdapter dayAdapter;
    public CalendarBean calendarBean;
    public List<Schedule> dayList = new ArrayList<>();
    public List<DateWeek> weekList = new ArrayList<>();
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

    @ViewInject(R.id.ll_start_day)
    LinearLayout ll_start_day;
    @ViewInject(R.id.ll_start_time)
    LinearLayout ll_start_time;
    @ViewInject(R.id.ll_end_day)
    LinearLayout ll_end_day;
    @ViewInject(R.id.ll_end_time)
    LinearLayout ll_end_time;
    @ViewInject(R.id.tv_start_day)
    TextView tv_start_day;
    @ViewInject(R.id.tv_end_day)
    TextView tv_end_day;
    @ViewInject(R.id.tv_start_time)
    TextView tv_start_time;
    @ViewInject(R.id.tv_end_time)
    TextView tv_end_time;

    //排班日期
    @ViewInject(R.id.grid_day)
    ResizeGridView grid_day;
    private String[] option = {};
    private List<WorkTimeBean> mList = new ArrayList<>();
    private String mStartDayStr;
    private String mEndDayStr;
    private String oneStartTime;
    private String oneEndTime;
    private String twoStartTime;
    private String twoEndTime;
    private String startTimeId;
    private String endTimeId;
    private String startHintStr;
    private String endHintStr;

    private String saveStartDay;
    private String saveEndDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_stop_diagnose);
        ViewUtils.inject(this); //注入view和事件
        //初使化
        init();
        getCalendar(currentDate);
        getOptionList();
    }
    private void init() {
        ((TextView)findViewById(R.id.tv_center)).setText("停诊发布");
        btn_left.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(this);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(this);
        tv_right.setText("提交");

        btn_last_week.setOnClickListener(this);
        btn_next_week.setOnClickListener(this);
        ll_start_day.setOnClickListener(this);
        ll_start_time.setOnClickListener(this);
        ll_end_day.setOnClickListener(this);
        ll_end_time.setOnClickListener(this);

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
                }
                dismissLoadDialog();

                //排班的日期
                dayList.clear();
                calendarBean = new CalendarBean();
                calendarBean = data;
                weekList = data.DateWeekList;
                DateDay week;
                Schedule schedule;
                for(int i=0;i<calendarBean.ScheduleList.size();i++){
                    week = calendarBean.ScheduleList.get(i);
                    for(int j=0;j<week.DoctorSchedule.size();j++){
                        schedule = week.DoctorSchedule.get(j);
                        dayList.add(schedule);
                    }
                }
//                if(dayList.size() == 42){
                //数据必须完整（6行7列 = 42个Schedule），适配器才能画出排班。 不然会闪退
                if (!refreshDate()) {
                    dayAdapter = new StopScheduleDayAdapter(mContext, dayList, weekList, 0, 0,0,0,0,0);
                    grid_day.setAdapter(dayAdapter);
                }
//                }
            }
        });

        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

    /**
     * 保存停诊时间
     */
    private void saveCalendar() {
        final String startDay = tv_start_day.getText().toString();
        final String startTime = tv_start_time.getText().toString();
        final String endDay = tv_end_day.getText().toString();
        final String endTime = tv_end_time.getText().toString();
        if (TextUtils.isEmpty(startDay)){
            ToastUtils.showShort(IssueStopDiagnoseActivity.this,"请选择停诊起始日期");
            return;
        }
        if (TextUtils.isEmpty(startTime)){
            ToastUtils.showShort(IssueStopDiagnoseActivity.this,"请选择停诊起始时间");
            return;
        }
        if (TextUtils.isEmpty(endDay)){
            ToastUtils.showShort(IssueStopDiagnoseActivity.this,"请选择停诊结束日期");
            return;
        }
        if (TextUtils.isEmpty(endTime)){
            ToastUtils.showShort(IssueStopDiagnoseActivity.this,"请选择停诊结束时间");
            return;
        }
        for (WorkTimeBean bean: mList){
            if (startTime.equals(bean.mWorkingTimeName)){
                startTimeId = bean.mWorkingTimeBaseID;
                oneStartTime = bean.mBeginTime;
                oneEndTime = bean.mEndTime;
            }
            if (endTime.equals(bean.mWorkingTimeName)){
                endTimeId = bean.mWorkingTimeBaseID;
                twoStartTime = bean.mBeginTime;
                twoEndTime = bean.mEndTime;
            }
        }
        oneStartTime = oneStartTime.substring(0,2)+oneStartTime.substring(3,5);
        oneEndTime = oneEndTime.substring(0,2)+oneEndTime.substring(3,5);
        twoStartTime = twoStartTime.substring(0,2)+twoStartTime.substring(3,5);
        twoEndTime = twoEndTime.substring(0,2)+twoEndTime.substring(3,5);
        int i = Integer.parseInt(mStartDayStr);
        int j = Integer.parseInt(mEndDayStr);
        if (i == j){
            int oneStart = Integer.parseInt(oneStartTime);
            int oneEnd = Integer.parseInt(oneEndTime);
            int twoStart = Integer.parseInt(twoStartTime);
            int twoEnd = Integer.parseInt(twoEndTime);
            if (oneStart != twoStart){
                if (oneEnd > twoStart){
                    ToastUtils.showShort(mContext,"起始时间段不能大于结束时间段");
                    return;
                }
            }
        }

        AlterDialogView.Builder builder = new AlterDialogView.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("停诊发布后，将导致  "+startHintStr+"  "+startTime+"  至  "+endHintStr+"  "+endTime+"  无法提供服务，患者在此时间段已预定的服务亦将被取消，确认发布停诊？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0,
                                        int arg1) {
                        arg0.dismiss();
                        //打开设置页面
                        showLoadDialog(R.string.string_wait);
                        Http_stopDiagnosis_Event event = new Http_stopDiagnosis_Event(saveStartDay, startTimeId, saveEndDay, endTimeId, new HttpListener() {
                            @Override
                            public void onError(int code, String msg) {
                                dismissLoadDialog();
                                ToastUtils.showShort(IssueStopDiagnoseActivity.this,"设置停诊失败");
                            }

                            @Override
                            public void onSuccess(Object o) {
                                ToastUtils.showShort(IssueStopDiagnoseActivity.this,"设置停诊成功");
                                dismissLoadDialog();
                                EventBus.getDefault().post(new InfoEvent.IssueStopSuc());
                                finish();
                            }
                        });
                        HttpClient httpClient = NetService.createClient(mContext,HttpClient.DOCTOR_API_URL, event);
                        httpClient.start();
                    }
                });
        builder.create().show();

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
            case R.id.ll_start_day://选择开始日期
                openSelectDayDialog(1);
                break;
            case R.id.ll_start_time://选择开始时间
                openTimeOption(1);
                break;
            case R.id.ll_end_day://选择结束日期
                openSelectDayDialog(2);
                break;
            case R.id.ll_end_time://选择结束时间
                openTimeOption(2);
                break;
            case R.id.tv_right://保存停诊设置
                NBSAppAgent.onEvent("服务-停诊-保存");
                saveCalendar();
                break;
        }
    }

    private void openSelectDayDialog(final int i){
        final SelectDayPopupWindow menuWindow = new SelectDayPopupWindow(IssueStopDiagnoseActivity.this);
        //显示窗口
        menuWindow.showAtLocation(IssueStopDiagnoseActivity.this.findViewById(R.id.main_view), Gravity.BOTTOM, 0, 0); //设置layout在PopupWindow中显示的位置
        menuWindow.confirmDay(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 1) {
                    saveStartDay = menuWindow.getTime();

                    String month = menuWindow.getMonthStr();
                    String day = menuWindow.getDayStr();
                    if(Integer.parseInt(month)<10){
                        month = "0"+month;
                    }
                    if(Integer.parseInt(day)<10){
                        day = "0"+day;
                    }
                    mStartDayStr = month+day;
                    startHintStr = menuWindow.getMonthStr()+"月"+menuWindow.getDayStr()+"日";
                    if (!TextUtils.isEmpty(mEndDayStr)){
                        if (Double.valueOf(mEndDayStr) < Double.valueOf(mStartDayStr)){
                            LogUtils.i("testtttttt","mEndDayStr:" + mEndDayStr + "mEndDayStr" + mEndDayStr);
                            ToastUtils.showShort(mContext,"起始日期不能大于结束日期");
                            return;
                        }
                    }
                    tv_start_day.setText(menuWindow.getYearStr()+"年"+month+"月"+day+"日");
                    refreshDate();
                }else {
                    saveEndDay = menuWindow.getTime();
                    String month = menuWindow.getMonthStr();
                    String day = menuWindow.getDayStr();
                    if(Integer.parseInt(month)<10){
                        month = "0"+month;
                    }
                    if(Integer.parseInt(day)<10){
                        day = "0"+day;
                    }

                    mEndDayStr = month + day;


                    endHintStr = menuWindow.getMonthStr()+"月"+menuWindow.getDayStr()+"日";
                    if (!TextUtils.isEmpty(mStartDayStr)){
                        if (Double.valueOf(mEndDayStr) < Double.valueOf(mStartDayStr)){
                            LogUtils.i("testtttttt","mEndDayStr:" + Double.valueOf(mEndDayStr) + "mEndDayStr" + mEndDayStr);
                            ToastUtils.showShort(mContext,"结束日期不能小于起始日期");
                            return;
                        }
                    }
                    tv_end_day.setText(menuWindow.getYearStr()+"年"+month+"月"+day+"日");
                    refreshDate();
                }
            }
        });
    }

    private void getOptionList(){
        Http_getWorkingTimeBase_Event event = new Http_getWorkingTimeBase_Event(new HttpListener<List<WorkTimeBean>>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(IssueStopDiagnoseActivity.this,"获取时间列表失败");
            }

            @Override
            public void onSuccess(List<WorkTimeBean> workTimeBeen) {
                ArrayList arrayList = new ArrayList();
                for (WorkTimeBean bean:workTimeBeen){
                    arrayList.add(bean.mWorkingTimeName);
                }
                option = (String[])arrayList.toArray(new String[arrayList.size()]);
                mList = workTimeBeen;
                SimpleDateFormat daymatter    =   new    SimpleDateFormat    ("yyyy-MM-dd");
                SimpleDateFormat textdaymatter    =   new    SimpleDateFormat    ("yyyy年MM月dd日");
                SimpleDateFormat mstartdaymatter    =   new    SimpleDateFormat    ("MMdd");
                SimpleDateFormat formatter    =   new    SimpleDateFormat    ("HH:mm");
                SimpleDateFormat hinttextdaymatter    =   new    SimpleDateFormat    ("MM月dd日");
                Date curDate  = new Date(System.currentTimeMillis());//获取当前时间
                saveStartDay = daymatter.format(curDate);
                tv_start_day.setText(textdaymatter.format(curDate));
                mStartDayStr = mstartdaymatter.format(curDate);
                startHintStr = hinttextdaymatter.format(curDate);
                String str = formatter.format(curDate);
                str = str.substring(0,2)+str.substring(3,5);
                for (WorkTimeBean bean: workTimeBeen){
                    oneStartTime = bean.mBeginTime.substring(0,2)+bean.mBeginTime.substring(3,5);
                    oneEndTime = bean.mEndTime.substring(0,2)+bean.mEndTime.substring(3,5);
                    if (Double.valueOf(str) < Double.valueOf(oneStartTime)){
                        tv_start_time.setText(bean.mWorkingTimeName);
                        return;
                    }
                }
                if (TextUtils.isEmpty(tv_start_time.getText())){
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    saveStartDay = daymatter.format(c.getTime());
                    tv_start_day.setText(textdaymatter.format(c.getTime()));
                    mStartDayStr = mstartdaymatter.format(c.getTime());
                    startHintStr = hinttextdaymatter.format(c.getTime());
                }
                if (workTimeBeen.size() != 0) {
                    oneStartTime = workTimeBeen.get(0).mBeginTime.substring(0, 2) + workTimeBeen.get(0).mBeginTime.substring(3, 5);
                    oneEndTime = workTimeBeen.get(0).mEndTime.substring(0, 2) + workTimeBeen.get(0).mEndTime.substring(3, 5);
                    tv_start_time.setText(workTimeBeen.get(0).mWorkingTimeName);
                }

            }
        });
        HttpClient httpClient = NetService.createClient(mContext,HttpClient.DOCTOR_API_URL, event);
        httpClient.start();
    }

    private void openTimeOption(final int i){
        OptionPicker picker = new OptionPicker(this,option);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
            if (i == 1){
                //开始时间段
                for (WorkTimeBean bean: mList){
                    if (option.equals(bean.mWorkingTimeName)){
                        oneStartTime = bean.mBeginTime;
                        oneEndTime = bean.mEndTime;
                    }
                }
                SimpleDateFormat daymatter    =   new    SimpleDateFormat    ("MMdd");
                SimpleDateFormat formatter    =   new    SimpleDateFormat    ("HH:mm");
                Date curDate  = new Date(System.currentTimeMillis());//获取当前时间
                String str = formatter.format(curDate);
                String dayStr = daymatter.format(curDate);
                oneStartTime = oneStartTime.substring(0,2)+oneStartTime.substring(3,5);
                oneEndTime = oneEndTime.substring(0,2)+oneEndTime.substring(3,5);
                if (!TextUtils.isEmpty(mStartDayStr)){
                    int i = Integer.valueOf(dayStr);
                    int j = Integer.valueOf(mStartDayStr);
                    if (i == j){
                        str = str.substring(0,2)+str.substring(3,5);
                        if (Double.valueOf(str) > Double.valueOf(oneStartTime)){
                            ToastUtils.showShort(mContext,"请选择后面的时间段");
                            return;
                        }
                    }
                }
                tv_start_time.setText(option);
                refreshDate();
            }else{
                //结束时间段
                for (WorkTimeBean bean: mList){
                    if (option.equals(bean.mWorkingTimeName)){
                        twoStartTime = bean.mBeginTime;
                        twoEndTime = bean.mEndTime;
                    }
                }
                SimpleDateFormat daymatter    =   new    SimpleDateFormat    ("MMdd");
                SimpleDateFormat formatter    =   new    SimpleDateFormat    ("hh:mm");
                Date curDate  = new Date(System.currentTimeMillis());//获取当前时间
                String str = formatter.format(curDate);
                String dayStr = daymatter.format(curDate);
                twoStartTime = twoStartTime.substring(0,2)+twoStartTime.substring(3,5);
                twoEndTime = twoEndTime.substring(0,2)+twoEndTime.substring(3,5);
                if (!TextUtils.isEmpty(mEndDayStr)){
                    int i = Integer.valueOf(dayStr);
                    int j = Integer.valueOf(mEndDayStr);
                    if (i == j){
                        str = str.substring(0,2)+str.substring(3,5);
                        if (Double.valueOf(str) > Double.valueOf(twoStartTime)){
                            ToastUtils.showShort(mContext,"请选择后面的时间段");
                            return;
                        }
                    }
                }
                tv_end_time.setText(option);
                refreshDate();
            }
            }
        });
        picker.show();
    }

    private boolean refreshDate(){
        String startDay = tv_start_day.getText().toString();
        String startTime = tv_start_time.getText().toString();
        String endDay = tv_end_day.getText().toString();
        String endTime = tv_end_time.getText().toString();
        if (TextUtils.isEmpty(startDay)){
            return false;
        }
        if (TextUtils.isEmpty(startTime)){
            return false;
        }
        if (TextUtils.isEmpty(endDay)){
            return false;
        }
        if (TextUtils.isEmpty(endTime)){
            return false;
        }
        for (WorkTimeBean bean: mList){
            if (startTime.equals(bean.mWorkingTimeName)){
                oneStartTime = bean.mBeginTime;
                oneEndTime = bean.mEndTime;
            }
            if (endTime.equals(bean.mWorkingTimeName)){
                twoStartTime = bean.mBeginTime;
                twoEndTime = bean.mEndTime;
            }
        }

        oneStartTime = oneStartTime.substring(0,2)+oneStartTime.substring(3,5);
        oneEndTime = oneEndTime.substring(0,2)+oneEndTime.substring(3,5);
        twoStartTime = twoStartTime.substring(0,2)+twoStartTime.substring(3,5);
        twoEndTime = twoEndTime.substring(0,2)+twoEndTime.substring(3,5);

        dayAdapter = new StopScheduleDayAdapter(mContext,dayList,weekList,Double.valueOf(mStartDayStr),Double.valueOf(mEndDayStr),Double.valueOf(oneStartTime),Double.valueOf(oneEndTime),Double.valueOf(twoStartTime),Double.valueOf(twoEndTime));
        grid_day.setAdapter(dayAdapter);
        return true;
    }

}
