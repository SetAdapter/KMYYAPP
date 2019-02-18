package com.kmwlyy.doctor.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.kmwlyy.doctor.model.PatientList;
import com.kmwlyy.doctor.model.httpEvent.MemberGroup;
import com.winson.ui.widget.AlterDialogView;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.EmptyViewUtils;
import com.winson.ui.widget.SwipeBackLayout;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Winson on 2017/7/5.
 */

public class PatientGroupDetailActivity extends BaseActivity implements PageListView.OnPageLoadListener {

    public static final String TAG = PatientGroupDetailActivity.class.getSimpleName();

    static final String _GROUP = "_GROUP";

    public static void startSelf(Context context, PatientGroup patientGroup) {
        Intent intent = new Intent(context, PatientGroupDetailActivity.class);
        intent.putExtra(_GROUP, patientGroup);
        context.startActivity(intent);
    }

    PageListView mPatientPageListView;
    PageListViewHelper<PatientList> mPatientListPageHelper;
    PatientGroup mPatientGroup;
    ViewGroup mPatientGroupContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_group_detail);

        mPatientGroup = (PatientGroup) getIntent().getSerializableExtra(_GROUP);

        mPatientGroupContent = (ViewGroup) findViewById(R.id.patient_group);
        ImageView ivRight = (ImageView) findViewById(R.id.iv_right);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.mipmap.icon_tj);
        ivRight.setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_group_name)).setText(String.format(getString(R.string.tv_group_name), mPatientGroup.groupName));
        ((TextView) findViewById(R.id.tv_center)).setText(mPatientGroup.groupName);
        findViewById(R.id.tv_left).setOnClickListener(this);
        findViewById(R.id.delete_group).setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);

        mPatientPageListView = (PageListView) findViewById(R.id.patient_list_view);
        mPatientListPageHelper = new PageListViewHelper<>(mPatientPageListView, new PatientListAdapter(this, null));
        mPatientListPageHelper.setOnPageLoadListener(this);
        mPatientListPageHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //进入患者详情界面
                Intent intent = new Intent();
                intent.setClass(PatientGroupDetailActivity.this, PatientDetailActivity.class);
                intent.putExtra("id", mPatientListPageHelper.getAdapter().getItem(position).doctorMemberID);
                PatientGroupDetailActivity.this.startActivity(intent);
            }
        });

        getPatientList(true);

    }

    private View.OnClickListener mGetPatientListAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getPatientList(true);
        }
    };

    private void getPatientList(final boolean refresh) {
        EmptyViewUtils.removeAllView(mPatientGroupContent);
        mPatientListPageHelper.setRefreshing(true);
        MemberGroup.GetMemberList getMemberList = new MemberGroup.GetMemberList(
                mPatientGroup.memberGroupID,
                refresh ? 1 : mPatientListPageHelper.getPageIndex(),
                mPatientListPageHelper.getPageSize(),
                new HttpListener<ArrayList<PatientList>>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("getMemberList", code, msg));
                        }
                        mPatientListPageHelper.setRefreshing(false);
                        EmptyViewUtils.removeAllView(mPatientGroupContent);
                        if (refresh) {
                            EmptyViewUtils.showErrorView(mPatientGroupContent, mGetPatientListAction);
                        }
                    }

                    @Override
                    public void onSuccess(ArrayList<PatientList> patientLists) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("getMemberList", DebugUtils.toJson(patientLists)));
                        }
                        mPatientListPageHelper.setRefreshing(false);
                        EmptyViewUtils.removeAllView(mPatientGroupContent);
                        if (refresh && (patientLists == null || patientLists.isEmpty())) {
                            // no data
                            mPatientListPageHelper.refreshData(null);
                            EmptyViewUtils.showEmptyView(mPatientGroupContent, R.mipmap.empty_patient_data, getResources().getString(R.string.string_no_patient_data), mGetPatientListAction);
                        } else {
                            if (refresh) {
                                ((PatientListAdapter) mPatientListPageHelper.getAdapter()).clearOpens();
                                mPatientListPageHelper.refreshData(patientLists);
                            } else {
                                mPatientListPageHelper.addPageData(patientLists);
                            }
                        }

                    }
                }
        );

        HttpClient client = NetService.createClient(this, HttpClient.DOCTOR_API_URL, getMemberList);
        client.start();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_left:
                onBackPressed();
                break;
            case R.id.delete_group:
                deletePatientGroup();
                break;
            case R.id.save:

                break;
            case R.id.iv_right:
                PatientSearchActivity.startResultSelf(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PatientSearchActivity.REQUEST_PATIENT && resultCode == PatientSearchActivity.RESULT_SUCCESS) {
            PatientList patientList = (PatientList) data.getSerializableExtra(PatientSearchActivity.SEARCH_RESULT);
            addPatient(patientList);
        }

    }

    private void addPatient(PatientList patientList) {
        showLoadDialog(R.string.string_wait);
        ArrayList<String> datas = new ArrayList<>();
        datas.add(patientList.doctorMemberID);
        MemberGroup.AddMembers addMembers = new MemberGroup.AddMembers(mPatientGroup.memberGroupID, datas, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("addMembers", code, msg));
                }
                dismissLoadDialog();
                ToastUtils.show(PatientGroupDetailActivity.this, R.string.add_member_fail, Toast.LENGTH_SHORT);

            }

            @Override
            public void onSuccess(Object o) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("addMembers", DebugUtils.toJson(o)));
                }
                dismissLoadDialog();
                ToastUtils.show(PatientGroupDetailActivity.this, R.string.add_member_success, Toast.LENGTH_SHORT);
                getPatientList(true);
            }
        });

        HttpClient client = NetService.createClient(this, HttpClient.DOCTOR_API_URL, addMembers);
        client.start();

    }

    private void deletePatientGroup() {
        new AlterDialogView.Builder(this)
                .setTitle(String.format(getString(R.string.delete_group_title), mPatientGroup.groupName))
                .setMessage(String.format(getString(R.string.delete_group_msg), mPatientGroup.groupName))
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
                        showLoadDialog(R.string.string_wait);
                        MemberGroup.Delete delete = new MemberGroup.Delete(mPatientGroup.memberGroupID, new HttpListener() {
                            @Override
                            public void onError(int code, String msg) {
                                if (DebugUtils.debug) {
                                    Log.d(TAG, DebugUtils.errorFormat("delete member group", code, msg));
                                }
                                dismissLoadDialog();
                                ToastUtils.show(PatientGroupDetailActivity.this, R.string.delete_group_fail, Toast.LENGTH_SHORT);

                            }

                            @Override
                            public void onSuccess(Object o) {
                                if (DebugUtils.debug) {
                                    Log.d(TAG, DebugUtils.successFormat("delete member group", DebugUtils.toJson(o)));
                                }
                                dismissLoadDialog();
                                ToastUtils.show(PatientGroupDetailActivity.this, R.string.delete_group_success, Toast.LENGTH_SHORT);

                                EventBus.getDefault().postSticky(new AppEventApi.UpdateMemberGroupList());
                                onBackPressed();

                            }
                        });

                        HttpClient client = NetService.createClient(PatientGroupDetailActivity.this, HttpClient.DOCTOR_API_URL, delete);
                        client.start();

                    }
                })
                .create()
                .show();
    }

    @Override
    public void onRefreshData() {
        getPatientList(true);
    }

    @Override
    public void onLoadPageData() {
        getPatientList(false);
    }

    class PatientListAdapter extends CommonAdapter<PatientList> {

        private HashSet<Integer> mOpenDatas = new HashSet<>();
        String mAgeFormat;

        public PatientListAdapter(Context context, List<PatientList> datas) {
            super(context, R.layout.item_patient_member_delete, datas);
            mAgeFormat = context.getString(R.string.string_age_format);
        }

        public void clearOpens() {
//            mOpenDatas.clear();
        }

        @Override
        public void convert(ViewHolder viewHolder, final PatientList data, final int position) {

            SwipeBackLayout swipeBackLayout = (SwipeBackLayout) viewHolder.findViewById(R.id.swipe_content);

            final View content = viewHolder.findViewById(R.id.content);
            View rightDelete = viewHolder.findViewById(R.id.right_delete);

            swipeBackLayout.setDragContent(content);
            swipeBackLayout.setDragRight(rightDelete);

            if (mOpenDatas.contains(position)) {
                swipeBackLayout.open(true);
            } else {
                swipeBackLayout.open(false);
            }

            rightDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> ids = new ArrayList<String>();
                    ids.add(data.doctorMemberID);
                    MemberGroup.RemoveMembers removeMembers = new MemberGroup.RemoveMembers(mPatientGroup.memberGroupID, ids, new HttpListener() {

                        @Override
                        public void onError(int code, String msg) {
                            if (DebugUtils.debug) {
                                Log.d(TAG, DebugUtils.errorFormat("removeMembers", code, msg));
                            }
                            ToastUtils.show(context, R.string.delete_patient_fail, Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onSuccess(Object o) {
                            if (DebugUtils.debug) {
                                Log.d(TAG, DebugUtils.successFormat("removeMembers", DebugUtils.toJson(o)));
                            }

                            ToastUtils.show(context, R.string.delete_patient_success, Toast.LENGTH_SHORT);

                            getPatientList(true);
                        }

                    });

                    HttpClient client = NetService.createClient(v.getContext(), HttpClient.DOCTOR_API_URL, removeMembers);
                    client.start();

                }
            });

            swipeBackLayout.setOnOpenListener(new SwipeBackLayout.OnOpenListener() {
                @Override
                public void opened(boolean opened) {
                    Log.d(TAG, "OnOpenListener -- > position : " + position + " , opened : " + opened);
                    if (opened) {
                        mOpenDatas.add(position);
                    } else {
                        mOpenDatas.remove(position);
                    }
                }
            });

            ((TextView) viewHolder.findViewById(R.id.tv_name)).setText(data.memberName);
            ((TextView) viewHolder.findViewById(R.id.tv_age)).setText(String.format(mAgeFormat, data.age));
            ((TextView) viewHolder.findViewById(R.id.tv_gender)).setText(data.gender == 0 ? context.getString(R.string.string_male) : context.getString(R.string.string_female));
            ((TextView) viewHolder.findViewById(R.id.tv_phone)).setText(data.mobile);
        }
    }

}
