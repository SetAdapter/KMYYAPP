package com.kmwlyy.patient.myplan;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.MemberOrder;
import com.kmwlyy.patient.helper.net.bean.UserPackage;
import com.kmwlyy.patient.helper.net.event.MemberPackages;
import com.kmwlyy.patient.pay.PayActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xcj on 2016/12/15.
 */

public class MembersPlanActivity extends BaseActivity{
    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.lv_member_plan)
    ListView mMemberPlanListView;

    private HttpClient mGetListClient;
    private String mOrderNO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_plan);
        butterknife.ButterKnife.bind(this);
        tv_center.setText(getString(R.string.member_plan_title));
        getList();
    }

    private void getList(){
        MemberPackages.GetList getList = new MemberPackages.GetList(new HttpListener<ArrayList<UserPackage>>() {
            @Override
            public void onError(int code, String msg) {

            }

            @Override
            public void onSuccess(ArrayList<UserPackage> listItems) {
                mMemberPlanListView.setAdapter(new MemberAdapter(MembersPlanActivity.this, listItems));
            }
        });
        mGetListClient = NetService.createClient(this, getList);
        mGetListClient.start();
    }

    public final class ViewHolder{
        public TextView info;
        public TextView count;
    }

    public final class MemberViewHolder{
        public ListView mOneList;
        public TextView money;
        public TextView memberName;
        public TextView period;
        public Button buy;
        public RelativeLayout bg;
    }

    public class MemberAdapter extends BaseAdapter{
        private LayoutInflater mInflater;
        private List<UserPackage> datas;


        public MemberAdapter(Context context,List<UserPackage> datas){
            this.mInflater = LayoutInflater.from(context);
            this.datas = datas;
        }


        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            MemberViewHolder holder;
            if (convertView == null) {
                holder= new MemberViewHolder();
                convertView = mInflater.inflate(R.layout.layout_member_item, null);
                holder.mOneList = (ListView) convertView.findViewById(R.id.lv_one);
                holder.money = (TextView) convertView.findViewById(R.id.tv_money);
                holder.memberName = (TextView) convertView.findViewById(R.id.tv_member_name);
                holder.period = (TextView) convertView.findViewById(R.id.tv_period);
                holder.buy = (Button) convertView.findViewById(R.id.btn_buy);
                holder.bg = (RelativeLayout) convertView.findViewById(R.id.rl_bg);
                convertView.setTag(holder);
            }else {
                holder = (MemberViewHolder)convertView.getTag();
            }
            if ((position+1)%3 == 1){
                holder.bg.setBackgroundResource(R.drawable.bg_normal_combo);
            }else if ((position+1)%3 == 2){
                holder.bg.setBackgroundResource(R.drawable.bg_advanced_combo);
            }else if((position+1)%3 == 0) {
                holder.bg.setBackgroundResource(R.drawable.bg_extreme_combo);
            }
            holder.mOneList.setAdapter(new MyAdapter(MembersPlanActivity.this, datas.get(position).mDetails));
            fixListViewHeight(holder.mOneList);
            holder.money.setText(datas.get(position).mAmount);
            holder.memberName.setText(datas.get(position).mUserPackageName);
            holder.period.setText(getString(R.string.period_of_validity)+" "+datas.get(position).mPeriod+datas.get(position).mPeriodUnitName);
            holder.buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获取订单号，然后跳转支付页面
                    getOrderNo(datas.get(position).mUserPackageID, datas.get(position).mAmount);
                }
            });
            return convertView;
        }
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<UserPackage.Detail> datas;


        public MyAdapter(Context context,List<UserPackage.Detail> datas){
            this.mInflater = LayoutInflater.from(context);
            this.datas = datas;
        }


        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder= new ViewHolder();
                convertView = mInflater.inflate(R.layout.member_item, null);
                holder.info = (TextView) convertView.findViewById(R.id.tv_info);
                holder.count = (TextView) convertView.findViewById(R.id.tv_count);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.info.setText(datas.get(position).mServiceGoodsName);
            holder.count.setText(String.valueOf(datas.get(position).mServiceGoodsCount));
            return convertView;
        }

    }

    public void fixListViewHeight(ListView listView) {
        // 如果没有设置数据适配器，则ListView没有子项，返回。
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        if (listAdapter == null) {
            return;
        }
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listViewItem = listAdapter.getView(i , null, listView);
            // 计算子项View 的宽高
            listViewItem.measure(0, 0);
            // 计算所有子项的高度和
            totalHeight += listViewItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // listView.getDividerHeight()获取子项间分隔符的高度
        // params.height设置ListView完全显示需要的高度
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void getOrderNo(String id, final String money){
        MemberPackages.CreateOrder order = new MemberPackages.CreateOrder(id, new HttpListener<MemberOrder>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(MembersPlanActivity.this, msg);
            }

            @Override
            public void onSuccess(MemberOrder memberOrder) {
                mOrderNO =  memberOrder.OrderNO;
                PayActivity.startPayActivity(MembersPlanActivity.this,mOrderNO,"0",PayActivity.MEMBERS_PLAN,true);
            }


        });
        mGetListClient = NetService.createClient(this, order);
        mGetListClient.start();
    }

}
