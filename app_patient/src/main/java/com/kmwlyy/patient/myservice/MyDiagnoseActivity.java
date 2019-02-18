package com.kmwlyy.patient.myservice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import butterknife.BindView;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.login.LoginActivity;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.PrescriptionOrders;
import com.kmwlyy.patient.helper.net.event.HttpUserOPDRegisters;
import com.kmwlyy.patient.helper.net.event.HttpUserRecipeOrders;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.winson.ui.widget.AlterDialogView;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.EmptyViewUtils;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;
import java.util.ArrayList;
import java.util.List;

/**我的处方
 * Created by Winson on 2016/8/16.
 */
public class MyDiagnoseActivity extends BaseActivity implements PageListView.OnPageLoadListener {

    private static final String TAG = MyDiagnoseActivity.class.getSimpleName();


    @BindView(R.id.doctor_page_listview)
    PageListView mDoctorPageListview;
    @BindView(R.id.tv_center)
    TextView tv_center;

    private HttpClient mHttpClient;
    private PageListViewHelper<PrescriptionOrders.ListItem> mPageListViewHelper;
    ArrayList<PrescriptionOrders.ListItem> datas = new ArrayList<>();
    private ViewGroup mRoot;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_diagnose);
        butterknife.ButterKnife.bind(this);

        tv_center.setText(R.string.my_diagnose);

        mPageListViewHelper = new PageListViewHelper<>(mDoctorPageListview, new DoctorListAdapter(MyDiagnoseActivity.this, datas));
        int diagnoseListPadding = getResources().getDimensionPixelSize(R.dimen.list_padding);
        mPageListViewHelper.getListView().setDivider(new ColorDrawable(getResources().getColor(R.color.app_background)));
        mPageListViewHelper.getListView().setDividerHeight(diagnoseListPadding);
        mPageListViewHelper.getListView().setPadding(0, diagnoseListPadding, 0, diagnoseListPadding);
        mPageListViewHelper.getListView().setClipToPadding(false);
        mPageListViewHelper.setOnPageLoadListener(this);
        mPageListViewHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent details = new Intent();
                details.setClass(MyDiagnoseActivity.this, DiagnoseDetailsActivity.class);
                details.putExtra("orderNo", datas.get(position).mOrder.mOrderNo);
                details.putExtra("patientName", datas.get(position).mMember.mMemberName);
                details.putExtra("patientAge", datas.get(position).mMember.mAge);
                details.putExtra("patientSex", datas.get(position).mMember.mGender);
                details.putExtra("doctorName", datas.get(position).mDoctor.mDoctorName);
                details.putExtra("hospitalName", datas.get(position).mDoctor.mHospitalName);
                details.putExtra("departmentName", datas.get(position).mDoctor.mDepartmentName);
                startActivity(details);
            }
        });

        getRecipeOrdersList(true);
    }



    @Override
    public void onResume() {
        super.onResume();
        if (!PUtils.checkHaveUser(this, false)) {
            EmptyViewUtils.showEmptyView(mRoot, getResources().getString(R.string.user_not_login_click_login), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.startActivity(MyDiagnoseActivity.this, LoginActivity.class);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NetService.closeClient(mHttpClient);
    }

    @Override
    public void onRefreshData() {
        getRecipeOrdersList(true);
    }

    @Override
    public void onLoadPageData() {
        getRecipeOrdersList(false);
    }

    class DoctorListAdapter extends CommonAdapter<PrescriptionOrders.ListItem> {

        public DoctorListAdapter(Context context, List<PrescriptionOrders.ListItem> datas) {
            super(context, R.layout.my_diagnose_list_item, datas);
        }

        @Override
        public void convert(ViewHolder viewHolder, PrescriptionOrders.ListItem recipeFile, final int position) {
            ((TextView) viewHolder.findViewById(R.id.tv_diagnose_title)).setText(getResources().getString(R.string.order_no) + recipeFile.mOrder.mOrderNo);
            ((TextView) viewHolder.findViewById(R.id.tv_diagnose_time)).setText(recipeFile.mOrder.mOrderTime.substring(0, 10));
            final int size = recipeFile.mRecipeFiles.size();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < size; i++) {
                sb.append("• ");
                if (i == size) {
                    sb.append(recipeFile.mRecipeFiles.get(i).mRecipeName);
                } else {
                    sb.append(recipeFile.mRecipeFiles.get(i).mRecipeName + "\n");
                }
            }
            String sbStr = sb.toString();
            if (sbStr.length() > 1){
                sbStr = sbStr.substring(0,sbStr.length()-1);
            }


            final String use = sbStr.toString();
            ((TextView) viewHolder.findViewById(R.id.tv_diagnose_use)).setText(use);
            ((TextView) viewHolder.findViewById(R.id.tv_drug_number)).setText(String.valueOf(size));
            ((TextView) viewHolder.findViewById(R.id.tv_drug_amount)).setText("￥" + CommonUtils.convertTowDecimalStr(recipeFile.mOrder.mTotalFee));
            final int state = datas.get(position).mOrder.mOrderState;
            TextView btn_buy = ((TextView) viewHolder.findViewById(R.id.btn_buy));
            if (state == 0 || state == -1) {
//                ((TextView) viewHolder.findViewById(R.id.btn_buy)).setText(getResources().getString(R.string.wait_pay));
//                ((TextView) viewHolder.findViewById(R.id.btn_buy)).setEnabled(true);
//                ((TextView) viewHolder.findViewById(R.id.btn_buy)).setBackgroundResource(R.drawable.yellow_solid_btn);

                btn_buy.setEnabled(true);
                btn_buy.setText(R.string.wait_pay);
                btn_buy.setTextColor(getResources().getColor(R.color.color_button_yellow));
                btn_buy.setBackgroundResource(R.drawable.yellow_btn);
                btn_buy.setTextColor(getResources().getColor(R.color.app_yellow));
            } else if (state == 1) {
//                ((TextView) viewHolder.findViewById(R.id.btn_buy)).setText(getResources().getString(R.string.delivery_ing));
//                ((TextView) viewHolder.findViewById(R.id.btn_buy)).setEnabled(true);
//                ((TextView) viewHolder.findViewById(R.id.btn_buy)).setBackgroundResource(R.drawable.app_btn);

//                btn_buy.setEnabled(true);btn_buy.setText(R.string.delivery_ing);
                btn_buy.setEnabled(true);btn_buy.setText("查看物流");
                btn_buy.setTextColor(getResources().getColor(R.color.primary_color));
                btn_buy.setBackgroundResource(R.drawable.btn_follow);
                btn_buy.setTextColor(getResources().getColor(R.color.primary_color));

            } else if(state == 2){
//                ((TextView) viewHolder.findViewById(R.id.btn_buy)).setText(getResources().getString(R.string.service_complete));
//                ((TextView) viewHolder.findViewById(R.id.btn_buy)).setEnabled(true);
//                ((TextView) viewHolder.findViewById(R.id.btn_buy)).setBackgroundResource(R.drawable.app_btn);

                btn_buy.setEnabled(true);
                btn_buy.setText(R.string.service_complete);
                btn_buy.setTextColor(getResources().getColor(R.color.third_text_color));
                btn_buy.setBackgroundResource(R.drawable.btn_unfollow);
                btn_buy.setTextColor(getResources().getColor(R.color.third_text_color));

            }else {
//                ((TextView) viewHolder.findViewById(R.id.btn_buy)).setText(getResources().getString(R.string.pay_canceled));
//                ((TextView) viewHolder.findViewById(R.id.btn_buy)).setEnabled(false);
//                ((TextView) viewHolder.findViewById(R.id.btn_buy)).setBackgroundResource(R.drawable.unable_btn);
                btn_buy.setEnabled(false);
                btn_buy.setText(R.string.pay_canceled);
                btn_buy.setTextColor(getResources().getColor(R.color.third_text_color));
                btn_buy.setBackgroundResource(R.drawable.btn_unfollow);
                btn_buy.setTextColor(getResources().getColor(R.color.third_text_color));
            }
            ((TextView) viewHolder.findViewById(R.id.btn_buy)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (state == 0  || state == -1) {
                        //支付页面
                        Intent pay = new Intent();
                        pay.putExtra("orderNo", datas.get(position).mOrder.mOrderNo);
                        pay.putExtra("totalFee", datas.get(position).mOrder.mTotalFee);
                        pay.putExtra("info", use);
                        pay.putExtra("num", size);
                        pay.putExtra("time", datas.get(position).mOrder.mOrderTime.substring(0, 10));
                        pay.setClass(MyDiagnoseActivity.this, PrescriptionPayActivity.class);
                        startActivity(pay);
                    } else if (state == 1) {
                        //配送中页面
                        Intent delivery = new Intent();
                        delivery.setClass(MyDiagnoseActivity.this, DeliveryActivity.class);
                        delivery.putExtra("patientName", datas.get(position).mMember.mMemberName);
                        delivery.putExtra("patientAge", datas.get(position).mMember.mAge);
                        delivery.putExtra("patientSex", datas.get(position).mMember.mGender);
                        delivery.putExtra("patientPhone", datas.get(position).mMember.mMobile);
                        delivery.putExtra("payType", datas.get(position).mOrder.mPayType);
                        delivery.putExtra("tradeTime", datas.get(position).mOrder.mOrderTime);
                        delivery.putExtra("totalFee", datas.get(position).mOrder.mTotalFee);
                        delivery.putExtra("info", use);
                        delivery.putExtra("num", size);
                        delivery.putExtra("time", datas.get(position).mOrder.mOrderTime.substring(0, 10));
                        delivery.putExtra("logisticNo", datas.get(position).mOrder.mLogisticNo);
                        delivery.putExtra("logisticState",datas.get(position).mOrder.mLogisticState);
                        startActivity(delivery);
                    }
                }
            });

            TextView btn_delete = (TextView) viewHolder.findViewById(R.id.btn_delete);
            if (recipeFile.mDeletable == true) {
                btn_delete.setText(R.string.delete_order);
                btn_delete.setVisibility(View.VISIBLE);
                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlterDialogView.Builder builder = new AlterDialogView.Builder(MyDiagnoseActivity.this);
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
                                        setRecipeFileOrderDelete(datas.get(position).mRecipeOrderID);
                                        arg0.dismiss();
                                    }
                                });
                        builder.create().show();


                    }
                });
            } else if (recipeFile.mCancelable == true) {
                btn_delete.setText(R.string.cancel_order);
                btn_delete.setVisibility(View.VISIBLE);
                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (datas.get(position).mOrder != null) {
                            AlterDialogView.Builder builder = new AlterDialogView.Builder(MyDiagnoseActivity.this);
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
                                            setOrderCancel(datas.get(position).mOrder.mOrderNo);
                                            arg0.dismiss();
                                        }
                                    });
                            builder.create().show();



                        }
                    }
                });
            } else {
                btn_delete.setVisibility(View.GONE);
            }
        }
    }

    private void getRecipeOrdersList(final boolean refresh) {
        mPageListViewHelper.setRefreshing(refresh);
        HttpUserRecipeOrders.GetList getList = new HttpUserRecipeOrders.GetList(null, null, null, refresh ? 1 : mPageListViewHelper.getPageIndex(), mPageListViewHelper.getPageSize(), new HttpListener<ArrayList<PrescriptionOrders.ListItem>>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("getRecipeOrdersList", code, msg));
                }
                mPageListViewHelper.setRefreshing(false);
                EmptyViewUtils.showErrorView((ViewGroup) mRoot, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getRecipeOrdersList(true);
                    }
                });
            }

            @Override
            public void onSuccess(ArrayList<PrescriptionOrders.ListItem> recipeFiles) {
                Log.d(TAG, DebugUtils.successFormat("getDoctorList", PUtils.toJson(recipeFiles)));
                mPageListViewHelper.setRefreshing(false);
                if (refresh) {
                    mPageListViewHelper.refreshData(recipeFiles);
                    datas = recipeFiles;
                } else {
                    mPageListViewHelper.addPageData(recipeFiles);
                    datas.addAll(recipeFiles);
                }
                EmptyViewUtils.removeAllView((ViewGroup) mRoot);
            }
        });

        mHttpClient = NetService.createClient(MyDiagnoseActivity.this, getList);
        mHttpClient.start();
    }

    /* 取消订单 */
    private void setOrderCancel(String orderNo) {
        showLoadDialog(R.string.cancel_order_wait);
        HttpUserOPDRegisters.CancelOrder cancelOrder = new HttpUserOPDRegisters.CancelOrder(
                orderNo,
                new HttpListener<String>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("cancelOrder", code, msg));
                        }
                        dismissLoadDialog();
                        ToastUtils.showLong(MyDiagnoseActivity.this, msg);
                    }

                    @Override
                    public void onSuccess(String result) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("cancelOrder", result));
                        }
                        dismissLoadDialog();
                        ToastUtils.showLong(MyDiagnoseActivity.this, R.string.cancel_order_success);
                        getRecipeOrdersList(true);
                    }
                });
        HttpClient cancelOrderClient = NetService.createClient(MyDiagnoseActivity.this, cancelOrder);
        cancelOrderClient.start();
    }


    /* 删除处方订单 */
    private void setRecipeFileOrderDelete(String id) {
       showLoadDialog(R.string.delete_order_wait);
        HttpUserRecipeOrders.DeleteRecipeFileOrder deleteRecipeFileOrder = new  HttpUserRecipeOrders.DeleteRecipeFileOrder(
                id,
                new HttpListener<String>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("deleteRecipeFileOrder", code, msg));
                        }
                        dismissLoadDialog();
                        ToastUtils.showLong(MyDiagnoseActivity.this, msg);
                    }

                    @Override
                    public void onSuccess(String result) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("deleteRecipeFileOrder", result));
                        }
                        dismissLoadDialog();
                        ToastUtils.showLong(MyDiagnoseActivity.this, R.string.delete_order_success);
                        getRecipeOrdersList(true);
                    }
                });
        HttpClient deleteOrderClient = NetService.createClient(MyDiagnoseActivity.this, deleteRecipeFileOrder);
        deleteOrderClient.start();
    }

}
