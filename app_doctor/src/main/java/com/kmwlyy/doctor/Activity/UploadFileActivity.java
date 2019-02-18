package com.kmwlyy.doctor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.event.HttpUserMember;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.AppEventApi;
import com.kmwlyy.doctor.adapter.MedicalHistoryAdapter;
import com.kmwlyy.doctor.model.MedicalHistoryBean;
import com.kmwlyy.doctor.model.SingedMemberBean;
import com.kmwlyy.doctor.model.httpEvent.MedicalHistoryEvent;
import com.kmwlyy.imchat.adapter.ChatAdapter;
import com.kmwlyy.personinfo.EventApi;
import com.kmwlyy.personinfo.PersonInfoActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.InsideGridView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TFeng on 2017/7/5.
 */

public class UploadFileActivity extends BaseActivity {
    private static final String UPLOAD_FILE_ACTIVITY  = "UploadFileActivity";
    private static final int NEW_FILE = 0;
    private static final int EXITS_FILE = 1;

    private static final int  SINGED_MEMBER=10;
    private static final int HOME_MEMBER = 11;

    @ViewInject(R.id.tv_left)
    TextView tv_left;
    @ViewInject(R.id.iv_left)
    ImageView iv_left;
    @ViewInject(R.id.tv_right)
    TextView tv_right;
    @ViewInject(R.id.tv_center)
    TextView tv_center;
    @ViewInject(R.id.ll_info_head)
    LinearLayout ll_info_head;
    @ViewInject(R.id.ll_name_icon)
    LinearLayout ll_name_icon;
    @ViewInject(R.id.iv_info_head_icon)
    ImageView iv_info_head_icon;
    @ViewInject(R.id.tv_info_name)
    TextView tv_info_name;
    @ViewInject(R.id.gv_emr)
    InsideGridView gv_emr;
    private int mIntExtra;
    private SingedMemberBean mSingedMemberInfo;
    private SingedMemberBean.UserMemberBean mHomeMemberInfo;
    private int mMember;
    private String mMemberID;
    private List<MedicalHistoryBean> mDataList = new ArrayList<>();
    private MedicalHistoryAdapter mAdapter;
    private String mFamilyDoctorID;
    private String mMmberIDNEW;
    private String mMemberName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);
        getIntentInfo();
        getEMRList();
        initView();
        initListener();
    }
    private void getEMRList(){
        showLoadDialog(R.string.string_wait);
        MedicalHistoryEvent.GetEMRList event = new MedicalHistoryEvent.GetEMRList("1", "9", mFamilyDoctorID, mMemberID, "", new HttpListener<List<MedicalHistoryBean>>() {
            @Override
            public void onError(int code, String msg) {
                dismissLoadDialog();
                ToastUtils.showShort(mContext,msg);
            }
            @Override
            public void onSuccess(List<MedicalHistoryBean> list) {
                dismissLoadDialog();
                mAdapter.update(list);
                mDataList.clear();
                mDataList.addAll(list);

            }
        });
        new HttpClient(mContext,event).startNewApi();
    }

    private void getIntentInfo() {
        mFamilyDoctorID = getIntent().getStringExtra("FamilyDoctorID");
        mIntExtra = getIntent().getIntExtra(UPLOAD_FILE_ACTIVITY,0);
        mMember = getIntent().getIntExtra("Member",10);
        mSingedMemberInfo = (SingedMemberBean) getIntent().getSerializableExtra("singedMemberInfo");
        mHomeMemberInfo = (SingedMemberBean.UserMemberBean) getIntent().getSerializableExtra("homeMemberInfo");

        if(mMember == SINGED_MEMBER){
            if(mSingedMemberInfo != null) {
                mMemberID = mSingedMemberInfo.getUserMember().getMemberID();
            }
        }else {
            if(mHomeMemberInfo != null) {
                mMemberID = mHomeMemberInfo.getMemberID();
            }
        }
        mMmberIDNEW = getIntent().getStringExtra("MemberID");
        mMemberName = getIntent().getStringExtra("MemberName");
        if(mIntExtra == 0){
            mMemberID = mMmberIDNEW;
        }

    }


    private void initView() {
        tv_left.setVisibility(View.GONE);
        iv_left.setVisibility(View.VISIBLE);
        mAdapter = new MedicalHistoryAdapter(mContext,mDataList);
        gv_emr.setAdapter(mAdapter);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("完成");

        if(mIntExtra == 0){
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText("完成");
            tv_center.setText(R.string.upload_file);
            tv_right.setText(R.string.commit);
            tv_info_name.setText(mMemberName);
        }else {
//            已经有档案
            tv_center.setText(R.string.health_file);
            ll_info_head.setVisibility(View.GONE);
            ll_name_icon.setVisibility(View.VISIBLE);

            if(mMember == SINGED_MEMBER){
//                签约居民信息
                setSingedMember(mSingedMemberInfo);

            }else{
//                家庭成员信息
                setHomeMember(mHomeMemberInfo);
            }


        }
        gv_emr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(mDataList.size() == i){
                    medcialHistory(i,false);

                }else{
                    medcialHistory(i,true);
                }
            }
        });


        

    }



    private void medcialHistory(int postion,boolean isEdit) {
        Intent intentSinged = new Intent();
        if(isEdit){
            Bundle bundleSinged = new Bundle();
            bundleSinged.putSerializable("MedicalHistory",mDataList.get(postion));
            intentSinged.putExtras(bundleSinged);
        }
        intentSinged.putExtra("IsEdit",isEdit);
        intentSinged.putExtra("MemberID",mMemberID);
        intentSinged.setClass(mContext, AddMedicalHistoryActivity.class);
        startActivity(intentSinged);
    }



    private void setSingedMember(SingedMemberBean singedMember){
        tv_info_name.setText(singedMember.getUserMember().getMemberName());
        ImageLoader.getInstance().displayImage(singedMember.getUserMember().getPhotoUrl(),
                iv_info_head_icon, PersonInfoActivity.getCircleDisplayOptions(R.drawable.default_avatar_patient));

    }
    private void setHomeMember(SingedMemberBean.UserMemberBean homeMember){
        tv_info_name.setText(homeMember.getMemberName());
        ImageLoader.getInstance().displayImage(homeMember.getPhotoUrl(),
                iv_info_head_icon, PersonInfoActivity.getCircleDisplayOptions(R.drawable.default_avatar_patient));

    }


    private void initListener() {
        iv_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);

        ll_name_icon.setOnClickListener(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteMedicalHistory(AppEventApi.Delete delete){
        getEMRList();

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void modifyMedicalHistory(AppEventApi.Modify modify){
        getEMRList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changSingedMember(SingedMemberBean singedMemberBean) {
        mSingedMemberInfo = singedMemberBean;
        setSingedMember(mSingedMemberInfo);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changHomeMember(SingedMemberBean.UserMemberBean homeMemberBean) {
        mHomeMemberInfo = homeMemberBean;
        setHomeMember(mHomeMemberInfo);
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()){
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_right:
                // TODO: 2017/7/5 保存数据
                finish();
                break;
            /*case R.id.ll_upload_emr:
               *//* Intent intent1 = new Intent();
                intent1.setClass(mContext,AddMedicalHistoryActivity.class);
                startActivity(intent1);*//*
                break;*/
            case R.id.ll_name_icon:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                if(mMember == SINGED_MEMBER) {
                    bundle.putSerializable("singedMemberInfo", mSingedMemberInfo);
                    intent.putExtra("BaseInfoActivity",1);
                    intent.putExtra("Member",10);
                }else{
                    bundle.putSerializable("homeMemberInfo",mHomeMemberInfo);
                    intent.putExtra("BaseInfoActivity",1);
                    intent.putExtra("Member",11);
                }
                intent.putExtra("FamilyDoctorID",mFamilyDoctorID);
                intent.putExtras(bundle);
                intent.setClass(mContext,BaseInfoActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
