package com.kmwlyy.patient.onlinediagnose;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.UserMember;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.Doctor;
import com.kmwlyy.patient.helper.net.bean.DoctorSchedule;
import com.kmwlyy.patient.helper.net.bean.DoctorScheduleItem;
import com.kmwlyy.patient.helper.net.bean.UserOPDRegisters;
import com.kmwlyy.patient.helper.net.event.HttpDoctor;
import com.kmwlyy.patient.helper.net.event.HttpDoctorSchedule;
import com.kmwlyy.patient.helper.net.event.HttpUserConsults;
import com.kmwlyy.patient.helper.net.event.HttpUserOPDRegisters;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.pay.PayActivity;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.NoScrollGridView;
import com.winson.ui.widget.RateLayout;
import com.winson.ui.widget.ToastMananger;
import com.winson.ui.widget.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xcj on 2016/12/16.
 */

public class BuyVideoConsultActivity extends BaseActivity implements View.OnClickListener{
    public static final String TAG = BuyVideoConsultActivity.class.getSimpleName();
    public static final String DOCTOR_INFO = "DOCTOR_INFO";
    public static final String BUY_TYPE = "BUY_TYPE";
    public static final String ENTRY = "ENTRY";
    public static final String MONEY = "MONEY";
    @BindView(R.id.previous_date)
    ImageView mPreviousDate;
    @BindView(R.id.start_date)
    TextView mStartDate;
    @BindView(R.id.end_date)
    TextView mEndDate;
    @BindView(R.id.next_date)
    ImageView mNextDate;
    @BindView(R.id.grid_day)
    NoScrollGridView mGridDay;
    @BindView(R.id.rate_layout)
    RateLayout mRateLayout;
    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.tv_left)
    TextView tv_left;

    public static void startBuyVideoConsultActivity(Context context, String doctorId,int type, int entry,String money) {
        Intent intent = new Intent(context, BuyVideoConsultActivity.class);
        intent.putExtra(DOCTOR_INFO, doctorId);
        intent.putExtra(BUY_TYPE, type);
        intent.putExtra(ENTRY,entry);
        intent.putExtra(MONEY,money);
        context.startActivity(intent);
    }

//    public static void startBuyVideoConsultActivity(Context context, String doctorId){
//        Intent intent = new Intent(context, BuyVideoConsultActivity.class);
//        intent.putExtra(DOCTOR_INFO, doctorId);
//        context.startActivity(intent);
//    }

    private DoctorScheduleGridAdapter mDoctorScheduleGridAdapter;
    private ProgressDialog mLoadScheduleDialog;
    private Doctor.Detail mDoctorDetail;
    private HttpClient mGetDoctorScheduleClient;
    public String mCurrentDate, mOldDate;
    private ArrayList<DoctorScheduleItem> mDoctorScheduleItems;
    private int mOPDType = HttpUserOPDRegisters.OPDTYPE_VIDEO;
    private boolean mFamilyDoctorServiceOn;
    private boolean mVideoServiceOn;
    private boolean mIMServiceOn;
    private boolean mImageServiceOn;
    private String mDoctorID;
    private int mEntry;
    private int mBuyType = 0;
    private String mMoney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_video_consult);
        butterknife.ButterKnife.bind(this);
        mDoctorScheduleItems = new ArrayList<>();
        mDoctorID = getIntent().getStringExtra(DOCTOR_INFO);
        mEntry = getIntent().getIntExtra(ENTRY,0);
        mBuyType = getIntent().getIntExtra(BUY_TYPE, 0);
        mMoney = getIntent().getStringExtra(MONEY);

        mNextDate.setOnClickListener(this);
        mPreviousDate.setOnClickListener(this);
        tv_center.setText(getString(R.string.appointment_times_title));
        tv_left.setOnClickListener(this);
        mGridDay.setAdapter(mDoctorScheduleGridAdapter);
        mCurrentDate = CommonUtils.getCurrentDate("yyyy-MM-dd");
        mDoctorScheduleGridAdapter = new DoctorScheduleGridAdapter(this, mDoctorScheduleItems, mCurrentDate, mEntry);
        mGridDay.setAdapter(mDoctorScheduleGridAdapter);
        refreshDoctorSchedules(null);
        setDate();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_left:
                onBackPressed();
                break;
            case R.id.previous_date:
                mOldDate = mCurrentDate;
                mCurrentDate = CommonUtils.getFutureDate(mCurrentDate, "yyyy-MM-dd", -7);
                setDate();
                break;
            case R.id.next_date:
                mOldDate = mCurrentDate;
                mCurrentDate = CommonUtils.getFutureDate(mCurrentDate, "yyyy-MM-dd", 7);
                setDate();
                break;

        }
    }

    static class DoctorScheduleGridAdapter extends CommonAdapter<DoctorScheduleItem> {

        private DoctorScheduleItem mSelectItem;
        private String mDate = null;
        private int mEntrys;

        public DoctorScheduleGridAdapter(Context context, List<DoctorScheduleItem> datas, String date,int entry) {
            super(context, R.layout.doctor_schedule_item, datas);
            mDate = date;
            this.mEntrys = entry;
        }

        public DoctorScheduleItem getSelectItem() {
            return mSelectItem;
        }

        @Override
        public void convert(ViewHolder viewHolder, final DoctorScheduleItem data, int position) {
            View weekLayout = viewHolder.findViewById(R.id.week_layout);
            View timeLayout = viewHolder.findViewById(R.id.time_layout);
            View actionLayout = viewHolder.findViewById(R.id.action_layout);

            weekLayout.setVisibility(View.GONE);
            timeLayout.setVisibility(View.GONE);
            actionLayout.setVisibility(View.GONE);

            switch (data.mType) {
                case DoctorScheduleItem.TYPE_WEEK:
                    weekLayout.setVisibility(View.VISIBLE);
                    TextView week = (TextView) viewHolder.findViewById(R.id.week);
                    TextView date = (TextView) viewHolder.findViewById(R.id.date);
                    if (mDate.equals(data.mDateStr)) {
                        week.setText(R.string.today);
                    } else {
                        week.setText(data.mWeekStr);
                    }
                    date.setText(data.mDateStr == null ? "--" : data.mDateStr.substring(5));
                    break;
                case DoctorScheduleItem.TYPE_DURATION:
                    timeLayout.setVisibility(View.VISIBLE);
                    TextView timeStart = (TextView) viewHolder.findViewById(R.id.time_start);
                    TextView timeEnd = (TextView) viewHolder.findViewById(R.id.time_end);
                    timeStart.setText(data.mStartTime);
                    timeEnd.setText(data.mEndTime);
                    break;
                case DoctorScheduleItem.TYPE_ACTION:
                    if (data.mDoctorScheduleID != null
                            && !data.mDoctorScheduleID.equals("0")
                            && data.mRegNum != -1
                            && data.mRegNum < data.mRegSum) {
                        // can not use
                        actionLayout.setVisibility(View.VISIBLE);

                        if (mSelectItem != null
                                && mSelectItem.mDoctorScheduleID != null
                                && mSelectItem.mDoctorScheduleID.equals(data.mDoctorScheduleID)) {

//                            actionLayout.setEnabled(false);
                            actionLayout.setSelected(true);

                            actionLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mSelectItem = null;
                                    notifyDataSetChanged();
                                }
                            });

                        } else {
//                            actionLayout.setEnabled(true);
                            actionLayout.setSelected(false);
                            actionLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mSelectItem = data;
                                    Log.d(TAG, "date str : " + data.mDateStr);
                                    notifyDataSetChanged();
                                    if (mEntrys == 0){
                                        openBuy(mSelectItem);
                                    }else {
                                        sendData(mSelectItem);
                                    }
                                }
                            });
                        }

                    }
                    break;
            }
        }
    }




    private void updateDoctorDetailInfo() {
        if (mDoctorDetail == null) {
            return;
        }
        if (mDoctorDetail.mDoctorServices != null) {
            for (Doctor.DoctorService service : mDoctorDetail.mDoctorServices) {
                switch (service.mServiceType) {
                    case Doctor.DoctorService.IMAGE_CONSULT:
                        mImageServiceOn = service.mServiceSwitch == Doctor.DoctorService.SERVICE_ON;
                        break;
                    case Doctor.DoctorService.IM_CONSULT:
                        mIMServiceOn = service.mServiceSwitch == Doctor.DoctorService.SERVICE_ON;
                        break;
                    case Doctor.DoctorService.VIDEO_CONSULT:
                        mVideoServiceOn = service.mServiceSwitch == Doctor.DoctorService.SERVICE_ON;
                        break;
                    case Doctor.DoctorService.FAMILY_DOCTOR_CONSULT:
                        mFamilyDoctorServiceOn = service.mServiceSwitch == Doctor.DoctorService.SERVICE_ON;
                        break;
                }
            }
        }
    }

    private void setDate() {
        mStartDate.setText(mCurrentDate);
        mEndDate.setText(CommonUtils.getFutureDate(mCurrentDate, "yyyy-MM-dd", 6));
        getDoctorSchedule();

    }

    private void refreshDoctorSchedules(DoctorSchedule schedule) {
        mDoctorScheduleItems.clear();

        DoctorScheduleItem item = new DoctorScheduleItem(DoctorScheduleItem.TYPE_NULL, null, null, null, null, null, 0, 0);
        mDoctorScheduleItems.add(item);

        if (schedule != null) {

            String[] dates = null;
            int length = 0;

            if (schedule.mDateWeekList != null) {
                length = schedule.mDateWeekList.size();
                dates = new String[schedule.mDateWeekList.size()];
                int index = 0;
                for (DoctorSchedule.DateWeek dateWeek : schedule.mDateWeekList) {
                    dates[index] = dateWeek.mDateStr;
                    index++;
                    DoctorScheduleItem data = new DoctorScheduleItem(
                           DoctorScheduleItem.TYPE_WEEK, dateWeek.mDateStr, dateWeek.mWeekStr, null, null, null, 0, 0);
                    mDoctorScheduleItems.add(data);
                }
            }

            if (schedule.mScheduleList != null) {

                int index = 0;

                for (DoctorSchedule.Schedule scheduleItem : schedule.mScheduleList) {
                    DoctorScheduleItem data = new DoctorScheduleItem(
                           DoctorScheduleItem.TYPE_DURATION, null, null, scheduleItem.mStartTime, scheduleItem.mEndTime, null, 0, 0);
                    mDoctorScheduleItems.add(data);

                    if (scheduleItem.mRegNumList != null) {
                        for (DoctorSchedule.RegNum reg : scheduleItem.mRegNumList) {

                            String dateStr = null;
                            if (dates != null) {
                                dateStr = dates[index % length];
                            }
                            index++;
                            DoctorScheduleItem data2 = new DoctorScheduleItem(
                                    DoctorScheduleItem.TYPE_ACTION, dateStr, null, scheduleItem.mStartTime, scheduleItem.mEndTime, reg.mDoctorScheduleID, reg.mRegSum, reg.mRegNum);
                            mDoctorScheduleItems.add(data2);
                        }
                    }
                }
            }

        } else {

            for (int i = 0; i < 55; i++) {
                DoctorScheduleItem data = new DoctorScheduleItem(0, null, null, null, null, null, -1, -1);
                mDoctorScheduleItems.add(data);
            }
        }

        mDoctorScheduleGridAdapter.notifyDataSetChanged();
    }

    private void getDoctorSchedule() {
        showLoadScheduleDialog();
        HttpDoctorSchedule.GetAvailableTimes availableTimes = new HttpDoctorSchedule.GetAvailableTimes(
                mDoctorID,
                mCurrentDate,
                7,
                new HttpListener<DoctorSchedule>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("getDoctorSchedule", code, msg));
                        }
                        dismissLoadScheduleDialog();
                        ToastUtils.showShort(BuyVideoConsultActivity.this, getString(R.string.get_doctor_schedule_fail));
                    }

                    @Override
                    public void onSuccess(DoctorSchedule doctorSchedule) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("getDoctorSchedule", PUtils.toJson(doctorSchedule)));
                        }
                        dismissLoadScheduleDialog();
                        refreshDoctorSchedules(doctorSchedule);
                    }
                }
        );

        mGetDoctorScheduleClient = NetService.createClient(this, availableTimes);
        mGetDoctorScheduleClient.start();
    }



    private static void sendData(DoctorScheduleItem item){
        EventApi.SelectTime time = new EventApi.SelectTime();
        time.doctorScheduleItem = item;
        EventBus.getDefault().post(time);
    }

    private static void openBuy(DoctorScheduleItem item){
        EventApi.OpenBuyVV time = new EventApi.OpenBuyVV();
        time.doctorScheduleItem = item;
        EventBus.getDefault().post(time);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setMainPayPosition(EventApi.SelectTime time) {
       finish();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void openBuyVV(EventApi.OpenBuyVV time) {
        BuyVVConsultActivity.startBuyVVConsultActivity(BuyVideoConsultActivity.this,mDoctorID, mBuyType,mMoney,time.doctorScheduleItem);
        finish();
    }

    private void showLoadScheduleDialog() {
        mLoadScheduleDialog = new ProgressDialog(this);
        mLoadScheduleDialog.setMessage(getResources().getString(R.string.on_load_doctor_schedule));
        mLoadScheduleDialog.setCancelable(false);
        mLoadScheduleDialog.show();
    }

    private void dismissLoadScheduleDialog() {
        if (mLoadScheduleDialog != null) {
            mLoadScheduleDialog.dismiss();
        }
    }

}
