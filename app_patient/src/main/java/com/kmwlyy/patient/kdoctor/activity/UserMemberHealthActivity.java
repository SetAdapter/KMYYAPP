package com.kmwlyy.patient.kdoctor.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.UserMember;
import com.kmwlyy.core.net.event.HttpUserMember;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.patient.R;

import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
/**
 * Created by xcj on 2017/8/22.
 */

public class UserMemberHealthActivity extends Activity implements View.OnClickListener, PageListView.OnPageLoadListener{
    public static final String TAG = UserMemberHealthActivity.class.getSimpleName();

    public static final String USER_MEMBER_INFO = "USER_MEMBER_INFO";
    public static final int SELECT_USER_MEMBER_REQ = 10001;

    @BindView(R.id.tv_left)
    TextView mLeftTxt;
    @BindView(R.id.tv_center)
    TextView mToolbarTitle;
    @BindView(R.id.tv_right)
    TextView mAdd;
    //    @BindView(R2.id.user_member_page_list)
    PageListView mUserMemberPageList;

    private PageListViewHelper<UserMember> mUserMemberPageListHelper;
    private HttpClient mGetUserMemberListClient;
    private boolean mIsSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_member_list);
        butterknife.ButterKnife.bind(this);
        mToolbarTitle.setText("家庭成员");
        mLeftTxt.setOnClickListener(this);
        mUserMemberPageList = (PageListView) findViewById(R.id.user_member_page_list);
        mUserMemberPageListHelper = new PageListViewHelper<>(mUserMemberPageList, new UserMemberListAdapter(this, this, null));
        int listPadding = getResources().getDimensionPixelSize(com.kmwlyy.usermember.R.dimen.list_padding);
        mUserMemberPageListHelper.getListView().setPadding(0, listPadding, 0, listPadding);
        mUserMemberPageListHelper.getListView().setClipToPadding(false);
        mUserMemberPageListHelper.getListView().setDivider(getResources().getDrawable(com.kmwlyy.usermember.R.drawable.transparent));
        mUserMemberPageListHelper.getListView().setDividerHeight(listPadding);
        mUserMemberPageListHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resultFinish(position);
            }
        });
        mUserMemberPageListHelper.setOnPageLoadListener(this);

        mLeftTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getUserMemberList(true);
    }

    private void resultFinish(int position) {
        String idnumber = mUserMemberPageListHelper.getAdapter().getData().get(position).mIDNumber;
        Intent intent = new Intent(UserMemberHealthActivity.this, HealthReportActivity.class);
        intent.putExtra("IDNUMBER", idnumber);
        startActivity(intent);
    }

    @Override
    public void onRefreshData() {
        getUserMemberList(true);
    }

    @Override
    public void onLoadPageData() {
        getUserMemberList(false);
    }

    static class UserMemberListAdapter extends CommonAdapter<UserMember> {
        private UserMemberHealthActivity mUserMemberListActivity;

        public UserMemberListAdapter(UserMemberHealthActivity userMemberListActivity, Context context, List<UserMember> datas) {
            super(context, R.layout.item_user_member_select_list, datas);
            mUserMemberListActivity = userMemberListActivity;
        }

        @Override
        public void convert(ViewHolder viewHolder, final UserMember data, final int position) {

            viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mUserMemberListActivity.resultFinish(position);
                }
            });
            ((TextView) viewHolder.findViewById(R.id.member_name)).setText(data.mMemberName);
            if (TextUtils.isEmpty(data.mMobile)){
                ((TextView) viewHolder.findViewById(R.id.tv_mobile)).setVisibility(View.GONE);
            }else {
                ((TextView) viewHolder.findViewById(R.id.tv_mobile)).setVisibility(View.VISIBLE);
                ((TextView) viewHolder.findViewById(R.id.tv_mobile)).setText("手机号码:"+"  "+data.mMobile);
            }
            if (TextUtils.isEmpty(data.mIDNumber)){
                ((TextView) viewHolder.findViewById(R.id.tv_idCard_number)).setVisibility(View.GONE);
            }else {
                ((TextView) viewHolder.findViewById(R.id.tv_idCard_number)).setVisibility(View.VISIBLE);
                //显示身份证
                String mIDNumber = data.mIDNumber;
                ((TextView) viewHolder.findViewById(R.id.tv_idCard_number)).setText("身份证号:"+"  "+mIDNumber.substring(0, 6) + "********" + mIDNumber.substring(14,18));
            }
            ((TextView) viewHolder.findViewById(R.id.tv_birth)).setText(data.mBirthday);
            if (data.mIsDefault) {
                ((TextView) viewHolder.findViewById(R.id.tv_default)).setVisibility(View.VISIBLE);
            }else{
                ((TextView) viewHolder.findViewById(R.id.tv_default)).setVisibility(View.GONE);
            }
            if (data.mGender == 0){
                ((TextView) viewHolder.findViewById(R.id.member_gender)).setText("男");
            }else if((data.mGender == 1)){
                ((TextView) viewHolder.findViewById(R.id.member_gender)).setText("女");
            }else{
                ((TextView) viewHolder.findViewById(R.id.member_gender)).setText("未知");
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.navigation_btn:
                onBackPressed();
                break;
        }
    }

    private void getUserMemberList(final boolean refresh) {
        mUserMemberPageListHelper.setRefreshing(refresh);
        HttpUserMember.GetList getUserMemberList = new HttpUserMember.GetList(
                refresh ? 1 : mUserMemberPageListHelper.getPageIndex(),
                mUserMemberPageListHelper.getPageSize(), new HttpListener<ArrayList<UserMember>>() {
            @Override
            public void onError(int code, String msg) {
                LogUtils.d(TAG, "getUserMemberList error, code : " + code + " , msg : " + msg);

                mUserMemberPageListHelper.setRefreshing(false);
            }

            @Override
            public void onSuccess(ArrayList<UserMember> userMembers) {
                if (DebugUtils.debug) {
                    Log.d(TAG, "getUserMemberList result : " + userMembers);
                }

                mUserMemberPageListHelper.setRefreshing(false);
                if (refresh) {
                    mUserMemberPageListHelper.refreshData(userMembers);
                } else {
                    mUserMemberPageListHelper.addPageData(userMembers);
                }

            }
        });

        mGetUserMemberListClient = new HttpClient(this, getUserMemberList);
        mGetUserMemberListClient.start();

    }

}
