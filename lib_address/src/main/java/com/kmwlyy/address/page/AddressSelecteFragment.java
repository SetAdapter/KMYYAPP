package com.kmwlyy.address.page;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmwlyy.address.R;
import com.kmwlyy.address.R2;
import com.kmwlyy.address.net.Address;
import com.kmwlyy.address.net.AddressEvent;
import com.kmwlyy.core.base.BaseFragment;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xcj on 2017/3/14.
 */

public class AddressSelecteFragment extends BaseFragment {

    @BindView(R2.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R2.id.refreshlayout)
    SwipeRefreshLayout mRefreshLayout;

    private List<Address> mAddressList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_addresslist;
    }

    @Override
    protected void afterBindView() {

        mCenterText.setText("请选择配送地址");
        mRightText.setText("管理");
        mRightText.setVisibility(View.VISIBLE);

        getAddressList();
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAddressList();
            }
        });
    }

    @Override
    public void onRightClick(View view) {
        //super.onRightClick(view);
        Intent intent = new Intent(mContext, ContainerActivity.class);
        intent.putExtra(ContainerActivity.TAG_FRAGMENT, ContainerActivity.ADDRESSLIST);
        startActivity(intent);
    }

    public static class AddressUpdate{}

    @Override
    public boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onAddressUpdate(AddressListFragment.AddressUpdate update) {
        getAddressList();
    }

    /*******************************************************/
    private void getAddressList() {//获取地址列表
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });
        AddressEvent.GetList event = new AddressEvent.GetList(new HttpListener<ArrayList<Address>>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(mContext,msg);
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(ArrayList<Address> addresses) {
                mRefreshLayout.setRefreshing(false);
                mAddressList = addresses;
                mListAdapter.notifyDataSetChanged();
            }
        });
        new HttpClient(getContext(), event).start();
    }


    /*******************************************************/
    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.layout_address)
        RelativeLayout mAddressLayout;
        @BindView(R2.id.tv_time)
        TextView tvTime;
        @BindView(R2.id.tv_title)
        TextView tvTitle;
        @BindView(R2.id.tv_content)
        TextView tvContent;
        @BindView(R2.id.tv_default)
        TextView btnDefault;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    RecyclerView.Adapter mListAdapter = new RecyclerView.Adapter() {

        @Override
        public int getItemCount() {
            return mAddressList == null ? 0 : mAddressList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getContext(), R.layout.item_address_selecte, null);
            return new AddressSelecteFragment.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final Address item = mAddressList.get(position);
            AddressSelecteFragment.MyViewHolder viewHolder = (AddressSelecteFragment.MyViewHolder) holder;
            viewHolder.tvTitle.setText(item.getUserName());
            viewHolder.tvTime.setText(item.getMobile());
            viewHolder.tvContent.setText(item.getProvinceName() + item.getCityName() + item.getAreaName()+item.getDetailAddress());
            if (item.isIsDefault()) {
                viewHolder.btnDefault.setVisibility(View.VISIBLE);
            } else {
                viewHolder.btnDefault.setVisibility(View.GONE);
            }
                viewHolder.mAddressLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(item);
                        getActivity().finish();
                    }
                });

        }
    };
}
