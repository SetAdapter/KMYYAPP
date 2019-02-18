package com.kmwlyy.patient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kmt518.kmpay.KMPayConfig;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.imchat.TimApplication;
import com.kmwlyy.login.EventLoginApi;
import com.kmwlyy.login.LoginActivity;
import com.kmwlyy.login.MessageEvent;
import com.kmwlyy.patient.account.ProfileFragment;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.bean.FollowBean;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.helper.utils.PermissionUtils;
import com.kmwlyy.patient.home.HomeFragment;
import com.kmwlyy.patient.kdoctor.activity.AiChatActivity2;
import com.kmwlyy.patient.module.homepage.NewFamilyDoctorFragment;
import com.kmwlyy.patient.myplan.MyPlanActivity;
import com.kmwlyy.patient.myservice.MyConsultActivity;
import com.kmwlyy.patient.myservice.MyDiagnoseActivity;
import com.kmwlyy.patient.myservice.MyFamilyDoctorActivity;
import com.kmwlyy.patient.onlinediagnose.OnlineDiagnoseFragment;
import com.kmwlyy.patient.pay.PayActivity;
import com.kmwlyy.patient.register.RegisterOrderFragment;
import com.winson.ui.widget.ToastMananger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tencent.qalsdk.service.QalService.context;

public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.tab_page)
    ViewPager mTabPage;
    @BindView(R.id.home)
    LinearLayout mHome;
    @BindView(R.id.register_order)
    LinearLayout mRegisterOrder;
//    @BindView(R.id.online_diagnose)
//    LinearLayout mOnlineDiagnose;
//    @BindView(R.id.my_service)
//    LinearLayout mMyService;
    @BindView(R.id.profile)
    LinearLayout mProfile;
    @BindView(R.id.jtyy)
    LinearLayout mHomeDoctor;

    private Fragment[] mTabFragments;
    private int mPosition;

    private boolean isExit = false;


    /**
     * 康美支付 广播通知
     */
    private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (KMPayConfig.ACTION_PAY_RESULT == intent.getAction()) {
                dealResult(intent.getStringExtra(KMPayConfig.RESULT_CODE));
            }
        }
    };

    /**
     * 处理支付结果
     *
     * @param code
     */
    private void dealResult(String code) {
        if (DebugUtils.debug) {
            Log.d(TAG, "kangmei pay result : " + code);
        }
        if (TextUtils.isEmpty(code)) {
            return;
        }
        switch (code) {
            case KMPayConfig.RESULT_SUCCEED:
                Toast.makeText(this, R.string.pay_success, Toast.LENGTH_SHORT).show();
                EventApi.Pay pay = new EventApi.Pay();
                pay.success = true;
                EventBus.getDefault().post(pay);
                break;
            case KMPayConfig.RESULT_FAILED:
                Toast.makeText(this, R.string.pay_fail, Toast.LENGTH_SHORT).show();
                break;
            case KMPayConfig.RESULT_CANCELED:
                Toast.makeText(this, R.string.pay_cancel, Toast.LENGTH_SHORT).show();
                break;
            case KMPayConfig.RESULT_PROCESS:
                Toast.makeText(this, R.string.pay_on_bank, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSkipFinsih = true;
        registerReceiver(mResultReceiver, new IntentFilter(KMPayConfig.ACTION_PAY_RESULT));

        PermissionUtils.verifyStoragePermissions(this);

        mTabFragments = new Fragment[]{
                new NewFamilyDoctorFragment(),
                new HomeFragment(),
                new RegisterOrderFragment(),
                //new OnlineDiagnoseFragment(),
                new ProfileFragment()
        };

        setDonotClickBack(true);//禁止自动设置左上角按钮点击返回
        mHomeDoctor.setOnClickListener(this);
        mHome.setOnClickListener(this);
        mRegisterOrder.setOnClickListener(this);
//        mOnlineDiagnose.setOnClickListener(this);
        mProfile.setOnClickListener(this);

        mTabPage.setAdapter(new MainPagerAdapter(mTabFragments, getSupportFragmentManager()));
        mTabPage.setOnPageChangeListener(this);
        mTabPage.setCurrentItem(0);
        mHomeDoctor.setSelected(true);
        mTabPage.setOffscreenPageLimit(4);

        TimApplication.checkLogin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mResultReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                ToastUtils.showShort(this, getResources().getString(R.string.string_exit));
                isExit = true;
                changIsExit();
                return false;
            } else {
                finish();
            }
        }
        return false;
    }

    private void changIsExit() {
        new Thread() {
            public void run() {
                try {
                    sleep(2000);
                    isExit = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){
        switch (event.mType){
            case MessageEvent.TYPE_USER_CONSULT:
//                mTabPage.setCurrentItem(3);
                //点击通知中的消息，跳转到我的咨询页面
                if (PUtils.checkHaveUser(this)) {
                    Intent intent = new Intent(this, MyConsultActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FollowBean event){
        AiChatActivity2.jump(MainActivity.this,AiChatActivity2.FOLLOW_UP,event.sessionID);
    }

    @Override
    public void onLogout(EventApi.Logout logout) {
        if (!logout.active) {
            ToastMananger.showToast(this, R.string.user_expired_please_login, Toast.LENGTH_SHORT);
        }
        CommonUtils.startActivity(this, MainActivity.class);
        CommonUtils.startActivity(this, LoginActivity.class);
        mTabPage.setCurrentItem(0);
        super.onLogout(logout);
    }

    @Override
    public void onLogin(EventLoginApi.Login login) {
        super.onLogin(login);
        TimApplication.setListener();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectTab(EventApi.MainTabSelect select) {
//        if (select.position == 3) {
//            if (mTabFragments[select.position] instanceof MyServiceFragment) {
//                MyServiceFragment msf = (MyServiceFragment) mTabFragments[select.position];
//                msf.selectPosition(select.secondPosition);
//            }
//        }
        mTabPage.setCurrentItem(select.position);
        if (select.position == 3) {
            if (select.type == EventApi.MainTabSelect.MY_COSULT) {
                MyConsultActivity.startMyConsultActivity(MainActivity.this, select.secondPosition);
            }else if(select.type == EventApi.MainTabSelect.MY_DIOAGNOSE){
                Intent diagnose = new Intent(MainActivity.this, MyDiagnoseActivity.class);
                startActivity(diagnose);
            }else if(select.type == EventApi.MainTabSelect.MY_PLAN){
                Intent plan = new Intent(MainActivity.this, MyPlanActivity.class);
                startActivity(plan);
            }else if(select.type == EventApi.MainTabSelect.MY_DOCTOR){
                Intent intent = new Intent(MainActivity.this, MyFamilyDoctorActivity.class);
                startActivity(intent);
            }
        }
//        EventApi.MyServiceTab myService = new EventApi.MyServiceTab();
//        myService.position = select.secondPosition;
//        EventBus.getDefault().post(myService);

    }

    private void clearSelect() {
        mHome.setSelected(false);
        mHomeDoctor.setSelected(false);
        mRegisterOrder.setSelected(false);
//        mOnlineDiagnose.setSelected(false);
//        mMyService.setSelected(false);
        mProfile.setSelected(false);
    }

    @Override
    public void onClick(View v) {
//        clearSelect();
        int id = v.getId();
        switch (id) {
            case R.id.jtyy:
                mTabPage.setCurrentItem(0);
//                mHome.setSelected(true);
                hideInput(mTabPage.getChildAt(0));
                break;
            case R.id.home:
                mTabPage.setCurrentItem(1);
//                mHome.setSelected(true);
                hideInput(mTabPage.getChildAt(1));
                break;
            case R.id.register_order:
                mTabPage.setCurrentItem(2);
//                mRegisterOrder.setSelected(true);
                hideInput(mTabPage.getChildAt(2));
                break;
//            case R.id.online_diagnose:
//                mTabPage.setCurrentItem(2);
////                mOnlineDiagnose.setSelected(true);
//                hideInput(mTabPage.getChildAt(2));
//                break;
//            case R.id.my_service:
//                mTabPage.setCurrentItem(3);
////                mMyService.setSelected(true);
//                break;
            case R.id.profile:
                mTabPage.setCurrentItem(3);
//                mProfile.setSelected(true);
                hideInput(mTabPage.getChildAt(3));
                break;
        }
    }

    //隐藏软键盘
    private void hideInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        clearSelect();
        switch (position) {
            case 0:
                mHomeDoctor.setSelected(true);
                break;
            case 1:
                mHome.setSelected(true);
                break;
            case 2:
                mRegisterOrder.setSelected(true);
//                mOnlineDiagnose.setSelected(true);
                break;
            case 3:
                mProfile.setSelected(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class MainPagerAdapter extends FragmentStatePagerAdapter {

        Fragment[] mFragments;

        public MainPagerAdapter(Fragment[] fragments, FragmentManager fm) {
            super(fm);
            this.mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }
    }

}
