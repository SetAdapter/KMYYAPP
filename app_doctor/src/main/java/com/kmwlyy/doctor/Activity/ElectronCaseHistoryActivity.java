package com.kmwlyy.doctor.Activity;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.UserMemberEMRsInfo;
import com.kmwlyy.doctor.model.httpEvent.UserMemberEMRsEvent;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.LineCheckTextView;
import com.winson.ui.widget.ResizeGridView;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by xcj on 2016/12/28.
 */

public class ElectronCaseHistoryActivity extends BaseActivity implements PageListView.OnPageLoadListener{
    private static final String TAG = ElectronCaseHistoryActivity.class.getSimpleName();
    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.tv_center)
    TextView tv_title;
    @ViewInject(R.id.tv_save)
    TextView mDetailsTxt;
    @ViewInject(R.id.doctor_page_listview)
    PageListView mDoctorPageListview;

    private PageListViewHelper<UserMemberEMRsInfo.ListItem> mPageListViewHelper;
    private HttpClient mGetFamilyDoctorListClient;
    private String memberId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_history);
        ViewUtils.inject(this);
        tv_title.setText("电子病历");
        btn_left.setVisibility(View.VISIBLE);
        tv_title.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        memberId = getIntent().getStringExtra("id");
        mPageListViewHelper = new PageListViewHelper<>(mDoctorPageListview, new AccountDetailListAdapter(ElectronCaseHistoryActivity.this, null));
        int diagnoseListPadding = getResources().getDimensionPixelSize(R.dimen.list_padding);
        mPageListViewHelper.getListView().setDividerHeight(0);
        mPageListViewHelper.getListView().setPadding(0, 0, 0, diagnoseListPadding);
        mPageListViewHelper.getListView().setClipToPadding(false);
        mPageListViewHelper.setOnPageLoadListener(this);
        mPageListViewHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
            ResizeGridView mGridView = (ResizeGridView) viewHolder.findViewById(R.id.grid_pictures);
            final LineCheckTextView describtion = (LineCheckTextView) viewHolder.findViewById(R.id.describtion);
            final TextView display = (TextView) viewHolder.findViewById(R.id.display);
            if (TextUtils.isEmpty(data.mRemark)){
                describtion.setText(Html.fromHtml(String.format(
                        getString(R.string.illness_tracing),
                        "无")));
            }else {
                describtion.setText(Html.fromHtml(String.format(
                        getString(R.string.illness_tracing),
                        data.mRemark)));
            }
            mCaseName.setText("病历名称："+data.mEMRName);
            mName.setText(data.mMemberName);
            mTime.setText(data.mDate.substring(0,10));
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
            describtion.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {

                    describtion.getViewTreeObserver().removeOnPreDrawListener(this);

                    if (describtion.checkIsOver()) {
                        if (describtion.isShowAll()) {
                            display.setText(R.string.pack_up);
                            Drawable drawable = context.getResources().getDrawable(R.mipmap.coll);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            display.setCompoundDrawables(null, null, drawable, null);
                        } else {
                            display.setText(R.string.display);
                            Drawable drawable = context.getResources().getDrawable(R.mipmap.display);
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
                        Drawable drawable = context.getResources().getDrawable(R.mipmap.display);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        display.setCompoundDrawables(null, null, drawable, null);
                        describtion.hiddenPart();
                    } else {
                        display.setText(R.string.pack_up);
                        Drawable drawable = context.getResources().getDrawable(R.mipmap.coll);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        display.setCompoundDrawables(null, null, drawable, null);
                        describtion.showAll();
                    }
                }
            });
        }
    }

    private void getDoctorClinicList(final boolean refresh) {
        mPageListViewHelper.setRefreshing(refresh);
        UserMemberEMRsEvent.Getlist getlist = new UserMemberEMRsEvent.Getlist(memberId,refresh ? 1 : mPageListViewHelper.getPageIndex(),
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



    class ImageAdapter extends BaseAdapter {
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
                    getSquareDisplayOptions(R.mipmap.add_square));

            return convertView;
        }
    }

    private class ImageViewHolder {
        ImageView imageView;
    }

    public static DisplayImageOptions getSquareDisplayOptions(int path) {
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.default_avatar) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(path) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(path) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new SimpleBitmapDisplayer()) // 设置成圆角图片
                .build(); // 构建完成
        return options;
    }
}
