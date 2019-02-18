package com.kmwlyy.patient.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.address.page.ContainerActivity;
import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.bean.UserData;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.patient.PApplication;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.casehistory.ElectronCaseHistoryActivity;
import com.kmwlyy.patient.center.MyDoctorActivity;
import com.kmwlyy.patient.center.SettingActivity;
import com.kmwlyy.patient.helper.base.BaseFragment;
import com.kmwlyy.patient.helper.utils.LibUtils;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.main.H5WebViewActivity;
import com.kmwlyy.patient.myplan.MyPlanActivity;
import com.kmwlyy.patient.myservice.MyConsultActivity;
import com.kmwlyy.patient.myservice.MyFamilyDoctorActivity;
import com.kmwlyy.patient.myservice.MyDiagnoseActivity;
import com.kmwlyy.personinfo.EventApi;
import com.kmwlyy.personinfo.PersonInfoActivity;
import com.kmwlyy.usermember.Event;
import com.kmwlyy.usermember.UserMemberListActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

//import com.kmwlyy.usermember.UserMemberListActivity;

/**
 * Created by Winson on 2016/8/9.
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = ProfileFragment.class.getSimpleName();

    @BindView(R.id.profile_bg)
    LinearLayout mProfileBg;//顶部用户信息栏
    @BindView(R.id.user_avatar)
    ImageView mUserAvatar;//用户头像
    @BindView(R.id.user_name)
    TextView mUserName;//用户名
    @BindView(R.id.user_phone)
    TextView user_phone;//用户手机号

    @BindView(R.id.my_consult)
    LinearLayout my_consult;//我的咨询
    @BindView(R.id.my_diagnose)
    LinearLayout my_diagnose;//我的处方
    @BindView(R.id.home_doctor)
    LinearLayout home_doctor;//家庭医生
    @BindView(R.id.my_package)
    LinearLayout my_package;//我的套餐

    @BindView(R.id.personal_data)
    LinearLayout mPersonalData;//个人资料
    @BindView(R.id.account_management)
    LinearLayout account_management;//账户管理
    @BindView(R.id.my_doctor)
    LinearLayout my_doctor;//我的医生
    @BindView(R.id.CPR)
    LinearLayout CPR;//电子病历
    @BindView(R.id.user_member)
    LinearLayout user_member;//家庭成员
    @BindView(R.id.user_address)
    LinearLayout user_address;//配送地址
    @BindView(R.id.setting)
    LinearLayout setting;//设置

    private boolean mIsLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile, container, false);
        ButterKnife.bind(this, root);
        EventBus.getDefault().register(this);


        mProfileBg.setOnClickListener(this);
        mPersonalData.setOnClickListener(this);
        account_management.setOnClickListener(this);
        CPR.setOnClickListener(this);
        setting.setOnClickListener(this);
        my_diagnose.setOnClickListener(this);
        my_doctor.setOnClickListener(this);
        my_package.setOnClickListener(this);
        my_consult.setOnClickListener(this);
        user_member.setOnClickListener(this);
        user_address.setOnClickListener(this);
        home_doctor.setOnClickListener(this);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsLogin = BaseApplication.getInstance().hasUserToken();
        if (mIsLogin) {
//            mUserName.setTextColor(getResources().getColor(R.color.white));

            String mobile = BaseApplication.getInstance().getUserData().mMobile;
            String userName = BaseApplication.getInstance().getUserData().mUserCNName;
            if (userName == null || userName.equals(mobile))
            {
                mUserName.setText(mobile);
                user_phone.setVisibility(View.GONE);
            }
            else{
                mUserName.setText(userName);
                user_phone.setText(mobile);
                user_phone.setVisibility(View.VISIBLE);
            }
//            mProfileBg.setBackgroundColor(
//                    getResources().getColor(R.color.primary_color)
//            );
            ImageLoader.getInstance().displayImage(
                    BaseApplication.getInstance().getUserData().PhotoUrl
                    , mUserAvatar, LibUtils.getCircleDisplayOptions(R.drawable.default_avatar_patient));
        } else {
            mUserName.setText(R.string.user_login_or_register);
            user_phone.setVisibility(View.GONE);
//            mUserName.setTextColor(getResources().getColor(R.color.third_text_color));
//            mProfileBg.setBackgroundColor(
//                    getResources().getColor(R.color.no_login_bg)
//            );
            ImageLoader.getInstance().displayImage(
                    BaseApplication.getInstance().getUserData().mUrl
                    , mUserAvatar, LibUtils.getCircleDisplayOptions(R.drawable.default_avatar_patient));

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHeadPhotoEvent(EventApi.HeadPhoto event){
        UserData userData = BaseApplication.getInstance().getUserData();
        userData.PhotoUrl = event.photoUrl;
        BaseApplication.getInstance().setUserData(userData);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void nickName(EventApi.NickName event){
        if (!TextUtils.isEmpty(event.nickname)){
            UserData userData = BaseApplication.getInstance().getUserData();
            userData.mUserCNName = event.nickname;
            BaseApplication.getInstance().setUserData(userData);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void nickName(Event.UserMemberChange event){
        if (!TextUtils.isEmpty(event.nickname)){
            UserData userData = BaseApplication.getInstance().getUserData();
            userData.mUserCNName = event.nickname;
            BaseApplication.getInstance().setUserData(userData);
        }
        if (!TextUtils.isEmpty(event.mobile)){
            UserData userData = BaseApplication.getInstance().getUserData();
            userData.mMobile = event.mobile;
            BaseApplication.getInstance().setUserData(userData);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            //我的咨询
            case R.id.my_consult:
                if (PUtils.checkHaveUser(getActivity())) {
                    Intent intent1 = new Intent(getActivity(), MyConsultActivity.class);
//                    Intent intent1 = new Intent(getActivity(), H5WebViewActivity.class);
                    startActivity(intent1);
                }
                break;
            //我的处方
            case R.id.my_diagnose:
                if (PUtils.checkHaveUser(getActivity())) {
                    Intent intent = new Intent(getActivity(), MyDiagnoseActivity.class);
                    startActivity(intent);
                }
                break;
            //家庭医生
            case R.id.home_doctor:
                if (PUtils.checkHaveUser(getActivity())) {
                    Intent intent = new Intent(getActivity(), MyFamilyDoctorActivity.class);
                    startActivity(intent);
                }
                break;
            //我的套餐
            case R.id.my_package:
                if (PUtils.checkHaveUser(getActivity())) {
                    Intent intent = new Intent(getActivity(), MyPlanActivity.class);
                    startActivity(intent);
                }
                break;
            //个人资料
            case R.id.personal_data:
                if (PUtils.checkHaveUser(getActivity())) {
                    PersonInfoActivity.startPersonInfoActivity(getActivity(), true, "",BaseApplication.getInstance().getUserData().PhotoUrl,
                            BaseApplication.getInstance().getUserData().mUserCNName, BaseApplication.getInstance().getUserData().mMobile );
                }
                break;
            //账户管理
            case R.id.account_management:
                if (PUtils.checkHaveUser(getActivity())) {
                    Intent intent = new Intent(getActivity(), AccountManagementActivity.class);
                    startActivity(intent);
                }
                break;
            //我的医生
            case R.id.my_doctor:
                if (PUtils.checkHaveUser(getActivity())) {
                    Intent intent = new Intent(getActivity(), MyDoctorActivity.class);
                    startActivity(intent);
                }
                break;
            //电子病历
            case R.id.CPR:
                if (PUtils.checkHaveUser(getActivity())) {
                    Intent intent = new Intent(getActivity(), ElectronCaseHistoryActivity.class);
                    startActivity(intent);
                }
                break;
            //配送地址
            case R.id.user_address:
                if (PUtils.checkHaveUser(getActivity())) {
                    Intent intent = new Intent(getActivity(), ContainerActivity.class);
                    intent.putExtra(ContainerActivity.TAG_FRAGMENT, ContainerActivity.ADDRESSLIST);
                    startActivity(intent);
                }
                break;
            //家庭成员
            case R.id.user_member:
                if (PUtils.checkHaveUser(getActivity())) {
                    CommonUtils.startActivity(getActivity(), UserMemberListActivity.class);
                }
                break;
            //设置
            case R.id.setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.profile_bg:
                PUtils.checkHaveUser(getActivity());
                break;
        }
    }

}
