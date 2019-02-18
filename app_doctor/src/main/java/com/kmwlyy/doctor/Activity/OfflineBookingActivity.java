package com.kmwlyy.doctor.Activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.adapter.OfflineBookingAdapter;
import com.kmwlyy.doctor.model.OfflineDayTimeBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getOfflineBookingList_Event;
import com.kmwlyy.doctor.model.httpEvent.OfflineBookingBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.winson.ui.widget.EmptyViewUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TFeng on 2017/7/25.
 */

public class OfflineBookingActivity extends BaseActivity {

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
    @ViewInject(R.id.iv_left)
    ImageView iv_left;
    @ViewInject(R.id.tv_center)
    TextView tv_center;
    @ViewInject(R.id.ll_offline_booking)
    ListView ll_offline_booking;
    @ViewInject(R.id.rg_offline_week_day)
    RadioGroup rg_offline_week_day;
    @ViewInject(R.id.rb_offline_monday)
    RadioButton rb_offline_monday;
    @ViewInject(R.id.rb_offline_tuesday)
    RadioButton rb_offline_tuesday;
    @ViewInject(R.id.rb_offline_wednesday)
    RadioButton rb_offline_wednesday;
    @ViewInject(R.id.rb_offline_thursday)
    RadioButton rb_offline_thursday;
    @ViewInject(R.id.rb_offline_friday)
    RadioButton rb_offline_friday;
    @ViewInject(R.id.rb_offline_saturday)
    RadioButton rb_offline_saturday;
    @ViewInject(R.id.rb_offline_sunday)
    RadioButton rb_offline_sunday;
    @ViewInject(R.id.lv_offline_name)
    ListView lv_offline_name;
    @ViewInject(R.id.ll_offline_cotent)
    LinearLayout ll_offline_cotent;
    private List<OfflineBookingBean> mOfflineBookingBeanList = new ArrayList<>();
    private Map<Integer, Map<String, List<OfflineBookingBean>>> mData;
    private OfflineBookingAdapter mOfflineBookingAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_booking);
        ViewUtils.inject(this);
        getOffLineBooking();
        initView();
        initListener();

    }

    private void getOffLineBooking() {
        showLoadDialog(R.string.string_wait);
        Http_getOfflineBookingList_Event event = new Http_getOfflineBookingList_Event("1", "20", new HttpListener<List<OfflineBookingBean>>() {
            @Override
            public void onError(int code, String msg) {
                dismissLoadDialog();
                ToastUtils.showShort(mContext,msg);
            }

            @Override
            public void onSuccess(List<OfflineBookingBean> offlineBookingBeen) {
                dismissLoadDialog();
                if (offlineBookingBeen == null || offlineBookingBeen.size() == 0) {
                    emptyView();
                    ToastUtils.showShort(mContext, "暂无数据");
                } else {
                    mOfflineBookingBeanList = offlineBookingBeen;
                    initList();
                    checkWeekDay(MONDAY);

                }
            }
        });
        new HttpClient(mContext, event).startNewApi();
    }


    private void initView() {
        tv_left.setVisibility(View.GONE);
        iv_left.setVisibility(View.VISIBLE);
        tv_center.setText("线下预约");
        mOfflineBookingAdapter = new OfflineBookingAdapter(mContext, null);

        lv_offline_name.setAdapter(mOfflineBookingAdapter);
        lv_offline_name.setDivider(null);
        rg_offline_week_day.check(R.id.rb_offline_monday);
    }

    private void initList() {
        mData = new HashMap<>();
        for (OfflineBookingBean bean : mOfflineBookingBeanList) {
            int dayOfweek = bean.getWorkingTime().getDayOfWeek();
            String dayTime = bean.getWorkingTime().getWorkingTimeName();
            switch (dayOfweek) {
                case MONDAY:
                    initData(MONDAY, mData, dayTime, bean);
                    break;
                case TUESDAY:
                    initData(TUESDAY, mData, dayTime, bean);
                    break;
                case WEDNESDAY:
                    initData(WEDNESDAY, mData, dayTime, bean);
                    break;
                case THURSDAY:
                    initData(THURSDAY, mData, dayTime, bean);
                    break;
                case FRIDAY:
                    initData(FRIDAY, mData, dayTime, bean);
                    break;
                case SATURDAY:
                    initData(SATURDAY, mData, dayTime, bean);
                    break;
                case SUNDAY:
                    initData(SUNDAY, mData, dayTime, bean);
                    break;
            }

        }


    }


    private void initListener() {
        iv_left.setOnClickListener(this);
        rg_offline_week_day.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rb_offline_monday:
                        checkWeekDay(MONDAY);
                        break;
                    case R.id.rb_offline_tuesday:
                        checkWeekDay(TUESDAY);
                        break;
                    case R.id.rb_offline_wednesday:
                        checkWeekDay(WEDNESDAY);
                        break;
                    case R.id.rb_offline_thursday:
                        checkWeekDay(THURSDAY);
                        break;
                    case R.id.rb_offline_friday:
                        checkWeekDay(FRIDAY);
                        break;
                    case R.id.rb_offline_saturday:
                        checkWeekDay(SATURDAY);
                        break;
                    case R.id.rb_offline_sunday:
                        checkWeekDay(SUNDAY);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()) {
            case R.id.iv_left:
                finish();
                break;
        }
    }

    private void checkWeekDay(int dayOfweek) {
        EmptyViewUtils.removeAllView(ll_offline_cotent);

        List<OfflineDayTimeBean> datalist = new ArrayList<OfflineDayTimeBean>();
        if(mData != null && mData.size()>0){
            if (mData.containsKey(dayOfweek)) {
                Map<String, List<OfflineBookingBean>> stringListMap = mData.get(dayOfweek);
                if (null != stringListMap) {
                    for (Map.Entry<String, List<OfflineBookingBean>> entry : stringListMap.entrySet()) {
                        OfflineDayTimeBean bean = new OfflineDayTimeBean();
                        bean.setDayTime(entry.getKey());

                        if (entry.getKey().equals("上午")) {
                            bean.setID(0);
                        } else if (entry.getKey().equals("下午")) {
                            bean.setID(1);
                        } else if (entry.getKey().equals("晚上")) {
                            bean.setID(2);
                        }
                        bean.setList(entry.getValue());
                        datalist.add(bean);
                    }
//                                排序
                    Collections.sort(datalist);

                } else {
                    emptyView();
//                    ToastUtils.showShort(mContext, "暂无数据");
                }

            } else {
                // TODO: 2017/7/28 空处理
                emptyView();
//                ToastUtils.showShort(mContext, "暂无数据");
            }
            mOfflineBookingAdapter.replaceData(datalist);

        }else{
           emptyView();
//            ToastUtils.showShort(mContext,"暂无数据");

        }


    }

    private void initDayTime(String dayTime, Map<String, List<OfflineBookingBean>> map, OfflineBookingBean bean) {
        if (map.containsKey(dayTime)) {
            List<OfflineBookingBean> dataList = map.get(dayTime);
            if (null == dataList) {
                dataList = new ArrayList<>();
            }
            dataList.add(bean);
        } else {
            List<OfflineBookingBean> newDataList = new ArrayList<>();
            newDataList.add(bean);
            map.put(MORNING, newDataList);
        }
    }

    private void initData(int dayOfWeek, Map<Integer, Map<String, List<OfflineBookingBean>>> data, String dayTime, OfflineBookingBean bean) {
        if (data.containsKey(dayOfWeek)) {
            Map<String, List<OfflineBookingBean>> map = data.get(dayOfWeek);
            if (map == null) {
                map = new HashMap<>();
            }
            initMap(map, dayTime, bean);

            data.put(dayOfWeek, map);
        } else {
            Map<String, List<OfflineBookingBean>> newMap = new HashMap<>();
            initMap(newMap, dayTime, bean);
            data.put(dayOfWeek, newMap);
        }

    }

    private void initMap(Map<String, List<OfflineBookingBean>> map, String dayTime, OfflineBookingBean bean) {
        if (dayTime.equals(MORNING)) {
            initDayTime(MORNING, map, bean);
        } else if (dayTime.equals(AFTERNOON)) {
            initDayTime(AFTERNOON, map, bean);
        } else if (dayTime.equals(NINGHT)) {
            initDayTime(NINGHT, map, bean);
        }
    }
    private void emptyView(){
        EmptyViewUtils.removeAllView(ll_offline_cotent);
        EmptyViewUtils.showEmptyView(ll_offline_cotent,R.mipmap.no_order,"暂无预约记录",null);
    }
}
