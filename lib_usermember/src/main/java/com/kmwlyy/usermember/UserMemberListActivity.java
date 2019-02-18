package com.kmwlyy.usermember;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.UserMember;
import com.kmwlyy.core.net.event.HttpUserMember;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.SPUtils;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.RateLayout;
import com.winson.ui.widget.ToastMananger;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Winson on 2016/8/17.
 */
public class UserMemberListActivity extends Activity implements View.OnClickListener, PageListView.OnPageLoadListener {

    public static final String TAG = UserMemberListActivity.class.getSimpleName();

    @BindView(R2.id.tv_left)
    TextView mLeftTxt;
    @BindView(R2.id.tv_center)
    TextView mToolbarTitle;
    @BindView(R2.id.tv_right)
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
        mAdd.setText(R.string.add);
        mAdd.setVisibility(View.VISIBLE);

        EventBus.getDefault().register(this);
        mToolbarTitle.setText(R.string.user_member);

        mLeftTxt.setOnClickListener(this);
        mAdd.setOnClickListener(this);

        mUserMemberPageList = (PageListView) findViewById(R.id.user_member_page_list);
        mUserMemberPageListHelper = new PageListViewHelper<>(mUserMemberPageList, new UserMemberListAdapter(this, this, null));
        int listPadding = getResources().getDimensionPixelSize(R.dimen.list_padding);
        mUserMemberPageListHelper.getListView().setPadding(0, listPadding, 0, listPadding);
        mUserMemberPageListHelper.getListView().setClipToPadding(false);
        mUserMemberPageListHelper.getListView().setDivider(getResources().getDrawable(R.drawable.transparent));
        mUserMemberPageListHelper.getListView().setDividerHeight(listPadding);
        mUserMemberPageListHelper.setOnPageLoadListener(this);

        mLeftTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.startActivity(UserMemberListActivity.this, UserMemberEditActivity.class);
            }
        });

        getUserMemberList(true);
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

        private ProgressDialog mDeleteDialog;
        private ProgressDialog mSetDefaultDialog;
        private UserMemberListActivity mUserMemberListActivity;

        public UserMemberListAdapter(UserMemberListActivity userMemberListActivity, Context context, List<UserMember> datas) {
            super(context, R.layout.user_member_list_item, datas);
            mUserMemberListActivity = userMemberListActivity;
        }

        @Override
        public void convert(ViewHolder viewHolder, final UserMember data, final int position) {

            if(data.mRelation == 0){
                Event.UserMemberChange memberChange = new Event.UserMemberChange();
                memberChange.nickname = data.mMemberName;
                memberChange.mobile = data.mMobile;
                EventBus.getDefault().post(memberChange);
            }
            ((TextView) viewHolder.findViewById(R.id.member_name)).setText(data.mMemberName);
            ((TextView) viewHolder.findViewById(R.id.member_mobile)).setText(data.mMobile);
            viewHolder.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserMemberEditActivity.startUserMemberEditActivity(context, true, data);
                }
            });

            CheckBox isDefault = ((CheckBox) viewHolder.findViewById(R.id.set_default));

            isDefault.setChecked(data.mIsDefault);
            isDefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSetDefaultDialog(context);

                    HttpUserMember.SetDefault setDefault = new HttpUserMember.SetDefault(data.mMemberID, new HttpListener() {
                        @Override
                        public void onError(int code, String msg) {
                            if (DebugUtils.debug) {
                                Log.d(TAG, DebugUtils.errorFormat("setDefault", code, msg));
                            }
                            dismissSetDefaultDialog();

                        }

                        @Override
                        public void onSuccess(Object o) {
                            dismissSetDefaultDialog();

                            EventBus.getDefault().post(new Event.UserMemberUpdated());
                        }
                    });

                    HttpClient setDefaultClient = new HttpClient(context, setDefault);
                    setDefaultClient.start();

                }
            });

            viewHolder.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteDialog(context);

                    HttpUserMember.Delete deleteUserMember = new HttpUserMember.Delete(data.mMemberID, new HttpListener() {
                        @Override
                        public void onError(int code, String msg) {
                            if (DebugUtils.debug) {
                                Log.d(TAG, DebugUtils.errorFormat("deleteUserMember", code, msg));
                            }
                            ToastMananger.showToast(context, R.string.delete_user_member_failed, Toast.LENGTH_SHORT);

                            dismissDeleteDialog();
                        }

                        @Override
                        public void onSuccess(Object o) {
                            String statu = (String) o;
                            int status = Integer.parseInt(statu);
                            if (DebugUtils.debug) {
                                Log.d(TAG, DebugUtils.successFormat("deleteUserMember", statu));
                            }
                            if (status == 1) {
                                ToastMananger.showToast(context, R.string.delete_user_member_success, Toast.LENGTH_SHORT);
                            } else if (status == 2) {
                                ToastMananger.showToast(context, "有预约记录不能删除", Toast.LENGTH_SHORT);
                            } else if (status == 3) {
                                ToastMananger.showToast(context, "成员关系为本人的记录不能删除", Toast.LENGTH_SHORT);
                            }
                            dismissDeleteDialog();
                            EventBus.getDefault().post(new Event.UserMemberUpdated());
                        }

                    });

                    HttpClient deleteUserMemberClient = new HttpClient(context, deleteUserMember);
                    deleteUserMemberClient.start();

                }
            });
        }

        private void showDeleteDialog(Context context) {
            mDeleteDialog = new ProgressDialog(context);
            mDeleteDialog.setMessage(context.getResources().getString(R.string.on_delete_user_member));
            mDeleteDialog.setCancelable(false);
            mDeleteDialog.show();
        }

        private void dismissDeleteDialog() {
            if (mDeleteDialog != null) {
                mDeleteDialog.dismiss();
            }
        }

        private void showSetDefaultDialog(Context context) {
            mSetDefaultDialog = new ProgressDialog(context);
            mSetDefaultDialog.setMessage(context.getResources().getString(R.string.on_update_user_member));
            mSetDefaultDialog.setCancelable(false);
            mSetDefaultDialog.show();
        }

        private void dismissSetDefaultDialog() {
            if (mSetDefaultDialog != null) {
                mSetDefaultDialog.dismiss();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R2.id.navigation_btn:
                onBackPressed();
                break;
            case R2.id.tv_right:
                CommonUtils.startActivity(this, UserMemberEditActivity.class);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddUserMember(Event.UserMemberUpdated addUserMember) {
        getUserMemberList(true);
    }

}
