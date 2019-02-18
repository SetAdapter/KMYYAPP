package com.kmwlyy.personinfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.event.HttpUserInfo;
import com.kmwlyy.core.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by xcj on 2016/10/23.
 */
public class ModificationInfoActivity extends Activity {

    static final String TYPE = "TYPE";
    static final String CONTENT = "CONTENT";

    private HttpClient mGetUserInfoClient;
    private int mType = 0;
    private String mContent = "";
    private ProgressDialog mLoginDialog;

    @BindView(R2.id.tv_center)
    TextView mToolbarTitle;
    @BindView(R2.id.tv_right)
    TextView mSaveTxt;
    @BindView(R2.id.et_content)
    EditText mContentEditTxt;
    @BindView(R2.id.et_nickname)
    EditText mNicknameEditText;
    @BindView(R2.id.tv_length)
    TextView mEdtLength;
    @BindView(R2.id.rl_edit_content)
    LinearLayout mEditContentRelativeLayout;

    public static void startModificationInfoActivity(Context context, int type, String content) {
        Intent intent = new Intent(context, ModificationInfoActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(CONTENT, content);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_info);
        butterknife.ButterKnife.bind(this);
        mType = getIntent().getIntExtra(TYPE, 0);
        mContent = getIntent().getStringExtra(CONTENT);
        mSaveTxt.setText(R.string.save);
        mSaveTxt.setVisibility(View.VISIBLE);
        switch (mType){
            //患者修改昵称
            case 1:
                mToolbarTitle.setText(R.string.nickname_modification_title);
                mNicknameEditText.setVisibility(View.VISIBLE);
                mEditContentRelativeLayout.setVisibility(View.GONE);
                mNicknameEditText.setText(mContent);
                break;
            //医生擅长疾病
            case 2:
                mToolbarTitle.setText(R.string.good_at_disease_title);
                mNicknameEditText.setVisibility(View.GONE);
                mEditContentRelativeLayout.setVisibility(View.VISIBLE);
                mContentEditTxt.setText(mContent);
                mContentEditTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
                mEdtLength.setText(mContent.length()+"/50");
                break;
            //医生个人简介
            case 3:
                mToolbarTitle.setText(R.string.intro_title);
                mNicknameEditText.setVisibility(View.GONE);
                mEditContentRelativeLayout.setVisibility(View.VISIBLE);
                mContentEditTxt.setText(mContent);
                mContentEditTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(300)});
                mEdtLength.setText(mContent.length()+"/300");
                break;
        }
        findViewById(R.id.tv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                onBackPressed();

            }
        });
        findViewById(R.id.tv_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存信息
                saveInfo();
            }
        });
        mContentEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = s.length();
                if (mType == 2){
                    mEdtLength.setText(i + "/50");
                }else if(mType == 3){
                    mEdtLength.setText(i + "/300");
                }

            }
        });

    }

    //保存信息
    private void saveInfo(){
        switch (mType){
            //患者修改昵称
            case 1:
                patientModificationNickname();
                break;
            //医生擅长疾病
            case 2:
                doctorGoodAtDisease();
                break;
            //医生个人简介
            case 3:
                doctorIndividualResume();
                break;
        }
    }

    //患者修改昵称
    private void patientModificationNickname(){
        if (mNicknameEditText.getText().toString().length() <= 0){
            ToastUtils.showShort(ModificationInfoActivity.this, getResources().getString(R.string.nickname_can_not_null));
        }
        postPatientInfo("UserCNName",mNicknameEditText.getText().toString().trim());
    }
    //医生擅长疾病
    private void doctorGoodAtDisease(){
        postDoctorInfo("Specialty", mContentEditTxt.getText().toString().trim());
    }
    //医生个人介绍
    private void doctorIndividualResume(){
        postDoctorInfo("Intro", mContentEditTxt.getText().toString().trim());
    }

    //更新患者个人资料
    private void postPatientInfo(final String type, final String content){
        showLoginDialog(getResources().getString(R.string.save_info));
        HttpUserInfo.PostPatientInfo postPatientInfo = new HttpUserInfo.PostPatientInfo(type, content, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                dismissLoginDialog();
                ToastUtils.showShort(ModificationInfoActivity.this,getResources().getString(R.string.save_fail));
            }

            @Override
            public void onSuccess(Object o) {
                dismissLoginDialog();
                if ("UserCNName".equals(type)) {
                    EventApi.NickName nickName = new EventApi.NickName();
                    nickName.nickname = content;
                    EventBus.getDefault().post(nickName);
                }
                finish();
            }
        });

        mGetUserInfoClient = new HttpClient(this, postPatientInfo);
        mGetUserInfoClient.start();
    }

    //更新医生个人资料
    private void postDoctorInfo(final String type, final String content){
        showLoginDialog(getResources().getString(R.string.save_info));
        HttpUserInfo.PostDoctorInfo postDoctorInfo = new HttpUserInfo.PostDoctorInfo(type, content, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                dismissLoginDialog();
                ToastUtils.showShort(ModificationInfoActivity.this,getResources().getString(R.string.save_fail));
            }

            @Override
            public void onSuccess(Object o) {
                dismissLoginDialog();
                if ("Specialty".equals(type)){
                    EventApi.GoodAtDisease goodAtDisease = new EventApi.GoodAtDisease();
                    goodAtDisease.goodAtDisease = content;
                    EventBus.getDefault().post(goodAtDisease);
                }else if ("Intro".equals(type)){
                    EventApi.Intro intro = new EventApi.Intro();
                    intro.introInfo = content;
                    EventBus.getDefault().post(intro);
                }
                finish();
            }
        });

        mGetUserInfoClient = new HttpClient(this, postDoctorInfo);
        mGetUserInfoClient.start();
    }

    private void showLoginDialog(String content) {
        mLoginDialog = new ProgressDialog(this);
        mLoginDialog.setMessage(content);
        mLoginDialog.setCancelable(false);
        mLoginDialog.show();
    }

    private void dismissLoginDialog() {
        if (mLoginDialog != null) {
            mLoginDialog.dismiss();
        }
    }
}
