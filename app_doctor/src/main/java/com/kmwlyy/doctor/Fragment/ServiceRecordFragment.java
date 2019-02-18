package com.kmwlyy.doctor.Fragment;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.Activity.PatientActivity;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.adapter.ConsultVoiceListAdapter;
import com.kmwlyy.doctor.model.Constant;
import com.kmwlyy.doctor.model.ConsultBeanNew;
import com.kmwlyy.doctor.model.MedRecordBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getPatientRecord_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_getTaskList_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.winson.ui.widget.EmptyViewUtils;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;
import com.xingyi.elonggradletaskdemo.widget.SExpandableListView;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;

/**
 * Created by TFeng on 2017/7/2.
 */

public class ServiceRecordFragment extends SearchFragment {
    private static final String TAG = "ServiceRecordFragment";

    @ViewInject(R.id.expand_list_view)
    SExpandableListView expand_list_view;
    @ViewInject(R.id.ll_content)
    ViewGroup mContent;
    @ViewInject(R.id.lv_record)
    PageListView lv_record;
    MyExpandableAdapter adapter;

    private Context mContext;
    private int loadCount = 1;
    private List<ConsultBeanNew> mDatas = new ArrayList<>();
    private PageListViewHelper<ConsultBeanNew> mPageListViewHelper;
    private boolean isPull = false;
    Http_getTaskList_Event getTaskListEvent = null;
    private Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loadData(msg.arg1);
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_record, null);
        ViewUtils.inject(this,view);
        mContext = getActivity();
        adapter = new MyExpandableAdapter(mContext,mDatas);
        adapter.setData(mDatas);
        // 在设置适配器之前设置是否支持下拉刷新
        expand_list_view.setLoadingMoreEnabled(false);
        expand_list_view.setPullRefreshEnabled(false);
        expand_list_view.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(!expand_list_view.isGroupExpanded(groupPosition)){
                    getChildData(mDatas.get(groupPosition).Room.ServiceID,groupPosition);
                }
                return false;
            }
        });
        expand_list_view.setmLoadingListener(new SExpandableListView.LoadingListener() {
            @Override
            public void onLoadMore() {
                isPull = false;
                loadCount++;
                Message msg = handler.obtainMessage();
                msg.arg1 = loadCount;
                handler.sendMessage(msg);
            }

            @Override
            public void onRefresh() {
                isPull = true;
                loadCount = 0;
                Message msg = handler.obtainMessage();
                msg.arg1 = loadCount;
                handler.sendMessage(msg);
            }
        });
        mPageListViewHelper = new PageListViewHelper<>(lv_record, new ConsultVoiceListAdapter(getActivity(), null));
        mPageListViewHelper.setRefreshing(true);
        loadData(loadCount);
        return view;
    }
    /**
     * 请求数据
     */
    private void loadData(final int currentPage) {
        getTaskListEvent = new Http_getTaskList_Event(currentPage+"","20",Constant.OPDTYPE_RECORD,
                getArguments().getString("MemberID"),getArguments().getString("FamilyDoctorID"),new HttpListener<List<ConsultBeanNew>>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, DebugUtils.errorFormat("getTaskListEvent", code, msg));
                }
                mPageListViewHelper.setRefreshing(false);
                if (isPull) {
                    expand_list_view.refreshComplete();
                }
            }

            @Override
            public void onSuccess(List<ConsultBeanNew> datas) {
                if (DebugUtils.debug) {
                    Log.i(TAG, DebugUtils.successFormat("getTaskListEvent", DebugUtils.toJson(datas)));
                }
                mPageListViewHelper.setRefreshing(false);
                if(datas == null || datas.size() == 0){
                    lv_record.setVisibility(View.GONE);
                    EmptyViewUtils.removeAllView(mContent);
                    EmptyViewUtils.showEmptyView(mContent, R.mipmap.no_message, "暂无数据", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            loadData(0);
                        }
                    });
                }else{
                    LogUtils.i(TAG,"total record:" + Integer.parseInt(getTaskListEvent.mTotal));
                    if(Integer.parseInt(getTaskListEvent.mTotal) > 20){
                        expand_list_view.setLoadingMoreEnabled(true);
                    }
                    if(currentPage == 0){
                        EmptyViewUtils.removeAllView(mContent);
                        adapter.setData(datas);
                        if (isPull) {
                            expand_list_view.refreshComplete();
                        }
                    }else {
                        EmptyViewUtils.removeAllView(mContent);
                        adapter.addData(datas);
                        if(datas.size() == 0){
                            expand_list_view.setNoMore(true);
                            expand_list_view.setLoadingMoreEnabled(false);
                        }
                    }
                    lv_record.setVisibility(View.GONE);
                    expand_list_view.setAdapter(adapter);
                    ((PatientActivity) getActivity()).setServiceTimes(getTaskListEvent.mTotal.equals("0")?"":getTaskListEvent.mTotal + getResources().getString(R.string.ci));
                }

            }
        });
        HttpClient client = NetService.createClient(getActivity(), HttpClient.DOCTOR_API_URL, getTaskListEvent);
        client.start();
    }

    /**
     * 请求数据
     */
    private void getChildData(String opdId, final int position) {
        Http_getPatientRecord_Event event = new Http_getPatientRecord_Event(opdId,new HttpListener<MedRecordBean>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, DebugUtils.errorFormat("Http_getPatientRecord_Event", code, msg));
                }
            }

            @Override
            public void onSuccess(MedRecordBean childDatas) {
                if (DebugUtils.debug) {
                    Log.i(TAG, DebugUtils.successFormat("Http_getPatientRecord_Event", DebugUtils.toJson(childDatas)));
                }
                if(null == childDatas){
                    ToastUtils.showShort(mContext,"暂无数据!");
                }else {
                    mDatas.get(position).setRecord(childDatas);
                }
                expand_list_view.collapseGroup(position);
                expand_list_view.expandGroup(position);
                adapter.notifyDataSetInvalidated();

            }
        });
        HttpClient client = NetService.createClient(getActivity(), HttpClient.DOCTOR_API_URL, event);
        client.start();
    }

    class MyExpandableAdapter extends BaseExpandableListAdapter {
        private Context mContext;
        private List<ConsultBeanNew> mData;
        private ConsultBeanNew consultBeanNew;
        private List<Integer> icons = new ArrayList<Integer>(){{add(R.mipmap.img_pic_text);
                                                                add(R.mipmap.img_voice);
                                                                add(R.mipmap.img_vieo);}};
        private List<Integer> texts = new ArrayList<Integer>(){{add(R.string.img_text_inqury);
                                                              add(R.string.string_set_voice);
                                                              add(R.string.video_inqury);}};

        public MyExpandableAdapter(Context context,List<ConsultBeanNew> datas){
            mContext = context;
            mData = datas;
        }

        public void setData(List<ConsultBeanNew> data){
            mData.clear();
            mData = data;
            notifyDataSetChanged();
        }
        public void addData(List<ConsultBeanNew> data){
            mData.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        }

        @Override
        public int getGroupCount() {
            if(mData != null || mData.size()>0){
                return mData.size();
            }
            return 0;
        }

        @Override
        public int getChildrenCount(int i) {
            return 1;
        }

        @Override
        public ConsultBeanNew getGroup(int i) {
            return mData.get(i);
        }

        @Override
        public MedRecordBean getChild(int i, int position) {
            return mData.get(i).getRecord();
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int postion) {
            return postion;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i,boolean isExpandable, View view, ViewGroup viewGroup) {
            GroupViewHodlder holder;
            if(view == null){
                view = View.inflate(mContext,R.layout.item_group_list,null);
                holder = new GroupViewHodlder(view);
                view.setTag(holder);
            }else{
                holder = (GroupViewHodlder) view.getTag();
            }
            if(isExpandable){
                holder.iv_group_arrow.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_arrow_up));
            }else {
                holder.iv_group_arrow.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_arrow_down1));
            }
            consultBeanNew = mData.get(i);
            switch (Integer.parseInt(consultBeanNew.Room.ServiceType)){
                case 1:
                case 2:
                case 3:
                    holder.iv_item_group_type.setImageResource(icons.get(Integer.parseInt(consultBeanNew.Room.ServiceType) -1 ));
                    holder.tv_item_group_type.setText(texts.get(Integer.parseInt(consultBeanNew.Room.ServiceType) - 1));
                    break;
                default:
                    holder.iv_item_group_type.setImageResource(icons.get(0));
                    holder.tv_item_group_type.setText("其它类型");
                    break;
            }
            holder.tv_item_group_time.setText(CommonUtils.changeTimeFormat(consultBeanNew.OPDDate.substring(0,10),"yyyy年MM月dd日"));
            return view;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getChildView(int i, int postion, boolean b, View view, ViewGroup viewGroup) {
            ChildViewHolder holder;
            if(view == null){
                view = View.inflate(mContext,R.layout.item_child_list,null);
                holder = new ChildViewHolder(view);
                view.setTag(holder);
            }else{
                holder = (ChildViewHolder) view.getTag();
            }

            MedRecordBean bean= mData.get(i).getRecord();
            holder.tv_sympton.setText(bean.getSympton());//主诉
            holder.tv_diag.setText(bean.getPreliminaryDiagnosis());//初步诊断
            holder.tv_advised.setText(bean.getAdvised());//医生建议
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void onGroupExpanded(int i) {

        }

        @Override
        public void onGroupCollapsed(int i) {

        }

        @Override
        public long getCombinedChildId(long l, long l1) {
            return 0;
        }

        @Override
        public long getCombinedGroupId(long l) {
            return 0;
        }
    }

    static class GroupViewHodlder{
        @ViewInject(R.id.iv_item_group_type)
        ImageView iv_item_group_type;
        @ViewInject(R.id.tv_item_group_type)
        TextView tv_item_group_type;
        @ViewInject(R.id.tv_item_group_time)
        TextView tv_item_group_time;
        @ViewInject(R.id.iv_group_arrow)
        ImageView iv_group_arrow;
        public GroupViewHodlder(View view) {
            ViewUtils.inject(this,view);
        }
    }
    static class ChildViewHolder{
        @ViewInject(R.id.tv_sympton)
        TextView tv_sympton;
        @ViewInject(R.id.tv_diag)
        TextView tv_diag;
        @ViewInject(R.id.tv_advised)
        TextView tv_advised;
        public ChildViewHolder(View view) {
            ViewUtils.inject(this,view);
        }
    }
}
