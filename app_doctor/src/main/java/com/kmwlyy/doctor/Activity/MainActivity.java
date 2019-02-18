package com.kmwlyy.doctor.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.Fragment.CenterFragment;
import com.kmwlyy.doctor.Fragment.ElectRecipeFragment;
import com.kmwlyy.doctor.Fragment.HomeFragment;
import com.kmwlyy.doctor.Fragment.OnlineChatFragment;
import com.kmwlyy.doctor.Fragment.ServiceFragment;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.adapter.UnSignRecipeAdapter;
import com.kmwlyy.imchat.TimApplication;
import com.kmwlyy.imchat.message.CustomMessage;
import com.kmwlyy.imchat.message.Message;
import com.kmwlyy.imchat.message.MessageFactory;
import com.kmwlyy.login.MessageEvent;
import com.networkbench.agent.impl.NBSAgent;
import com.networkbench.agent.impl.NBSAppAgent;
import com.tencent.TIMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import cn.org.bjca.sdk.core.bean.ResultBean;
import cn.org.bjca.sdk.core.kit.BJCASDK;
import cn.org.bjca.sdk.core.values.ConstantParams;

public class MainActivity extends BaseActivity implements Observer {
    private static final String TAG = "MainActivity";
    private RadioGroup mRadioGroup;
    private ViewPager mViewPager;
    private List<Fragment> mFragments;
    private CheckBox cb_select;
    private boolean isExit = false;
    private OnlineChatFragment onlineChatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        com.kmwlyy.imchat.model.MessageEvent.getInstance().addObserver(this);
        initlize();
        TimApplication.checkLogin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //注销消息监听
        com.kmwlyy.imchat.model.MessageEvent.getInstance().deleteObserver(this);
    }

    private void initlize() {
        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        mRadioGroup.setOnCheckedChangeListener(mCheckedListener);

        mFragments = new ArrayList<Fragment>();

        onlineChatFragment = new OnlineChatFragment();
        mFragments.add(new HomeFragment());
        mFragments.add(onlineChatFragment);
        mFragments.add(new ServiceFragment());
        mFragments.add(new CenterFragment());

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOnPageChangeListener(mPageListener);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        cb_select = (CheckBox) findViewById(R.id.cb_select);

        //底部的处方签名按钮
        findViewById(R.id.tv_close).setOnClickListener(this);
        findViewById(R.id.tv_cancel_recipe).setOnClickListener(this);
        findViewById(R.id.tv_sign_recipe).setOnClickListener(this);
        findViewById(R.id.ll_sign_all).setOnClickListener(this);
    }

    @Override
    public void update(Observable observable, Object data) {
//        Log.d(TAG, "接收到云通信数据 update : " + data);
        if (observable instanceof com.kmwlyy.imchat.model.MessageEvent && data != null) {
            Message mMessage = MessageFactory.getMessage((TIMMessage) data);
            if (mMessage != null && mMessage instanceof CustomMessage) {
                CustomMessage customMessage = (CustomMessage) mMessage;
                LogUtils.i(TAG, "接收到云通信数据：CustomMessage type: " + customMessage.getType() +
                        ", Ext: " + customMessage.getExt());
                if (customMessage.getExt().equals("Room.StateChanged")) {
                    EventBus.getDefault().post(new CustomMessage.reLoadData());
                }
            }


        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                ToastUtils.showShort(mContext, getResources().getString(R.string.string_exit));
                isExit = true;
                changIsExit();
                return false;
            } else {
                finish();
            }
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.mType) {
            case MessageEvent.TYPE_DOCTOR_ORDER:
                startActivity(new Intent(mContext, OrderListActivity.class));
                break;
            case MessageEvent.TYPE_DOCTOR_RECIPE:
                startActivity(new Intent(mContext, DrugStoreRecipeActivity.class));
                break;
            case MessageEvent.TYPE_DOCTOR_CONSULT:
                mViewPager.setCurrentItem(1);
                onlineChatFragment.setCurrentPage(1);
                break;
        }
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

    private RadioGroup.OnCheckedChangeListener mCheckedListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup arg0, int arg1) {
            switch (arg1) {
                case R.id.btn_clinic://我的诊所
                    mViewPager.setCurrentItem(0);
                    hideInput(mViewPager.getChildAt(0));
                    break;
                case R.id.btn_question://诊室
                    mViewPager.setCurrentItem(1);
                    break;
                case R.id.btn_ask://消息
                    mViewPager.setCurrentItem(2);
                    hideInput(mViewPager.getChildAt(2));
                    break;
                case R.id.btn_center://个人中心
                    mViewPager.setCurrentItem(3);
                    hideInput(mViewPager.getChildAt(3));
                    break;
                default:
                    break;
            }
        }
    };

    //隐藏软键盘
    private void hideInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter(
            getSupportFragmentManager()) {

        @Override
        public int getCount() {
            return mFragments == null ? 0 : mFragments.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return mFragments.get(arg0);
        }
    };

    private ViewPager.OnPageChangeListener mPageListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    mRadioGroup.check(R.id.btn_clinic);
                    break;
                case 1:
                    mRadioGroup.check(R.id.btn_question);
                    break;
                case 2:
                    mRadioGroup.check(R.id.btn_ask);
                    break;
                case 3:
                    mRadioGroup.check(R.id.btn_center);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * 下载医网签证书
     */
    public void getCertYWQ() {
        BJCASDK.getInstance().startDoctor(this, BaseApplication.getInstance().getUserData().BJCA_ClientID);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showSignWingow(ElectRecipeFragment.ShowSignWindow bean) {//打开批量签名窗口
        findViewById(R.id.ll_sign_layout).setVisibility(bean.showWindow ? View.VISIBLE : View.GONE);
        findViewById(R.id.rel_navigation).setVisibility(bean.showWindow ? View.GONE : View.VISIBLE);
        ((CheckBox) findViewById(R.id.cb_select)).setChecked(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void gotoConsult(MessageEvent bean) {//跳转到诊室
        mViewPager.setCurrentItem(1);
    }

    /**
     * 2.通过onActivityResult接收签名返回值
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            ToastUtils.showShort(mContext, "操作取消");
            return;
        }
        if (requestCode == ConstantParams.ACTIVITY_SIGN_DATA) {
            String result = data.getStringExtra(ConstantParams.KEY_SIGN_BACK);
            Gson gson = new Gson();
            ResultBean resultBean = gson.fromJson(result, ResultBean.class);
            if (resultBean != null && TextUtils.equals(resultBean.getStatus(), ConstantParams.SUCCESS)) {
                ToastUtils.showShort(mContext, "签名成功!");
                EventBus.getDefault().post(new ElectRecipeFragment.ShowSignWindow(false));
                EventBus.getDefault().post(new ElectRecipeFragment.SignOperation(ElectRecipeFragment.TYPE_REFRESH));
            } else {
                ToastUtils.showShort(mContext, "签名失败：" + resultBean.getMessage());
            }
        }
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        ElectRecipeFragment.SignOperation bean = null;
        switch (arg0.getId()) {
            case R.id.tv_close: //取消
                NBSAppAgent.onEvent("诊室-电子处方-取消");
                bean = new ElectRecipeFragment.SignOperation(ElectRecipeFragment.TYPE_CLOSE);
                break;
            case R.id.tv_cancel_recipe: //撤回
                NBSAppAgent.onEvent("诊室-电子处方-撤回");
                bean = new ElectRecipeFragment.SignOperation(ElectRecipeFragment.TYPE_CANCEL);
                break;
            case R.id.tv_sign_recipe: //签名
                NBSAppAgent.onEvent("诊室-电子处方-签名");
                bean = new ElectRecipeFragment.SignOperation(ElectRecipeFragment.TYPE_SIGN);
                break;
            case R.id.ll_sign_all: //全选
                NBSAppAgent.onEvent("诊室-电子处方-全选");
                bean = new ElectRecipeFragment.SignOperation(cb_select.isChecked() ?
                        UnSignRecipeAdapter.TYPE_SELECT_NULL : UnSignRecipeAdapter.TYPE_SELECT_ALL);
                cb_select.setChecked(!cb_select.isChecked());
                break;
        }
        EventBus.getDefault().post(bean);
    }


}
