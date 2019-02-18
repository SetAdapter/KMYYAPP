package com.kmwlyy.doctor.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.AppEventApi;
import com.kmwlyy.doctor.model.PatientGroup;
import com.kmwlyy.doctor.model.httpEvent.MemberGroup;
import com.networkbench.agent.impl.NBSAppAgent;
import com.winson.ui.widget.AlterDialogView;
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
 * Created by Winson on 2017/7/5.
 */

public class PatientGroupListActivity extends BaseActivity implements PageListView.OnPageLoadListener {

    public static final String TAG = PatientGroupListActivity.class.getSimpleName();

    ImageView mIVRight;
    PageListView mGroupListView;
    PageListViewHelper<PatientGroup> mGroupPageListHelper;
    ViewGroup mContent;
    ViewGroup mEmptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_group);

        mEmptyLayout = (ViewGroup) findViewById(R.id.empty_layout);
        mEmptyLayout.setVisibility(View.GONE);
        findViewById(R.id.add_patient_group).setOnClickListener(this);


        mContent = (ViewGroup) findViewById(R.id.content);
        findViewById(R.id.tv_left).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_center)).setText(R.string.divide_group);
        mIVRight = (ImageView) findViewById(R.id.iv_right);
        mIVRight.setVisibility(View.VISIBLE);
        mIVRight.setImageResource(R.mipmap.add_patient_group);
        mIVRight.setOnClickListener(this);

        mGroupListView = (PageListView) findViewById(R.id.group_list_view);
        mGroupPageListHelper = new PageListViewHelper<>(mGroupListView, new GroupListAdapter(this, null));
        mGroupPageListHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PatientGroupDetailActivity.startSelf(view.getContext(), mGroupPageListHelper.getAdapter().getItem(position));

            }
        });
        mGroupPageListHelper.setOnPageLoadListener(this);

        getMemberGroupList(true);

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void updateMemberGroup(AppEventApi.UpdateMemberGroupList updateMemberGroupList) {
        getMemberGroupList(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.add_patient_group:
            case R.id.iv_right:
                NBSAppAgent.onEvent("首页-患者管理-添加分组");
                addPatientGroup();
                break;
            case R.id.tv_left:
                onBackPressed();
                break;
        }
    }

    private void addPatientGroup() {

        final View editViewGroup = LayoutInflater.from(this).inflate(R.layout.dialog_add_patient_group, null);

        AlterDialogView alterDialogView = new AlterDialogView.Builder(this)
                .setTitle(R.string.add_patient_group)
                .setContentView(editViewGroup)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.remember_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        String groupName = ((EditText) editViewGroup.findViewById(R.id.patient_group_name)).getEditableText().toString();

                        MemberGroup.Insert insert = new MemberGroup.Insert(groupName, 0, new HttpListener() {
                            @Override
                            public void onError(int code, String msg) {
                                if (DebugUtils.debug) {
                                    Log.d(TAG, DebugUtils.errorFormat("MemberGroup.Insert", code, msg));
                                }

                                ToastUtils.show(PatientGroupListActivity.this, R.string.insert_member_group_fail, Toast.LENGTH_SHORT);
                            }

                            @Override
                            public void onSuccess(Object o) {
                                if (DebugUtils.debug) {
                                    Log.d(TAG, DebugUtils.successFormat("MemberGroup.Insert", DebugUtils.toJson(o)));
                                }

                                ToastUtils.show(PatientGroupListActivity.this, R.string.insert_member_group_success, Toast.LENGTH_SHORT);

//                                getMemberGroupList(true);
                                mEmptyLayout.setVisibility(View.GONE);

                                EventBus.getDefault().postSticky(new AppEventApi.UpdateMemberGroupList());
                            }
                        });

                        HttpClient client = NetService.createClient(editViewGroup.getContext(), HttpClient.DOCTOR_API_URL, insert);
                        client.start();

                    }
                })
                .create();
        alterDialogView.show();

    }

    @Override
    public void onRefreshData() {
        getMemberGroupList(true);
    }

    @Override
    public void onLoadPageData() {
        getMemberGroupList(false);
    }

    static class GroupListAdapter extends CommonAdapter<PatientGroup> {

        public GroupListAdapter(Context context, List<PatientGroup> datas) {
            super(context, R.layout.item_patient_group, datas);
        }

        @Override
        public void convert(ViewHolder viewHolder, PatientGroup obj, int position) {
            ((TextView) viewHolder.findViewById(R.id.tv_name)).setText(obj.groupName);
        }
    }

    private void getMemberGroupList(final boolean refresh) {

        mGroupPageListHelper.setRefreshing(refresh);
        MemberGroup.GetList getList = new MemberGroup.GetList(null, refresh ? 1 : mGroupPageListHelper.getPageIndex(), 100, new HttpListener<ArrayList<PatientGroup>>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("getMemberGroupList", code, msg));
                }
                mGroupPageListHelper.setRefreshing(false);

                if (refresh) {
                    EmptyViewUtils.showErrorView(mContent, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getMemberGroupList(true);
                        }
                    });
                }

            }

            @Override
            public void onSuccess(ArrayList<PatientGroup> patientGroups) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("getMemberGroupList", DebugUtils.toJson(patientGroups)));
                }
                mGroupPageListHelper.setRefreshing(false);
                EmptyViewUtils.removeAllView(mContent);
                if (refresh) {
                    if (patientGroups == null || patientGroups.isEmpty()) {
//                        EmptyViewUtils.showEmptyView(mContent, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                getMemberGroupList(true);
//                            }
//                        });
                        mEmptyLayout.setVisibility(View.VISIBLE);
                    } else {
                        mEmptyLayout.setVisibility(View.GONE);
                        mGroupPageListHelper.refreshData(patientGroups);
                    }
                } else {
                    mEmptyLayout.setVisibility(View.GONE);
                    mGroupPageListHelper.addPageData(patientGroups);
                }

            }
        });
        HttpClient httpClient = NetService.createClient(this, HttpClient.DOCTOR_API_URL, getList);
        httpClient.start();

    }

}
