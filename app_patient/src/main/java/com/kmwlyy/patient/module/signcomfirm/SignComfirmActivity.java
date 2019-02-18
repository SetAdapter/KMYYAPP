package com.kmwlyy.patient.module.signcomfirm;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.SignatureBean;
import com.kmwlyy.core.net.event.GetMySignature;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.kdoctor.activity.AiChatActivity2;
import com.kmwlyy.patient.weight.TimeUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/18.
 */

public class SignComfirmActivity extends BaseActivity {

    @BindView(R.id.iv_tools_left)
    Button mIvToolsLeft;
    @BindView(R.id.tv_title_center)
    TextView mTvTitleCenter;
    Button btn_commit;
    String mIDNumberEdt;//身份证
    @BindView(R.id.SignatureUserName)
    TextView mSignatureUserName;
    @BindView(R.id.CreateTime)
    TextView mCreateTime;
    @BindView(R.id.FDGroupName)
    TextView mFDGroupName;
    @BindView(R.id.SignatureUserName_tv)
    TextView mSignatureUserNameTv;
    @BindView(R.id.CreateTime_tv)
    TextView mCreateTimeTv;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_comfirm;
    }

    @Override
    protected void afterBindView() {


        mTvTitleCenter.setText("签约完成");
        mIvToolsLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        mIDNumberEdt = intent.getStringExtra("mIDNumberEdt");
        String id = intent.getStringExtra("id");
        Log.i("nihao",id);
        if (!TextUtils.isEmpty(id)) {
            GetDetail(id);
        }
        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignComfirmActivity.this, "签约完成", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(SignComfirmActivity.this, AiChatActivity2.class);
//                intent.putExtra("mIDNumberEdt", mIDNumberEdt);
//                startActivity(new Intent(intent));
                AiChatActivity2.jump(SignComfirmActivity.this,AiChatActivity2.GATHER_INFORMATION,mIDNumberEdt);
                getState();
                EventBus.getDefault().post(new FinishEvent("finish"));
                finish();
            }
        });
    }

    private void getState() {
        //获取签约状态并更新
        GetMySignature getMySignature = new GetMySignature(new HttpListener<SignatureBean>() {
            @Override
            public void onError(int code, String msg) {
                // ToastUtils.showShort();
            }

            @Override
            public void onSuccess(SignatureBean signatureBean) {
                if (signatureBean != null) {
                    if (!TextUtils.isEmpty(signatureBean.mFDGroupID)) {
                        BaseApplication.getInstance().setSignatureData(signatureBean);
                    } else {
                        BaseApplication.getInstance().setSignatureData(new SignatureBean());
                    }
                } else {
                    BaseApplication.getInstance().setSignatureData(new SignatureBean());
                }
            }
        });
        new HttpClient(SignComfirmActivity.this, getMySignature, HttpClient.FAMILY_URL, BaseApplication.getInstance().getHttpFilter()).start();
    }

    private void GetDetail(String sss) {
        Http_GetDetail event = new Http_GetDetail(sss, new HttpListener<SignDetailBean>() {
            @Override
            public void onError(int code, String msg) {
                Log.d("nihao",msg);
            }

            @Override
            public void onSuccess(SignDetailBean familyListBean) {
                if (familyListBean != null) {
                    mSignatureUserName.setText(familyListBean.SignatureUserName + "用户你好");
                    String createTime = familyListBean.CreateTime;
                    String yearStr = createTime.substring(0, 4);
                    String monthStr = createTime.substring(5, 7);
                    String dayStr = createTime.substring(8, 10);
                    mCreateTime.setText("\t\t\t你于" + yearStr + "年" + monthStr + "月" + dayStr + "日成功签约家庭医生。");

                    mFDGroupName.setText(familyListBean.FDGroupName);

                    mSignatureUserNameTv.setText(familyListBean.SignatureUserName);
                    String t = createTime.replace("T", " ");
                    String timeStr = t.substring(0, 19);
//                    String timeStr = createTime.substring(0, 10);
                    mCreateTimeTv.setText(timeStr);
                }
            }
        });
        NetService.createClient(this, HttpClient.FAMILY_URL, event).start();
    }

}
