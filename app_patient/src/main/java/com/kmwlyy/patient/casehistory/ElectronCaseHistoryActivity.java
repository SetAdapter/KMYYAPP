package com.kmwlyy.patient.casehistory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.account.AccountManagementActivity;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.BankBean;
import com.kmwlyy.patient.helper.net.bean.DoctorClinic;
import com.kmwlyy.patient.helper.net.bean.UserMemberEMRsInfo;
import com.kmwlyy.patient.helper.net.event.HttpDoctorFreeClinic;
import com.kmwlyy.patient.helper.net.event.UserMemberEMRsEvent;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.LibUtils;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.AlterDialogView;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.LineCheckTextView;
import com.winson.ui.widget.RateLayout;
import com.winson.ui.widget.ResizeGridView;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xcj on 2016/12/12.
 */

public class ElectronCaseHistoryActivity extends BaseActivity implements PageListView.OnPageLoadListener{
    private static final String TAG = ElectronCaseHistoryActivity.class.getSimpleName();
    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.tv_right)
    TextView mDetailsTxt;
    @BindView(R.id.doctor_page_listview)
    PageListView mDoctorPageListview;

    private PageListViewHelper<UserMemberEMRsInfo.ListItem> mPageListViewHelper;
    private HttpClient mGetFamilyDoctorListClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_history);
        butterknife.ButterKnife.bind(this);
        tv_center.setText(getString(R.string.electron_case_history_title));
        mDetailsTxt.setVisibility(View.VISIBLE);
        mDetailsTxt.setText(getString(R.string.add_new));
        mPageListViewHelper = new PageListViewHelper<>(mDoctorPageListview, new AccountDetailListAdapter(ElectronCaseHistoryActivity.this, null));
        int diagnoseListPadding = getResources().getDimensionPixelSize(R.dimen.list_padding);
        mPageListViewHelper.getListView().setDividerHeight(0);
        mPageListViewHelper.getListView().setPadding(0, 0, 0, diagnoseListPadding);
        mPageListViewHelper.getListView().setClipToPadding(false);
        mPageListViewHelper.setOnPageLoadListener(this);
        mPageListViewHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!PUtils.checkHaveUser(ElectronCaseHistoryActivity.this)) {
                    return;
                }
            }
        });
        mDetailsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PUtils.checkHaveUser(ElectronCaseHistoryActivity.this)) {
                    EditCaseHistoryActivity.startEditCaseHistoryActivity(ElectronCaseHistoryActivity.this, false, "");
                }
            }
        });
        getDoctorClinicList(true);
    }

    @Override
    public void onRefreshData() {
        getDoctorClinicList(true);
    }

    @Override
    public void onLoadPageData() {
       getDoctorClinicList(false);
    }

    class AccountDetailListAdapter extends CommonAdapter<UserMemberEMRsInfo.ListItem> {
        public AccountDetailListAdapter(Context context, List<UserMemberEMRsInfo.ListItem> datas) {
            super(context, R.layout.item_case_history, datas);
        }

        @Override
        public void convert(ViewHolder viewHolder, final UserMemberEMRsInfo.ListItem data, int position) {
            TextView mCaseName = (TextView) viewHolder.findViewById(R.id.tv_case_name);
            TextView mName = (TextView) viewHolder.findViewById(R.id.tv_name);
            TextView mTime = (TextView) viewHolder.findViewById(R.id.tv_time);
            TextView mUpdateTime = (TextView) viewHolder.findViewById(R.id.tv_update_time);
            LinearLayout mEditInfo = (LinearLayout) viewHolder.findViewById(R.id.ll_edit_info);
            LinearLayout mDeleteInfo = (LinearLayout) viewHolder.findViewById(R.id.ll_delete_info);
            final ResizeGridView mGridView = (ResizeGridView) viewHolder.findViewById(R.id.grid_pictures);
            final LineCheckTextView describtion = (LineCheckTextView) viewHolder.findViewById(R.id.describtion);
            final TextView display = (TextView) viewHolder.findViewById(R.id.display);
            if (TextUtils.isEmpty(data.mRemark)){
                describtion.setText(Html.fromHtml(String.format(
                        getString(R.string.illness_tracing),
                        getString(R.string.nothing))));
            }else {
                describtion.setText(Html.fromHtml(String.format(
                        getString(R.string.illness_tracing),
                        data.mRemark)));
            }
            mCaseName.setText(getString(R.string.electron_case_name)+data.mEMRName);
            mName.setText(data.mMemberName);
            mTime.setText(data.mDate.substring(0,10));
            mUpdateTime.setText(data.mModifyTime.substring(0,10));
            mGridView.setAdapter(new ImageAdapter(context,data.mFiles));
            ArrayList arrayList = new ArrayList();
            for(UserMemberEMRsInfo.File file: data.mFiles){
                arrayList.add(file.mUrlPrefix+file.mFileUrl);
            }
            final  String[] photos = (String[])arrayList.toArray(new String[arrayList.size()]);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PhotoViewActivity.startPhotoViewActivity(context, photos, position);
                }
            });
            mEditInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳到编辑页面
                    EditCaseHistoryActivity.startEditCaseHistoryActivity(ElectronCaseHistoryActivity.this, true, data.mUserMemberEMRID);
                }
            });
            mDeleteInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除
                    AlterDialogView.Builder builder = new AlterDialogView.Builder(ElectronCaseHistoryActivity.this);
                    builder.setTitle(getString(R.string.case_delete_title));
                    builder.setMessage(getString(R.string.case_delete_note));
                    builder.setNegativeButton(getString(R.string.string_exit_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(getString(R.string.string_exit_yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    arg0.dismiss();
                                    deleteInfo(data.mUserMemberEMRID);
                                }
                            });
                    builder.create().show();

                }
            });
            describtion.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {

                    describtion.getViewTreeObserver().removeOnPreDrawListener(this);

                    if (describtion.checkIsOver()) {
                        if (describtion.isShowAll()) {
                            display.setText(R.string.pack_up);
                            Drawable drawable = context.getResources().getDrawable(R.drawable.coll);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            display.setCompoundDrawables(null, null, drawable, null);
                        } else {
                            display.setText(R.string.display);
                            Drawable drawable = context.getResources().getDrawable(R.drawable.display);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            display.setCompoundDrawables(null, null, drawable, null);
                        }
                        display.setVisibility(View.VISIBLE);
                    } else {
                        display.setVisibility(View.GONE);
                    }

                    return false;
                }
            });

            display.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (describtion.isShowAll()) {
                        display.setText(R.string.display);
                        Drawable drawable = context.getResources().getDrawable(R.drawable.display);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        display.setCompoundDrawables(null, null, drawable, null);
                        describtion.hiddenPart();
                    } else {
                        display.setText(R.string.pack_up);
                        Drawable drawable = context.getResources().getDrawable(R.drawable.coll);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        display.setCompoundDrawables(null, null, drawable, null);
                        describtion.showAll();
                    }
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setCaseHistory(EventApi.CaseHistory caseHistory) {
        getDoctorClinicList(true);
    }

    private void getDoctorClinicList(final boolean refresh) {
        mPageListViewHelper.setRefreshing(refresh);
        UserMemberEMRsEvent.Getlist getlist = new UserMemberEMRsEvent.Getlist("ID",refresh ? 1 : mPageListViewHelper.getPageIndex(),
                mPageListViewHelper.getPageSize(), new HttpListener<ArrayList<UserMemberEMRsInfo.ListItem>>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("getFamilyDoctorList", code, msg));
                }

                mPageListViewHelper.setRefreshing(false);
            }

            @Override
            public void onSuccess(ArrayList<UserMemberEMRsInfo.ListItem> listItems) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("getDoctorClinicList", DebugUtils.toJson(listItems)));
                }
                mPageListViewHelper.setRefreshing(false);
                if (refresh) {
                    mPageListViewHelper.refreshData(listItems);
                } else {
                    mPageListViewHelper.addPageData(listItems);
                }

            }
        });

        mGetFamilyDoctorListClient = NetService.createClient(this, getlist);
        mGetFamilyDoctorListClient.start();

    }

    private void deleteInfo(String id){
        UserMemberEMRsEvent.DeleteInfo deleteInfo = new UserMemberEMRsEvent.DeleteInfo(id, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                //删除失败
                ToastUtils.showShort(ElectronCaseHistoryActivity.this, getString(R.string.case_delete_fail));
            }

            @Override
            public void onSuccess(Object o) {
                //删除成功
                getDoctorClinicList(true);
            }
        });

        mGetFamilyDoctorListClient = NetService.createClient(this, deleteInfo);
        mGetFamilyDoctorListClient.start();
    }

    class ImageAdapter extends BaseAdapter{
        private ArrayList<UserMemberEMRsInfo.File> imagePathList;
        private Context context;
        public ImageAdapter(Context context, ArrayList<UserMemberEMRsInfo.File> imagePath) {
            this.context = context;
            this.imagePathList = imagePath;
        }
        @Override
        public int getCount() {
            return imagePathList == null ? 0 : imagePathList.size();
        }

        @Override
        public Object getItem(int position) {
            return imagePathList == null ? null : imagePathList.get(position).mUrlPrefix+imagePathList.get(position).mFileUrl;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_image_adapter, parent, false);
                holder = new ImageViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.main_gridView_item_photo);
                convertView.setTag(holder);
            } else {
                holder = (ImageViewHolder) convertView.getTag();
            }

                ImageLoader.getInstance().displayImage("" + getItem(position),
                        holder.imageView,
                        LibUtils.getSquareDisplayOptions());

            return convertView;
        }
    }

    private class ImageViewHolder {
        ImageView imageView;
    }
}
