package com.kmwlyy.patient.myservice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.login.LoginActivity;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.evaluate.EvaluateForDoctorActivity;
import com.kmwlyy.patient.helper.ConsultConstants;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.base.BaseFragment;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.Doctor;
import com.kmwlyy.patient.helper.net.bean.UserOPDRegisters;
import com.kmwlyy.patient.helper.net.event.HttpUserOPDRegisters;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.home.HealthInfoDetailActivity;
import com.kmwlyy.patient.home.HealthInfoListActivity;
import com.kmwlyy.patient.pay.PayActivity;
import com.winson.ui.widget.AlterDialogView;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.EmptyViewUtils;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.agora.core.AgoraApplication;
import io.agora.core.ConsultBean;
import io.agora.core.RoomActivity1;

/**
 * @Description描述: 语音咨询列表
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/20
 */
public class VVConsultFragment extends BaseFragment implements PageListView.OnPageLoadListener {
    private static final String TAG = VVConsultFragment.class.getSimpleName();


    @BindView(R.id.doctor_page_listview)
    PageListView listView;

    private PageListViewHelper<UserOPDRegisters> mPageListViewHelper;
    private ViewGroup mRoot;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = (ViewGroup) inflater.inflate(R.layout.my_diagnose, container, false);
        ButterKnife.bind(this, mRoot);
        mPageListViewHelper = new PageListViewHelper<>(listView, new VoiceVideoConsultAdapter(getActivity(), null));
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
        HttpUserOPDRegisters.GetList getOPDRegisterList = new HttpUserOPDRegisters.GetList(
                null,
                null,
                null,
                refresh ? 1 : mPageListViewHelper.getPageIndex(),
                mPageListViewHelper.getPageSize(),
                new HttpListener<ArrayList<UserOPDRegisters>>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("getMyConsultList", code, msg));
                        }
                        EmptyViewUtils.removeAllView(mRoot);
                        mPageListViewHelper.setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(ArrayList<UserOPDRegisters> userConsults) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("getMyConsultList", PUtils.toJson(userConsults)));
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

        HttpClient getMyConsultListClient = NetService.createClient(getActivity(), getOPDRegisterList);
        getMyConsultListClient.start();
    }

    /* 取消订单 */
    private void setOrderCancel(String orderNo) {
        ((BaseActivity) getActivity()).showLoadDialog(R.string.cancel_order_wait);
        HttpUserOPDRegisters.CancelOrder cancelOrder = new HttpUserOPDRegisters.CancelOrder(
                orderNo,
                new HttpListener<String>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("cancelOrder", code, msg));
                        }
                        ((BaseActivity) getActivity()).dismissLoadDialog();
                        ToastUtils.showLong(getActivity(), msg);
                    }

                    @Override
                    public void onSuccess(String result) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("cancelOrder", result));
                        }
                        ((BaseActivity) getActivity()).dismissLoadDialog();
                        ToastUtils.showLong(getActivity(), R.string.cancel_order_success);
                        getConsultData(true);
                    }
                });
        HttpClient cancelOrderClient = NetService.createClient(getActivity(), cancelOrder);
        cancelOrderClient.start();
    }

    /* 删除订单 */
    private void setOrderDelete(String id) {
        ((BaseActivity) getActivity()).showLoadDialog(R.string.cancel_order_wait);
        HttpUserOPDRegisters.DeleteVVOrder deleteVVOrder = new HttpUserOPDRegisters.DeleteVVOrder(
                id,
                new HttpListener<String>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("deleteVVOrder", code, msg));
                        }
                        ((BaseActivity) getActivity()).dismissLoadDialog();
                        ToastUtils.showLong(getActivity(), msg);
                    }

                    @Override
                    public void onSuccess(String result) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("deleteVVOrder", result));
                        }
                        ((BaseActivity) getActivity()).dismissLoadDialog();
                        ToastUtils.showLong(getActivity(), R.string.delete_order_success);
                        getConsultData(true);
                    }
                });
        HttpClient deleteOrderClient = NetService.createClient(getActivity(), deleteVVOrder);
        deleteOrderClient.start();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void payedRefresh(EventApi.RefreshVVConsultList event) {
        getConsultData(true);
    }


    /****************************  Adapter  ***************************************/
    class VoiceVideoConsultAdapter extends CommonAdapter<UserOPDRegisters> {

        public VoiceVideoConsultAdapter(Context context, List<UserOPDRegisters> datas) {
            super(context, R.layout.item_consult, datas);
        }

        @Override
        public void convert(ViewHolder viewHolder, UserOPDRegisters data, final int position) {
            try {
                // 设置标题
                setTitle((TextView) viewHolder.findViewById(R.id.tv_title), data.mOPDType);
                //填充内容文字
                setBody(viewHolder, data);
                // 设置状态、价格和按钮
                setState(viewHolder, data);

                viewHolder.findViewById(R.id.ll_root).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserOPDRegisters data = mPageListViewHelper.getAdapter().getItem(position);
                        Intent intent = new Intent(getContext(), VVConsultDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("OPDRegisterID", data.mOPDRegisterID);
                        bundle.putString("name", data.mMember.mMemberName);
                        bundle.putInt("sex", data.mMember.mGender);
                        bundle.putInt("age", data.mMember.mAge);
                        if (data.mRoom != null) {
                            bundle.putString("channelID", data.mRoom.mChannelID);
                        } else {
                            bundle.putString("channelID", "");
                        }
                        bundle.putString("orderNo", data.mOrder.mOrderNo);
                        bundle.putInt("orderState", data.mOrder.mOrderState);
                        bundle.putInt("opDType", data.mOPDType);
                        bundle.putBoolean("isEvaluated", data.mOrder.mIsEvaluated);
                        bundle.putString("doctorID", data.mDoctor.mDoctorID);
                        bundle.putString("doctorName", data.mDoctor.mDoctorName);
                        bundle.putString("doctorTitle", data.mDoctor.mTitle);
                        bundle.putString("doctorPhotoUrl", data.mDoctor.mUser.mPhotoUrl);
                        bundle.putString("doctorDepartmentName", data.mDoctor.mDepartmentName);
                        bundle.putString("doctorHospitalName", data.mDoctor.mHospitalName);
                        bundle.putString("appointmentTime", PUtils.convertTimeToDay(data.mOPDDate) + "  " + data.mSchedule.mStartTime + "~" + data.mSchedule.mEndTime);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            } catch (Exception e) {
            }
        }

        /* 设置标题 */
        private void setTitle(TextView tv_title, int type) throws Exception {
            if (type == ConsultConstants.VoiceVideo.TYPE_VOICE) {
                tv_title.setText(R.string.voice_diagnose2);
            } else {
                tv_title.setText(R.string.video_diagnose2);
            }
        }


        /* 填充内容文字 */
        private void setBody(ViewHolder viewHolder, UserOPDRegisters data) throws Exception {

            //姓名
            ((TextView) viewHolder.findViewById(R.id.tv_value1)).setText(data.mMember.mMemberName);

            //医生
            ((TextView) viewHolder.findViewById(R.id.tv_lab2)).setText(R.string.inspection_doctor);
            ((TextView) viewHolder.findViewById(R.id.tv_value2)).setText(data.mDoctor.mDoctorName);


            //时间
            ((TextView) viewHolder.findViewById(R.id.tv_lab3)).setText(R.string.appointment_time);
            ((TextView) viewHolder.findViewById(R.id.tv_value3)).setText(
                    PUtils.convertTimeToDay(data.mOPDDate) + "  " + data.mSchedule.mStartTime + "~" + data.mSchedule.mEndTime);


            //病情描述
            ((TextView) viewHolder.findViewById(R.id.tv_value4)).setText(data.mConsultContent);
        }


        /* 设置状态、价格和按钮 */
        private void setState(ViewHolder viewHolder, final UserOPDRegisters data) throws Exception {

            if (data.mOrder == null) {
                return;
            }

            ((TextView) viewHolder.findViewById(R.id.tv_amount)).setText(CommonUtils.convertTowDecimalStr(data.mOrder.mTotalFee));

            TextView tv_state = (TextView) viewHolder.findViewById(R.id.tv_state);
            TextView tv_btn1 = (TextView) viewHolder.findViewById(R.id.tv_btn1);
            TextView tv_btn2 = (TextView) viewHolder.findViewById(R.id.tv_btn2);
            tv_btn1.setVisibility(View.GONE);
            tv_btn2.setVisibility(View.GONE);

            /* 状态 */
            int orderState = data.mOrder.mOrderState;

            switch (orderState) {
                case ConsultConstants.OrderState.ORDER_WAIT_PAY://未支付
                    tv_state.setText(R.string.wait_pay2);
                    tv_state.setTextColor(getResources().getColor(R.color.color_button_yellow));
                    tv_btn1.setVisibility(View.VISIBLE);
                    tv_btn1.setText(R.string.pay_now);
                    tv_btn1.setBackgroundResource(R.drawable.yellow_btn);
                    tv_btn1.setTextColor(getResources().getColor(R.color.app_yellow));
                    tv_btn2.setVisibility(View.GONE);
                    tv_btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                PayActivity.startPayActivity(getContext(), data.mOrder.mOrderNo, CommonUtils.convertTowDecimalStr(data.mOrder.mTotalFee), PayActivity.VIDEO_VOICE_CONSULT, true);
                            } catch (Exception e) {
                            }
                        }
                    });
                    break;
                case ConsultConstants.OrderState.ORDER_PAYED://已支付
                    if (data.mRoom != null && data.mRoom.mChannelID != null && !"0".equals(data.mRoom.mChannelID)) {
                        tv_state.setText(R.string.wait_diagnose);
                        tv_state.setTextColor(getResources().getColor(R.color.primary_color));
                        //按钮状态
                        tv_btn1.setVisibility(View.VISIBLE);
                        tv_btn1.setText(R.string.goto_diagnose);
                        tv_btn1.setBackgroundResource(R.drawable.btn_follow);
                        tv_btn1.setTextColor(getResources().getColor(R.color.primary_color));
                        tv_btn2.setVisibility(View.GONE);
                        tv_btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    ConsultBean bean = new ConsultBean();
                                    bean.mRoomID = data.mRoom.mChannelID;
                                    if (data.mOPDType == ConsultConstants.VoiceVideo.TYPE_VOICE) {
                                        bean.mCallType = RoomActivity1.CALLING_TYPE_VOICE;
                                    } else {
                                        bean.mCallType = RoomActivity1.CALLING_TYPE_VIDEO;
                                    }
                                    bean.mUserType = RoomActivity1.USER_TYPE_PATIENT;
                                    bean.mUserID = data.mDoctor.mDoctorID;
                                    bean.mUserFace = data.mDoctor.mUser.mPhotoUrl;
                                    bean.mUserName = data.mDoctor.mDoctorName;
                                    bean.mUserPhone = data.mDoctor.mDepartmentName;
                                    bean.mUserInfo = data.mDoctor.mHospitalName;
                                    AgoraApplication.loginAgora(getActivity(), bean);
                                } catch (Exception e) {
                                }
                            }
                        });
                    } else {
                        //未到预约时间
                        tv_state.setText(R.string.have_buyed);
                        tv_state.setTextColor(getResources().getColor(R.color.primary_color));
                        tv_btn1.setVisibility(View.GONE);
                        tv_btn2.setVisibility(View.GONE);
                    }
                    break;
                case ConsultConstants.OrderState.ORDER_FINISH://已完成
                    tv_state.setText(R.string.service_complete);
                    tv_state.setTextColor(getResources().getColor(R.color.third_text_color));
                    if (!TextUtils.isEmpty(data.mRecipeFileUrl)) {
                        //按钮状态
                        tv_btn1.setVisibility(View.VISIBLE);
                        tv_btn1.setText(R.string.show_diagnose);
                        tv_btn1.setBackgroundResource(R.drawable.btn_follow);
                        tv_btn1.setTextColor(getResources().getColor(R.color.primary_color));
                        tv_btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent intent;
                                    if (Build.VERSION.SDK_INT >= 21) {
                                        intent = new Intent(getContext(), PDFActivity.class);
                                    } else {
                                        intent = new Intent(getContext(), LegacyPDFActivity.class);
                                    }
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", data.mRecipeFileUrl);
                                    bundle.putString("id", CommonUtils.toMD5(data.mRecipeFileUrl));
                                    bundle.putInt("pageType", PayActivity.VIDEO_VOICE_CONSULT);
                                    bundle.putString("OPDRegisterID", data.mOPDRegisterID);
//                                    bundle.putSerializable("recipeFiles",data.mRecipeFiles);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                } catch (Exception e) {
                                }
                            }
                        });
                    }


                    if (data.mOrder != null && !data.mOrder.mIsEvaluated) {
                        tv_btn2.setVisibility(View.VISIBLE);
                        tv_btn2.setText(R.string.goto_evaluate);
                        tv_btn2.setBackgroundResource(R.drawable.btn_follow);
                        tv_btn2.setTextColor(getResources().getColor(R.color.primary_color));
                        tv_btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    EvaluateForDoctorActivity.startEvaluateForDoctorActivity(getContext(),data.mOPDRegisterID ,data.mDoctor,false);

//                                    EvaluateForDoctorActivity.startEvaluateForDoctorActivity(getContext(), data.mOPDRegisterID,
//                                            data.mDoctor.mDoctorName, data.mDoctor.mTitle, data.mDoctor.mHospitalName, data.mDoctor.mDepartmentName, false);
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

            setDeleteBtn(data, viewHolder);
        }


        //设置取消按钮
        private void setDeleteBtn(final UserOPDRegisters data, ViewHolder viewHolder) {
            TextView tv_delete = (TextView) viewHolder.findViewById(R.id.tv_delete);
            if (data.mDeletable == true) {
                tv_delete.setText(R.string.delete_order);
                tv_delete.setVisibility(View.VISIBLE);
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlterDialogView.Builder builder = new AlterDialogView.Builder(getActivity());
                        builder.setTitle(R.string.string_dialog_title);
                        builder.setMessage(R.string.delete_order_ask);
                        builder.setNegativeButton(R.string.error_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton(R.string.error_confirm,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        setOrderDelete(data.mOPDRegisterID);
                                        arg0.dismiss();
                                    }
                                });
                        builder.create().show();


                    }
                });
            } else if (data.mCancelable == true) {
                tv_delete.setText(R.string.cancel_order);
                tv_delete.setVisibility(View.VISIBLE);
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (data.mOrder != null) {
                            AlterDialogView.Builder builder = new AlterDialogView.Builder(getActivity());
                            builder.setTitle(R.string.string_dialog_title);
                            builder.setMessage(R.string.cancel_order_ask);
                            builder.setNegativeButton(R.string.error_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setPositiveButton(R.string.error_confirm,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {
                                            setOrderCancel(data.mOrder.mOrderNo);
                                            arg0.dismiss();
                                        }
                                    });
                            builder.create().show();



                        }
                    }
                });
            } else {
                tv_delete.setVisibility(View.GONE);
            }
        }


    }
}
