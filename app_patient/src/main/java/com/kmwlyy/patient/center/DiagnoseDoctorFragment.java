package com.kmwlyy.patient.center;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseFragment;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.Doctor;
import com.kmwlyy.patient.helper.net.bean.MyDoctor;
import com.kmwlyy.patient.helper.net.event.DiagnoseDoctorEvent;
import com.kmwlyy.patient.helper.net.event.FollowDoctorEvent;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.LibUtils;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.onlinediagnose.DoctorServiceActivity;
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
import butterknife.ButterKnife;

/**
 * @Description描述: 问诊医生列表
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/13
 */
public class DiagnoseDoctorFragment extends BaseFragment implements PageListView.OnPageLoadListener{
    public static final String TAG = DiagnoseDoctorFragment.class.getSimpleName();

    @BindView(R.id.listView)
    PageListView listView;

    private HttpClient getDiagnoseDoctorClient;
    private HttpClient changeFollowClient;
    private PageListViewHelper<MyDoctor> listViewHelper;
    private ProgressDialog mLoadingDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        ButterKnife.bind(this, root);
        initListView();

        return root;
    }


    private void initListView(){
        listViewHelper =  new PageListViewHelper<>(listView, new DiagnoseDoctorFragment.DiagnoseDoctorAdapter(getContext(), null));
        int diagnoseListPadding = getResources().getDimensionPixelSize(R.dimen.list_padding);
//        listViewHelper.getListView().setDividerHeight(0);
        listViewHelper.getListView().setPadding(0, 0, 0, diagnoseListPadding);
        listViewHelper.getListView().setClipToPadding(false);
        listViewHelper.setOnPageLoadListener(this);
        listViewHelper.setRefreshing(true);
        getDiagnoseDoctor(true);
    }

    @Override
    public void onRefreshData() {
        getDiagnoseDoctor(true);
    }

    @Override
    public void onLoadPageData() {
        getDiagnoseDoctor(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshFollowState(EventApi.RefreshFollowState refreshFollowState) {
        listViewHelper.setRefreshing(true);
        onRefreshData();
    }

    private void getDiagnoseDoctor(final boolean refresh){
        DiagnoseDoctorEvent.GetList getDiagnoseDoctorList = new DiagnoseDoctorEvent.GetList(
                refresh ? 1 : listViewHelper.getPageIndex(),
                listViewHelper.getPageSize(),
                new HttpListener<ArrayList<MyDoctor>>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("getDiagnoseDoctorList", code, msg));
                        }
                        listViewHelper.setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(ArrayList<MyDoctor> doctors) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("getDiagnoseDoctorList", DebugUtils.toJson(doctors)));
                        }
                        listViewHelper.setRefreshing(false);
                        if (refresh) {
                            listViewHelper.refreshData(doctors);
                        } else {
                            listViewHelper.addPageData(doctors);
                        }
                    }
                }
        );

        getDiagnoseDoctorClient = NetService.createClient(getContext(), getDiagnoseDoctorList);
        getDiagnoseDoctorClient.start();
    }


    private void changeFollow(final String doctorId, final boolean follow){
        showLoadDialog(R.string.setting);
        FollowDoctorEvent.ChangeFollow changeFollow = new FollowDoctorEvent.ChangeFollow(
                doctorId,
                new HttpListener() {
                    @Override
                    public void onError(int code, String msg) {
                        dismissLoadDialog();

                        if (!follow) {
                            ToastUtils.showLong(getContext(),R.string.follow_fail);
                        }
                        else{
                            ToastUtils.showLong(getContext(),R.string.unfollow_fail);
                        }

                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("changeFollow", code, msg));
                        }
                    }
                    @Override
                    public void onSuccess(Object o) {
                        dismissLoadDialog();
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("changeFollow", ""));
                        }

                        if (!follow) {
                            ToastUtils.showLong(getContext(),R.string.follow_success);
                        }
                        else{
                            ToastUtils.showLong(getContext(),R.string.unfollow_success);
                        }

                        //刷新列表
                        listViewHelper.setRefreshing(true);
                        EventBus.getDefault().post(new EventApi.RefreshFollowState(doctorId));

                    }
                }
        );

        changeFollowClient = NetService.createClient(getContext(), changeFollow);
        changeFollowClient.start();
    }


    /**
     * 显示 Dialog
     */
    public void showLoadDialog(int id) {
        mLoadingDialog = new ProgressDialog(getContext());
        mLoadingDialog.setMessage(getResources().getString(id));
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.show();
    }

    /**
     * 取消 Dialog
     */
    public void dismissLoadDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    class DiagnoseDoctorAdapter extends CommonAdapter<MyDoctor> {

        public DiagnoseDoctorAdapter(Context context, List<MyDoctor> datas) {
            super(context, R.layout.item_doctor, datas);
        }


        @Override
        public void convert(ViewHolder viewHolder, final MyDoctor data, int position) {

            RelativeLayout rl_root = (RelativeLayout) viewHolder.findViewById(R.id.rl_root);
            TextView doctor_name = (TextView) viewHolder.findViewById(R.id.doctor_name);
            TextView doctor_title = (TextView) viewHolder.findViewById(R.id.doctor_title);
            TextView tv_hospital_name = (TextView) viewHolder.findViewById(R.id.tv_hospital_name);
            TextView doctor_department = (TextView) viewHolder.findViewById(R.id.doctor_department);
            ImageView doctor_avatar = (ImageView) viewHolder.findViewById(R.id.doctor_avatar);
            TextView tv_follow = (TextView) viewHolder.findViewById(R.id.tv_follow);

            doctor_name.setText(data.doctorName);

            doctor_title.setText(PUtils.getDoctorTitle(getContext(),data.title));


            if (data.isFollowed)
            {
                tv_follow.setBackgroundResource(R.drawable.btn_unfollow);
                tv_follow.setText(R.string.already_follow);
                tv_follow.setTextColor(getResources().getColor(R.color.unable));
            }
            else{
                tv_follow.setBackgroundResource(R.drawable.btn_follow);
                tv_follow.setText(R.string.follow);
                tv_follow.setTextColor(getResources().getColor(R.color.color_main_green));
            }

            tv_hospital_name.setText(data.hospitalName);
            doctor_department.setText(data.departmentName);

            ImageLoader.getInstance().displayImage(data.portrait, doctor_avatar, LibUtils.getCircleDisplayOptions(R.drawable.default_avatar_doctor));

            rl_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Doctor.ListItem listItem = new Doctor.ListItem();
                        listItem.mDoctorID = data.doctorId;
                        listItem.mDoctorName = data.doctorName;
                        listItem.mDepartmentID = data.departmentId;
                        listItem.mDepartmentName = data.departmentName;
                        listItem.mHospitalName = data.hospitalName;
                        listItem.mGender = data.gender;
                        listItem.mTitle = data.title;


                        listItem.mUser = new Doctor.User();
                        listItem.mUser.mPhotoUrl = data.portrait;

                        DoctorServiceActivity.startDoctorServiceActivity(getContext(), listItem, true);
                    }catch (Exception e){}
                }
            });

            tv_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeFollow(data.doctorId,data.isFollowed);
                }
            });

        }
    }



}
