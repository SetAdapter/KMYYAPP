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
import com.kmwlyy.doctor.adapter.ScheduleDayAdapter;
import com.kmwlyy.doctor.model.CalendarBean;
import com.kmwlyy.doctor.model.DateDay;
import com.kmwlyy.doctor.model.Schedule;
import com.kmwlyy.doctor.model.httpEvent.Http_getCalendar_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_saveCalendar_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.winson.ui.widget.ResizeGridView;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends BaseActivity {
    public static final String TAG = ScheduleActivity.class.getSimpleName();
    public String currentDate, oldDate;
    public ScheduleDayAdapter dayAdapter;
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

    TextView mCurrentWeekTV, mNextWeekTV;
    boolean mOnNextWeek;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ViewUtils.inject(this); //注入view和事件
        //初使化
        init();
        getCalendar(currentDate);
    }

    private void init() {
        ((TextView) findViewById(R.id.tv_center)).setText(getResources().getString(R.string.my_schedule));
        btn_left.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(this);

        btn_last_week.setOnClickListener(this);
        btn_next_week.setOnClickListener(this);

        mCurrentWeekTV = (TextView) findViewById(R.id.current_week);
        mNextWeekTV = (TextView) findViewById(R.id.next_week);
        mCurrentWeekTV.setOnClickListener(this);
        mNextWeekTV.setOnClickListener(this);
        mCurrentWeekTV.setSelected(true);

        mCurrentWeekTV.setSelected(true);
        mNextWeekTV.setSelected(false);
        currentDate = MyUtils.getCurrentDate("yyyy-MM-dd");
        today = MyUtils.getCurrentDate("yyyy-MM-dd");
        setDateTitle();

    }

    /**
     * 设置 时间文字
     */
    private void setDateTitle() {
        tv_start_date.setText(currentDate);
        tv_end_date.setText(MyUtils.getFutureDate(currentDate, "yyyy-MM-dd", 6));

        if (currentDate.equals(today)) {
            btn_last_week.setImageResource(R.mipmap.icon_previous_week2);
            btn_last_week.setClickable(false);
        } else {
            btn_last_week.setImageResource(R.mipmap.icon_previous_week1);
            btn_last_week.setClickable(true);
        }
    }

    /**
     * 获取排班
     *
     * @param date
     */
    private void getCalendar(String date) {
        showLoadDialog(R.string.string_wait);
        Http_getCalendar_Event event = new Http_getCalendar_Event(date, new HttpListener<CalendarBean>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                dismissLoadDialog();
                if (oldDate != null) {
                    currentDate = oldDate;
                    setDateTitle();
                }
                ToastUtils.showShort(mContext, msg);
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
                for (int i = 0; i < calendarBean.ScheduleList.size(); i++) {
                    week = calendarBean.ScheduleList.get(i);
                    for (int j = 0; j < week.DoctorSchedule.size(); j++) {
                        schedule = week.DoctorSchedule.get(j);
                        dayList.add(schedule);
                    }
                }
//                if(dayList.size() == 42){
                //数据必须完整（6行7列 = 42个Schedule），适配器才能画出排班。 不然会闪退
                dayAdapter = new ScheduleDayAdapter(mContext, dayList, data.DateWeekList);
                grid_day.setAdapter(dayAdapter);
//                }
            }
        });

        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_last_week://上一周
                oldDate = currentDate;
                currentDate = MyUtils.getFutureDate(currentDate, "yyyy-MM-dd", -7);
                setDateTitle();
                getCalendar(currentDate);
                break;
            case R.id.iv_next_week://下一周
                oldDate = currentDate;
                currentDate = MyUtils.getFutureDate(currentDate, "yyyy-MM-dd", 7);
                setDateTitle();
                getCalendar(currentDate);
                break;
            case R.id.tv_left://退出
                finish();
                break;
            case R.id.current_week:
                if (mOnNextWeek) {
                    mCurrentWeekTV.setSelected(true);
                    mNextWeekTV.setSelected(false);
                    mOnNextWeek = false;
                    currentDate = MyUtils.getCurrentDate("yyyy-MM-dd");
                    getCalendar(currentDate);
                }
                break;
            case R.id.next_week:
                if (!mOnNextWeek) {
                    mCurrentWeekTV.setSelected(false);
                    mNextWeekTV.setSelected(true);
                    mOnNextWeek = true;
                    currentDate = MyUtils.getFutureDate(currentDate, "yyyy-MM-dd", 7);
                    getCalendar(currentDate);
                }
                break;
        }
    }
}
