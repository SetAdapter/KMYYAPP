package com.kmwlyy.registry.page;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/13.
 */

public class SelectDepartmentFragment extends BaseFragment {
    public static final String TAG_HOSPITAL = "TAG_SELECT_DEPARTMENT";



    @BindView(R2.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;


    @BindView(R2.id.recycler_left)
    ListView recycler_left;

    @BindView(R2.id.recycler_right)
    RecyclerView recycler_right;


    private List<NetBean.DepartmentTree> departmentTreeList = new ArrayList<>();
    private Map<Integer, List<NetBean.Department>> departmentMap = new HashMap<>();
    private List<NetBean.Department> rightDepartmentList;
    private  int mCheckIndex = 0;
    private int unit_id = 0;
    private String class_id ="";


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_registry_department;
    }

    @Override
    protected void afterBindView() {
        unit_id = getArguments().getInt(TAG_HOSPITAL);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getClassId(unit_id);
            }
        });

        mCenterText.setText(getResources().getString(R.string.r_select_department));
        recycler_left.setAdapter(mLeftAdapter);
        recycler_right.setAdapter(mRightAdapter);
        recycler_right.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getClassId(unit_id);
            }
        });

    }

    /*******************************************************/
    private void getClassId(final int uid){
        NetEvent.GetHospital event = new NetEvent.GetHospital(unit_id);
        event.setHttpListener(new HttpListener<List<NetBean.Hospital>>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(getContext(), msg);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<NetBean.Hospital> hospitals) {
                try {
                    class_id = hospitals.get(0).getUNIT_SON().get(0).CLASS_ID;
                }catch (Exception e){}
                getDepartmentTree(uid,class_id);
            }
        });
        new HttpClient(getContext(), event).start();
    }


    private void getDepartmentTree(int uid,String class_id){
        NetEvent.GetHospitalDepartmentTree event = new NetEvent.GetHospitalDepartmentTree(uid,class_id);
        event.setHttpListener(new HttpListener<ArrayList<NetBean.DepartmentTree>>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(getContext(), msg);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(ArrayList<NetBean.DepartmentTree> newList) {
                departmentTreeList.clear();
                departmentTreeList.addAll(newList);
                departmentMap.clear();
                for (int i = 0 ; i < departmentTreeList.size(); i ++){
                    departmentMap.put(i,departmentTreeList.get(i).getDepartments());
                }
                mLeftAdapter.notifyDataSetChanged();
                notifyRightListChanged();
            }
        });
        new HttpClient(getContext(), event).start();
    }


    private void notifyRightListChanged() {
            rightDepartmentList = departmentMap.get(mCheckIndex);
            mRightAdapter.notifyDataSetChanged();
    }

    /*******************************************************/


    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.check_area)
        public RadioButton mCheckArea;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    BaseAdapter mLeftAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return departmentTreeList == null ? 0 : departmentTreeList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return departmentTreeList.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            MyViewHolder mHolder = null;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.item_area, null);
                mHolder = new MyViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (MyViewHolder) convertView.getTag();
            }
            NetBean.DepartmentTree item = departmentTreeList.get(position);
            mHolder.mCheckArea.setText(item.getNAME());
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



    RecyclerView.Adapter mRightAdapter = new RecyclerView.Adapter() {

        @Override
        public int getItemCount() {
            return rightDepartmentList == null ? 0 : rightDepartmentList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getContext(), R.layout.item_area, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {

            final NetBean.Department item = rightDepartmentList.get(position);
            MyViewHolder mHolder = (MyViewHolder) holder;
            mHolder.mCheckArea.setText(item.getDEP_NAME());
            mHolder.mCheckArea.setBackgroundResource(R.drawable.bg_item_list);
            mHolder.mCheckArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ContainerActivity.class);
                    NetBean.Department item = rightDepartmentList.get(position);
                    intent.putExtra(SelectDoctorListFragment.TAG_DEPT_ID, item.getDEP_ID());
                    intent.putExtra(SelectDoctorListFragment.TAG_UNIT_ID, item.getUNIT_ID());
                    intent.putExtra(ContainerActivity.TAG_FRAGMENT, ContainerActivity.DOCTORLIST);
                    startActivity(intent);

                }
            });

        }
    };


}
