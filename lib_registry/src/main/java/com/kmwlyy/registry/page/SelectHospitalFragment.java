package com.kmwlyy.registry.page;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.base.BaseFragment;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.SPUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.registry.R;
import com.kmwlyy.registry.R2;
import com.kmwlyy.registry.bean.Contants;
import com.kmwlyy.registry.net.NetBean;
import com.kmwlyy.registry.net.NetEvent;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-8-15.
 */
public class SelectHospitalFragment extends BaseFragment {

    @BindView(R2.id.refresh_left)
    SwipeRefreshLayout mRefreshLeft;

    @BindView(R2.id.refresh_right)
    SwipeRefreshLayout mRefreshRight;

    @BindView(R2.id.recycler_left)
    ListView mLeftRecyclerView;

    @BindView(R2.id.recycler_right)
    RecyclerView mRightRecyclerView;

    private List<NetBean.Area> mAreaList;
    private List<NetBean.Hospital> mHospitalList;
    private Map<Integer, List<NetBean.Hospital>> mHospitalMap;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hospital;
    }

    @Override
    protected void afterBindView() {
        mHospitalMap = new HashMap<>();
        Contants.initImageLoader(mContext);
        mRefreshLeft.post(new Runnable() {
            @Override
            public void run() {
                getAreaList();
            }
        });

        mLeftText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_area_location, 0, 0, 0);
        mLeftText.setText(SPUtils.get(mContext, SPUtils.REG_AREANAME, "深圳").toString());
        mCenterText.setText("预约挂号");
        mRightText.setText("挂号单");
        mRightText.setVisibility(View.VISIBLE);

        mLeftRecyclerView.setAdapter(mLeftAdapter);
        mLeftRecyclerView.setDividerHeight(1);
        mRightRecyclerView.setAdapter(mRightAdapter);
        mRightRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRefreshLeft.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAreaList();
            }
        });
        mRefreshRight.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mAreaList != null && mAreaList.size() != 0) {
                    NetBean.Area item = mAreaList.get(mCheckIndex);
                    mHospitalMap.remove(item.getAREA_ID());
                    getHospistalList(item.getAREA_ID(), item.getCITY_ID());
                } else {
                    mRefreshRight.setRefreshing(false);
                }
            }
        });


    }




    @Override
    public void onLeftClick(View view) {
        //测试
        //new HttpClient(this,new NetEvent.GetArea("5")).start();
        //new HttpClient(this,new NetEvent.GetHospital("3317","5")).start();
        //new HttpClient(this,new NetEvent.GetDepartment("21","0")).start();
        //new HttpClient(this,new NetEvent.GetDoctor("21","4249")).start();
        //new HttpClient(this,new NetEvent.GetSchedule("21","4249","2083","2016-08-18")).start();
        //new HttpClient(this,new NetEvent.GetSchDetl("21","114150626")).start();

        Intent intent = new Intent(mContext, ContainerActivity.class);
        intent.putExtra(ContainerActivity.TAG_FRAGMENT, ContainerActivity.AREA);
        startActivity(intent);
    }

    @Override
    public void onRightClick(View view) {
        if (BaseApplication.instance.hasUserToken()) {
            Intent intent = new Intent(mContext, ContainerActivity.class);
            intent.putExtra(ContainerActivity.TAG_FRAGMENT, ContainerActivity.REGISTRYLIST);
            startActivity(intent);
        } else {
            BaseApplication.instance.logout();
        }
    }

    @Override
    public boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAreaChange(NetBean.Area area) {
        SPUtils.put(mContext, SPUtils.REG_AREANAME, area.getAREA_Name());
        SPUtils.put(mContext, SPUtils.REG_AREAID, area.getAREA_ID());
        mLeftText.setText(area.getAREA_Name());
        getAreaList();
    }

    /*******************************************************/
    private void getAreaList() {//获取区域列表
        mCheckIndex = 0;
        mRefreshLeft.setRefreshing(true);
        String areaid = SPUtils.get(mContext, SPUtils.REG_AREAID, 5) + "";
        NetEvent.GetArea event = new NetEvent.GetArea(areaid);
        event.setHttpListener(new HttpListener<List<NetBean.Area>>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(getActivity(), msg);
                mRefreshLeft.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<NetBean.Area> areas) {
                mRefreshLeft.setRefreshing(false);
                if (areas != null) {
                    mAreaList = areas;
                    mLeftAdapter.notifyDataSetChanged();
                    notifyRightListChanged();
                }
            }
        });
        new HttpClient(getContext(), event).start();
    }

    private void getHospistalList(final int areaid, int cityid) {//获取医院列表
        mRefreshRight.setRefreshing(true);
        NetEvent.GetHospital event = new NetEvent.GetHospital(areaid + "", cityid + "");
        event.setHttpListener(new HttpListener<List<NetBean.Hospital>>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(getContext(), msg);
                mRefreshRight.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<NetBean.Hospital> hospitals) {
                mHospitalMap.put(areaid, hospitals);
                notifyRightListChanged();
                mRefreshRight.setRefreshing(false);
            }
        });
        new HttpClient(getContext(), event).start();
    }

    private void notifyRightListChanged() {
        if (mAreaList != null && mAreaList.size() != 0) {
            NetBean.Area item = mAreaList.get(mCheckIndex);
            if (mHospitalMap.containsKey(item.getAREA_ID())) {
                mHospitalList = mHospitalMap.get(item.getAREA_ID());
            } else {
                mHospitalList = null;
                getHospistalList(item.getAREA_ID(), item.getCITY_ID());
            }
            mRightAdapter.notifyDataSetChanged();
        }
    }

    /*******************************************************/
    private static int mCheckIndex;

    class LeftHolder {
        @BindView(R2.id.check_area)
        public RadioButton mCheckArea;

        public LeftHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    BaseAdapter mLeftAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mAreaList == null ? 0 : mAreaList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return mAreaList.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LeftHolder mHolder = null;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.item_area, null);
                mHolder = new LeftHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (LeftHolder) convertView.getTag();
            }
            NetBean.Area item = mAreaList.get(position);
            mHolder.mCheckArea.setText(item.getAREA_Name());
            mHolder.mCheckArea.setChecked(position == mCheckIndex);
            mHolder.mCheckArea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mCheckIndex = position;
                        notifyDataSetChanged();
                        notifyRightListChanged();
                    }
                }
            });
            return convertView;
        }
    };

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
        @BindView(R2.id.layout_item)
        LinearLayout mLayoutItem;

        public RightHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    RecyclerView.Adapter mRightAdapter = new RecyclerView.Adapter() {

        @Override
        public int getItemCount() {
            return mHospitalList == null ? 0 : mHospitalList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getContext(), R.layout.item_hospital, null);
            return new RightHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final NetBean.Hospital item = mHospitalList.get(position);
            RightHolder mHolder = (RightHolder) holder;
            mHolder.mNameText.setText(item.getUNIT_NAME());
            mHolder.mAddrText.setText(item.getADDRESS());
            mHolder.mInfoText.setText(getSpannableText(item.getLEFT_NUM() +
                    getResources().getString(R.string.r_person_signal_resources), 0, item.getLEFT_NUM().length(), Color.parseColor("#0accce")));
            ImageLoader.getInstance().displayImage(item.getIMAGE(), mHolder.mFaceImage, Contants.getDisplayOptions());
            mHolder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ContainerActivity.class);
                    intent.putExtra(SelectDepartmentFragment.TAG_HOSPITAL, item.getUNIT_ID());
                    intent.putExtra(ContainerActivity.TAG_FRAGMENT, ContainerActivity.DEPARTMENT);
                    startActivity(intent);
                }
            });
        }

        private SpannableStringBuilder getSpannableText(String str, int start, int end, int clolor) {
            ForegroundColorSpan fcs = new ForegroundColorSpan(clolor);
            SpannableStringBuilder style = new SpannableStringBuilder(str);
            style.setSpan(fcs, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            return style;
        }
    };
}
