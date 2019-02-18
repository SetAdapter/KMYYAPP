package com.kmwlyy.doctor.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.Activity.BaseActivity;
import com.kmwlyy.doctor.Activity.CalendarActivity;
import com.kmwlyy.doctor.Activity.FreeClinicShowActivity;
import com.kmwlyy.doctor.Activity.HomeDoctorActivity;

import com.kmwlyy.doctor.Activity.IMConsultListActivity;
import com.kmwlyy.doctor.Activity.OpenedHomeDoctorActivity;

import com.kmwlyy.doctor.Activity.IssueStopDiagnoseActivity;

import com.kmwlyy.doctor.Activity.VideoInquiryActivity;
import com.kmwlyy.doctor.Activity.VideoVoiceChatListActivity;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.HomeSettingBean;
import com.kmwlyy.doctor.model.InfoEvent;
import com.kmwlyy.doctor.model.ServiceSetBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getHomeDoctorConfig_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_getServiceSet_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_recoverDiagnosis_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.EventBase;
import com.networkbench.agent.impl.NBSAgent;
import com.networkbench.agent.impl.NBSAppAgent;
import com.winson.ui.widget.AlterDialogView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by xcj on 2017/6/19.
 */

public class ServiceFragment extends Fragment implements OnClickListener {
    @ViewInject(R.id.rl_video_inquiry)
    RelativeLayout rl_video_inquiry;
    @ViewInject(R.id.rl_image_consult)
    RelativeLayout rl_image_consult;
    @ViewInject(R.id.rl_image_clinic)
    RelativeLayout rl_image_clinic;
    @ViewInject(R.id.rl_family_doctor)
    RelativeLayout rl_family_doctor;
    @ViewInject(R.id.rl_normal_consultation)
    RelativeLayout rl_normal_consultation;
    @ViewInject(R.id.rl_both_way_consultation)
    RelativeLayout rl_both_way_consultation;
    @ViewInject(R.id.rl_scheduling_setting)
    RelativeLayout rl_scheduling_setting;
    @ViewInject(R.id.rl_stop_inquiry)
    RelativeLayout rl_stop_inquiry;
    @ViewInject(R.id.tv_stop_time)
    TextView tv_stop_time;
    @ViewInject(R.id.tv_stop_consult)
    TextView tv_stop_consult;

    public Context mContext;
    private boolean is_start_video_inquiry;
    private boolean is_start_image_consult;
    private boolean is_start_image_clinic;
    private boolean is_start_family_doctor;
    private boolean is_start_voice_inquiry;
    private boolean is_stop_consult = false;
    private boolean is_opened_familydoctor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, null);
        ((TextView) view.findViewById(R.id.tv_center)).setText( getResources().getString(R.string.string_tab_service));
        view.findViewById(R.id.tv_left).setVisibility(View.GONE);
        ViewUtils.inject(this,view); //注入view和事件
        mContext = getActivity();
        rl_video_inquiry.setOnClickListener(this);
        rl_image_consult.setOnClickListener(this);
        rl_image_clinic.setOnClickListener(this);
        rl_family_doctor.setOnClickListener(this);
        rl_normal_consultation.setOnClickListener(this);
        rl_both_way_consultation.setOnClickListener(this);
        rl_scheduling_setting.setOnClickListener(this);
        rl_stop_inquiry.setOnClickListener(this);
        getServiceSet(null);
        getFamilyDoctorConfig(null);

        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshData(InfoEvent.IssueStopSuc refreshData) {
        getServiceSet(null);
    }

    @Override
    public void onClick(View v) {
        getServiceSet(v);
        getFamilyDoctorConfig(v);
    }

    /**
     * 获取服务设置
     */
    private void getServiceSet(final View v) {
        ((BaseActivity)getActivity()).showLoadDialog(R.string.string_wait);
        Http_getServiceSet_Event event = new Http_getServiceSet_Event(new HttpListener<ServiceSetBean>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                }
                ((BaseActivity)getActivity()).dismissLoadDialog();
                ToastUtils.show(mContext, msg, Toast.LENGTH_SHORT);
            }

            @Override
            public void onSuccess(ServiceSetBean bean) {
                ((BaseActivity)getActivity()).dismissLoadDialog();
                if (bean.StopDiagnosis == null){
                    is_stop_consult = false;
                }else if(!TextUtils.isEmpty(bean.StopDiagnosis.BeginDate)){
                    is_stop_consult = true;
                }else{
                    is_stop_consult = false;
                }

                if (is_stop_consult){
                    tv_stop_time.setText("截止到"+bean.StopDiagnosis.EndDate+bean.StopDiagnosis.EndTimeName);
                    tv_stop_time.setVisibility(View.VISIBLE);
                    tv_stop_consult.setText("已停诊");
                }else{
                    tv_stop_time.setVisibility(View.GONE);
                    tv_stop_consult.setText("停诊发布");
                }
                is_start_image_clinic =  bean.getFreeClinicSetting().isState();
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
                if (v != null) {
                    clickBtn(v);
                }
            }
        });
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

    private void  recoverDiagnosis(){
        ((BaseActivity)getActivity()).showLoadDialog(R.string.string_wait);
        Http_recoverDiagnosis_Event event = new Http_recoverDiagnosis_Event(new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                ((BaseActivity)getActivity()).dismissLoadDialog();
                ToastUtils.showShort(getActivity(),"取消停诊失败");
                is_stop_consult = true;
            }

            @Override
            public void onSuccess(Object o) {
                ((BaseActivity)getActivity()).dismissLoadDialog();
                ToastUtils.showShort(getActivity(),"取消停诊成功");
                is_stop_consult = false;
                getServiceSet(null);
            }
        });
        HttpClient httpClient = NetService.createClient(mContext, HttpClient.DOCTOR_API_URL, event);
        httpClient.start();
    }

    public void clickFamilyDoctor(View view){
        if(view.getId() == R.id.rl_family_doctor){
            NBSAppAgent.onEvent("服务-家庭医生");
            if (is_opened_familydoctor){
                //已开通
                startActivity(new Intent(mContext, OpenedHomeDoctorActivity.class));
            }else if (is_start_video_inquiry && is_start_voice_inquiry && is_start_image_consult){
                //未开通
                startActivity(new Intent(mContext, HomeDoctorActivity.class));
            }else{
                ToastUtils.showShort(getActivity(),"请先开通视话问诊和图文咨询");
            }

        }
    }

    public void clickBtn(View v){
        switch (v.getId()) {
            //视话问诊
            case R.id.rl_video_inquiry:
                NBSAppAgent.onEvent("服务-视话问诊");
                if(is_start_video_inquiry){
                    //视话列表页面
                    startActivity(new Intent(mContext, VideoVoiceChatListActivity.class));
                }else {
                    //开通页面
                    VideoInquiryActivity.startVideoInquiryActivity(mContext, 1);
                }
                break;
            //图文咨询
            case R.id.rl_image_consult:
                NBSAppAgent.onEvent("服务-图文咨询");
                if(is_start_image_consult){
                    //图文咨询列表页面
                    startActivity(new Intent(mContext, IMConsultListActivity.class));
                }else {
                    //开通页面
                    VideoInquiryActivity.startVideoInquiryActivity(mContext, 2);
                }
                break;
            //图文义诊
            case R.id.rl_image_clinic:
                NBSAppAgent.onEvent("服务-图文义诊");
                if (is_start_image_clinic){
                    //义诊列表页面
                    startActivity(new Intent(mContext, FreeClinicShowActivity.class));
                }else if (is_start_image_consult){
                    //开通页面
                    VideoInquiryActivity.startVideoInquiryActivity(mContext, 3);
                }else{
                    //义诊需先开通图文
                    ToastUtils.showShort(getActivity(),"请先开通图文咨询");
                }
                break;
            //家庭医生
           /* case R.id.rl_family_doctor:
                NBSAppAgent.onEvent("服务-家庭医生");
                if (is_opened_familydoctor){
                    //已开通
                    startActivity(new Intent(mContext, OpenedHomeDoctorActivity.class));
                }else if (is_start_video_inquiry && is_start_voice_inquiry && is_start_image_consult){
                    //未开通
                    startActivity(new Intent(mContext, HomeDoctorActivity.class));
                }else{
                    ToastUtils.showShort(getActivity(),"请先开通视话问诊和图文咨询");
                }

                break;*/
            //普通会诊
            case R.id.rl_normal_consultation:
                ToastUtils.showShort(mContext,"功能即将上线，敬请期待！");
                break;
            //双向转诊
            case R.id.rl_both_way_consultation:
                ToastUtils.showShort(mContext,"功能即将上线，敬请期待！");
                break;
            //排班设置
            case R.id.rl_scheduling_setting:
                NBSAppAgent.onEvent("服务—排班设置");
                if(is_start_video_inquiry && is_start_voice_inquiry){
                    //排版设置页面
                    startActivity(new Intent(mContext,CalendarActivity.class));
                }else {
                    //开通页面
                    VideoInquiryActivity.startVideoInquiryActivity(mContext, 1);
                }
                break;
            //停诊发布
            case R.id.rl_stop_inquiry:
                NBSAppAgent.onEvent("服务-停诊发布");
                if (is_stop_consult) {
                    AlterDialogView.Builder builder = new AlterDialogView.Builder(getActivity());
                    builder.setTitle("取消停诊");
                    builder.setMessage("是否取消停诊？");
                    builder.setNegativeButton(getString(R.string.string_exit_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(getString(R.string.string_exit_yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    //打开设置页面
                                    arg0.dismiss();
                                    recoverDiagnosis();
                                }
                            });
                    builder.create().show();
                }else{
                    startActivity(new Intent(mContext, IssueStopDiagnoseActivity.class));
                }
                break;
        }
    }
    /*获取家庭医生设置*/
    private void getFamilyDoctorConfig(final View view){
        Http_getHomeDoctorConfig_Event event = new Http_getHomeDoctorConfig_Event(new HttpListener<HomeSettingBean>() {
            @Override
            public void onError(int code, String msg) {

            }
            @Override
            public void onSuccess(HomeSettingBean homeSettingBean) {
                List<HomeSettingBean.DoctorPackageBean> doctorFamilyPackages = homeSettingBean.getDoctorFamilyPackages();
                if(doctorFamilyPackages != null && doctorFamilyPackages.size()>0){
                        is_opened_familydoctor = true;
                }else{
                        is_opened_familydoctor = false;
                }

                if(view != null){
                    clickFamilyDoctor(view);
                }

            }
        });
        new HttpClient(mContext, event).startNewApi();
    }

}
