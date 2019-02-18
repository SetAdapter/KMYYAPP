package com.kmwlyy.registry.page;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-8-15.
 */
public class SchedulelFragment extends BaseFragment {

    public static final String TAG_DOCTOR = "TAG_DOCTOR";

    @BindView(R2.id.tv_face)
    ImageView mFaceImage;
    @BindView(R2.id.tv_name)
    TextView mNameText;
    @BindView(R2.id.tv_addr)
    TextView mAddrText;
    @BindView(R2.id.tv_info)
    TextView mInfoText;
    @BindView(R2.id.tv_other)
    TextView mOtherText;
    @BindView(R2.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R2.id.refreshlayout)
    SwipeRefreshLayout mRefreshLayout;
    private NetBean.Doctor mDoctor;
    private List<NetBean.Schedule> mScheduleList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_schedule;
    }

    @Override
    protected void afterBindView() {
        mCenterText.setText(getResources().getString(R.string.r_doctor_schedule));
        mDoctor = (NetBean.Doctor) getArguments().getSerializable(TAG_DOCTOR);
        ImageLoader.getInstance().displayImage(mDoctor.getIMAGE(), mFaceImage, Contants.getCircleDisplayOptions());
        mAddrText.setText(Contants.getDoctorZC(getActivity(), mDoctor.getZCID()));
        mNameText.setText(mDoctor.getDOCTOR_NAME());
        mInfoText.setText(mDoctor.getDEP_NAME());
        mOtherText.setText(mDoctor.getEXPERT());

        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getScheduleList();
            }
        });
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getScheduleList();
            }
        });
    }

    /*******************************************************/
    private void getScheduleList() {//获取排班列表
        mRefreshLayout.setRefreshing(true);
        NetEvent.GetSchedule event = new NetEvent.GetSchedule(mDoctor.getUNIT_ID() + "", mDoctor.getDEP_ID() + "", mDoctor.getDOCTOR_ID() + "", "");//2016-08-18
        event.setHttpListener(new HttpListener<List<NetBean.Schedule>>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(getContext(), msg);
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<NetBean.Schedule> schedules) {
                mRefreshLayout.setRefreshing(false);
                if (schedules != null) {
                    mScheduleList = schedules;
                    mListAdapter.notifyDataSetChanged();
                }
            }
        });
        new HttpClient(getContext(), event).start();
    }

    /*******************************************************/
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.tv_name)
        TextView mNameText;
        @BindView(R2.id.tv_charge)
        TextView mChargeText;
        @BindView(R2.id.btn_commit)
        Button mCommitBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    RecyclerView.Adapter mListAdapter = new RecyclerView.Adapter() {

        @Override
        public int getItemCount() {
            return mScheduleList == null ? 0 : mScheduleList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getContext(), R.layout.item_schedule1, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final NetBean.Schedule item = mScheduleList.get(position);
            ViewHolder mHolder = (ViewHolder) holder;
            mHolder.mNameText.setText(String.format(getResources().getString(R.string.r_leave),item.getTO_DATE(),item.getTIME_TYPE_DESC(),item.getLEFT_NUM()));
            mHolder.mChargeText.setText(getResources().getString(R.string.r_money_flag)+item.getGUAHAO_AMT());
            if (Integer.valueOf(item.getLEFT_NUM())==0){
                mHolder.mCommitBtn.setEnabled(false);
                mHolder.mCommitBtn.setText(getResources().getString(R.string.r_full_register));
            }else {
                mHolder.mCommitBtn.setEnabled(true);
                mHolder.mCommitBtn.setText(getResources().getString(R.string.r_register));
                mHolder.mCommitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (BaseApplication.instance.hasUserToken()){
                            Intent intent = new Intent(mContext, ContainerActivity.class);
                            intent.putExtra(RegistryFragment.TAG_DOCTOR, mDoctor);
                            intent.putExtra(RegistryFragment.TAG_SCHEDULEL, item);
                            intent.putExtra(ContainerActivity.TAG_FRAGMENT, ContainerActivity.REGISTRY);
                            startActivity(intent);
                        }else{
                            BaseApplication.instance.logout();
                        }
                    }
                });
            }
        }
    };
}
