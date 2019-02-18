package com.kmwlyy.doctor.Activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.Fragment.FollowUpManagementFragment;
import com.kmwlyy.doctor.Fragment.HomeManagementFragment;
import com.kmwlyy.doctor.Fragment.QueryChatFragment;
import com.kmwlyy.doctor.Fragment.ServiceRecordFragment;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.View.NoScrollViewPager;
import com.kmwlyy.doctor.model.Constant;
import com.kmwlyy.doctor.model.MedicalHistoryBean;
import com.kmwlyy.doctor.model.SingedMemberBean;
import com.kmwlyy.doctor.model.httpEvent.MedicalHistoryEvent;
import com.kmwlyy.personinfo.EventApi;
import com.kmwlyy.personinfo.PersonInfoActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TFeng on 2017/6/30.
 */

public class PatientActivity extends BaseActivity {

    private static final int SERVICE_RECORD = 0;
    private static final int FOLLOWUP_MANAGEMENT = 1;
    private static final int HOME_MANAGEMENT = 2;

    @ViewInject(R.id.tv_left)
    TextView tv_left;
    @ViewInject(R.id.iv_left)
    ImageView iv_left;
    @ViewInject(R.id.tv_center)
    TextView tv_center;
    @ViewInject(R.id.base_title_line)
    View base_title_line;

    @ViewInject(R.id.iv_phone)
    ImageView iv_phone;
    @ViewInject(R.id.iv_patient_look)
    ImageView iv_patient_look;
    @ViewInject(R.id.iv_patient_icon)
    ImageView iv_patient_icon;
    @ViewInject(R.id.tv_home_patient_name)
    TextView tv_home_patient_name;
    @ViewInject(R.id.tv_home_patient_gender)
    TextView tv_home_patient_gender;
    @ViewInject(R.id.tv_home_patient_age)
    TextView tv_home_patient_age;
    @ViewInject(R.id.tv_home_patient_remark)
    TextView tv_home_patient_remark;
    @ViewInject(R.id.tv_total)
    TextView tv_total;

    @ViewInject(R.id.tv_relationship)
    TextView tv_relationship;
    @ViewInject(R.id.iv_patient_edit)
    ImageView iv_patient_edit;


    @ViewInject(R.id.rg_tab)
    RadioGroup rg_tab;
    @ViewInject(R.id.rb_service_record)
    RadioButton rb_service_record;
    @ViewInject(R.id.rb_followup_manage)
    RadioButton rb_followup_management;
    @ViewInject(R.id.rb_home_management)
    RadioButton rb_home_management;

    @ViewInject(R.id.scroll_vp)
    NoScrollViewPager no_scroll_vp;
    private static final int HOME_SINGED = 0;
    private static final int HOME_MEMBER = 1;

    private int page = 0;
    private List<Fragment> fragmentlist;
    private ServiceRecordFragment serviceRecordFragment;
    private FollowUpManagementFragment followUpManagementFragment;
    private HomeManagementFragment homeManagementFragment;
    private android.support.v4.app.FragmentManager fragmentManager;
    private MyAdapter myAdapter;
    private Intent mIntent;
    private int mPatient;


    private SingedMemberBean mSingedMember;
    private Bundle mBundle;
    private SingedMemberBean.UserMemberBean mHomeMember;
    private String mFamilyDoctorID;
    private String mMobile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);
        getIntentForm();
        initView();
        initListener();
    }



    private void getIntentForm() {
        mIntent = getIntent();
        mPatient = mIntent.getIntExtra("patient", 0);
        mSingedMember = (SingedMemberBean) mIntent.getSerializableExtra("singedMember");
        mHomeMember = (SingedMemberBean.UserMemberBean) mIntent.getSerializableExtra("HomeMember");
        mFamilyDoctorID = mIntent.getStringExtra("FamilyDoctorID");
    }
    private void initView() {
        initBaseTitle();

        if(mPatient == HOME_SINGED){
           tv_relationship.setVisibility(View.GONE);
            iv_patient_edit.setVisibility(View.GONE);
            iv_patient_look.setVisibility(View.VISIBLE);
//           签约患者信息
            setSingedMember(mSingedMember);
            mMobile = mSingedMember.getUserMember().getMobile();

        }else{

            tv_relationship.setVisibility(View.VISIBLE);
            iv_patient_edit.setVisibility(View.VISIBLE);
            iv_patient_look.setVisibility(View.GONE);
            iv_phone.setVisibility(View.INVISIBLE);
            rb_followup_management.setVisibility(View.GONE);
            rb_home_management.setVisibility(View.GONE);
            tv_center.setVisibility(View.VISIBLE);
            tv_center.setText("家庭管理");
            setHomeMeber(mHomeMember);
            mMobile = mHomeMember.getMobile();

        }

//        Viewpager不能左右滑动
        no_scroll_vp.setScroll(false);
        initFragment();
        myAdapter = new MyAdapter(fragmentManager);
        no_scroll_vp.setAdapter(myAdapter);
        no_scroll_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                switch (position){
                    case SERVICE_RECORD:
                        rg_tab.check(R.id.rb_service_record);
                        break;
                    case FOLLOWUP_MANAGEMENT:
                        rg_tab.check(R.id.rb_followup_manage);
                        break;
                    case HOME_MANAGEMENT:
                        rg_tab.check(R.id.rb_home_management);
                        EventBus.getDefault().post(mSingedMember);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rg_tab.check(R.id.rb_service_record);
        rg_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    case R.id.rb_service_record:
                        page = SERVICE_RECORD;
                        break;
                    case R.id.rb_followup_manage:
                        page = FOLLOWUP_MANAGEMENT;
                       ToastUtils.showShort(mContext,"功能暂未上线，敬请期待！");
                        break;
                    case R.id.rb_home_management:
                        page = HOME_MANAGEMENT;
                        break;
                }
                no_scroll_vp.setCurrentItem(page);
            }
        });

    }

    private void setSingedMember(SingedMemberBean singedMember) {

        ImageLoader.getInstance().displayImage(singedMember.getUserMember().getPhotoUrl(),
                iv_patient_icon, PersonInfoActivity.getCircleDisplayOptions(R.drawable.default_avatar_patient));
        tv_home_patient_name.setText(singedMember.getUserMember().getMemberName());
        tv_home_patient_age.setText(singedMember.getUserMember().getAge()+"岁");
        int gender = mSingedMember.getUserMember().getGender();
        if(gender == 0){
            tv_home_patient_gender.setText("男");
        }else{
            tv_home_patient_gender.setText("女");
        }
        tv_home_patient_remark.setText(singedMember.getUserMember().getRemark());




    }
    private void setHomeMeber(SingedMemberBean.UserMemberBean homeMember){
        ImageLoader.getInstance().displayImage(homeMember.getPhotoUrl(),
                iv_patient_icon, PersonInfoActivity.getCircleDisplayOptions(R.drawable.default_avatar_patient));
        tv_home_patient_name.setText(homeMember.getMemberName());
        tv_home_patient_age.setText(homeMember.getAge()+"岁");
        int gender = homeMember.getGender();
        if(gender == 0){
            tv_home_patient_gender.setText("男");
        }else{
            tv_home_patient_gender.setText("女");
        }
        switch (homeMember.getRelation()){
            case 0:
                tv_relationship.setText("自己");
                break;
            case 1:
                tv_relationship.setText("配偶");
                break;
            case 2:
                tv_relationship.setText("父亲");
                break;
            case 3:
                tv_relationship.setText("母亲");
                break;
            case 4:
                tv_relationship.setText("儿子");
                break;
            case 5:
                tv_relationship.setText("女儿");
                break;
            case 6:
                tv_relationship.setText("其他");
                break;

        }
        tv_home_patient_remark.setText(homeMember.getRemark());


    }
    private void initFragment() {
        fragmentlist = new ArrayList<>();
        serviceRecordFragment = new ServiceRecordFragment();
        followUpManagementFragment = new FollowUpManagementFragment();
        homeManagementFragment = new HomeManagementFragment();

        //传 FamilyDoctorID  MemberID 给子页面
        if(mPatient == HOME_SINGED){
            Bundle bundle1 = new Bundle();
            bundle1.putString("FamilyDoctorID", mSingedMember.getFamilyDoctorID());
            bundle1.putString("MemberID", mSingedMember.getUserMember().getMemberID());
            serviceRecordFragment.setArguments(bundle1);
        }else{
            Bundle bundel2 = new Bundle();
            bundel2.putString("FamilyDoctorID",mFamilyDoctorID);
            bundel2.putString("MemberID",mHomeMember.getMemberID());
            serviceRecordFragment.setArguments(bundel2);
        }


        fragmentlist.add(serviceRecordFragment);
        fragmentlist.add(followUpManagementFragment);
        fragmentlist.add(homeManagementFragment);
        fragmentManager = getSupportFragmentManager();
    }

    private void initBaseTitle() {
        tv_left.setVisibility(View.GONE);
        iv_left.setVisibility(View.VISIBLE);
        tv_center.setVisibility(View.GONE);
        base_title_line.setVisibility(View.GONE);
    }

    private void initListener() {
        iv_left.setOnClickListener(this);
        iv_phone.setOnClickListener(this);
        iv_patient_look.setOnClickListener(this);
        iv_patient_edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()){
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_phone:
                call();
                break;
            case R.id.iv_patient_look:
                    Intent intent = new Intent();
                    intent.putExtra("UploadFileActivity",1);
                    intent.putExtra("FamilyDoctorID",mFamilyDoctorID);
                    intent.putExtra("Mmeber",10);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("singedMemberInfo",mSingedMember);
                    intent.putExtras(bundle);
                    intent.setClass(mContext,UploadFileActivity.class);
                    startActivity(intent);

                break;
            case R.id.iv_patient_edit:
                    Intent intent1 = new Intent();
                    intent1.putExtra("FamilyDoctorID",mFamilyDoctorID);
                    intent1.putExtra("UploadFileActivity",1);
                    intent1.putExtra("Member",11);
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("homeMemberInfo",mHomeMember);
                    intent1.putExtras(bundle1);
                    intent1.setClass(mContext,UploadFileActivity.class);
                    startActivity(intent1);
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeSingedMember(SingedMemberBean singedMemberBean) {
        mSingedMember = singedMemberBean;
        setSingedMember(mSingedMember);
    }
 /*   @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeHomeMember(SingedMemberBean.UserMemberBean homeMember) {
            mHomeMember = homeMember;
            setHomeMeber(mHomeMember);
            EventBus.getDefault().cancelEventDelivery(homeMember);

    }*/
    private void call() {

        if(mMobile != null && mMobile.length()>0) {
            Intent call = new Intent();
            call.setAction(Intent.ACTION_CALL);
            call.setData(Uri.parse("tel:"+mMobile));
            startActivity(call);
        }else{
            ToastUtils.showShort(mContext,"电话错误");
        }
    }

    public class MyAdapter extends FragmentPagerAdapter{


        public MyAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
            fragmentManager = fm;

        }

        @Override
        public Fragment getItem(int position) {
            return fragmentlist.get(position);
        }

        @Override
        public int getCount() {
            return fragmentlist.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void setServiceTimes(String str){
        if(!str.isEmpty() && mPatient == HOME_SINGED){
            tv_total.setVisibility(View.VISIBLE);
            tv_total.setText(str);
        }
    }
}
