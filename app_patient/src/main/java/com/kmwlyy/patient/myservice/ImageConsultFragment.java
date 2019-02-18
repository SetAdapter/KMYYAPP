package com.kmwlyy.patient.myservice;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.imchat.TimApplication;
import com.kmwlyy.login.LoginActivity;
import com.kmwlyy.login.MessageApi;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.evaluate.EvaluateForDoctorActivity;
import com.kmwlyy.patient.helper.ConsultConstants;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.base.BaseFragment;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.UserConsult;
import com.kmwlyy.patient.helper.net.event.HttpUserConsults;
import com.kmwlyy.patient.helper.net.event.HttpUserOPDRegisters;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.pay.PayActivity;
import com.kmwlyy.usermember.Event;
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

/**
 * @Description描述: 图文咨询列表
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/20
 */
public class ImageConsultFragment extends BaseFragment implements PageListView.OnPageLoadListener {
    private static final String TAG = ImageConsultFragment.class.getSimpleName();


    @BindView(R.id.doctor_page_listview)
    PageListView listView;

    private PageListViewHelper<UserConsult> mPageListViewHelper;
    private ViewGroup mRoot;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = (ViewGroup) inflater.inflate(R.layout.my_diagnose, container, false);
        ButterKnife.bind(this, mRoot);
        mPageListViewHelper = new PageListViewHelper<>(listView, new ImageConsultFragment.ImageConsultAdapter(getActivity(), null));
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
        HttpUserConsults.GetList getMyConsultList = new HttpUserConsults.GetList(
                refresh ? 1 : mPageListViewHelper.getPageIndex(),
                mPageListViewHelper.getPageSize(), null, new HttpListener<ArrayList<UserConsult>>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("getMyConsultList", code, msg));
                }
                EmptyViewUtils.removeAllView(mRoot);
                mPageListViewHelper.setRefreshing(false);
            }

            @Override
            public void onSuccess(ArrayList<UserConsult> userConsults) {
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

        HttpClient getMyConsultListClient = NetService.createClient(getActivity(), getMyConsultList);
        getMyConsultListClient.start();
    }

    /* 取消订单 */
    private void setOrderCancel(String orderNo) {
        ((BaseActivity)getActivity()).showLoadDialog(R.string.cancel_order_wait);
        HttpUserOPDRegisters.CancelOrder cancelOrder = new HttpUserOPDRegisters.CancelOrder(
                orderNo,
                new HttpListener<String>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("cancelOrder", code, msg));
                        }
                        ((BaseActivity)getActivity()).dismissLoadDialog();
                        ToastUtils.showLong(getActivity(),msg);
                    }

                    @Override
                    public void onSuccess(String result) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("cancelOrder", result));
                        }
                        ((BaseActivity)getActivity()).dismissLoadDialog();
                        ToastUtils.showLong(getActivity(),R.string.cancel_order_success);
                        getConsultData(true);
                    }
                });
        HttpClient cancelOrderClient = NetService.createClient(getActivity(), cancelOrder);
        cancelOrderClient.start();
    }
    /* 删除订单 */
    private void setOrderDelete(String id) {
        ((BaseActivity)getActivity()).showLoadDialog(R.string.cancel_order_wait);
        HttpUserOPDRegisters.DeleteImageOrder deleteImageOrder = new HttpUserOPDRegisters.DeleteImageOrder(
                id,
                new HttpListener<String>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("deleteImageOrder", code, msg));
                        }
                        ((BaseActivity)getActivity()).dismissLoadDialog();
                        ToastUtils.showLong(getActivity(),msg);
                    }

                    @Override
                    public void onSuccess(String result) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("deleteImageOrder", result));
                        }
                        ((BaseActivity)getActivity()).dismissLoadDialog();
                        ToastUtils.showLong(getActivity(),R.string.delete_order_success);
                        getConsultData(true);
                    }
                });
        HttpClient deleteOrderClient = NetService.createClient(getActivity(), deleteImageOrder);
        deleteOrderClient.start();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void payedRefresh(EventApi.RefreshImageConsultList event) {
        getConsultData(true);
    }


    /****************************  Adapter  ***************************************/
    class ImageConsultAdapter extends CommonAdapter<UserConsult> {

        public ImageConsultAdapter(Context context, List<UserConsult> datas) {
            super(context, R.layout.item_consult, datas);
        }

        @Override
        public void convert(ViewHolder viewHolder, UserConsult userConsult, final int position) {
            try {
                // 设置标题
                setTitle((TextView) viewHolder.findViewById(R.id.tv_title), userConsult.mConsultType);
                // 设置状态
                setState((TextView) viewHolder.findViewById(R.id.tv_state), userConsult);
                //填充内容文字
                setBody(viewHolder, userConsult);
                //设置底部价格和按钮
                setBottom(viewHolder, userConsult);

                viewHolder.findViewById(R.id.ll_root).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserConsult data =  mPageListViewHelper.getAdapter().getItem(position);
                        boolean hiddenInput = data.mConsultState == 1
                                || data.mConsultState == 5
                                || data.mRoom == null
                                || data.mRoom.mChannelID == null
                                || data.mRoom.mChannelID.equals("0");
//data.mConsultState == 0
//                        ||
                        TimApplication.enterTimchat(getActivity(), data.mRoom.mChannelID, data.mDoctor.mDoctorName, !hiddenInput);
                    }
                });
            }catch (Exception e){}
        }

         /* 设置标题 */
        private void setTitle(TextView tv_title, int type) throws Exception{
                switch (type) {
                    case ConsultConstants.PAY://付费
                        tv_title.setText(R.string.string_evaluate_record1);
                        break;
                    case ConsultConstants.FREE://免费咨询
                        tv_title.setText(R.string.free_consult);
                        break;
                    case ConsultConstants.DUTY://义诊咨询
                        tv_title.setText(R.string.duty_consult);
                        break;
                    case ConsultConstants.MEAL://套餐
                        tv_title.setText(R.string.string_evaluate_record6);
                        break;
                    case ConsultConstants.MEMBER://会员
                        tv_title.setText(R.string.string_member);
                        break;
                    case ConsultConstants.FAMILY://家庭医生
                        tv_title.setText(R.string.home_doctor);
                        break;
                    default:
                        tv_title.setText(R.string.free_consult);
                        break;
                }
        }

        /* 设置状态 */
        private void setState(TextView tv_state, UserConsult userConsult) throws Exception{
            /* 状态 */
            int orderState = userConsult.mOrder == null ? 0 : userConsult.mOrder.mOrderState;
            switch (orderState) {
                case ConsultConstants.OrderState.ORDER_WAIT_PAY:
                    tv_state.setText(R.string.wait_pay2);
                    tv_state.setTextColor(getResources().getColor(R.color.color_button_yellow));
                    break;
                case ConsultConstants.OrderState.ORDER_PAYED:
                    switch (userConsult.mConsultState) {
                        case ConsultConstants.STATE_WAIT_ASSIGNED:
                        case ConsultConstants.STATE_WAIT_FILTER:
                        case ConsultConstants.STATE_WAIT_REPLY:
                            tv_state.setText(R.string.not_reply);
                            tv_state.setTextColor(getResources().getColor(R.color.color_button_yellow));
                            break;
                        case ConsultConstants.STATE_ALREADY_REPLY:
                            tv_state.setText(R.string.already_reply);
                            tv_state.setTextColor(getResources().getColor(R.color.primary_color));
                            break;
                        case ConsultConstants.STATE_FINISH:
                            tv_state.setText(R.string.service_complete);
                            tv_state.setTextColor(getResources().getColor(R.color.primary_color));
                            break;
                    }
                    break;
                case ConsultConstants.OrderState.ORDER_FINISH:
                    tv_state.setText(R.string.service_complete);
                    tv_state.setTextColor(getResources().getColor(R.color.third_text_color));
                    break;
                case ConsultConstants.OrderState.ORDER_CANCELED:
                    tv_state.setText(R.string.pay_canceled);
                    tv_state.setTextColor(getResources().getColor(R.color.third_text_color));
                    break;
                case ConsultConstants.OrderState.ORDER_APPLY_REFUNDED:
                    tv_state.setText(R.string.pay_apply_refund);
                    tv_state.setTextColor(getResources().getColor(R.color.third_text_color));
                    break;
                case ConsultConstants.OrderState.ORDER_REFUSE_REFUNDED:
                    tv_state.setText(R.string.pay_refuse_refund);
                    tv_state.setTextColor(getResources().getColor(R.color.third_text_color));
                    break;
                case ConsultConstants.OrderState.ORDER_REFUNDED:
                    tv_state.setText(R.string.pay_refunded);
                    tv_state.setTextColor(getResources().getColor(R.color.third_text_color));
                    break;
            }
        }

        /* 填充内容文字 */
        private void setBody(ViewHolder viewHolder,UserConsult userConsult) throws Exception{

            //姓名
            ((TextView)viewHolder.findViewById(R.id.tv_value1)).setText(userConsult.mUserMember.mMemberName);

            //时间
            ((TextView)viewHolder.findViewById(R.id.tv_lab2)).setText(R.string.consult_time);
            ((TextView)viewHolder.findViewById(R.id.tv_value2)).setText(PUtils.convertTimeSSS(userConsult.mConsultTime));

            //看诊医生。免费咨询不显示看诊医生
            if (userConsult.mConsultType == ConsultConstants.FREE){
                (viewHolder.findViewById(R.id.ll3)).setVisibility(View.GONE);
            }
            else{
                (viewHolder.findViewById(R.id.ll3)).setVisibility(View.VISIBLE);
                ((TextView)viewHolder.findViewById(R.id.tv_lab3)).setText(R.string.inspection_doctor);
                ((TextView)viewHolder.findViewById(R.id.tv_value3)).setText(userConsult.mDoctor.mDoctorName);

            }

            //病情描述
            ((TextView)viewHolder.findViewById(R.id.tv_value4)).setText(userConsult.mConsultContent);
        }

        /* 设置底部价格和按钮 */
        private void setBottom(ViewHolder viewHolder, final UserConsult userConsult) throws Exception{

            //义诊和免费咨询没有价格和底部按钮
//            if ((userConsult.mConsultType == ConsultConstants.FREE)
//                    || (userConsult.mConsultType == ConsultConstants.DUTY)){
//
//                (viewHolder.findViewById(R.id.root_price_btn)).setVisibility(View.GONE);
//                (viewHolder.findViewById(R.id.line_price_btn)).setVisibility(View.GONE);
//                return;
//            }

            //设置价格
            ((TextView)viewHolder.findViewById(R.id.tv_amount)).setText(CommonUtils.convertTowDecimalStr(userConsult.mOrder.mTotalFee));


            //图文咨询不需要第二个按钮
            viewHolder.findViewById(R.id.tv_btn2).setVisibility(View.GONE);

            //设置第一个按钮
            int orderState = userConsult.mOrder == null ? 0 : userConsult.mOrder.mOrderState;
            if (orderState == ConsultConstants.OrderState.ORDER_FINISH
                    || (orderState == ConsultConstants.OrderState.ORDER_PAYED && userConsult.mConsultState == ConsultConstants.STATE_FINISH))
            {
                //订单已完成，判断支付状态
                setEvaluateBtn(userConsult,viewHolder);
            }
            else if(orderState == ConsultConstants.OrderState.ORDER_WAIT_PAY){
                //订单待支付，显示支付按钮
                setPayBtn(userConsult.mOrder.mOrderNo,CommonUtils.convertTowDecimalStr(userConsult.mOrder.mTotalFee),  viewHolder);
            }
            else{
                //已支付，已评价，隐藏按钮
                viewHolder.findViewById(R.id.tv_btn1).setVisibility(View.GONE);
//                (viewHolder.findViewById(R.id.root_price_btn)).setVisibility(View.GONE);
//                (viewHolder.findViewById(R.id.line_price_btn)).setVisibility(View.GONE);
            }

            setDeleteBtn(userConsult,viewHolder);
        }

        //设置评价按钮
        private void setEvaluateBtn(final UserConsult userConsult,ViewHolder viewHolder){
            //图文咨询只需要一个按钮
            TextView tv_btn1 = (TextView) viewHolder.findViewById(R.id.tv_btn1);
            if (userConsult.mOrder !=null && !userConsult.mOrder.mIsEvaluated)
            {
                tv_btn1.setVisibility(View.VISIBLE);
                tv_btn1.setText(R.string.goto_evaluate);
                tv_btn1.setBackgroundResource(R.drawable.btn_follow);
                tv_btn1.setTextColor(getResources().getColor(R.color.primary_color));
                tv_btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EvaluateForDoctorActivity.startEvaluateForDoctorActivity(getContext(),userConsult.mUserConsultID ,userConsult.mDoctor,true);
                    }
                });
            }
            else{
                tv_btn1.setVisibility(View.GONE);
            }
        }

        //设置支付按钮
        private void setPayBtn(final String orderNo,final String priceStr,ViewHolder viewHolder){
            TextView tv_btn1 = (TextView) viewHolder.findViewById(R.id.tv_btn1);
            tv_btn1.setVisibility(View.VISIBLE);
            tv_btn1.setText(R.string.pay_now);
            tv_btn1.setBackgroundResource(R.drawable.yellow_btn);
            tv_btn1.setTextColor(getResources().getColor(R.color.app_yellow));
            tv_btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PayActivity.startPayActivity(getContext(), orderNo, priceStr,PayActivity.IMAGE_CONSULT,true);
                }
            });

        }

        //设置取消按钮
        private void setDeleteBtn(final UserConsult userConsult,ViewHolder viewHolder){
            TextView tv_delete = (TextView) viewHolder.findViewById(R.id.tv_delete);
            if (userConsult.mDeletable == true){
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
                                        setOrderDelete(userConsult.mUserConsultID);
                                        arg0.dismiss();
                                    }
                                });
                        builder.create().show();

                    }
                });
            }else if (userConsult.mCancelable == true){
                tv_delete.setText(R.string.cancel_order);
                tv_delete.setVisibility(View.VISIBLE);
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (userConsult.mOrder!=null) {

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
                                            setOrderCancel(userConsult.mOrder.mOrderNo);
                                            arg0.dismiss();
                                        }
                                    });
                            builder.create().show();



                        }
                    }
                });
            }else{
                tv_delete.setVisibility(View.GONE);
            }
        }


    }
}
