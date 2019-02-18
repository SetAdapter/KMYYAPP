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
import com.kmwlyy.patient.helper.net.event.ExpertRecommendEvent;
import com.kmwlyy.patient.helper.utils.LibUtils;
import com.kmwlyy.patient.helper.utils.PUtils;
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
 * @Description描述: 专家推荐列表页面
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/16
 */
public class ExpertRecommendListActivity extends BaseActivity implements PageListView.OnPageLoadListener{
    private static final String TAG = ExpertRecommendListActivity.class.getSimpleName();

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
        tv_center.setText(R.string.expert_recommend);
    }

    private void initListView(){
        listViewHelper =  new PageListViewHelper<>(listView, new ExpertRecommendListActivity.ExpertRecommendAdapter(ExpertRecommendListActivity.this, null));
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
                    DoctorServiceActivity.startDoctorServiceActivity(ExpertRecommendListActivity.this, listItem, true);
                }catch (Exception e){}
            }
        });

        getExpertRecommend(true);
    }

    @Override
    public void onRefreshData() {
        getExpertRecommend(true);
    }

    @Override
    public void onLoadPageData() {
        getExpertRecommend(false);
    }

    private void getExpertRecommend(final boolean refresh){
        ExpertRecommendEvent.GetList getExpertRecommend = new ExpertRecommendEvent.GetList(
                refresh ? 1 : listViewHelper.getPageIndex(),
                listViewHelper.getPageSize(),
                new HttpListener<ArrayList<Doctor.ListItem>>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("getExpertRecommendList", code, msg));
                        }
                        listViewHelper.setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(ArrayList<Doctor.ListItem> homeDoctors) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("getExpertRecommendList", DebugUtils.toJson(homeDoctors)));
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

        getHomeDoctorClient = NetService.createClient(this, getExpertRecommend);
        getHomeDoctorClient.start();
    }


    class ExpertRecommendAdapter extends CommonAdapter<Doctor.ListItem> {

        public ExpertRecommendAdapter(Context context, List<Doctor.ListItem> datas) {
            super(context, R.layout.doctor_list_item, datas);
        }


        @Override
        public void convert(ViewHolder viewHolder, Doctor.ListItem data, int position) {
            ((TextView) viewHolder.findViewById(R.id.doctor_name)).setText(data.mDoctorName);
            ((TextView) viewHolder.findViewById(R.id.department)).setText(data.mDepartmentName);
            ((TextView) viewHolder.findViewById(R.id.hospital_name)).setText(data.mHospitalName);
            ((TextView) viewHolder.findViewById(R.id.tv_specialty)).setText(data.mSpecialty);
//            ((TextView) viewHolder.findViewById(R.id.diagnose_quantity)).setText(""+data.mDiagnoseNum);
            TextView doctorTitle = (TextView) viewHolder.findViewById(R.id.doctor_title);
            doctorTitle.setText(PUtils.getDoctorTitle(ExpertRecommendListActivity.this,data.mTitle));
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
                        }else{
                            image.setText("未开通");
                            image.setEnabled(false);
                        }
                        break;
                    case Doctor.DoctorService.IM_CONSULT:
                        if (service.mServiceSwitch == Doctor.DoctorService.SERVICE_ON)
                        {
                            im.setText("¥"+service.mServicePrice);
                            im.setEnabled(true);
                        }else{
                            im.setText("未开通");
                            im.setEnabled(false);
                        }
                        break;
                    case Doctor.DoctorService.VIDEO_CONSULT:
                        if (service.mServiceSwitch == Doctor.DoctorService.SERVICE_ON)
                        {
                            video.setText("¥"+service.mServicePrice);
                            video.setEnabled(true);
                        }else{
                            video.setText("未开通");
                            video.setEnabled(false);
                        }
                        break;
                    case Doctor.DoctorService.FAMILY_DOCTOR_CONSULT:
                        if (service.mServiceSwitch == Doctor.DoctorService.SERVICE_ON)
                        {
                            homeDoctor.setText("¥"+service.mServicePrice);
                            homeDoctor.setEnabled(true);
                        }else{
                            homeDoctor.setText("未开通");
                            homeDoctor.setEnabled(false);
                        }
                        break;
                }
            }

        }
    }
}
