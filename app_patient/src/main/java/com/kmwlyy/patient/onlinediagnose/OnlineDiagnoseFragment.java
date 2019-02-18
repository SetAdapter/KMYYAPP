package com.kmwlyy.patient.onlinediagnose;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseFragment;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.Doctor;
import com.kmwlyy.patient.helper.net.event.HttpDoctor;
import com.kmwlyy.patient.helper.utils.LibUtils;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Winson on 2016/8/9.
 */
public class OnlineDiagnoseFragment extends BaseFragment implements PageListView.OnPageLoadListener {

    private static final String TAG = OnlineDiagnoseFragment.class.getSimpleName();

    @BindView(R.id.doctor_page_listview)
    PageListView mDoctorPageListView;
    @BindView(R.id.et_search)
    EditText mSearchEditText;
    @BindView(R.id.tv_noResult)
    TextView tv_noResult;

    private HttpClient mGetDoctorListClient;
    private PageListViewHelper<Doctor.ListItem> mPageListViewHelper;

    @Override
    public void firstShow() {
        super.firstShow();
        getDoctorList(true, null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.online_diagnose, container, false);
        ButterKnife.bind(this, root);

        mPageListViewHelper = new PageListViewHelper<>(mDoctorPageListView, new DoctorListAdapter(getActivity(), null));
//        mPageListViewHelper.getListView().setDividerHeight(0);
        mPageListViewHelper.getListView().setDivider(getResources().getDrawable(R.drawable.divider));
        mPageListViewHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Doctor.ListItem listItem = mPageListViewHelper.getAdapter().getItem(position);
                DoctorServiceActivity.startDoctorServiceActivity(getActivity(), listItem);
            }
        });
        mPageListViewHelper.setOnPageLoadListener(this);
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    String keyWord = mSearchEditText.getText().toString();
                    getDoctorList(true, keyWord);
                }

                return false;
            }
        });

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NetService.closeClient(mGetDoctorListClient);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getDoctorList(final boolean refresh, String keyWord) {
        tv_noResult.setVisibility(View.GONE);
        mPageListViewHelper.setRefreshing(refresh);
        HttpDoctor.GetList getList = new HttpDoctor.GetList(keyWord,
                refresh ? 1 : mPageListViewHelper.getPageIndex(),
                mPageListViewHelper.getPageSize(), new HttpListener<ArrayList<Doctor.ListItem>>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("getDoctorList", code, msg));
                }
                mPageListViewHelper.setRefreshing(false);

            }

            @Override
            public void onSuccess(ArrayList<Doctor.ListItem> listItems) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("getDoctorList", PUtils.toJson(listItems)));
                }
                mPageListViewHelper.setRefreshing(false);

                if (refresh) {
                    mPageListViewHelper.refreshData(listItems);
                    if (listItems.size() <= 0 ){
                        tv_noResult.setVisibility(View.VISIBLE);
                    }
                } else {
                    mPageListViewHelper.addPageData(listItems);
                }
            }
        });

        mGetDoctorListClient = NetService.createClient(getActivity(), getList);
        mGetDoctorListClient.start();

    }

    @Override
    public void onRefreshData() {
        getDoctorList(true, null);
    }

    @Override
    public void onLoadPageData() {
        getDoctorList(false, null);
    }

    static class DoctorListAdapter extends CommonAdapter<Doctor.ListItem> {

        String mPriceFormat;
        Context context;

        public DoctorListAdapter(Context context, List<Doctor.ListItem> datas) {
            super(context, R.layout.doctor_list_item, datas);
            mPriceFormat = context.getResources().getString(R.string.price_format);
            this.context = context;
        }

        @Override
        public void convert(ViewHolder viewHolder, Doctor.ListItem data, int position) {
            ((TextView) viewHolder.findViewById(R.id.doctor_name)).setText(data.mDoctorName);
            ((TextView) viewHolder.findViewById(R.id.department)).setText(data.mDepartmentName);
            ((TextView) viewHolder.findViewById(R.id.hospital_name)).setText(data.mHospitalName);
            ((TextView) viewHolder.findViewById(R.id.tv_specialty)).setText(data.mSpecialty);
//            ((TextView) viewHolder.findViewById(R.id.diagnose_quantity)).setText(""+data.mDiagnoseNum);
            TextView doctorTitle = (TextView) viewHolder.findViewById(R.id.doctor_title);
            doctorTitle.setText(PUtils.getDoctorTitle(context,data.mTitle));

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
