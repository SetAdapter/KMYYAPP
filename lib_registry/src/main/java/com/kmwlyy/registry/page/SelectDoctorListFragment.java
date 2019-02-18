package com.kmwlyy.registry.page;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/13.
 */

public class SelectDoctorListFragment extends BaseFragment {
    public static final String TAG_DEPT_ID = "TAG_DEPT_ID";
    public static final String TAG_UNIT_ID = "TAG_UNIT_ID";

    private List<NetBean.Doctor> mDoctorList = new ArrayList<>();

    @BindView(R2.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;

    protected int unit_id = 0;
    protected int dept_id = 0;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_registry_list;
    }

    @Override
    protected void afterBindView() {
        try {
            unit_id = getArguments().getInt(TAG_UNIT_ID, 0);
            dept_id = getArguments().getInt(TAG_DEPT_ID, 0);
        }catch (Exception e){}


        mCenterText.setText(getResources().getString(R.string.r_select_doctor));
        recyclerView.setAdapter(mRightAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    getDoctorList(unit_id, dept_id);
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getDoctorList(unit_id, dept_id);
            }
        });
    }


    private void getDoctorList(int uid, final int deptid) {//获取医生列表
        swipeRefreshLayout.setRefreshing(true);
        NetEvent.GetDoctor event = new NetEvent.GetDoctor(uid + "", deptid + "");
        event.setHttpListener(new HttpListener<List<NetBean.Doctor>>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(getContext(), msg);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<NetBean.Doctor> doctors) {
                mDoctorList.clear();
                mDoctorList.addAll(doctors);
                mRightAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        new HttpClient(getContext(), event).start();
    }


    /*******************************************************/
    class RightHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.tv_face)
        ImageView mFaceImage;
        @BindView(R2.id.tv_name)
        TextView mNameText;
        @BindView(R2.id.tv_addr)
        TextView mAddrText;
        @BindView(R2.id.tv_info)
        TextView mInfoText;
        @BindView(R2.id.tv_department)
        TextView tv_department;
        @BindView(R2.id.tv_hospital)
        TextView tv_hospital;
        @BindView(R2.id.tv_state)
        TextView tv_state;
        @BindView(R2.id.layout_item)
        RelativeLayout mLayoutItem;

        public RightHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    RecyclerView.Adapter mRightAdapter = new RecyclerView.Adapter() {

        @Override
        public int getItemCount() {
            return mDoctorList == null ? 0 : mDoctorList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getContext(), R.layout.item_registry_doctor, null);
            return new SelectDoctorListFragment.RightHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final NetBean.Doctor item = mDoctorList.get(position);
            SelectDoctorListFragment.RightHolder mHolder = (SelectDoctorListFragment.RightHolder) holder;
            mHolder.mNameText.setText(item.getDOCTOR_NAME());
            mHolder.mAddrText.setText(Contants.getDoctorZC(getActivity(), item.getZCID()));
            mHolder.mInfoText.setText(item.getEXPERT());
            mHolder.tv_department.setText(item.getDEP_NAME());
            mHolder.tv_hospital.setText(item.getUNIT_NAME());
            ImageLoader.getInstance().displayImage(item.getIMAGE(), mHolder.mFaceImage, Contants.getCircleDisplayOptions());

            if (item.getLEFT_NUM() > 0){
                mHolder.tv_state.setEnabled(true);
                mHolder.tv_state.setText(R.string.r_surplus_register);
                mHolder.tv_state.setTextColor(getResources().getColor(R.color.color_main_green));
            }else{
                mHolder.tv_state.setEnabled(false);
                mHolder.tv_state.setText(R.string.r_full_register);
                mHolder.tv_state.setTextColor(getResources().getColor(R.color.full_state));
            }

            mHolder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ContainerActivity.class);
                    intent.putExtra(SchedulelFragment.TAG_DOCTOR, item);
                    intent.putExtra(ContainerActivity.TAG_FRAGMENT, ContainerActivity.SCHEDULE);
                    startActivity(intent);
                }
            });
        }
    };

}
