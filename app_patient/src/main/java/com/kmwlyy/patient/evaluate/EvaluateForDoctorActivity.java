package com.kmwlyy.patient.evaluate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.Doctor;
import com.kmwlyy.patient.helper.net.bean.EvaluateTagBean;
import com.kmwlyy.patient.helper.net.event.HttpDoctorService;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.weight.FlowLayout;
import com.kmwlyy.personinfo.PersonInfoActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.RateLayout;
import com.winson.ui.widget.TagBaseAdapter;
import com.winson.ui.widget.TagCloudLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xcj on 2016/12/19.
 * 就诊评价
 */

public class EvaluateForDoctorActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = EvaluateForDoctorActivity.class.getSimpleName();
    private TagBaseAdapter tagAdapter;
    private HttpClient mTagClient;
    private Context mContext = EvaluateForDoctorActivity.this;
    private List<String> mTagList,mSelectList;
    private static final String DOCTOR_ITEM = "DOCTOR_ITEM";
    private static final String OUTERID = "OUTERID";
    private static final String PAGE_SIGN = "PAGESIGN";
    private static final String NAME = "NAME";
    private static final String TITLE = "TITLE";
    private static final String HOSPITAL = "HOSPITAL";
    private static final String DEPARTMENT = "DEPARTMENT";
    private Doctor.ListItem doctorItem;
    private String mOuterID;
    private boolean evaluateImageConsult = true;


    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.doctor_name)
    TextView doctor_name;
    @BindView(R.id.doctor_title)
    TextView doctor_title;
    @BindView(R.id.hospital_name)
    TextView hospital_name;
    @BindView(R.id.department)
    TextView department;
    @BindView(R.id.avatar)
    ImageView iv_head;
    @BindView(R.id.ratingbarId)
    RatingBar ratingbar;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.tcl_container)
    TagCloudLayout tcl_container;
    @BindView(R.id.submit)
    Button btn_submit;

    public static void startEvaluateForDoctorActivity(Context context, String OuterID,Doctor.ListItem listItem,boolean evaluateImageConsult) {
        Intent intent = new Intent(context, EvaluateForDoctorActivity.class);

        intent.putExtra(DOCTOR_ITEM, listItem);
        intent.putExtra(OUTERID, OuterID);

        intent.putExtra(PAGE_SIGN, evaluateImageConsult);
        context.startActivity(intent);
    }

    public static void startEvaluateForDoctorActivity(Context context, String OuterID,String DoctorName,String Title,String HospitalName,String DepartmentName,boolean evaluateImageConsult,String doctorPhotoUrl) {
        Doctor.ListItem item = new Doctor.ListItem();
        item.mDoctorName = DoctorName;
        item.mTitle = Title;
        item.mHospitalName = HospitalName;
        item.mDepartmentName = DepartmentName;
        Doctor.User user = new Doctor.User();
        user.mPhotoUrl = doctorPhotoUrl;
        item.mUser = user;
        startEvaluateForDoctorActivity(context,OuterID,item,evaluateImageConsult);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_for_doctor);
        butterknife.ButterKnife.bind(this);
        init();
    }

    private void init() {
        tv_center.setText(getResources().getString(R.string.string_evaluate));
        btn_submit.setOnClickListener(this);
        doctorItem = (Doctor.ListItem) getIntent().getSerializableExtra(DOCTOR_ITEM);
        mOuterID = getIntent().getStringExtra(OUTERID);
        evaluateImageConsult = getIntent().getBooleanExtra(PAGE_SIGN,true);
        doctor_name.setText(doctorItem.mDoctorName);
        doctor_title.setText(PUtils.getDoctorTitle(mContext,doctorItem.mTitle));
        hospital_name.setText(doctorItem.mHospitalName);
        department.setText(doctorItem.mDepartmentName);
        ImageLoader.getInstance().displayImage(doctorItem.mUser.mPhotoUrl, iv_head, PersonInfoActivity.getCircleDisplayOptions(R.drawable.default_avatar_patient));

        //Tag 列表
        mTagList = new ArrayList<>();
        mSelectList = new ArrayList<>();
        tagAdapter = new TagBaseAdapter(this,mTagList,mSelectList,TagBaseAdapter.YELLOW);
        tcl_container.setAdapter(tagAdapter);

        tcl_container.setItemClickListener(new TagCloudLayout.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                tagAdapter.setHighLight(mTagList.get(position));
                //点击标签
                updateData(mTagList.get(position));
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tv_left:
                onBackPressed();
                break;
            case R.id.submit:
                submit();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //隐藏键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        loadTag();
    }

    /**
     * 加载Tag数据
     */
    public void loadTag(){showLoadDialog(R.string.string_wait);
        HttpDoctorService.GetTags getTags = new HttpDoctorService.GetTags(new HttpListener<ArrayList<EvaluateTagBean>>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("HttpDoctorService.GetTags", code, msg));
                }

                dismissLoadDialog();
                ToastUtils.showShort(mContext,msg);
            }

            @Override
            public void onSuccess(ArrayList<EvaluateTagBean> list) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("HttpDoctorService.GetTags", PUtils.toJson(list)));
                }
                dismissLoadDialog();
                if(null != list){
                    mTagList.clear();
                    for(int i=0;i<list.size();i++){
                        mTagList.add(list.get(i).getTagName());
                    }
                    tagAdapter.notifyDataSetChanged();
                }
            }
        });

        mTagClient = NetService.createClient(this, getTags);
        mTagClient.start();
    }

    /**
     * 提交数据
     */
    public void submit(){
        if(checkParm()){
            HttpDoctorService.SubmitEvaluate getTags = new HttpDoctorService.SubmitEvaluate(mOuterID,(int)ratingbar.getRating()+"",getSubmitTags(),et_content.getText().toString(),new HttpListener<String>() {
                @Override
                public void onError(int code, String msg) {
                    if (DebugUtils.debug) {
                        Log.d(TAG, DebugUtils.errorFormat("HttpDoctorService.SubmitEvaluate", code, msg));
                    }
                    dismissLoadDialog();
                    ToastUtils.showShort(mContext,msg);
                }

                @Override
                public void onSuccess(String str) {
                    if (DebugUtils.debug) {
                        Log.d(TAG, DebugUtils.successFormat("HttpDoctorService.SubmitEvaluate", str));
                    }
                    dismissLoadDialog();
                    ToastUtils.showShort(mContext,getResources().getString(R.string.evaluate_success));
                    if (evaluateImageConsult){
                        EventBus.getDefault().post(new EventApi.RefreshImageConsultList());
                    }else{
                        EventBus.getDefault().post(new EventApi.RefreshVVConsultList());
                    }
                    finish();
                }
            });
            mTagClient = NetService.createClient(this, getTags);
            mTagClient.start();
        }
    }

    /**
     * 检查参数
     */
    public boolean checkParm(){
        if(et_content.getText().toString().isEmpty()){
            ToastUtils.showShort(mContext,getResources().getString(R.string.please_input_content1));
            return false;
        }

//        if(ratingbar.getRating() == 0){
//
//            return false;
//        }
        return true;
    }

    /**
     * 添加tag，或取消tag
     */
    public void updateData(String str){
        if(!mSelectList.contains(str)){//之前没有选择此tag,加上并高亮显示
            mSelectList.add(str);
        }else{//之前已经选择了，取消高亮
            for(int i=0;i<mSelectList.size();i++){
                if(mSelectList.get(i).equals(str)){
                    mSelectList.remove(i);
                }
            }
        }
        tagAdapter.notifyDataSetChanged();
    }

    /**
     * 返回 用户选择的tags,用于提交数据
     */
    public String getSubmitTags(){
        String str = "";
        if(mSelectList.size() > 0) {
            for (int i = 0; i < mSelectList.size(); i++) {
                if(i!= mSelectList.size() - 1){
                    str = str + mSelectList.get(i) + ";";
                }else{
                    str = str + mSelectList.get(i);
                }
            }
        }
        return str;
    }
}
