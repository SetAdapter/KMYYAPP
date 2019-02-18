package com.kmwlyy.patient.myservice;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.Doctor;
import com.kmwlyy.patient.helper.net.bean.UserFamilyDoctor;
import com.kmwlyy.patient.helper.net.event.HttpUserFamilyDoctor;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.LibUtils;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.onlinediagnose.DoctorServiceActivity;
import com.kmwlyy.patient.pay.PayActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.EmptyViewUtils;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xcj on 2017/1/6.
 */

public class MyFamilyDoctorActivity extends BaseActivity implements PageListView.OnPageLoadListener{
    private static final String TAG = MyFamilyDoctorActivity.class.getSimpleName();

    @BindView(R.id.doctor_page_listview)
    PageListView mDoctorPageListview;

    private PageListViewHelper<UserFamilyDoctor> mPageListViewHelper;
    private HttpClient mGetFamilyDoctorListClient;
    private ViewGroup mRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_home_doctor);
        butterknife.ButterKnife.bind(this);
        setBarTitle(getString(R.string.home_doctor));
        mPageListViewHelper = new PageListViewHelper<>(mDoctorPageListview, new DoctorListAdapter(MyFamilyDoctorActivity.this, null));
        int diagnoseListPadding = getResources().getDimensionPixelSize(R.dimen.list_padding);
        mPageListViewHelper.getListView().setDividerHeight(0);
        mPageListViewHelper.getListView().setPadding(0, diagnoseListPadding, 0, diagnoseListPadding);
        mPageListViewHelper.getListView().setClipToPadding(false);
        mPageListViewHelper.setOnPageLoadListener(this);
        mPageListViewHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserFamilyDoctor data = mPageListViewHelper.getAdapter().getItem(position);
                if (data.mStatus == UserFamilyDoctor.PAYED) {
                    Doctor.ListItem listItem = new Doctor.ListItem();
                    listItem.mDoctorID = data.mDoctorID;
                    listItem.mDoctorName = data.mDoctorName;
                    listItem.mUser = new Doctor.User();
                    listItem.mUser.mPhotoUrl = data.mDoctorPhotoUrl;
                    listItem.mDepartmentName = data.mDepartmentName;
                    DoctorServiceActivity.startDoctorServiceActivity(MyFamilyDoctorActivity.this, listItem, true);
                }
            }
        });
        getFamilyDoctorList(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NetService.closeClient(mGetFamilyDoctorListClient);
    }

    @Override
    public void onRefreshData() {
        getFamilyDoctorList(true);
    }

    @Override
    public void onLoadPageData() {
        getFamilyDoctorList(false);
    }

    private void getFamilyDoctorList(final boolean refresh) {
        mPageListViewHelper.setRefreshing(refresh);

        HttpUserFamilyDoctor.GetList getFamilyDoctorList = new HttpUserFamilyDoctor.GetList(
                refresh ? 1 : mPageListViewHelper.getPageIndex(),
                mPageListViewHelper.getPageSize(),
                0, 1,
                new HttpListener<ArrayList<UserFamilyDoctor>>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("getFamilyDoctorList", code, msg));
                        }
                        EmptyViewUtils.removeAllView(mRoot);
                        mPageListViewHelper.setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(ArrayList<UserFamilyDoctor> userFamilyDoctors) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("getFamilyDoctorList", DebugUtils.toJson(userFamilyDoctors)));
                        }
                        EmptyViewUtils.removeAllView(mRoot);
                        mPageListViewHelper.setRefreshing(false);
                        if (refresh) {
                            mPageListViewHelper.refreshData(userFamilyDoctors);
                        } else {
                            mPageListViewHelper.addPageData(userFamilyDoctors);
                        }

                    }
                }
        );

        mGetFamilyDoctorListClient = NetService.createClient(MyFamilyDoctorActivity.this, getFamilyDoctorList);
        mGetFamilyDoctorListClient.start();

    }

    class DoctorListAdapter extends CommonAdapter<UserFamilyDoctor> {

        public DoctorListAdapter(Context context, List<UserFamilyDoctor> datas) {
            super(context, R.layout.my_family_doctor_list_item, datas);
        }

        @Override
        public void convert(ViewHolder viewHolder, final UserFamilyDoctor data, int position) {
            TextView serviceDuration = (TextView) viewHolder.findViewById(R.id.service_duration);
            TextView doctorName = (TextView) viewHolder.findViewById(R.id.doctor_name);
            TextView doctorTitle = (TextView) viewHolder.findViewById(R.id.doctor_title);
            TextView doctorDepartment = (TextView) viewHolder.findViewById(R.id.doctor_department);
            TextView hospitalName = (TextView) viewHolder.findViewById(R.id.hospital_name);
            Button haveBuyed = (Button) viewHolder.findViewById(R.id.have_buyed);
            Button waitPay = (Button) viewHolder.findViewById(R.id.wait_pay);
            ImageView nextFlag = (ImageView) viewHolder.findViewById(R.id.next_flag);
            ImageView doctorAvatar = (ImageView) viewHolder.findViewById(R.id.doctor_avatar);

            // 1,//住院医师 2,//主治医师 3,//副主任医师 4,//主任医师
            if ("1".equals(data.mTitle)) {
                doctorTitle.setText(context.getResources().getString(R.string.in_hospital_doctor));
            } else if ("2".equals(data.mTitle)) {
                doctorTitle.setText(context.getResources().getString(R.string.primary_doctor));
            } else if ("3".equals(data.mTitle)) {
                doctorTitle.setText(context.getResources().getString(R.string.second_doctor));
            } else if ("4".equals(data.mTitle)) {
                doctorTitle.setText(context.getResources().getString(R.string.director_doctor));
            } else {
                doctorTitle.setText("");
            }

            serviceDuration.setText(String.format(context.getResources().getString(R.string.service_duration),
                    data.mStartDate.substring(0, 10),
                    data.mEndDate.substring(0, 10)));

            ImageView divider = (ImageView) viewHolder.findViewById(R.id.divider);
            if (getCount() > position + 1) {
                divider.setVisibility(View.GONE);
            } else {
                divider.setVisibility(View.VISIBLE);
            }

            doctorName.setText(data.mDoctorName);
            doctorDepartment.setText(data.mDepartmentName);
            hospitalName.setText("图文咨询: "+data.mPicConsumeCount+"/"+data.mPicServiceCount+"  "+"视频咨询: "+data.mVidConsumeCount+"/"+data.mVidServiceCount);
            ImageLoader.getInstance().displayImage(PUtils.convertUrl(data.mDoctorPhotoUrl), doctorAvatar, LibUtils.getCircleDisplayOptions(R.drawable.default_avatar_doctor));

            switch (data.mStatus) {
                case UserFamilyDoctor.PAYED:
                    haveBuyed.setText(R.string.have_buyed);
                    haveBuyed.setVisibility(View.GONE);
                    waitPay.setVisibility(View.GONE);
                    nextFlag.setVisibility(View.VISIBLE);
                    serviceDuration.setVisibility(View.VISIBLE);
                    break;
                case UserFamilyDoctor.NOT_PAY:
                    haveBuyed.setVisibility(View.GONE);
                    waitPay.setVisibility(View.VISIBLE);
                    nextFlag.setVisibility(View.GONE);
                    serviceDuration.setVisibility(View.GONE);
                    break;
                case UserFamilyDoctor.EXPIRED:
                    haveBuyed.setText(R.string.expired);
                    haveBuyed.setVisibility(View.VISIBLE);
                    haveBuyed.setBackgroundResource(R.drawable.unable_btn);
                    waitPay.setVisibility(View.GONE);
                    nextFlag.setVisibility(View.GONE);
                    serviceDuration.setVisibility(View.VISIBLE);
                    break;
            }

            waitPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    CommonUtils.startActivity(context, PayActivity.class);

                     PayActivity.startPayActivity(context, data.mOrderNo, "0",PayActivity.FAMILY_DOCTOR, true);
                }
            });

        }
    }
}
