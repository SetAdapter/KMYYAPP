package com.kmwlyy.doctor.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.PatientDetailBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getPatientDetailEvent;
import com.kmwlyy.personinfo.PersonInfoActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PatientDetailActivity extends BaseActivity {
    public static final String TAG = "PatientDetailActivity";
    Context mContext = PatientDetailActivity.this;
    private String memberId;
    private String electronId;


    @ViewInject(R.id.iv_patient_head)
    ImageView iv_patient_head;
    @ViewInject(R.id.tv_patient_name)
    TextView tv_patient_name;
    @ViewInject(R.id.tv_patient_sex)
    TextView tv_patient_sex;
    @ViewInject(R.id.tv_patient_age)
    TextView tv_patient_age;
    @ViewInject(R.id.tv_patient_phone)
    TextView tv_patient_phone;
    @ViewInject(R.id.tv_idcard)
    TextView tv_idcard;
    @ViewInject(R.id.tv_email)
    TextView tv_email;
    @ViewInject(R.id.tv_addrress)
    TextView tv_addrress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);
        ViewUtils.inject(this);

        memberId = getIntent().getStringExtra("id");

        ((TextView)findViewById(R.id.tv_center)).setText(getResources().getString(R.string.string_patient_detail));
        findViewById(R.id.lay_record).setOnClickListener(this);
        findViewById(R.id.lay_history).setOnClickListener(this);

        init();
    }

    public void init(){
        mLeftBtn = (TextView) findViewById(R.id.tv_left);
        mLeftBtn.setVisibility(View.VISIBLE);
        mLeftBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_left://退出
                finish();
                break;
            case R.id.lay_record://就诊记录
                //进入患者详情界面
                Intent intent = new Intent();
                intent.setClass(mContext,MedicalRecordActivity.class);
                intent.putExtra("id",memberId);
                startActivity(intent);
                break;
            case R.id.lay_history://电子病历
                if (TextUtils.isEmpty(electronId)){
                    return;
                }
                Intent caseHistory = new Intent();
                caseHistory.setClass(mContext,ElectronCaseHistoryActivity.class);
                caseHistory.putExtra("id",electronId);
                startActivity(caseHistory);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(memberId);
    }

    /**
     * 请求患者详情数据
     * @param id
     */
    public void loadData(String id){
        Http_getPatientDetailEvent event = new Http_getPatientDetailEvent(id,new HttpListener<PatientDetailBean>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                ToastUtils.showShort(mContext,msg);
            }

            @Override
            public void onSuccess(PatientDetailBean bean) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request success");
                }
                updateUI(bean);

            }
        });
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

    public void updateUI(PatientDetailBean bean){
        if(null != bean){
            tv_patient_name.setText(bean.getMemberName());
            tv_patient_sex.setText(bean.getGenderName());
            tv_patient_age.setText(bean.getAge()+mContext.getResources().getString(R.string.string_age));
            tv_patient_phone.setText(bean.getMobile());
            tv_idcard.setText(bean.getIDNumber());
            tv_email.setText(bean.getEmail());
            tv_addrress.setText(bean.getAddress());
            electronId = bean.getMemberID();
//            ImageLoader.getInstance().displayImage("file://"+event.photoUrl, iv_head, PersonInfoActivity.getCircleDisplayOptions(R.drawable.default_avatar_patient));
//
        }
    }
}
