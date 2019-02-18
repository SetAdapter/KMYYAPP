package com.kmwlyy.patient.myservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.login.LoginActivity;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.evaluate.EvaluateForDoctorActivity;
import com.kmwlyy.patient.helper.ConsultConstants;
import com.kmwlyy.patient.helper.base.BaseFragment;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.MeetingConsultBean;
import com.kmwlyy.patient.helper.net.bean.MeetingConsultBean.Invite;
import com.kmwlyy.patient.helper.net.bean.MeetingDetailBean;
import com.kmwlyy.patient.helper.net.event.HttpMeetingConsults;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.pay.PayActivity;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.EmptyViewUtils;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import io.agora.core.MeetingDoctor;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.agora.core.AgoraApplication;
import io.agora.core.MeetingRoomBean;

/**
 * @Description描述: 远程会诊列表
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/20
 */
public class MeetingConsultFragment extends BaseFragment implements PageListView.OnPageLoadListener {
    private static final String TAG = MeetingConsultFragment.class.getSimpleName();


    @BindView(R.id.doctor_page_listview)
    PageListView listView;

    private PageListViewHelper<MeetingConsultBean> mPageListViewHelper;
    private ViewGroup mRoot;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = (ViewGroup) inflater.inflate(R.layout.my_diagnose, container, false);
        ButterKnife.bind(this, mRoot);
        mPageListViewHelper = new PageListViewHelper<>(listView, new MeetingConsultFragment.MeetingConsultAdapter(getActivity(), null));
        int diagnoseListPadding = getResources().getDimensionPixelSize(R.dimen.list_padding);
        mPageListViewHelper.getListView().setPadding(0, 0, 0, diagnoseListPadding);
        mPageListViewHelper.getListView().setClipToPadding(false);
        mPageListViewHelper.setOnPageLoadListener(this);
        onRefreshData();
        return mRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!PUtils.checkHaveUser(getActivity(), false)) {
            EmptyViewUtils.showEmptyView(mRoot, getResources().getString(R.string.user_not_login_click_login), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.startActivity(getActivity(), LoginActivity.class);
                }
            });
            clearShowState();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onRefreshData() {
        getConsultData(true);
    }

    @Override
    public void onLoadPageData() {
        getConsultData(false);
    }

    private void getConsultData(final boolean refresh) {
        mPageListViewHelper.setRefreshing(refresh);
        HttpMeetingConsults.GetList getMeetingList = new HttpMeetingConsults.GetList(
                refresh ? 1 : mPageListViewHelper.getPageIndex(),
                mPageListViewHelper.getPageSize(),
                new HttpListener<ArrayList<MeetingConsultBean>>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("getMeetingList", code, msg));
                        }
                        EmptyViewUtils.removeAllView(mRoot);
                        mPageListViewHelper.setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(ArrayList<MeetingConsultBean> userConsults) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("getMeetingList", PUtils.toJson(userConsults)));
                        }
                        EmptyViewUtils.removeAllView(mRoot);
                        mPageListViewHelper.setRefreshing(false);
                        if (refresh) {
                            mPageListViewHelper.refreshData(userConsults);
                        } else {
                            mPageListViewHelper.addPageData(userConsults);
                        }
                    }
                });

        HttpClient getMyConsultListClient = NetService.createClient(getActivity(), getMeetingList);
        getMyConsultListClient.start();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void payedRefresh(EventApi.RefreshMeetingConsultList event) {
        getConsultData(true);
    }




    /****************************
     * Adapter
     ***************************************/
    class MeetingConsultAdapter extends CommonAdapter<MeetingConsultBean> {

        public MeetingConsultAdapter(Context context, List<MeetingConsultBean> datas) {
            super(context, R.layout.item_consult, datas);
        }

        @Override
        public void convert(ViewHolder viewHolder, MeetingConsultBean data, final int position) {
            try {
                // 设置标题
                ((TextView) viewHolder.findViewById(R.id.tv_title)).setText(R.string.meeting);
                //填充内容文字
                setBody(viewHolder, data);
                // 设置状态、价格和按钮
                setState(viewHolder, data);

                viewHolder.findViewById(R.id.ll_root).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MeetingConsultBean data = mPageListViewHelper.getAdapter().getItem(position);
                        Intent intent = new Intent(getContext(), MeetingDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("recipeFiles", data.mRecipeFiles);
                        bundle.putString("id", data.mConsultationID);
                        bundle.putString("orderNo", data.mOrder.mOrderNo);
                        bundle.putString("recipeFileUrl", data.mRecipeFileUrl);
                        bundle.putBoolean("isEvaluated", data.mOrder.mIsEvaluated);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            } catch (Exception e) {
            }
        }

        /* 填充内容文字 */
        private void setBody(ViewHolder viewHolder, MeetingConsultBean data) throws Exception {

            //姓名
            ((TextView) viewHolder.findViewById(R.id.tv_value1)).setText(data.mMember.mMemberName);

            //医生
            ((TextView) viewHolder.findViewById(R.id.tv_lab2)).setText(R.string.meeting_doctor);
            ((TextView) viewHolder.findViewById(R.id.tv_value2)).setText(data.mDoctor.mDoctorName);


            //时间
            ((TextView) viewHolder.findViewById(R.id.tv_lab3)).setText(R.string.begin_time);
            ((TextView) viewHolder.findViewById(R.id.tv_value3)).setText(PUtils.convertTimeToDay(data.mConsultationDate));


            //主体描述
            ((TextView) viewHolder.findViewById(R.id.tv_lab4)).setText(R.string.meeting_Subject);
            ((TextView) viewHolder.findViewById(R.id.tv_value4)).setText(data.mSubject);

            //价格
            if (data.mOrder != null) {
                ((TextView) viewHolder.findViewById(R.id.tv_amount)).setText(CommonUtils.convertTowDecimalStr(data.mAmount));
            }

        }


        /* 设置状态、价格和按钮 */
        private void setState(ViewHolder viewHolder, final MeetingConsultBean data) throws Exception {

            //设置状态文字
            TextView tv_state = (TextView) viewHolder.findViewById(R.id.tv_state);

            TextView tv_btn1 = (TextView) viewHolder.findViewById(R.id.tv_btn1);
            TextView tv_btn2 = (TextView) viewHolder.findViewById(R.id.tv_btn2);

//            tv_state.setText(data.mConsultationStatusName);
            //根据状态设置颜色，按钮
            switch (data.mConsultationStatus) {
                /* 未提交 */
                case ConsultConstants.MeetingState.MEETING_WAIT_SUMBIT:
                    tv_state.setTextColor(getResources().getColor(R.color.third_text_color));

                    tv_btn1.setVisibility(View.GONE);
                    tv_btn2.setVisibility(View.GONE);
                    break;
                /* 未支付 */
                case ConsultConstants.MeetingState.MEETING_WAIT_PAY:
                    tv_state.setTextColor(getResources().getColor(R.color.color_button_yellow));
                    tv_state.setText(R.string.wait_pay2);
                    tv_btn1.setVisibility(View.VISIBLE);
                    tv_btn1.setText(R.string.pay_now);
                    tv_btn1.setBackgroundResource(R.drawable.yellow_btn);
                    tv_btn1.setTextColor(getResources().getColor(R.color.app_yellow));
                    tv_btn2.setVisibility(View.GONE);
                    tv_btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (data.mOrder != null) {
                                PayActivity.startPayActivity(getContext(), data.mOrder.mOrderNo,CommonUtils.convertTowDecimalStr(data.mAmount), PayActivity.MEETING_CONSULT, true);
                            }
                        }
                    });
                    break;
                /* 等待开始 */
                case ConsultConstants.MeetingState.MEETING_WAIT_BEGIN:
                    tv_state.setTextColor(getResources().getColor(R.color.primary_color));
                    tv_state.setText(R.string.wait_diagnose);
                    tv_btn1.setVisibility(View.VISIBLE);
                    tv_btn1.setText(R.string.goto_meeting);
                    tv_btn1.setBackgroundResource(R.drawable.btn_follow);
                    tv_btn1.setTextColor(getResources().getColor(R.color.primary_color));
                    tv_btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                MeetingRoomBean bean = new MeetingRoomBean();
                                bean.mRoomID = data.mRoom.mChannelID;
                                bean.mUserID = data.mDoctor.mDoctorID;
                                bean.mUserName = data.mDoctor.mDoctorName;
                                bean.doctors.addAll(getMeetingDoctor(data));
                                AgoraApplication.loginAgoraMeeting(getActivity(), bean);
                            } catch (Exception e) {
                            }
                        }
                    });
                    tv_btn2.setVisibility(View.GONE);
                    break;
                /* 进行中 */
                case ConsultConstants.MeetingState.MEETING:
                    tv_state.setTextColor(getResources().getColor(R.color.primary_color));
                    tv_state.setText(R.string.ongoing);
                    tv_btn1.setVisibility(View.VISIBLE);
                    tv_btn1.setText(R.string.goto_meeting);
                    tv_btn1.setBackgroundResource(R.drawable.btn_follow);
                    tv_btn1.setTextColor(getResources().getColor(R.color.primary_color));
                    tv_btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                MeetingRoomBean bean = new MeetingRoomBean();
                                bean.mRoomID = data.mRoom.mChannelID;
                                bean.mUserID = data.mDoctor.mDoctorID;
                                bean.mUserName = data.mDoctor.mDoctorName;
                                bean.doctors.addAll(getMeetingDoctor(data));
                                AgoraApplication.loginAgoraMeeting(getActivity(), bean);
                            } catch (Exception e) {
                            }
                        }
                    });
                    tv_btn2.setVisibility(View.GONE);
                    break;
                /* 已完成 */
                case ConsultConstants.MeetingState.MEETING_FINISH:
                    tv_state.setTextColor(getResources().getColor(R.color.third_text_color));
                    tv_state.setText(R.string.service_complete);

                    //远程会诊没有处方
//                    if (data.mRecipeFiles != null && data.mRecipeFiles.size() > 0) {
//                        tv_btn1.setVisibility(View.VISIBLE);
//                        tv_btn1.setText(R.string.show_diagnose);
//                        tv_btn1.setBackgroundResource(R.drawable.btn_follow);
//                        tv_btn1.setTextColor(getResources().getColor(R.color.primary_color));
//                        tv_btn1.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                try {
//                                    Intent intent = new Intent(getContext(), PDFActivity.class);
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("url",  data.mRecipeFileUrl);
//                                    bundle.putString("id", CommonUtils.toMD5(data.mRecipeFileUrl));
//                                    bundle.putString("OPDRegisterID", data.mConsultationID);
//                                    bundle.putInt("pageType", PayActivity.MEETING_CONSULT);
//                                    bundle.putSerializable("recipeFiles",data.mRecipeFiles);
//                                    intent.putExtras(bundle);
//                                    startActivity(intent);
//                                } catch (Exception e) {
//                                }
//                            }
//                        });
//                    } else {
                        tv_btn1.setVisibility(View.GONE);
//                    }

//                    if (data.mOrder!=null && !data.mOrder.mIsEvaluated) {
//                        tv_btn1.setVisibility(View.VISIBLE);
//                        tv_btn1.setText(R.string.goto_evaluate);
//                        tv_btn1.setBackgroundResource(R.drawable.btn_follow);
//                        tv_btn1.setTextColor(getResources().getColor(R.color.primary_color));
//                        tv_btn1.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                try {
//                                    EvaluateForDoctorActivity.startEvaluateForDoctorActivity(getContext(), data.mOrder.mOrderNo,
//                                            data.mDoctor.mDoctorName, data.mDoctor.mTitle, data.mDoctor.mHospitalName, data.mDoctor.mDepartmentName);
//                                } catch (Exception e) {
//                                }
//                            }
//                        });
//                    } else {
                        tv_btn2.setVisibility(View.GONE);
//                    }

                    break;
            }

        }

        private  ArrayList<MeetingDoctor> getMeetingDoctor(MeetingConsultBean data){
            ArrayList<MeetingDoctor> meetingDoctors = new ArrayList<>();
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
            return meetingDoctors;
        }

    }

}
