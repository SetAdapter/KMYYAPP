package com.kmwlyy.doctor.Activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.AppEventApi;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.HomeSettingBean;
import com.kmwlyy.doctor.model.httpEvent.Http_saveWorkingTime_Event;
import com.kmwlyy.personinfo.EventApi;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.qqtheme.framework.picker.OptionPicker;

/**
 * Created by TFeng on 2017/7/10.
 */

public class SettingWorkTimeActivity extends BaseActivity {

    private static final int SUNDAY = 0;
    private static final int MONDAY = 1;
    private static final int TUESDAY = 2;
    private static final int WEDNESDAY = 3;
    private static final int THURSDAY = 4;
    private static final int FRIDAY = 5;
    private static final int SATURDAY = 6;
    private static final String AFTERNOON = "下午";
    private static final String MORNING = "上午";
    private static final String NINGHT = "晚上";


    @ViewInject(R.id.tv_left)
    TextView tv_left;
    @ViewInject(R.id.tv_center)
    TextView tv_center;
    @ViewInject(R.id.iv_left)
    ImageView iv_left;
    @ViewInject(R.id.tv_right)
    TextView tv_right;
    @ViewInject(R.id.rg_week_day)
    RadioGroup rg_week_day;
    @ViewInject(R.id.lv_set_work_time)
    ListView lv_set_work_time;
    @ViewInject(R.id.rb_monday)
    RadioButton rb_monday;
    @ViewInject(R.id.rb_tuesday)
    RadioButton rb_tuesday;
    @ViewInject(R.id.rb_wednesday)
    RadioButton rb_wednesday;
    @ViewInject(R.id.rb_thursday)
    RadioButton rb_thursday;
    @ViewInject(R.id.rb_friday)
    RadioButton rb_friday;
    @ViewInject(R.id.rb_saturday)
    RadioButton rb_saturday;
    @ViewInject(R.id.rb_sunday)
    RadioButton rb_sunday;

    private List<HomeSettingBean.WorkingTimeBasesBean> mDataList;
    private List<HomeSettingBean.WorkingTimeBean> mBeans;
    private List<HomeSettingBean.WorkingTimeBean> mMondayList = new ArrayList<>();
    private List<HomeSettingBean.WorkingTimeBean> mTuesdayList = new ArrayList<>();
    private List<HomeSettingBean.WorkingTimeBean> mWednesdayList = new ArrayList<>();
    private List<HomeSettingBean.WorkingTimeBean> mThursdayList = new ArrayList<>();
    private List<HomeSettingBean.WorkingTimeBean> mFridayList = new ArrayList<>();
    private List<HomeSettingBean.WorkingTimeBean> mSturdayList = new ArrayList<>();
    private List<HomeSettingBean.WorkingTimeBean> mSundayList = new ArrayList<>();
    private HomeSettingBean mHomeSettingBean;
    private WorkTimeAdapter mAdapter;
    private String time;
    private String mBeginTime;
    private boolean mBoolean;
    private boolean mBeginChecked;
    private boolean mEndChecked;
    private Drawable mIcon_confirmed;
    private String mEndTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_time_setting);
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);
        getIntentInfo();
        initView();
        initListener();
    }



    private void initView() {
        tv_left.setVisibility(View.GONE);
        iv_left.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);
        tv_center.setText(R.string.home_doctor_setting);
        tv_right.setText(R.string.save);

        mIcon_confirmed = getResources().getDrawable(R.mipmap.icon_confirmed);
        mIcon_confirmed.setBounds(0,0,mIcon_confirmed.getMinimumWidth(),mIcon_confirmed.getMinimumHeight());
//        mAdapter = new WorkTimeAdapter(mDataList);

//      初始化构建list
        initWoringTimeList();
        mAdapter = new WorkTimeAdapter(mMondayList);
        lv_set_work_time.setAdapter(mAdapter);
        lv_set_work_time.setDivider(null);
        rg_week_day.check(R.id.rb_monday);
    }

    private void initWoringTimeList() {
        if(mBeans == null || mBeans.size() == 0){
//            第一次进入设置
            initList(mMondayList,MONDAY);
            initList(mTuesdayList,TUESDAY);
            initList(mWednesdayList,WEDNESDAY);
            initList(mThursdayList,THURSDAY);
            initList(mFridayList,FRIDAY);
            initList(mSturdayList,SATURDAY);
            initList(mSundayList,SUNDAY);

        }else{
            for (HomeSettingBean.WorkingTimeBean bean :mBeans) {
                int dayOfWeek = bean.getDayOfWeek();
                switch (dayOfWeek){
                    case MONDAY:
                        mMondayList.add(bean);
                        break;
                    case TUESDAY:
                        mTuesdayList.add(bean);
                        break;
                    case WEDNESDAY:
                        mWednesdayList.add(bean);
                        break;
                    case THURSDAY:
                        mThursdayList.add(bean);
                        break;
                    case FRIDAY:
                        mFridayList.add(bean);
                        break;
                    case SATURDAY:
                        mSturdayList.add(bean);
                        break;
                    case SUNDAY:
                        mSundayList.add(bean);
                        break;
                }
            }


            /*标记*/
            mark();

        }
        //           排序
        sortList(mMondayList);
        sortList(mTuesdayList);
        sortList(mWednesdayList);
        sortList(mThursdayList);
        sortList(mFridayList);
        sortList(mSturdayList);
        sortList(mSundayList);


    }

    private void mark() {
        confirmed(mMondayList,rb_monday);
        confirmed(mTuesdayList,rb_tuesday);
        confirmed(mWednesdayList,rb_wednesday);
        confirmed(mThursdayList,rb_thursday);
        confirmed(mFridayList,rb_friday);
        confirmed(mSturdayList,rb_saturday);
        confirmed(mSundayList,rb_sunday);
    }

    private void sortList(List<HomeSettingBean.WorkingTimeBean> list){
        for (HomeSettingBean.WorkingTimeBean bean:list) {
            if(bean.getWorkingTimeName().equals(MORNING)){
                bean.setID(0);
            }else if(bean.getWorkingTimeName().equals(AFTERNOON)){
                bean.setID(1);
            }else if(bean.getWorkingTimeName().equals(NINGHT)){
                bean.setID(2);
            }else{
                ToastUtils.showShort(mContext,"数据有错误");
            }

        }
        Collections.sort(list);

    }

    private void initList(List<HomeSettingBean.WorkingTimeBean> list,int dayOfWeek) {

        for(int i = 0; i<mDataList.size();i++){
            HomeSettingBean.WorkingTimeBean bean = new HomeSettingBean.WorkingTimeBean();
            bean.setWorkingTimeBaseID(mDataList.get(i).getWorkingTimeBaseID());
            bean.setWorkingTimeName(mDataList.get(i).getWorkingTimeName());
            bean.setDayOfWeek(dayOfWeek);
            bean.setBeginTime("");
            bean.setEndTime("");
            list.add(bean);
        }
    }

    private void initListener() {
        iv_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);

        rg_week_day.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    case R.id.rb_monday:
                        mark();
                        mAdapter.relaceData(mMondayList);
                        break;
                    case R.id.rb_tuesday:
                        mark();
                        mAdapter.relaceData(mTuesdayList);
                        break;
                    case R.id.rb_wednesday:
                        mark();
                        mAdapter.relaceData(mWednesdayList);
                        break;
                    case R.id.rb_thursday:
                        mark();
                        mAdapter.relaceData(mThursdayList);
                        break;
                    case R.id.rb_friday:
                        mark();
                        mAdapter.relaceData(mFridayList);
                        break;
                    case R.id.rb_saturday:
                        mark();
                        mAdapter.relaceData(mSturdayList);
                        break;
                    case R.id.rb_sunday:
                        mark();
                        mAdapter.relaceData(mSundayList);
                        break;
                }
            }
        });
    }
    private void showTimePick(final TextView textView, final HomeSettingBean.WorkingTimeBean workTimeBean, final boolean start,int id) {
        String[] options = null;
        switch (id){
            case 0:
                options = new String[]{"未设置", "06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00",
                        "10:30", "11:00", "11:30", "12:00"};
                break;
            case 1:
                options = new String[]{"未设置", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00",
                        "15:30", "16:00", "16:30", "17:00", "17:30", "18:00"};
                break;
            case 2:
                options = new String[]{"未设置","18:00","18:30","19:00","19:30","20:00","20:30","21:00","21:30","22:00","22:30","23:00",
                        "23:30","24:00"};
                break;
        }


        OptionPicker picker = new OptionPicker(this,options);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                if(start){
                    if(option.equals("未设置")){
                        workTimeBean.setBeginTime("");
                    }else {
                        workTimeBean.setBeginTime(option);
                    }
                    textView.setText(option+"");
                    if(!option.equals("未设置")){
                        textView.setBackground(getResources().getDrawable(R.drawable.selected_border));
                        workTimeBean.setBeginChecked(true);
                        ToastUtils.showShort(mContext,"请选择结束时间");
                    }else {
                        workTimeBean.setBeginChecked(false);
                    }

                }else {

                    mBeginTime = workTimeBean.getBeginTime();
                    if(mBeginTime.equals("") && option.equals("未设置")){
                        workTimeBean.setEndChecked(false);
                        workTimeBean.setEndTime("");
                        textView.setText(option);
                    }else if(mBeginTime.equals("") && !option.equals("未设置")){
                        ToastUtils.showShort(mContext,"起始时间为空，不能设置结束时间");
                        workTimeBean.setEndChecked(false);
                        return;
                    }else if(!mBeginTime.equals("") && option.equals("未设置")){
                        ToastUtils.showShort(mContext,"起始时间不为空，结束时间不能为空");
                        workTimeBean.setEndChecked(false);
                        return;
                    }else{
                        mBoolean = MyUtils.beginTOend(mContext, mBeginTime, option);
                        if(!mBoolean){
                            ToastUtils.showShort(mContext,"起始时间不能大于或者等于结束时间");
                            workTimeBean.setEndChecked(false);
                            return;
                        }else{
                            if(option.equals("未设置")){
                                workTimeBean.setEndTime("");
                                workTimeBean.setEndChecked(false);
                            }else{
                                workTimeBean.setEndTime(option);
                                textView.setBackground(getResources().getDrawable(R.drawable.selected_border));
                                workTimeBean.setEndChecked(true);
                            }
                            textView.setText(option+"");

                        }

                    }

                }
            }
        });
        picker.show();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getOption(String option){
            time = option;
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()){
            case R.id.iv_left:
                back();
                break;
            case R.id.tv_right:
                saveWoringTime();
                break;
        }
    }

    private void back() {
      /*  AppEventApi.Success success = new AppEventApi.Success();
       *//* success.setSuccess(false);*//*
        EventBus.getDefault().post(success);*/
        finish();
    }

    private void saveWoringTime() {
        mBeans.clear();
        mBeans.addAll(mMondayList);
        mBeans.addAll(mTuesdayList);
        mBeans.addAll(mWednesdayList);
        mBeans.addAll(mThursdayList);
        mBeans.addAll(mFridayList);
        mBeans.addAll(mSturdayList);
        mBeans.addAll(mSundayList);

        boolean checkFlag = false;
        for (HomeSettingBean.WorkingTimeBean bean : mBeans) {

            int dayofweek = bean.getDayOfWeek();
            String weekday = "";
            String worktimename = bean.getWorkingTimeName();


            switch (dayofweek){
                case 0:
                    weekday = "星期日";
                    break;
                case 1:
                    weekday = "星期一";
                    break;
                case 2:
                    weekday = "星期二";
                    break;
                case 3:
                    weekday = "星期三";
                    break;
                case 4:
                    weekday = "星期四";
                    break;
                case 5:
                    weekday = "星期五";
                    break;
                case 6:
                    weekday = "星期六";
                    break;
            }

            if(bean.getBeginTime().equals("") && !bean.getEndTime().equals("")){
                ToastUtils.showShort(mContext,"请设置完整"+weekday+worktimename+"工作时间");
                return;
            }
            if(!bean.getBeginTime().equals("") && bean.getEndTime().equals("")){
                ToastUtils.showShort(mContext,"请设置完整"+weekday+worktimename+"工作时间");
                return;
            }
            if(!bean.getBeginTime().equals("") && !bean.getEndTime().equals("")){
                checkFlag = true;
            }

        }
        if(checkFlag){
            if(mBeans.size() == 21){
                showLoadDialog(R.string.string_save);
                Http_saveWorkingTime_Event event  = new Http_saveWorkingTime_Event(mBeans, new HttpListener() {
                    @Override
                    public void onError(int code, String msg) {
                        dismissLoadDialog();
                        ToastUtils.showShort(mContext,msg);
                    }
                    @Override
                    public void onSuccess(Object o) {
                        dismissLoadDialog();
                        ToastUtils.showShort(mContext,"保存成功");
                        EventBus.getDefault().post(mBeans);
                        finish();

                    }
                });
                new HttpClient(mContext,event).startNewApi();
            }else{
                ToastUtils.showShort(mContext,"数据缺失");
                return;
            }
        }else {
            ToastUtils.showShort(mContext,"请设置至少一段工作时间");
            return;
        }







    }

    public void getIntentInfo() {
        mHomeSettingBean = (HomeSettingBean) getIntent().getSerializableExtra("WorkingTime");
        mDataList = mHomeSettingBean.getWorkingTimeBases();
        mBeans = mHomeSettingBean.getDoctorWorkingTimes();

    }

    private class WorkTimeAdapter extends BaseAdapter{


        private List<HomeSettingBean.WorkingTimeBean> mDataList;
        
        private HomeSettingBean.WorkingTimeBean mWorkingTimeBean;

        public WorkTimeAdapter(List<HomeSettingBean.WorkingTimeBean> baseDataList){
            mDataList = baseDataList;
        }
        

        @Override
        public int getCount() {
            if(mDataList == null || mDataList.size() == 0){
                return mDataList.size();
            }
            return mDataList.size();
        }

        @Override
        public HomeSettingBean.WorkingTimeBean getItem(int i) {
            return mDataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int postion, View convertView, ViewGroup viewGroup) {
            convertView = View.inflate(mContext,R.layout.item_workging_time,null);
            final TextView tv_begin_time = (TextView) convertView.findViewById(R.id.tv_begin_time);
            final TextView tv_end_time = (TextView) convertView.findViewById(R.id.tv_end_time);
            TextView tv_working_time_title=(TextView)convertView.findViewById(R.id.tv_working_time_title);
            mWorkingTimeBean = mDataList.get(postion);
            mBeginChecked = mWorkingTimeBean.isBeginChecked();
            mEndChecked = mWorkingTimeBean.isEndChecked();

            tv_working_time_title.setText(mWorkingTimeBean.getWorkingTimeName());
            TextPaint paint = tv_working_time_title.getPaint();
            paint.setFakeBoldText(true);

            if(mWorkingTimeBean.getBeginTime().equals("")) {
                tv_begin_time.setText("未设置");
            }else{
                tv_begin_time.setText(mWorkingTimeBean.getBeginTime());
            }
            if(mBeginChecked || (!mWorkingTimeBean.getBeginTime().equals(""))){
                tv_begin_time.setBackground(getResources().getDrawable(R.drawable.selected_border));
            }

            if(mWorkingTimeBean.getEndTime().equals("")){
                tv_end_time.setText("未设置");
            }else{
                tv_end_time.setText(mWorkingTimeBean.getEndTime());
            }
            if(mEndChecked || (!mWorkingTimeBean.getEndTime().equals(""))){
                tv_end_time.setBackground(getResources().getDrawable(R.drawable.selected_border));
            }


            tv_begin_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showTimePick(tv_begin_time,mDataList.get(postion),true,mDataList.get(postion).getID());

                }
            });
            tv_end_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTimePick(tv_end_time,mDataList.get(postion),false,mDataList.get(postion).getID());

                }
            });


            return convertView;
        }



        public List<HomeSettingBean.WorkingTimeBean> getDataList(){
            return mDataList;
        }


        class ViewHolder{
            @ViewInject(R.id.tv_working_time_title)
            TextView tv_working_time_title;
            @ViewInject(R.id.tv_begin_time)
            TextView tv_begin_time;
            @ViewInject(R.id.tv_end_time)
            TextView tv_end_time;
            public ViewHolder(View view){
                ViewUtils.inject(this,view);
            }
        }

        public void relaceData(List<HomeSettingBean.WorkingTimeBean> list){
            mDataList = new ArrayList<>();
            mDataList = list;
            notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void confirmed(List<HomeSettingBean.WorkingTimeBean> list,RadioButton radioButton){
        boolean confirm = false;
        for (HomeSettingBean.WorkingTimeBean bean:list) {
            String beginTime = bean.getBeginTime();
            String endTime = bean.getEndTime();
            if((!beginTime.equals("")) || (!endTime.equals(""))){
                confirm = true;
                break;
            }

        }
        if(confirm){
            radioButton.setCompoundDrawables(null,null,mIcon_confirmed,null);
        }else {
            radioButton.setCompoundDrawables(null,null,null,null);
        }
    }

    @Override
    public void onBackPressed() {

        back();
    }
}
