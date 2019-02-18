package com.kmwlyy.doctor.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.VVConsultDetailBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getVoiceDetail_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class VoiceDetailActivity extends BaseActivity {
    public static final String TAG = "VoiceDetailActivity";
    //返回
    @ViewInject(R.id.tv_left)
    TextView btn_left;@ViewInject(R.id.tv_title)
    TextView tv_title;
    @ViewInject(R.id.tv_state)
    TextView tv_state;
    @ViewInject(R.id.tv_name)
    TextView tv_name;
    @ViewInject(R.id.tv_doctor)
    TextView tv_doctor;
    @ViewInject(R.id.tv_sex)
    TextView tv_sex;
    @ViewInject(R.id.tv_age)
    TextView tv_age;
    @ViewInject(R.id.tv_expense)
    TextView tv_expense;
    @ViewInject(R.id.tv_time)
    TextView tv_time;
    @ViewInject(R.id.tv_demand)
    TextView tv_demand;//主诉
    @ViewInject(R.id.tv_medical_history)
    TextView tv_medical_history;//现病史
    @ViewInject(R.id.tv_medical_history2)
    TextView tv_medical_history2;//既往病史
    @ViewInject(R.id.tv_prescribed)
    TextView tv_prescribed;//医嘱
    @ViewInject(R.id.tv_diagnosed)
    TextView tv_diagnosed;//初步诊断
    @ViewInject(R.id.btn_show_diagnose)
    TextView btn_show_diagnose;//查看处方
    @ViewInject(R.id.rl_show_diagnose)
    RelativeLayout rl_show_diagnose;//查看处方
    private String doctorName ="";
    private String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_detail);
        ViewUtils.inject(this); //注入view和事件
        //初使化
        init();
        getData();
    }

    private void init() {
        ((TextView)findViewById(R.id.tv_center)).setText(getResources().getString(R.string.string_voice_detail));
        btn_left.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(this);
        btn_left.setText(getResources().getString(R.string.string_back));
        btn_show_diagnose.setOnClickListener(this);

        try {
            tv_name.setText(getIntent().getStringExtra("name"));
            doctorName = getIntent().getStringExtra("doctor");
            URL = getIntent().getStringExtra("url");
            tv_doctor.setText(doctorName);
//            tv_age.setText(String.valueOf(getIntent().getIntExtra("age",0)));
            tv_sex.setText(getIntent().getIntExtra("sex",0) == 0 ? "男":"女");
//            channelID = getIntent().getStringExtra("channelID");
        }catch (Exception e){
        }
    }

    private void initView(VVConsultDetailBean data){
        URL = data.getRecipeFileUrl();
        if(!URL.isEmpty()){
            rl_show_diagnose.setVisibility(View.VISIBLE);
        }

        tv_time.setText(data.getOPDDate().substring(0,10));
        if(data.getOrder()!=null){
            tv_expense.setText(CommonUtils.convertTowDecimalStr(data.getOrder().getTotalFee()) + "元");
            tv_state.setText(MyUtils.getVoiceTypeState(mContext,data.getOrder().getOrderState(),tv_state));
        }

        if (data.getOPDType() == MyUtils.VoiceVideo.TYPE_VOICE){
            tv_title.setText(R.string.voice_diagnose2);
        }
        else{
            tv_title.setText(R.string.video_diagnose2);
        }

        if (data.getUserMedicalRecord() != null)
        {
            if (!TextUtils.isEmpty(data.getUserMedicalRecord().getSympton())) {
                tv_demand.setText(data.getUserMedicalRecord().getSympton());
            }
            if (!TextUtils.isEmpty(data.getUserMedicalRecord().getPastMedicalHistory())) {
                tv_medical_history2.setText(data.getUserMedicalRecord().getPastMedicalHistory());
            }
            if (!TextUtils.isEmpty(data.getUserMedicalRecord().getPresentHistoryIllness())) {
                tv_medical_history.setText(data.getUserMedicalRecord().getPresentHistoryIllness());
            }
            if (!TextUtils.isEmpty(data.getUserMedicalRecord().getPreliminaryDiagnosis())) {
                tv_diagnosed.setText(data.getUserMedicalRecord().getPreliminaryDiagnosis());
            }
            if (!TextUtils.isEmpty(data.getUserMedicalRecord().getAdvised())) {
                tv_prescribed.setText(data.getUserMedicalRecord().getAdvised());
            }
            if (null != data.getMember() ) {
                tv_age.setText(data.getMember().getAge());
            }
        }
    }


    /**
     * 获取数据
     */
    private void getData() {
        showLoadDialog(R.string.string_get_data);

        Http_getVoiceDetail_Event event = new Http_getVoiceDetail_Event(getIntent().getStringExtra("id"),new HttpListener<VVConsultDetailBean>() {
            @Override
            public void onError(int code, String msg) {
                dismissLoadDialog();
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }

            }

            @Override
            public void onSuccess(VVConsultDetailBean bean) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request success");
                }
                dismissLoadDialog();
                initView(bean);
            }
        }
        );
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_left://退出
                finish();
                break;
            case R.id.btn_show_diagnose://查看处方
                Intent intent;
                if (Build.VERSION.SDK_INT >= 21) {
                    intent = new Intent(mContext, PDFActivity.class);
                }else{
                    intent = new Intent(mContext, LegacyPDFActivity.class);
                }
                Bundle bundle = new Bundle();
                bundle.putString("url",  URL);
                bundle.putString("id", CommonUtils.toMD5(URL));
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
