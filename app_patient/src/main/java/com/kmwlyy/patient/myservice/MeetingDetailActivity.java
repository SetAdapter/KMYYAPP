package com.kmwlyy.patient.myservice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.imchat.TimApplication;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.ConsultConstants;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.ConsultRecipeBean;
import com.kmwlyy.patient.helper.net.bean.MeetingConsultBean.Invite;
import com.kmwlyy.patient.helper.net.bean.MeetingDetailBean;
import com.kmwlyy.patient.helper.net.event.HttpMeetingConsults;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.pay.PayActivity;
import io.agora.core.AgoraApplication;
import io.agora.core.MeetingDoctor;
import io.agora.core.MeetingRoomBean;
import java.util.ArrayList;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @Description描述:
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/23
 */
public class MeetingDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = MeetingDetailActivity.class.getSimpleName();

    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;


    @BindView(R.id.tv_subject)
    TextView tv_subject;
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
    @BindView(R.id.tv_meeting_doctor)
    TextView tv_meeting_doctor;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_meetingContent)
    TextView tv_meetingContent;
    @BindView(R.id.tv_remind)
    TextView tv_remind;
    @BindView(R.id.tv_summarize)
    TextView tv_summarize;

    @BindView(R.id.btn_show_diagnose)
    TextView btn_show_diagnose;//查看处方
    @BindView(R.id.rl_show_diagnose)
    RelativeLayout rl_show_diagnose;//查看处方
    @BindView(R.id.goto_room)
    TextView goto_room;//进入诊室

    private HttpClient mHttpClient;

    private boolean isEvaluated = false;
    private String id = "";
    private String recipeFileUrl = "";
    private String orderNo = "";
    private String channelID = "";
    private String doctorName = "";
    private ArrayList<ConsultRecipeBean> recipeFiles;
    private ArrayList<MeetingDoctor> meetingDoctors = new ArrayList<>();

    private int consultationStatus = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);
        butterknife.ButterKnife.bind(this);

        try{
            Bundle bundle = getIntent().getExtras();
            isEvaluated = bundle.getBoolean("isEvaluated");
            id = bundle.getString("id");
            orderNo = bundle.getString("orderNo");
            recipeFileUrl = bundle.getString("recipeFileUrl");
            recipeFiles = (ArrayList<ConsultRecipeBean>)bundle.getSerializable("recipeFiles");
        }
        catch (Exception e){}


        initToolBar();
        initSwipeRefreshLayout();
    }

    @Override
    public void onDestroy() {
        NetService.closeClient(mHttpClient);
        super.onDestroy();
    }

    private void initToolBar(){
        tv_center.setText(R.string.meeting_detail);
        tv_right.setText(R.string.meeting_recode);
        tv_right.setVisibility(View.VISIBLE);
    }

    private void initSwipeRefreshLayout() {
        swipeRefresh.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);


        swipeRefresh.setOnRefreshListener(this);
        onRefresh();
    }




    @Override
    public void onRefresh() {
        HttpMeetingConsults.GetDetail getDetail = new HttpMeetingConsults.GetDetail(id,new HttpListener<MeetingDetailBean>(){
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("getVVDetail", code, msg));
                }

                ToastUtils.showLong(MeetingDetailActivity.this,R.string.page_load_fail_refresh);
                swipeRefresh.setRefreshing(false);

            }

            @Override
            public void onSuccess(MeetingDetailBean data) {
                Log.d(TAG, DebugUtils.successFormat("getVVDetail", PUtils.toJson(data)));
                swipeRefresh.setRefreshing(false);
                channelID = data.mRoom.mChannelID;
                doctorName = data.mDoctor.mDoctorName;
                initView(data);
            }
        });

        mHttpClient = NetService.createClient(this, getDetail);
        mHttpClient.start();
    }

    private void initView(final MeetingDetailBean data){
        tv_subject.setText(data.mConsultationSubject);
        tv_doctor.setText(data.mDoctor.mDoctorName);
        tv_name.setText(data.mMember.mMemberName);
        tv_sex.setText(data.mMember.mGender == 0 ? "男":"女");
        tv_age.setText(String.valueOf(data.mMember.mAge));
        tv_meeting_doctor.setText(data.mDoctorNames);
        tv_time.setText(PUtils.convertTimeToDay(data.mConsultationDate));
        tv_expense.setText(CommonUtils.convertTowDecimalStr(data.mAmount) + "元");
        if (!TextUtils.isEmpty(data.mConsultationContent)) {
            tv_meetingContent.setText(data.mConsultationContent);
        }
        if (!TextUtils.isEmpty(data.mConsultationRemind)) {
            tv_remind.setText(data.mConsultationRemind);
        }
        if (!TextUtils.isEmpty(data.mConsultationResult)) {
            tv_summarize.setText(data.mConsultationResult);
        }
        addMeetingDoctor(data);
        consultationStatus = data.mConsultationStatus;
        switch (data.mConsultationStatus) {
            /* 未提交 */
            case ConsultConstants.MeetingState.MEETING_WAIT_SUMBIT:
                tv_state.setTextColor(getResources().getColor(R.color.third_text_color));
                tv_state.setText(R.string.wait_submit);
                break;
             /* 未支付 */
            case ConsultConstants.MeetingState.MEETING_WAIT_PAY:
                tv_state.setTextColor(getResources().getColor(R.color.color_button_yellow));
                tv_state.setText(R.string.wait_pay2);
                goto_room.setVisibility(View.VISIBLE);
                goto_room.setText(R.string.pay_now);
                goto_room.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (orderNo != null) {
                            PayActivity.startPayActivity(MeetingDetailActivity.this, orderNo, CommonUtils.convertTowDecimalStr(data.mAmount),PayActivity.MEETING_CONSULT, true);
                        }
                    }
                });
                break;
            /* 等待开始 */
            case ConsultConstants.MeetingState.MEETING_WAIT_BEGIN:
                tv_state.setTextColor(getResources().getColor(R.color.primary_color));
                tv_state.setText(R.string.wait_diagnose);
                goto_room.setText(R.string.goto_diagnose);
                goto_room.setVisibility(View.VISIBLE);
                goto_room.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            MeetingRoomBean bean = new MeetingRoomBean();
                            bean.mRoomID = data.mRoom.mChannelID;
                            bean.mUserID = data.mDoctor.mDoctorID;
                            bean.mUserName = data.mDoctor.mDoctorName;
                            bean.doctors.addAll(meetingDoctors);
                            AgoraApplication.loginAgoraMeeting(MeetingDetailActivity.this, bean);
                        } catch (Exception e) {
                        }
                    }
                });
                break;
             /* 进行中 */
            case ConsultConstants.MeetingState.MEETING:

                tv_right.setVisibility(View.VISIBLE);
                tv_state.setTextColor(getResources().getColor(R.color.primary_color));
                tv_state.setText(R.string.ongoing);
                goto_room.setText(R.string.goto_diagnose);
                goto_room.setVisibility(View.VISIBLE);
                goto_room.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            MeetingRoomBean bean = new MeetingRoomBean();
                            bean.mRoomID = data.mRoom.mChannelID;
                            bean.mUserID = data.mDoctor.mDoctorID;
                            bean.mUserName = data.mDoctor.mDoctorName;
                            bean.doctors.addAll(meetingDoctors);
                            AgoraApplication.loginAgoraMeeting(MeetingDetailActivity.this, bean);
                        } catch (Exception e) {
                        }
                    }
                });
                break;
             /* 已完成 */
            case ConsultConstants.MeetingState.MEETING_FINISH:
                tv_right.setVisibility(View.VISIBLE);
                tv_state.setTextColor(getResources().getColor(R.color.third_text_color));
                tv_state.setText(R.string.service_complete);

                //远程会诊没有处方
//                if (recipeFiles != null && recipeFiles.size() > 0) {
//                    rl_show_diagnose.setVisibility(View.VISIBLE);
//                    btn_show_diagnose.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            try {
//                                Intent intent = new Intent(MeetingDetailActivity.this, PDFActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("url", recipeFileUrl);
//                                bundle.putString("id", CommonUtils.toMD5(recipeFileUrl));
//                                bundle.putSerializable("recipeFiles",recipeFiles);
//                                bundle.putString("OPDRegisterID", data.mConsultationID);
//                                bundle.putInt("pageType", PayActivity.MEETING_CONSULT);
//                                intent.putExtras(bundle);
//                                startActivity(intent);
//                            } catch (Exception e) {
//                            }
//                        }
//                    });
//                } else {
                    rl_show_diagnose.setVisibility(View.GONE);
//                }

//                if (!isEvaluated) {
//                    goto_room.setVisibility(View.VISIBLE);
//
//                    goto_room.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            try {
//                                EvaluateForDoctorActivity.startEvaluateForDoctorActivity(MeetingDetailActivity.this, orderNo,
//                                        data.mDoctor.mDoctorName, data.mDoctor.mTitle, data.mDoctor.mHospitalName, data.mDoctor.mDepartmentName);
//                            } catch (Exception e) {
//                            }
//                        }
//                    });
//                } else {
                    goto_room.setVisibility(View.GONE);
//                }


                break;
        }

    }


    private void addMeetingDoctor(MeetingDetailBean data){
        try {
            MeetingDoctor doctor = new MeetingDoctor();
            doctor.doctorAgoraUid = data.mDoctor.mUser.agoraUid;
            doctor.doctorName = data.mDoctor.mDoctorName;
            meetingDoctors.add(doctor);

            for (Invite invite : data.invites){
                MeetingDoctor doctorTemp = new MeetingDoctor();
                doctorTemp.doctorAgoraUid = invite.mDoctor.mUser.agoraUid;
                doctorTemp.doctorName = invite.mDoctor.mDoctorName;
                meetingDoctors.add(doctorTemp);
            }
        }catch (Exception e){

        }
    }

    //记录
    @OnClick(R.id.tv_right)
    public void jumpRecode() {
        //跳转远程会诊的聊天界面

//        boolean hiddenInput = data.mConsultState == 0
//                || data.mConsultState == 1
//                || data.mConsultState == 5
//                || data.mRoom == null
//                || data.mRoom.mChannelID == null
//                || data.mRoom.mChannelID.equals("0");
        if(!TextUtils.isEmpty(channelID) &&
                (consultationStatus == ConsultConstants.MeetingState.MEETING || consultationStatus ==ConsultConstants.MeetingState.MEETING_WAIT_BEGIN)) {
            TimApplication.enterTimchat(this, channelID, doctorName, true);
        }
        else {
            TimApplication.enterTimchat(this, channelID, doctorName, false);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void payedRefresh(EventApi.RefreshMeetingConsultList event) {
        swipeRefresh.setOnRefreshListener(this);
        onRefresh();
    }
}
