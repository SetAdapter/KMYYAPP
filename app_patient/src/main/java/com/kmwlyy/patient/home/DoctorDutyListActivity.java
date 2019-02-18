package com.kmwlyy.patient.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.onlinediagnose.BuyIMConsultActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.DoctorClinic;
import com.kmwlyy.patient.helper.net.event.HttpDoctorFreeClinic;
import com.kmwlyy.patient.helper.net.event.HttpUserConsults;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.LibUtils;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.CommonAdapter;
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
 * Created by xcj on 2016/9/2.
 */
public class DoctorDutyListActivity extends BaseActivity implements PageListView.OnPageLoadListener {
    private static final String TAG = DoctorDutyListActivity.class.getSimpleName();
    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.doctor_page_listview)
    PageListView mDoctorPageListview;

    private PageListViewHelper<DoctorClinic> mPageListViewHelper;
    private HttpClient mGetFamilyDoctorListClient;
    private View root = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_duty_list);
        butterknife.ButterKnife.bind(this);
        tv_center.setText(R.string.today_duty_diagnose);
        mPageListViewHelper = new PageListViewHelper<>(mDoctorPageListview, new DoctorClinicListAdapter(DoctorDutyListActivity.this, null));
        int diagnoseListPadding = getResources().getDimensionPixelSize(R.dimen.list_padding);
        mPageListViewHelper.getListView().setDividerHeight(0);
        mPageListViewHelper.getListView().setPadding(0, 0, 0, diagnoseListPadding);
        mPageListViewHelper.getListView().setClipToPadding(false);
        mPageListViewHelper.setOnPageLoadListener(this);
        mPageListViewHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!PUtils.checkHaveUser(DoctorDutyListActivity.this)) {
                    return;
                }
                if (mPageListViewHelper.getAdapter().getItem(position).mDoctorClinic.mAcceptCount-mPageListViewHelper.getAdapter().getItem(position).mDoctorClinic.mCurrentCount <= 0){
                    ToastUtils.showLong(DoctorDutyListActivity.this, R.string.doctor_clinic_unused);
                    return;
                }
                BuyIMConsultActivity.startBuyIMConsultActivity(DoctorDutyListActivity.this, mPageListViewHelper.getAdapter().getItem(position).mDoctorID, HttpUserConsults.DUTY, 0+"");
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void duty(EventApi.Duty duty) {
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NetService.closeClient(mGetFamilyDoctorListClient);
        EventBus.getDefault().post(new EventApi.DutyList());
    }

    class DoctorClinicListAdapter extends CommonAdapter<DoctorClinic> {
        public DoctorClinicListAdapter(Context context, List<DoctorClinic> datas) {
            super(context, R.layout.doctor_clinic_list, datas);
        }

        @Override
        public void convert(ViewHolder viewHolder, DoctorClinic data, int position) {
            TextView serviceDuration = (TextView) viewHolder.findViewById(R.id.service_duration);
            TextView surplusTitle = (TextView) viewHolder.findViewById(R.id.tv_surplus_title);
            TextView doctorName = (TextView) viewHolder.findViewById(R.id.doctor_name);
            TextView doctorTitle = (TextView) viewHolder.findViewById(R.id.doctor_title);
            TextView doctorDepartment = (TextView) viewHolder.findViewById(R.id.doctor_department);
            TextView hospitalName = (TextView) viewHolder.findViewById(R.id.hospital_name);
            ImageView doctorAvatar = (ImageView) viewHolder.findViewById(R.id.doctor_avatar);

            doctorName.setText(data.mDoctorName);
            doctorTitle.setText(PUtils.getDoctorTitle(context,data.mTitle));
            doctorDepartment.setText(data.mDepartmentName);
            hospitalName.setText(data.mHospitalName);
            if (data.mDoctorClinic != null) {
                surplusTitle.setText(R.string.doctor_clinic_unused_chance);
                serviceDuration.setText(data.mDoctorClinic.mAcceptCount - data.mDoctorClinic.mCurrentCount + "/" + data.mDoctorClinic.mAcceptCount);
            } else {
                surplusTitle.setText(R.string.doctor_clinic_no_limit);
            }
            ImageLoader.getInstance().displayImage(PUtils.convertUrl(data.mUser.mPhotoUrl), doctorAvatar, LibUtils.getCircleDisplayOptions(R.drawable.default_avatar_doctor));
        }
    }

    private void getDoctorClinicList(final boolean refresh) {
        mPageListViewHelper.setRefreshing(refresh);

        HttpDoctorFreeClinic.GetList getFamilyDoctorList = new HttpDoctorFreeClinic.GetList(
                refresh ? 1 : mPageListViewHelper.getPageIndex(),
                mPageListViewHelper.getPageSize(),
                new HttpListener<ArrayList<DoctorClinic>>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("getFamilyDoctorList", code, msg));
                        }

                        mPageListViewHelper.setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(ArrayList<DoctorClinic> userFamilyDoctors) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("getDoctorClinicList", DebugUtils.toJson(userFamilyDoctors)));
                        }

                        mPageListViewHelper.setRefreshing(false);
                        if (refresh) {
                            mPageListViewHelper.refreshData(userFamilyDoctors);
                        } else {
                            mPageListViewHelper.addPageData(userFamilyDoctors);
                        }

                    }
                }
        );

        mGetFamilyDoctorListClient = NetService.createClient(this, getFamilyDoctorList);
        mGetFamilyDoctorListClient.start();

    }
}
