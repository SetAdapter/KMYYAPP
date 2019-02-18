package com.kmwlyy.doctor.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.AppEventApi;
import com.kmwlyy.doctor.model.HomeSettingBean;
import com.kmwlyy.doctor.model.SaveServiceSetBean;
import com.kmwlyy.doctor.model.ServiceSetBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getHomeDoctorConfig_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_getServiceSet_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_saveFamilyDoctorSetting_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_saveServiceSet_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.networkbench.agent.impl.NBSAppAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by KM on 2017/6/20.
 */

public class HomeDoctorSettingActivity extends BaseActivity {

    private static final int RESULT_SET_WORKTIEM = 0;
    private static final int RESULT_ADDRESS_SETTING = 1;
    private static final int RESULT_PRICE_PEOPLE = 2;
    private static final int RESULT_VIDEO_PHONE_COUNT = 3;
    private static final int RESULT_AUTO_RELAY = 4;
    private static final int RESULT_ADD_OTHER_SERVICE = 5;
    @ViewInject(R.id.tv_left)
    TextView tv_left;
    @ViewInject(R.id.tv_center)
    TextView tv_center;
    @ViewInject(R.id.iv_left)
    ImageView iv_left;
    @ViewInject(R.id.tv_right)
    TextView tv_right;
    @ViewInject(R.id.set_worktime)
    LinearLayout set_worktime;
    @ViewInject(R.id.free_ticket)
    LinearLayout free_ticket;
    @ViewInject(R.id.address_setting)
    LinearLayout address_setting;
    @ViewInject(R.id.price_people)
    LinearLayout price_people;
    @ViewInject(R.id.video_phone_count)
    LinearLayout video_phone_count;
    @ViewInject(R.id.atuo_replay)
    LinearLayout atuo_replay;
    @ViewInject(R.id.orther_service)
    LinearLayout orther_service;
    @ViewInject(R.id.switch_free_ticket)
    CheckBox switch_free_ticket;
    @ViewInject(R.id.cb_select_protocol)
    CheckBox cb_select_protocol;
    @ViewInject(R.id.btn_open_service)
    Button btn_open_service;
    @ViewInject(R.id.tv_order_service_setting)
    TextView tv_oreder_service_setting;
    @ViewInject(R.id.tv_auto_relay_setting)
    TextView tv_auto_replay_setting;
    @ViewInject(R.id.tv_price_people_setting)
    TextView tv_price_people_setting;
    @ViewInject(R.id.tv_video_phone_setting)
    TextView tv_video_phone_setting;
    @ViewInject(R.id.tv_work_time_setting)
    TextView tv_work_time_setting;
    @ViewInject(R.id.tv_address_setting)
    TextView tv_address_setting;

    @ViewInject(R.id.ll_family_doctor_open)
    LinearLayout ll_family_doctor_open;
    @ViewInject(R.id.switch_open)
    CheckBox switch_open;
    @ViewInject(R.id.ll_bottom)
    LinearLayout ll_bottom;
    private boolean isChecked;
    private boolean isFreeTicketChecked;
    private boolean isFamilyDoctorChecked;
    private HomeSettingBean mHomeSettingBean;
    private List<HomeSettingBean.DoctorPackageBean> settingList = new ArrayList<>();
    private List<HomeSettingBean.DoctorPackageBasesBean> baseList = new ArrayList<>();
    private String mMapAddress;
    private String mClicnicAddress;
    private int isFreeCoupon;
    private int mPersonCount;
    private String mAutoReplay = "";
    private boolean mIsSetting_address;
    private boolean mIsSetting_video;
    private boolean mIsSetting_price_people;
    private boolean mIsSetting_auto_replay;
    private boolean mIsSetting_order_service;
    private boolean mIsSettingWorkTime;
    private boolean mIsOpened;
    private boolean is_start_image_consult;
    private boolean is_start_voice_inquiry;
    private boolean is_start_video_inquiry;
    private boolean is_start_family_doctor;
    private int mBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_doctor_setting);
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);
        getIntentInfo();
        initView();
        initListener();
        getConfig();


    }

    private void getIntentInfo() {
        mIsOpened = getIntent().getBooleanExtra("IsOpened", false);
    }

    private void initSetting() {

        if(mHomeSettingBean.getDoctorWorkingTimes() == null || mHomeSettingBean.getDoctorWorkingTimes().size() == 0){
            mIsSettingWorkTime = false;
        }else{
            mIsSettingWorkTime = true;
        }

//        初始化设置
        if (mHomeSettingBean.getDoctorFamilyPackages() == null || mHomeSettingBean.getDoctorFamilyPackages().size() == 0) {
            baseList = mHomeSettingBean.getDoctorPackageBases();
            if(settingList == null || settingList.size()==0) {
                for (int i = 0; i < baseList.size(); i++) {
                    HomeSettingBean.DoctorPackageBean bean = new HomeSettingBean.DoctorPackageBean();
                    bean.setFamilyPackageBaseID(baseList.get(i).getFamilyPackageBaseID());
                    bean.setMonthCount(baseList.get(i).getMonthCount());
                    bean.setMonthCountName(baseList.get(i).getMonthCountName());
                    bean.setDoctorFamilyPackageDetail(baseList.get(i).getDoctorFamilyPackageDetail());
                    settingList.add(bean);
                }
            }

            mIsSetting_price_people = false;
            mIsSetting_video = false;
            mIsSetting_order_service = false;
        } else {

            if(mHomeSettingBean.isIsFreeCoupon()){
                isFreeTicketChecked = true;
                switch_free_ticket.setChecked(true);
            }else{
                isFreeTicketChecked = false;
                switch_free_ticket.setChecked(false);
            }
            if(mHomeSettingBean.getOfflineAddress() == null || mHomeSettingBean.getOfflineAddress().equals("") ){

                mIsSetting_address = false;
            }else {
                mIsSetting_address = true;
                mClicnicAddress = mHomeSettingBean.getOfflineAddress();
                mMapAddress = mHomeSettingBean.getMapAddress();
            }

            if(mHomeSettingBean.getAutoReplyContent() == null || mHomeSettingBean.getAutoReplyContent().equals("")){
                mIsSetting_auto_replay = false;
            }else {
                mIsSetting_auto_replay = true;
                mAutoReplay = mHomeSettingBean.getAutoReplyContent();
            }
            settingList = mHomeSettingBean.getDoctorFamilyPackages();
            mIsSetting_price_people = true;
            mIsSetting_video = true;
            for (HomeSettingBean.DoctorPackageBean bean: settingList) {
                if(bean.getRemark() != null && !bean.getRemark().equals("")) {
                    mIsSetting_order_service = true;
                    break;
                }
            }
            mPersonCount = mHomeSettingBean.getMaxSaleCount();
            Collections.sort(settingList);
        }
    }




    private void initView() {
        tv_left.setVisibility(View.GONE);
        iv_left.setVisibility(View.VISIBLE);
        tv_center.setText(R.string.home_doctor_setting);
        
        if(mIsOpened){
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText(R.string.save);
            ll_family_doctor_open.setVisibility(View.VISIBLE);
            ll_bottom.setVisibility(View.GONE);
            getService();
        }else{

            btn_open_service.setBackgroundColor(getResources().getColor(R.color.color_unclick));
            isChecked = cb_select_protocol.isChecked();
        }
//        isFreeTicketChecked = switch_free_ticket.isChecked();
        
    }

    private void initListener() {
        iv_left.setOnClickListener(this);
        set_worktime.setOnClickListener(this);
        free_ticket.setOnClickListener(this);
        address_setting.setOnClickListener(this);
        price_people.setOnClickListener(this);
        video_phone_count.setOnClickListener(this);
        atuo_replay.setOnClickListener(this);
        orther_service.setOnClickListener(this);
        cb_select_protocol.setOnClickListener(this);
        switch_free_ticket.setOnClickListener(this);
        switch_open.setOnClickListener(this);
        btn_open_service.setOnClickListener(this);
        tv_right.setOnClickListener(this);

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.set_worktime:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("WorkingTime", mHomeSettingBean);
                intent.putExtras(bundle);
                intent.setClass(mContext, SettingWorkTimeActivity.class);
                startActivity(intent);
                break;
            case R.id.switch_free_ticket:
                isFreeTicketChecked = !isFreeTicketChecked;
                if (isFreeTicketChecked) {
                    address_setting.setVisibility(View.VISIBLE);
                } else {
                    address_setting.setVisibility(View.GONE);
                }
                break;
            case R.id.address_setting:
                Intent address = new Intent();
                address.putExtra("MapAddress", mHomeSettingBean.getMapAddress());
                address.putExtra("ClicnicAddress", mHomeSettingBean.getOfflineAddress());
                address.setClass(mContext, SettingAddressActivity.class);
                startActivityForResult(address, RESULT_ADDRESS_SETTING);
                break;
            case R.id.price_people:
                Intent price = new Intent();
                price.putExtra("Price", (Serializable) settingList);
                price.putExtra("PersonNum",mHomeSettingBean.getMaxSaleCount());
                price.setClass(mContext, SettingPeriodActivity.class);
                startActivityForResult(price, RESULT_PRICE_PEOPLE);
                break;
            case R.id.video_phone_count:

                Intent vidCount = new Intent();
                vidCount.putExtra("vidCount", (Serializable) settingList);
                vidCount.setClass(mContext, VideoPhoneCountActivity.class);
                startActivityForResult(vidCount, RESULT_VIDEO_PHONE_COUNT);

                break;
            case R.id.atuo_replay:

                Intent autoReplay = new Intent();
                autoReplay.putExtra("AutoReplay", mHomeSettingBean.getAutoReplyContent());
                autoReplay.setClass(mContext, SettingAutoReplayActivity.class);
                startActivityForResult(autoReplay, RESULT_AUTO_RELAY);
                break;
            case R.id.orther_service:

                Intent remark = new Intent();
                remark.putExtra("Remark", (Serializable) settingList);
                remark.setClass(mContext, AddOtherServiceActivity.class);

                startActivityForResult(remark, RESULT_ADD_OTHER_SERVICE);
                break;
            case R.id.cb_select_protocol:
                isChecked = !isChecked;
                if (isChecked) {
                    btn_open_service.setEnabled(true);
                    btn_open_service.setBackground(getDrawable(R.drawable.app_btn));
                } else {
                    btn_open_service.setEnabled(false);
                    btn_open_service.setBackgroundColor(getResources().getColor(R.color.color_unclick));
                }
                break;
            case R.id.btn_open_service:
                check_upload(true);
                break;
            case R.id.tv_right:
                NBSAppAgent.onEvent("服务-家庭医生-保存");
                if(isFamilyDoctorChecked){
                    check_upload(true);
                }else{
                    if(is_start_image_consult && is_start_video_inquiry && is_start_voice_inquiry) {
                       check_upload(false);
                    }else{
                        ToastUtils.showShort(mContext,"请先开通视话问诊和图文咨询");
                    }
                }

                break;
            case R.id.switch_open:
                NBSAppAgent.onEvent("服务-家庭医生-开通");
                isFamilyDoctorChecked = !isFamilyDoctorChecked;

                break;


        }
    }

    private void close_family_doctor(int open) {
        List<SaveServiceSetBean> list = new ArrayList<>();
        SaveServiceSetBean bean = new SaveServiceSetBean();
        bean.ServiceSwitch = open;
        bean.ServiceType = 4;
        list.add(bean);
        Http_saveServiceSet_Event event = new Http_saveServiceSet_Event(list, new HttpListener() {
            @Override
            public void onError(int code, String msg) {

            }
            @Override
            public void onSuccess(Object o) {
                ToastUtils.showShort(mContext,"保存成功");
                finish();
            }
        });
        new HttpClient(mContext,event).startNewApi();
    }

    //    发送设置请求
    private void check_upload(final boolean isOpen) {
     if(isOpen){
         if(!mIsSettingWorkTime){
             ToastUtils.showShort(mContext,"未设置看诊时间");
             return;
         }

         if(!mIsSetting_price_people){
             ToastUtils.showShort(mContext,"未设置开通周期，价格及服务人数");
             return;
         }

         if(!mIsSetting_video){
             ToastUtils.showShort(mContext,"未设置视话问诊次数");
             return;
         }
     }

        if (isFreeTicketChecked) {
            isFreeCoupon = 1;

        } else {
            isFreeCoupon = 0;

            mMapAddress = "";
            mClicnicAddress = "";
        }

        showLoadDialog(R.string.string_wait);
        Http_saveFamilyDoctorSetting_Event event = new Http_saveFamilyDoctorSetting_Event(isFreeCoupon, mMapAddress, mClicnicAddress, mPersonCount + "", mAutoReplay+"",isOpen,settingList, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                dismissLoadDialog();
                ToastUtils.showShort(mContext,msg);
            }

            @Override
            public void onSuccess(Object o) {
                dismissLoadDialog();
                if(isOpen){
                    ToastUtils.showShort(mContext,"开通成功");
                }else {
                    ToastUtils.showShort(mContext,"取消成功");
                }

//                startActivity(new Intent(mContext,OpenedHomeDoctorActivity.class));
                finish();
            }
        });
        new HttpClient(mContext, event).startNewApi();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_ADD_OTHER_SERVICE:
                mBack = data.getExtras().getInt("Back",0);
                if(mBack == 1){
                    mIsSetting_order_service = data.getExtras().getBoolean("isSetting");
                    settingList = (List<HomeSettingBean.DoctorPackageBean>) data.getSerializableExtra("Remark");
                    tv_oreder_service_setting.setText(R.string.setted);
                    tv_oreder_service_setting.setTextColor(getResources().getColor(R.color.color_sub_string));
                }else {
                    if (mIsSetting_order_service) {
                        tv_oreder_service_setting.setText(R.string.setted);
                        tv_oreder_service_setting.setTextColor(getResources().getColor(R.color.color_sub_string));
                    }else{
                        tv_oreder_service_setting.setText("未设置");
                        tv_oreder_service_setting.setTextColor(getResources().getColor(R.color.app_color_string));
                    }

                }


                break;
            case RESULT_AUTO_RELAY:
                mBack = data.getExtras().getInt("Back",0);
                if(mBack == 1){
                    mAutoReplay = data.getStringExtra("AutoReplay");
                    mIsSetting_auto_replay = data.getExtras().getBoolean("isSetting");
                    tv_auto_replay_setting.setText(R.string.setted);
                    tv_auto_replay_setting.setTextColor(getResources().getColor(R.color.color_sub_string));
                }else{
                    if (mIsSetting_auto_replay) {
                        tv_auto_replay_setting.setText(R.string.setted);
                        tv_auto_replay_setting.setTextColor(getResources().getColor(R.color.color_sub_string));
                    }else{
                        tv_auto_replay_setting.setText("未设置");
                        tv_auto_replay_setting.setTextColor(getResources().getColor(R.color.app_color_string));
                    }
                }
                break;

            case RESULT_PRICE_PEOPLE:
                mBack = data.getExtras().getInt("Back", 0);

                if(mBack == 1){
                    mIsSetting_price_people = data.getExtras().getBoolean("isSetting");
                    settingList = (List<HomeSettingBean.DoctorPackageBean>) data.getSerializableExtra("PriceList");
                    mPersonCount = data.getIntExtra("PersonCount", 0);
                    tv_price_people_setting.setText(R.string.setted);
                    tv_price_people_setting.setTextColor(getResources().getColor(R.color.color_sub_string));
                }else{

                    if (mIsSetting_price_people) {
                        tv_price_people_setting.setText(R.string.setted);
                        tv_price_people_setting.setTextColor(getResources().getColor(R.color.color_sub_string));

                    }else{
                        tv_price_people_setting.setText("未设置");
                        tv_price_people_setting.setTextColor(getResources().getColor(R.color.app_color_string));
                    }

                }
                break;
            case RESULT_ADDRESS_SETTING:
                mBack = data.getExtras().getInt("Back",0);
                if(mBack == 1){
                    mIsSetting_address = data.getExtras().getBoolean("isSetting");
                    mMapAddress = data.getStringExtra("MapAddress");
                    mClicnicAddress = data.getStringExtra("ClicnicAddress");
                    tv_address_setting.setText(R.string.setted);
                    tv_address_setting.setTextColor(getResources().getColor(R.color.color_sub_string));
                }else {
                    if (mIsSetting_address) {
                        tv_address_setting.setText(R.string.setted);
                        tv_address_setting.setTextColor(getResources().getColor(R.color.color_sub_string));
                    }else{
                        tv_address_setting.setText("未设置");
                        tv_address_setting.setTextColor(getResources().getColor(R.color.app_color_string));
                    }
                }
                break;
            case RESULT_VIDEO_PHONE_COUNT:
                mBack = data.getExtras().getInt("Back",0);

                if(mBack == 1){
                    mIsSetting_video = data.getExtras().getBoolean("isSetting");
                    settingList = (List<HomeSettingBean.DoctorPackageBean>) data.getSerializableExtra("vidCount");
                    tv_video_phone_setting.setText(R.string.setted);
                    tv_video_phone_setting.setTextColor(getResources().getColor(R.color.color_sub_string));
                }else{
                    if (mIsSetting_video) {
                        tv_video_phone_setting.setText(R.string.setted);
                        tv_video_phone_setting.setTextColor(getResources().getColor(R.color.color_sub_string));
                    }else {
                        tv_video_phone_setting.setText("未设置");
                        tv_video_phone_setting.setTextColor(getResources().getColor(R.color.app_color_string));
                    }
                }
                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getWorkingSetting(List<HomeSettingBean.WorkingTimeBean> beans) {
        getConfig();
        mIsSettingWorkTime = true;
        tv_work_time_setting.setText(R.string.setted);
        tv_work_time_setting.setTextColor(getResources().getColor(R.color.color_sub_string));
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getSuccess(AppEventApi.Success success){
        mIsSettingWorkTime = success.isSuccess();
        tv_work_time_setting.setText("未设置");
        tv_work_time_setting.setTextColor(getResources().getColor(R.color.app_color_string));
    }

    public void getConfig() {
//        showLoadDialog(R.string.string_wait);
        Http_getHomeDoctorConfig_Event event = new Http_getHomeDoctorConfig_Event(new HttpListener<HomeSettingBean>() {
            @Override
            public void onError(int code, String msg) {
                dismissLoadDialog();
                ToastUtils.showShort(mContext,msg);

            }
            @Override
            public void onSuccess(HomeSettingBean homeSettingBean) {
                dismissLoadDialog();
                mHomeSettingBean = homeSettingBean;
                initSetting();
                initSetted();
            }
        });
        new HttpClient(mContext, event).startNewApi();
    }

    private void initSetted() {
//        其他服务
        if (mIsSetting_order_service) {
            tv_oreder_service_setting.setText(R.string.setted);
            tv_oreder_service_setting.setTextColor(getResources().getColor(R.color.color_sub_string));
        }else {
            tv_oreder_service_setting.setText("未设置");
            tv_oreder_service_setting.setTextColor(getResources().getColor(R.color.app_color_string));
        }
//        自动回复
        if (mIsSetting_auto_replay) {
            tv_auto_replay_setting.setText(R.string.setted);
            tv_auto_replay_setting.setTextColor(getResources().getColor(R.color.color_sub_string));
        }else{
            tv_auto_replay_setting.setText("未设置");
            tv_auto_replay_setting.setTextColor(getResources().getColor(R.color.app_color_string));
        }
//        服务周期价格人数
        if (mIsSetting_price_people) {
            tv_price_people_setting.setText(R.string.setted);
            tv_price_people_setting.setTextColor(getResources().getColor(R.color.color_sub_string));
        }else{
            tv_price_people_setting.setText("未设置");
            tv_price_people_setting.setTextColor(getResources().getColor(R.color.app_color_string));
        }
//        地址
        if (mIsSetting_address) {
            tv_address_setting.setText(R.string.setted);
            tv_address_setting.setTextColor(getResources().getColor(R.color.color_sub_string));
        }else{
            tv_address_setting.setText("未设置");
            tv_address_setting.setTextColor(getResources().getColor(R.color.app_color_string));
        }
//         视频
        if (mIsSetting_video) {
            tv_video_phone_setting.setText(R.string.setted);
            tv_video_phone_setting.setTextColor(getResources().getColor(R.color.color_sub_string));
        }else {
            tv_video_phone_setting.setText("未设置");
            tv_video_phone_setting.setTextColor(getResources().getColor(R.color.app_color_string));
        }
//        时间
        if(mIsSettingWorkTime){
            tv_work_time_setting.setText(R.string.setted);
            tv_work_time_setting.setTextColor(getResources().getColor(R.color.color_sub_string));
        }else{
            tv_work_time_setting.setText("未设置");
            tv_work_time_setting.setTextColor(getResources().getColor(R.color.app_color_string));
        }
//        打开线下地址
        if (isFreeTicketChecked) {
            address_setting.setVisibility(View.VISIBLE);
        } else {
            address_setting.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getService(){
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
                dismissLoadDialog();
                //加载服务设置数据
                List<ServiceSetBean.DataBean> list = bean.getData();
                for(int i=0;i<list.size();i++){
                    ServiceSetBean.DataBean dataBean = list.get(i);
                    switch (dataBean.getServiceType()){
                        case 1://图文咨询
                            is_start_image_consult = dataBean.getServiceSwitch()==1?true:false;
                            break;
                        case 2://语音咨询
                            is_start_voice_inquiry = dataBean.getServiceSwitch()==1?true:false;
                            break;
                        case 3://视频咨询
                            is_start_video_inquiry = dataBean.getServiceSwitch()==1?true:false;
                            break;
                        case 4://家庭医生
                            is_start_family_doctor = dataBean.getServiceSwitch()==1?true:false;
                            break;
                    }
                }
                isFamilyDoctorChecked = is_start_family_doctor;
                switch_open.setChecked(isFamilyDoctorChecked);

            }
        });
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

}
