package com.kmwlyy.patient.myservice;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.imchat.TimApplication;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.evaluate.EvaluateForDoctorActivity;
import com.kmwlyy.patient.helper.ConsultConstants;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.ConsultRecipeBean;
import com.kmwlyy.patient.helper.net.bean.UserOPDRegisters;
import com.kmwlyy.patient.helper.net.bean.VVConsultDetailBean;
import com.kmwlyy.patient.helper.net.event.HttpUserOPDRegisters;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.pay.PayActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.agora.core.AgoraApplication;
import io.agora.core.ConsultBean;
import io.agora.core.RoomActivity1;

/**
 * @Description描述: 音视频咨询详情页
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/23
 */
public class VVConsultDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = VVConsultDetailActivity.class.getSimpleName();


    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;


    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_state)
    TextView tv_state;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_doctor)
    TextView tv_doctor;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.tv_age)
    TextView tv_age;
    @BindView(R.id.tv_expense)
    TextView tv_expense;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_demand)
    TextView tv_demand;//主诉
    @BindView(R.id.tv_medical_history)
    TextView tv_medical_history;//现病史
    @BindView(R.id.tv_medical_history2)
    TextView tv_medical_history2;//既往病史
    @BindView(R.id.tv_prescribed)
    TextView tv_prescribed;//医嘱
    @BindView(R.id.tv_diagnosed)
    TextView tv_diagnosed;//初步诊断
    @BindView(R.id.btn_show_diagnose)
    TextView btn_show_diagnose;//查看处方
    @BindView(R.id.rl_show_diagnose)
    RelativeLayout rl_show_diagnose;//查看处方
    @BindView(R.id.goto_room)
    TextView goto_room;//进入诊室

    private String channelID = "";

    private HttpClient mHttpClient;

    private String oPDRegisterID = "";
    private String orderNo = "";
    private int opDType = 0;
    private int orderState = 0;
    private boolean isEvaluated = false;


    private String doctorID = "";
    private String doctorPhotoUrl = "";
    private String doctorDepartmentName = "";
    private String doctorHospitalName = "";
    private String doctorName = "";
    private String doctorTitle = "";
    private String appointmentTime = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vv_detail);
        butterknife.ButterKnife.bind(this);

        try {
            Bundle bundle = getIntent().getExtras();
            oPDRegisterID = bundle.getString("OPDRegisterID");
            doctorTitle = bundle.getString("doctorTitle");
            channelID = bundle.getString("channelID");
            opDType = bundle.getInt("opDType", 0);
            doctorID = bundle.getString("doctorID");
            doctorName = bundle.getString("doctorName");
            doctorPhotoUrl = bundle.getString("doctorPhotoUrl");
            doctorDepartmentName = bundle.getString("doctorDepartmentName");
            doctorHospitalName = bundle.getString("doctorHospitalName");
            appointmentTime = bundle.getString("appointmentTime");
//            recipeFileUrl = bundle.getString("recipeFileUrl");
//            recipeFiles = (ArrayList<ConsultRecipeBean>) bundle.getSerializable("recipeFiles");


            tv_name.setText(bundle.getString("name"));
            tv_age.setText(String.valueOf(bundle.getInt("age", 0)));
            tv_sex.setText(bundle.getInt("sex", 0) == 0 ? "男" : "女");
            tv_doctor.setText(doctorName);
            tv_time.setText(appointmentTime);





        } catch (Exception e) {
        }


        initToolBar();
        initSwipeRefreshLayout();
    }

    @Override
    public void onDestroy() {
        NetService.closeClient(mHttpClient);
        super.onDestroy();
    }

    private void initToolBar() {
        tv_center.setText(R.string.vv_detail);
        tv_right.setText(R.string.vv_recode);
    }

    private void initSwipeRefreshLayout() {
        swipeRefresh.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);


        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
        onRefresh();
    }


    @Override
    public void onRefresh() {
        HttpUserOPDRegisters.GetDetail getDetail = new HttpUserOPDRegisters.GetDetail(oPDRegisterID, new HttpListener<VVConsultDetailBean>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("getVVDetail", code, msg));
                }

                ToastUtils.showLong(VVConsultDetailActivity.this, R.string.page_load_fail_refresh);
                swipeRefresh.setRefreshing(false);

            }

            @Override
            public void onSuccess(VVConsultDetailBean data) {
                Log.d(TAG, DebugUtils.successFormat("getVVDetail", PUtils.toJson(data)));
                swipeRefresh.setRefreshing(false);
                initView(data);
            }
        });

        mHttpClient = NetService.createClient(this, getDetail);
        mHttpClient.start();
    }

    private void initView(final VVConsultDetailBean data) {

//        tv_doctor.setText(data.mDoctor.mDoctorName);
//        tv_name.setText(data.mMember.mMemberName);
//        tv_sex.setText(data.mMember.mGender == 0 ? "男":"女");
        tv_age.setText(String.valueOf(data.mMember.mAge));

        opDType = data.mOPDType;

//        if (!TextUtils.isEmpty(data.mConsultContent)) {
//            tv_demand.setText(data.mConsultContent);
//        }
        tv_expense.setText(CommonUtils.convertTowDecimalStr(data.mFee) + "元");

        if (data.mOPDType == ConsultConstants.VoiceVideo.TYPE_VOICE) {
            tv_title.setText(R.string.voice_diagnose2);
        } else {
            tv_title.setText(R.string.video_diagnose2);
        }

        if (data.mUserMedicalRecord != null) {
            if (!TextUtils.isEmpty(data.mUserMedicalRecord.mSympton)) {
                tv_demand.setText(data.mUserMedicalRecord.mSympton);
            }
            if (!TextUtils.isEmpty(data.mUserMedicalRecord.mPastMedicalHistory)) {
                tv_medical_history2.setText(data.mUserMedicalRecord.mPastMedicalHistory);
            }
            if (!TextUtils.isEmpty(data.mUserMedicalRecord.mPresentHistoryIllness)) {
                tv_medical_history.setText(data.mUserMedicalRecord.mPresentHistoryIllness);
            }
            if (!TextUtils.isEmpty(data.mUserMedicalRecord.mPreliminaryDiagnosis)) {
                tv_diagnosed.setText(data.mUserMedicalRecord.mPreliminaryDiagnosis);
            }
            if (!TextUtils.isEmpty(data.mUserMedicalRecord.mAdvised)) {
                tv_prescribed.setText(data.mUserMedicalRecord.mAdvised);
            }
        }

        if (data.mOrder!=null) {
            orderState = data.mOrder.mOrderState;
            orderNo = data.mOrder.mOrderNo;
            isEvaluated = data.mOrder.mIsEvaluated;
        }

        switch (orderState) {
            case ConsultConstants.OrderState.ORDER_WAIT_PAY://未支付
                tv_state.setText(R.string.wait_pay2);
                tv_state.setTextColor(getResources().getColor(R.color.color_button_yellow));
                goto_room.setVisibility(View.VISIBLE);
                goto_room.setText(R.string.pay_now);
                goto_room.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            PayActivity.startPayActivity(VVConsultDetailActivity.this, orderNo,CommonUtils.convertTowDecimalStr(data.mFee), PayActivity.VIDEO_VOICE_CONSULT, true);
                        } catch (Exception e) {
                        }
                    }
                });
                break;
            case ConsultConstants.OrderState.ORDER_PAYED://已支付

                tv_state.setTextColor(getResources().getColor(R.color.primary_color));

                if (!TextUtils.isEmpty(channelID) && !"0".equals(channelID)) {
                    tv_right.setVisibility(View.VISIBLE);
                    tv_state.setText(R.string.wait_diagnose);
                    goto_room.setVisibility(View.VISIBLE);
                    goto_room.setText(R.string.goto_diagnose);
                    goto_room.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                ConsultBean bean = new ConsultBean();
                                bean.mRoomID = channelID;
                                if (opDType == ConsultConstants.VoiceVideo.TYPE_VOICE) {
                                    bean.mCallType = RoomActivity1.CALLING_TYPE_VOICE;
                                } else {
                                    bean.mCallType = RoomActivity1.CALLING_TYPE_VIDEO;
                                }
                                bean.mUserType = RoomActivity1.USER_TYPE_PATIENT;
                                bean.mUserID = doctorID;
                                bean.mUserFace = doctorPhotoUrl;
                                bean.mUserName = doctorName;
                                bean.mUserPhone = doctorDepartmentName;
                                bean.mUserInfo = doctorHospitalName;
                                AgoraApplication.loginAgora(VVConsultDetailActivity.this, bean);
                            } catch (Exception e) {
                            }
                        }
                    });
                }
                else{
                    tv_state.setText(R.string.have_buyed);
                }

                break;
            case ConsultConstants.OrderState.ORDER_FINISH://已完成
                tv_right.setVisibility(View.VISIBLE);
                tv_state.setText(R.string.service_complete);
                tv_state.setTextColor(getResources().getColor(R.color.third_text_color));

                if (!TextUtils.isEmpty(data.mRecipeFileUrl)) {
                    //按钮状态
                    rl_show_diagnose.setVisibility(View.VISIBLE);

                    btn_show_diagnose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent intent;
                                if (Build.VERSION.SDK_INT >= 21) {
                                    intent = new Intent(VVConsultDetailActivity.this, PDFActivity.class);
                                }else{
                                    intent = new Intent(VVConsultDetailActivity.this, LegacyPDFActivity.class);
                                }
                                Bundle bundle = new Bundle();
                                bundle.putString("url", data.mRecipeFileUrl);
                                bundle.putString("id", CommonUtils.toMD5(data.mRecipeFileUrl));
                                bundle.putInt("pageType", PayActivity.VIDEO_VOICE_CONSULT);
                                bundle.putString("OPDRegisterID", data.mOPDRegisterID);
//                                bundle.putSerializable("recipeFiles", data.mRecipeFiles);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } catch (Exception e) {
                            }
                        }
                    });
                }


                if (!isEvaluated) {
                    goto_room.setVisibility(View.VISIBLE);
                    goto_room.setText(R.string.goto_evaluate);

                    goto_room.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                EvaluateForDoctorActivity.startEvaluateForDoctorActivity(VVConsultDetailActivity.this, oPDRegisterID,
                                        doctorName, doctorTitle, doctorHospitalName, doctorDepartmentName,false,doctorPhotoUrl);
                            } catch (Exception e) {
                            }
                        }
                    });
                }

                break;

            default:
                tv_state.setText(R.string.pay_canceled);
                tv_state.setTextColor(getResources().getColor(R.color.third_text_color));

        }


    }


    //记录
    @OnClick(R.id.tv_right)
    public void jumpRecode() {
//        boolean hiddenInput = data.mConsultState == 0
//                || data.mConsultState == 1
//                || data.mConsultState == 5
//                || data.mRoom == null
//                || data.mRoom.mChannelID == null
//                || data.mRoom.mChannelID.equals("0");

//        boolean hiddenInput = data.mConsultState == 0
//                || data.mConsultState == 1
//                || data.mConsultState == 5
//                || data.mRoom == null
//                || data.mRoom.mChannelID == null
//                || data.mRoom.mChannelID.equals("0");
//
//        TimApplication.enterTimchat(getActivity(), data.mRoom.mChannelID, data.mDoctor.mDoctorName, !hiddenInput);

        if (orderState == ConsultConstants.OrderState.ORDER_PAYED  && !TextUtils.isEmpty(channelID) && !"0".equals(channelID)) {
            TimApplication.enterTimchat(this, channelID, doctorName, true);
        }
        else{
            TimApplication.enterTimchat(this, channelID, doctorName, false);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void payedRefresh(EventApi.RefreshVVConsultList event) {
        onRefresh();
    }

}
