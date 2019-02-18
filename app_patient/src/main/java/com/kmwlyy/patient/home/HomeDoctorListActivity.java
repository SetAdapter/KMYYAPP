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
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.Doctor;
import com.kmwlyy.patient.helper.net.event.HomeDoctorEvent;
import com.kmwlyy.patient.helper.utils.LibUtils;
import com.kmwlyy.patient.onlinediagnose.DoctorServiceActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Description描述: 家庭医生列表
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/13
 */
public class HomeDoctorListActivity extends BaseActivity implements PageListView.OnPageLoadListener{
    private static final String TAG = HomeDoctorListActivity.class.getSimpleName();

    @BindView(R.id.listView)
    PageListView listView;
    @BindView(R.id.tv_center)
    TextView tv_center;

    private HttpClient getHomeDoctorClient;
    private PageListViewHelper<Doctor.ListItem> listViewHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_listview);
        butterknife.ButterKnife.bind(this);

        initToolBar();
        initListView();
    }

    private void initToolBar(){
        tv_center.setText(R.string.home_doctor);
    }

    private void initListView(){
        listViewHelper =  new PageListViewHelper<>(listView, new HomeDoctorListActivity.HomeDoctorAdapter(HomeDoctorListActivity.this, null));
        int diagnoseListPadding = getResources().getDimensionPixelSize(R.dimen.list_padding);
//        listViewHelper.getListView().setDividerHeight(0);
        listViewHelper.getListView().setPadding(0, 0, 0, diagnoseListPadding);
        listViewHelper.getListView().setClipToPadding(false);
        listViewHelper.setOnPageLoadListener(this);
        listViewHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Doctor.ListItem data = listViewHelper.getAdapter().getItem(position);
                    Doctor.ListItem listItem = new Doctor.ListItem();
                    listItem.mDoctorID = data.mDoctorID;
                    listItem.mDoctorName = data.mDoctorName;
                    listItem.mUser = new Doctor.User();
                    listItem.mUser.mPhotoUrl = data.mUser.mPhotoUrl;
                    listItem.mDepartmentName = data.mDepartmentName;
                    DoctorServiceActivity.startDoctorServiceActivity(HomeDoctorListActivity.this, listItem, true);
                }catch (Exception e){}
            }
        });

        getHomeDoctor(true);
    }

    @Override
    public void onRefreshData() {
        getHomeDoctor(true);
    }

    @Override
    public void onLoadPageData() {
        getHomeDoctor(false);
    }

    private void getHomeDoctor(final boolean refresh){
        HomeDoctorEvent.GetList getHomeDoctor = new HomeDoctorEvent.GetList(
                refresh ? 1 : listViewHelper.getPageIndex(),
                listViewHelper.getPageSize(),
                new HttpListener<ArrayList<Doctor.ListItem>>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("getHomeDoctorList", code, msg));
                        }
                        listViewHelper.setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(ArrayList<Doctor.ListItem> homeDoctors) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("getHomeDoctorList", DebugUtils.toJson(homeDoctors)));
                        }
                        listViewHelper.setRefreshing(false);
                        if (refresh) {
                            listViewHelper.refreshData(homeDoctors);
                        } else {
                            listViewHelper.addPageData(homeDoctors);
                        }
                    }
                }
        );

        getHomeDoctorClient = NetService.createClient(this, getHomeDoctor);
        getHomeDoctorClient.start();
    }


    class HomeDoctorAdapter extends CommonAdapter<Doctor.ListItem> {

        public HomeDoctorAdapter(Context context, List<Doctor.ListItem> datas) {
            super(context, R.layout.doctor_list_item, datas);
        }


        @Override
        public void convert(ViewHolder viewHolder, Doctor.ListItem data, int position) {
            ((TextView) viewHolder.findViewById(R.id.doctor_name)).setText(data.mDoctorName);
            ((TextView) viewHolder.findViewById(R.id.department)).setText(data.mDepartmentName);
            ((TextView) viewHolder.findViewById(R.id.hospital_name)).setText(data.mHospitalName);
            ((TextView) viewHolder.findViewById(R.id.tv_specialty)).setText(data.mSpecialty);
            ((TextView) viewHolder.findViewById(R.id.diagnose_quantity)).setText(""+data.mDiagnoseNum);
            TextView doctorTitle = (TextView) viewHolder.findViewById(R.id.doctor_title);
            doctorTitle.setText(data.mTitle);
            ImageView avatar = (ImageView) viewHolder.findViewById(R.id.avatar);
            ImageLoader.getInstance().displayImage(data.mUser.mPhotoUrl, avatar, LibUtils.getCircleDisplayOptions(R.drawable.default_avatar_doctor));

            TextView image = (TextView) viewHolder.findViewById(R.id.image);
            TextView video = (TextView) viewHolder.findViewById(R.id.video);
            TextView im = (TextView) viewHolder.findViewById(R.id.im);
            TextView homeDoctor = (TextView) viewHolder.findViewById(R.id.home_doctor);

            for (Doctor.DoctorService service : data.mDoctorServices) {
                switch (service.mServiceType) {
                    case Doctor.DoctorService.IMAGE_CONSULT:
//                            image.setText(String.format(mPriceFormat, service.mServicePrice));
                        if (service.mServiceSwitch == Doctor.DoctorService.SERVICE_ON)
                        {
                            image.setText("¥"+service.mServicePrice);
                            image.setEnabled(true);
                        }
                        break;
                    case Doctor.DoctorService.IM_CONSULT:
                        if (service.mServiceSwitch == Doctor.DoctorService.SERVICE_ON)
                        {
                            im.setText("¥"+service.mServicePrice);
                            im.setEnabled(true);
                        }
                        break;
                    case Doctor.DoctorService.VIDEO_CONSULT:
                        if (service.mServiceSwitch == Doctor.DoctorService.SERVICE_ON)
                        {
                            video.setText("¥"+service.mServicePrice);
                            video.setEnabled(true);
                        }
                        break;
                    case Doctor.DoctorService.FAMILY_DOCTOR_CONSULT:
                        if (service.mServiceSwitch == Doctor.DoctorService.SERVICE_ON)
                        {
                            homeDoctor.setText("¥"+service.mServicePrice);
                            homeDoctor.setEnabled(true);
                        }
                        break;
                }
            }

        }
    }

}
