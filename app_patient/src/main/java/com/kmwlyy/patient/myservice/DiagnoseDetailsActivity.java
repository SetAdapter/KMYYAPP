package com.kmwlyy.patient.myservice;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.DiagnoseDetails;
import com.kmwlyy.patient.helper.net.event.HttpUserRecipeOrders;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.EmptyViewUtils;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import java.util.List;

import butterknife.BindView;

/**
 * Created by xcj on 2016/8/19.
 * 处方详情页面
 */
public class DiagnoseDetailsActivity extends BaseActivity {

    private static final String TAG = DiagnoseDetailsActivity.class.getSimpleName();


    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.doctor_page_listview)
    PageListView mDoctorPageListview;

    private TextView mPatientName;
    private TextView mPatientSex;
    private TextView mPatientAge;
    private TextView mPatientPhone;
    private TextView mDoctorInfo;
    private TextView mPhoneTitle;


    private String ordrtNo = null;
    private List<DiagnoseDetails.RecipeFile> datas = null;
    private HttpClient mHttpClient;
    private String patientName = null;
    private int age = 0;
    private int sex = 0;
    private String doctorName = null;
    private String hospitalName = null;
    private String departmentName = null;
    private PageListViewHelper<DiagnoseDetails.RecipeFile> mPageListViewHelper;
    private View root = null;
    private String phoneNumber = null;
    private LinearLayout footLinearLayout = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose_details);
        butterknife.ButterKnife.bind(this);
        root = ((ViewGroup)this.findViewById(android.R.id.content)).getChildAt(0);
        EmptyViewUtils.showLoadingView((ViewGroup) root);
        mPageListViewHelper = new PageListViewHelper<>(mDoctorPageListview, new RPListAdapter(this, datas));
        mDoctorPageListview.setEnabled(false);
        View footerView = getLayoutInflater().inflate(R.layout.layout_diagnose_foot, null);
        footLinearLayout = (LinearLayout) footerView.findViewById(R.id.ll_end);
        mPageListViewHelper.getListView().addFooterView(footerView);
        tv_center.setText(R.string.diagnose_detail);
        ordrtNo = getIntent().getStringExtra("orderNo");
        patientName = getIntent().getStringExtra("patientName");
        age = getIntent().getIntExtra("patientAge",0);
        sex = getIntent().getIntExtra("patientSex",0);
        doctorName = getIntent().getStringExtra("doctorName");
        hospitalName = getIntent().getStringExtra("hospitalName");
        departmentName = getIntent().getStringExtra("departmentName");
        phoneNumber = getIntent().getStringExtra("patientPhone");
        footLinearLayout.setVisibility(View.GONE);
        getDetails();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NetService.closeClient(mHttpClient);
    }

    private void getDetails(){
        HttpUserRecipeOrders.GetDetail getDetail = new HttpUserRecipeOrders.GetDetail(ordrtNo,new HttpListener<DiagnoseDetails>(){
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("getRecipeOrdersList", code, msg));
                    EmptyViewUtils.showErrorView((ViewGroup) root, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getDetails();
                        }
                    });
                }
            }

            @Override
            public void onSuccess(DiagnoseDetails diagnoseDetails) {
                Log.d(TAG, DebugUtils.successFormat("getDoctorList", PUtils.toJson(diagnoseDetails)));
                datas = diagnoseDetails.mRecipeFiles;
                mPageListViewHelper.refreshData(datas);
                EmptyViewUtils.removeAllView((ViewGroup) root);
                footLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        mHttpClient = NetService.createClient(this, getDetail);
        mHttpClient.start();
    }

    class RPListAdapter extends CommonAdapter<DiagnoseDetails.RecipeFile>{

        public RPListAdapter(Context context, List<DiagnoseDetails.RecipeFile> datas) {
            super(context, R.layout.item_rp_list, datas);
        }

        @Override
        public void convert(ViewHolder viewHolder, DiagnoseDetails.RecipeFile data, int position) {
            TextView mPatientDiagnose = (TextView) viewHolder.findViewById(R.id.tv_patient_diagnose);
            TextView mPatientRP = (TextView) viewHolder.findViewById(R.id.tv_patient_rp);
            mPatientName = (TextView) viewHolder.findViewById(R.id.tv_patient_name);
            mPatientSex = (TextView) viewHolder.findViewById(R.id.tv_patient_sex);
            mPatientAge = (TextView) viewHolder.findViewById(R.id.tv_patient_age);
            mPatientPhone = (TextView) viewHolder.findViewById(R.id.tv_patient_phone);
            mPhoneTitle = (TextView) viewHolder.findViewById(R.id.tv_phone_title);
            mDoctorInfo = (TextView) viewHolder.findViewById(R.id.tv_doctor_info);
            mPatientName.setText(patientName);
            mPatientAge.setText(String.valueOf(age));
            mDoctorInfo.setText(hospitalName + "    " + departmentName + "    "+doctorName + "    "+ datas.get(0).mRecipeDate);
            if (TextUtils.isEmpty(phoneNumber)){
                mPhoneTitle.setVisibility(View.GONE);
                mPatientPhone.setText("");
            }else{
                mPhoneTitle.setVisibility(View.VISIBLE);
                mPatientPhone.setText(phoneNumber);
            }
            if (sex == 0){
                mPatientSex.setText(R.string.man);
            }else {
                mPatientSex.setText(R.string.women);
            }
            int size = data.mDiagnoses.size();
            StringBuilder sb = new StringBuilder();
            if (size == 0){
                mPatientDiagnose.setText(R.string.have_no_diagnose);
            }else{
                for (int i = 0;i < size; i++){
                    sb.append(data.mDiagnoses.get(i).mDescription);
                }
                mPatientDiagnose.setText(sb.toString());
            }

            int detailsSize = data.mDetails.size();
            StringBuilder builder = new StringBuilder();
            if (detailsSize == 0){
                mPatientRP.setText(R.string.have_no_drug);
            }else{
                for (int i = 0;i < detailsSize; i++){
                    if (i == detailsSize){
                        builder.append(data.mDetails.get(i).mDrug.mDrugName + "(" + data.mDetails.get(i).mDrug.mSpecification + ")" + "*" + String.valueOf(data.mDetails.get(i).mQuantity) + data.mDetails.get(i).mDrug.mUnit + "\n" + String.valueOf(data.mDetails.get(i).mDose) + data.mDetails.get(i).mDrug.mDoseUnit + "  " + data.mDetails.get(i).mDrugRouteName + "  " + data.mDetails.get(i).mFrequency );
                    }else {
                        builder.append(data.mDetails.get(i).mDrug.mDrugName + "(" + data.mDetails.get(i).mDrug.mSpecification + ")" + "*" + String.valueOf(data.mDetails.get(i).mQuantity) + data.mDetails.get(i).mDrug.mUnit + "\n" + String.valueOf(data.mDetails.get(i).mDose) + data.mDetails.get(i).mDrug.mDoseUnit + "  " + data.mDetails.get(i).mDrugRouteName + "  " + data.mDetails.get(i).mFrequency + "\n");
                    }
                }
                mPatientRP.setText(builder);
            }
        }
    }
}
