package com.kmwlyy.doctor.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.PatientList;
import com.kmwlyy.doctor.model.httpEvent.MemberGroup;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.EmptyViewUtils;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Winson on 2017/7/7.
 */

public class PatientSearchActivity extends BaseActivity implements PageListView.OnPageLoadListener {

    public static final String TAG = PatientSearchActivity.class.getSimpleName();

    public static final String SEARCH_RESULT = "SEARCH_RESULT";
    public static final int RESULT_SUCCESS = 10231;
    public static final int REQUEST_PATIENT = 12345;
    public static final String FOR_RESULT = "FOR_RESULT";

    public static void startResultSelf(Activity context) {
        Intent intent = new Intent(context, PatientSearchActivity.class);
        intent.putExtra(FOR_RESULT, true);
        context.startActivityForResult(intent, REQUEST_PATIENT);
    }

    EditText mSearchEdit;
    PageListView mSearchPageListView;
    PageListViewHelper<PatientList> mSearchPageHelper;
    ViewGroup mSearchPanel;
    String mSearchKey;
    boolean mForResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_search);

        mForResult = getIntent().getBooleanExtra(FOR_RESULT, false);
        findViewById(R.id.cancel).setOnClickListener(this);
        mSearchPanel = (ViewGroup) findViewById(R.id.search_panel);
        mSearchEdit = (EditText) findViewById(R.id.edit_search);
        mSearchPageListView = (PageListView) findViewById(R.id.search_page_list);

        mSearchPageHelper = new PageListViewHelper<>(mSearchPageListView, new PatientListAdapter(this, null));
        mSearchPageHelper.setOnPageLoadListener(this);
        mSearchPageHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mForResult) {
                    Intent intent = new Intent();
                    intent.putExtra(SEARCH_RESULT, mSearchPageHelper.getAdapter().getItem(position));
                    setResult(RESULT_SUCCESS, intent);
                    finish();
                } else {
                    //进入患者详情界面
                    Intent intent = new Intent();
                    intent.setClass(PatientSearchActivity.this, PatientDetailActivity.class);
                    intent.putExtra("id", mSearchPageHelper.getAdapter().getItem(position).doctorMemberID);
                    PatientSearchActivity.this.startActivity(intent);
                }

            }
        });

        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchMember(s.toString(), true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.cancel:
                onBackPressed();
                break;
        }
    }

    private void searchMember(final String key, final boolean refresh) {
        EmptyViewUtils.removeAllView(mSearchPanel);
        mSearchPageHelper.setRefreshing(true);
        mSearchKey = key;
        final MemberGroup.GetMemberList getMemberList = new MemberGroup.GetMemberList(null, key,
                refresh ? 1 : mSearchPageHelper.getPageIndex(), mSearchPageHelper.getPageSize(), new HttpListener<ArrayList<PatientList>>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("searchMemberList", code, msg));
                }
                mSearchPageHelper.setRefreshing(false);
                if (mSearchKey == null && key == null) {
                    return;
                }
                if (!key.equals(mSearchKey)) {
                    // new search happened!
                    return;
                }
                EmptyViewUtils.removeAllView(mSearchPanel);
                if (refresh) {
                    EmptyViewUtils.showErrorView(mSearchPanel, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchMember(key, refresh);
                        }
                    });
                }
            }

            @Override
            public void onSuccess(ArrayList<PatientList> patientLists) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("searchMemberList", DebugUtils.toJson(patientLists)));
                }
                mSearchPageHelper.setRefreshing(false);
                if (mSearchKey == null && key == null) {
                    return;
                }
                if (!key.equals(mSearchKey)) {
                    // new search happened!
                    return;
                }
                EmptyViewUtils.removeAllView(mSearchPanel);
                if (refresh && (patientLists == null || patientLists.isEmpty())) {
                    // no data
                    mSearchPageHelper.refreshData(null);
                    EmptyViewUtils.showEmptyView(mSearchPanel, R.mipmap.empty_patient_data, getString(R.string.search_patient_no_data), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchMember(key, refresh);
                        }
                    });
                } else {
                    if (refresh) {
                        mSearchPageHelper.refreshData(patientLists);
                    } else {
                        mSearchPageHelper.addPageData(patientLists);
                    }
                }
            }
        });

        HttpClient client = NetService.createClient(this, HttpClient.DOCTOR_API_URL, getMemberList);
        client.start();

    }

    @Override
    public void onRefreshData() {
        searchMember(mSearchKey, true);
    }

    @Override
    public void onLoadPageData() {
        searchMember(mSearchKey, false);
    }

    static class PatientListAdapter extends CommonAdapter<PatientList> {

        String mAgeFormat;

        public PatientListAdapter(Context context, List<PatientList> datas) {
            super(context, R.layout.item_patient_member, datas);
            mAgeFormat = context.getString(R.string.string_age_format);
        }

        @Override
        public void convert(ViewHolder viewHolder, PatientList data, int position) {
            ((TextView) viewHolder.findViewById(R.id.tv_name)).setText(data.memberName);
            ((TextView) viewHolder.findViewById(R.id.tv_age)).setText(String.format(mAgeFormat, data.age));
            ((TextView) viewHolder.findViewById(R.id.tv_gender)).setText(data.gender == 0 ? context.getString(R.string.string_male) : context.getString(R.string.string_female));
            ((TextView) viewHolder.findViewById(R.id.tv_phone)).setText(data.mobile);
        }
    }

}
