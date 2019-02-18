package com.kmwlyy.doctor.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.doctor.Activity.PatientDetailActivity;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.AppEventApi;
import com.kmwlyy.doctor.model.PatientGroup;
import com.kmwlyy.doctor.model.PatientList;
import com.kmwlyy.doctor.model.httpEvent.MemberGroup;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.EmptyViewUtils;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Winson on 2017/7/4.
 */

public class PatientListFragment extends Fragment implements PageListView.OnPageLoadListener {

    public static final String TAG = PatientListFragment.class.getSimpleName();

    PageListViewHelper<PatientList> mPatientListPageHelper;
    PatientGroup mPatientGroup;
    ViewGroup mContent;
    PageListView mPageListView;

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateGroup(AppEventApi.UpdateGroup updateGroup) {
        mPatientGroup = updateGroup.patientGroup;
        if (mPageListView != null) {
            mPageListView.setEnabled(mPatientGroup != null);
            getPatientList(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_patient_list, container, false);

        mContent = (ViewGroup) root.findViewById(R.id.content);
        mPageListView = (PageListView) root.findViewById(R.id.list_view);
        mPatientListPageHelper = new PageListViewHelper<>(mPageListView, new PatientListAdapter(getActivity(), null));
        mPatientListPageHelper.setOnPageLoadListener(this);
        mPatientListPageHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //进入患者详情界面
                Intent intent = new Intent();
                intent.setClass(getActivity(), PatientDetailActivity.class);
                intent.putExtra("id", mPatientListPageHelper.getAdapter().getItem(position).doctorMemberID);
                getActivity().startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPageListView.setEnabled(mPatientGroup != null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getPatientList(true);

    }

    @Override
    public void onRefreshData() {
        getPatientList(true);

    }

    @Override
    public void onLoadPageData() {
        getPatientList(false);

    }

    static class PatientListAdapter extends CommonAdapter<PatientList> {

        String mAgeFormat;

        public PatientListAdapter(Context context, List<PatientList> datas) {
            super(context, R.layout.item_patient_member, datas);
            mAgeFormat = context.getResources().getString(R.string.string_age_format);
        }

        @Override
        public void convert(ViewHolder viewHolder, PatientList data, int position) {
            ((TextView) viewHolder.findViewById(R.id.tv_name)).setText(data.memberName);
            ((TextView) viewHolder.findViewById(R.id.tv_age)).setText(String.format(mAgeFormat, data.age));
            ((TextView) viewHolder.findViewById(R.id.tv_gender)).setText(data.gender == 0 ? context.getString(R.string.string_male) : context.getString(R.string.string_female));
            ((TextView) viewHolder.findViewById(R.id.tv_phone)).setText(data.mobile);
        }
    }

    private void getPatientList(final boolean refresh) {
        EmptyViewUtils.removeAllView(mContent);
        if (mPatientGroup == null) {
            return;
        }
        if (refresh) {
            mPatientListPageHelper.refreshData(null);
        }
        mPatientListPageHelper.setRefreshing(refresh);
        MemberGroup.GetMemberList getMemberList = new MemberGroup.GetMemberList(mPatientGroup.memberGroupID,
                refresh ? 1 : mPatientListPageHelper.getPageIndex(), mPatientListPageHelper.getPageSize(), new HttpListener<ArrayList<PatientList>>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("getPatientList", code, msg));
                }
                mPatientListPageHelper.setRefreshing(false);
                if (refresh) {
                    EmptyViewUtils.showErrorView(mContent, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getPatientList(true);
                        }
                    });
                }
            }

            @Override
            public void onSuccess(ArrayList<PatientList> patientLists) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("getPatientList", DebugUtils.toJson(patientLists)));
                }
                mPatientListPageHelper.setRefreshing(false);

                if (refresh) {
                    if (patientLists == null || patientLists.isEmpty()) {
                        mPatientListPageHelper.refreshData(null);
                        EmptyViewUtils.showEmptyView(mContent, R.mipmap.empty_patient_data, getResources().getString(R.string.string_no_patient_data), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getPatientList(true);
                            }
                        });
                    } else {
                        mPatientListPageHelper.refreshData(patientLists);
                    }
                } else {
                    mPatientListPageHelper.addPageData(patientLists);
                }
            }
        });

        HttpClient client = NetService.createClient(getActivity(), HttpClient.DOCTOR_API_URL, getMemberList);
        client.start();
    }

}
