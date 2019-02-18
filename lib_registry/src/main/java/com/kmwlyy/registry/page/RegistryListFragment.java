package com.kmwlyy.registry.page;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.base.BaseFragment;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.registry.R;
import com.kmwlyy.registry.R2;
import com.kmwlyy.registry.bean.Contants;
import com.kmwlyy.registry.net.NetBean;
import com.kmwlyy.registry.net.NetEvent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.AlterDialogView;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016-8-15.
 */
public class RegistryListFragment extends BaseFragment implements PageListView.OnPageLoadListener{

    @BindView(R2.id.pageListView)
    PageListView pageListView;

    private PageListViewHelper<NetBean.Order> mPageListViewHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_registrylist;
    }

    @Override
    protected void afterBindView() {
        mCenterText.setText(getResources().getString(R.string.registration_form));
        mPageListViewHelper = new PageListViewHelper<>(pageListView, new MListAdapter(getActivity(), null));
        int diagnoseListPadding = getResources().getDimensionPixelSize(R.dimen.list_padding);
        mPageListViewHelper.getListView().setPadding(0, 0, 0, diagnoseListPadding);
        mPageListViewHelper.getListView().setClipToPadding(false);
        mPageListViewHelper.setOnPageLoadListener(this);
        getOrderList(true);

    }

    /*******************************************************/
    private void getOrderList(final boolean refresh) {//获取排班列表
        mPageListViewHelper.setRefreshing(refresh);
        String userid = BaseApplication.instance.getUserData().mUserId;
        NetEvent.GetFamilyYuyue event = new NetEvent.GetFamilyYuyue(userid, "",
                refresh ? 1 : mPageListViewHelper.getPageIndex(),
                mPageListViewHelper.getPageSize()
        );//"6621F39A9CD14B17BE795D5B3C4DE321"
        event.setHttpListener(new HttpListener<List<NetBean.Order>>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(getContext(), msg);
                mPageListViewHelper.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<NetBean.Order> orders) {
                mPageListViewHelper.setRefreshing(false);
                if (refresh) {
                    mPageListViewHelper.refreshData(orders);
                } else {
                    mPageListViewHelper.addPageData(orders);
                }
            }
        });
        new HttpClient(getContext(), event).start();
    }

    private void cancelRegistry(NetBean.Order order) {
        String userid = BaseApplication.instance.getUserData().mUserId;//"6621F39A9CD14B17BE795D5B3C4DE321";
        NetEvent.CancelRegister event = new NetEvent.CancelRegister(userid, order.getOrderID(), getResources().getString(R.string.r_patient));
        event.setHttpListener(new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(getContext(), msg);
            }

            @Override
            public void onSuccess(String s) {
                ToastUtils.showShort(getContext(), getResources().getString(R.string.cancel_success));
                getOrderList(true);
            }
        });
        new HttpClient(getContext(), event).start();
    }

    @Override
    public void onRefreshData() {
        getOrderList(true);
    }

    @Override
    public void onLoadPageData() {
        getOrderList(false);
    }

    /*******************************************************/


    class MListAdapter extends CommonAdapter<NetBean.Order> {

        public MListAdapter(Context context, List<NetBean.Order> datas) {
            super(context, R.layout.item_registry, datas);
        }

        @Override
        public void convert(ViewHolder viewHolder, NetBean.Order data, final int position) {
            final NetBean.Order item = data;

            TextView tvDoctor = (TextView)viewHolder.findViewById(R.id.tv_doctor);
            TextView tvHospital = (TextView)viewHolder.findViewById(R.id.tv_hospital);
            TextView tvName = (TextView)viewHolder.findViewById(R.id.tv_name);
            TextView tvTime = (TextView)viewHolder.findViewById(R.id.tv_time);
            TextView tvPassword = (TextView)viewHolder.findViewById(R.id.tv_password);
            TextView tvCharge = (TextView)viewHolder.findViewById(R.id.tv_charge);
            ImageView tvFace = (ImageView)viewHolder.findViewById(R.id.tv_face);
            Button btnCancel = (Button)viewHolder.findViewById(R.id.btn_cancel);
            RelativeLayout layoutItem = (RelativeLayout)viewHolder.findViewById(R.id.layout_item);

            String docname = TextUtils.isEmpty(item.getDoctorName()) ? getResources().getString(R.string.r_doctor) : item.getDoctorName();
            tvDoctor.setText(getSpannableText(docname + " " + item.getLevel_Name(), 0, docname.length(), Color.BLACK));
            tvHospital.setText(item.getUName() + " " + item.getDepName());
            tvName.setText(getSpannableText(getResources().getString(R.string.r_member) + item.getMemberName(), 0, 3));
            tvTime.setText(getSpannableText(getResources().getString(R.string.r_duration) + item.getToDate() + " " + item.getBeginTime() + "-" + item.getEndTime(), 0, 4));
            if (TextUtils.isEmpty(item.getHisTakeNo())) {
                tvPassword.setVisibility(View.GONE);
            } else {
                tvPassword.setText(getSpannableText(getResources().getString(R.string.r_take_no_pwd) + item.getHisTakeNo(), 0, 4));
            }
            String charge = TextUtils.isEmpty(item.getGuoHaoAMT()) ? "0" : item.getGuoHaoAMT();
            tvCharge.setText(getSpannableText(getResources().getString(R.string.register_expenses) + charge + getResources().getString(R.string.money), 4, charge.length() + 4, Color.RED));
            ImageLoader.getInstance().displayImage(item.getDoctor_Image(),tvFace, Contants.getCircleDisplayOptions());
            btnCancel.setText(getOrderStatus(getActivity(), item.getOrderState()));
            if (item.getOrderState() == 1) {
                btnCancel.setEnabled(true);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlterDialogView.Builder builder = new AlterDialogView.Builder(mContext);
                        builder.setTitle(getResources().getString(R.string.system_notify));
                        builder.setMessage(getResources().getString(R.string.signal_is_less));
                        builder.setNegativeButton(getResources().getString(R.string.r_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton(getResources().getString(R.string.r_confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelRegistry(item);
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                });
            } else {
                btnCancel.setEnabled(false);
            }
        }



    }

    private SpannableStringBuilder getSpannableText(String str, int start, int end) {
        return getSpannableText(str, start, end, Color.GRAY);
    }

    private SpannableStringBuilder getSpannableText(String str, int start, int end, int clolor) {
        ForegroundColorSpan fcs = new ForegroundColorSpan(clolor);
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(fcs, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return style;
    }

    private String getOrderStatus(Context context, int status) {
        //订单状态（-1 已取消、0 停诊、1 预约成功、2 已支付、3 爽约、4 取号、5 已就诊）
        switch (status) {
            case -1:
                return context.getResources().getString(R.string.r_have_cancel);
            case 0:
                return context.getResources().getString(R.string.r_have_stop);
            case 1:
                return context.getResources().getString(R.string.r_cancel_register);
            case 2:
                return context.getResources().getString(R.string.r_have_payed);
            case 3:
                return context.getResources().getString(R.string.r_have_break_appointment);
            case 4:
                return context.getResources().getString(R.string.r_have_take_no);
            case 5:
                return context.getResources().getString(R.string.r_have_use);
            case 10:
                return context.getResources().getString(R.string.r_have_expired);
            default:
                return context.getResources().getString(R.string.r_have_close);
        }
    }
}
