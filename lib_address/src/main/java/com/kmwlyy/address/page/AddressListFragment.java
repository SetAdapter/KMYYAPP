package com.kmwlyy.address.page;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmwlyy.address.R;
import com.kmwlyy.address.R2;
import com.kmwlyy.address.net.Address;
import com.kmwlyy.address.net.AddressEvent;
import com.kmwlyy.core.base.BaseFragment;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.ToastUtils;
import com.winson.ui.widget.AlterDialogView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-8-18.
 */
public class AddressListFragment extends BaseFragment {


    @BindView(R2.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R2.id.refreshlayout)
    SwipeRefreshLayout mRefreshLayout;

    private boolean isSelect = false;
    private List<Address> mAddressList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_addresslist;
    }

    @Override
    protected void afterBindView() {

        mCenterText.setText("配送地址管理");
        mRightText.setText("新增");
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
    public void onLeftClick(View view) {
        super.onLeftClick(view);
        EventBus.getDefault().post(new AddressUpdate());
    }

    @Override
    public void onRightClick(View view) {
        //super.onRightClick(view);
        Intent intent = new Intent(mContext, ContainerActivity.class);
        intent.putExtra(ContainerActivity.TAG_FRAGMENT, ContainerActivity.ADDRESS);
        startActivity(intent);
    }

    public static class AddressUpdate{}

    @Override
    public boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onAddressUpdate(AddressUpdate update) {
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

    private void setAddress(int type,String id){
        HttpEvent event;
        if (type==1) {
            event = new AddressEvent.Delete(id);
        } else {
            event = new AddressEvent.SetDefault(id);
        }
        event.setHttpListener(new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(mContext, msg);
            }

            @Override
            public void onSuccess(String result) {
                ToastUtils.showShort(mContext, "操作成功");
                getAddressList();
            }
        });
        new HttpClient(mContext, event).start();
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
        @BindView(R2.id.btn_default)
        CheckBox btnDefault;
        @BindView(R2.id.btn_delete)
        Button btnDelete;
        @BindView(R2.id.btn_edit)
        Button btnEdit;

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
            View view = View.inflate(getContext(), R.layout.item_address, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final Address item = mAddressList.get(position);
            MyViewHolder viewHolder = (MyViewHolder) holder;
            viewHolder.tvTitle.setText(item.getUserName());
            viewHolder.tvTime.setText(item.getMobile());
            viewHolder.tvContent.setText(item.getProvinceName() + item.getCityName() + item.getAreaName()+item.getDetailAddress());

            if (item.isIsDefault()) {
                viewHolder.btnDefault.setChecked(true);
                viewHolder.btnDefault.setText("默认地址");
            } else {
                viewHolder.btnDefault.setChecked(false);
                viewHolder.btnDefault.setText("设为默认");
            }

            viewHolder.btnDefault.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (item.isIsDefault()) {
                            ToastUtils.showShort(mContext, "已是默认地址");
                        } else {
                            setAddress(0,item.getAddressID());
                        }
                    }
                    return true;
                }
            });
            viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ContainerActivity.class);
                    intent.putExtra(ContainerActivity.TAG_FRAGMENT, ContainerActivity.ADDRESS);
                    intent.putExtra(AddressFragment.TAG_ADDRESS, item);
                    startActivity(intent);
                }
            });
            viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlterDialogView.Builder builder = new AlterDialogView.Builder(mContext);
                    builder.setTitle("系统提示");
                    builder.setMessage("确定要删除该地址？");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int arg1) {
                                    // TODO Auto-generated method stub
                                    setAddress(1,item.getAddressID());
                                    dialog.dismiss();

                                }
                            });
                    builder.create().show();
                }
            });
        }
    };
}
