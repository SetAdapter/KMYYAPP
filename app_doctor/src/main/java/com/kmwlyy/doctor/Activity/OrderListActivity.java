package com.kmwlyy.doctor.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kmwlyy.doctor.Fragment.OrderChatFragment;
import com.kmwlyy.doctor.Fragment.OrderFamilyFragment;
import com.kmwlyy.doctor.Fragment.OrderVoiceFragment;
import com.kmwlyy.doctor.R;
import com.kmwlyy.login.MessageApi;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends BaseActivity {

    private List<Fragment> fragmentList;
    private FragmentManager manager;
    private OrderChatFragment chatFragment;
    private OrderVoiceFragment voiceFragment;
    private OrderFamilyFragment familyFragment;

    @ViewInject(R.id.tv_left)
    TextView btn_left;  //返回
    @ViewInject(R.id.radiogroup)
    RadioGroup mRadioGroup;
    @ViewInject(R.id.viewpager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ViewUtils.inject(this);
        ((TextView) findViewById(R.id.tv_center)).setText(getResources().getString(R.string.string_order));
        btn_left.setOnClickListener(this);
        btn_left.setVisibility(View.VISIBLE);

        init();

        MessageApi.Message mItem = (MessageApi.Message) getIntent().getSerializableExtra("item");
        if (null != mItem) {
            if (mItem.getContent().contains("图文")) {
                mViewPager.setCurrentItem(0);
            } else if (mItem.getContent().contains("视频")) {
                mViewPager.setCurrentItem(1);
            } else if (mItem.getContent().contains("家庭")) {
                mViewPager.setCurrentItem(2);
            }
        }
    }

    private void init() {
        fragmentList = new ArrayList<Fragment>();
        chatFragment = new OrderChatFragment();
        voiceFragment = new OrderVoiceFragment();
        familyFragment = new OrderFamilyFragment();

        fragmentList.add(chatFragment);
        fragmentList.add(voiceFragment);
        fragmentList.add(familyFragment);


        mViewPager.setOnPageChangeListener(mPageListener);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        mRadioGroup.check(R.id.btn_chat);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == R.id.btn_chat) {
                    mViewPager.setCurrentItem(0);
                } else if (checkedId == R.id.btn_voice) {
                    mViewPager.setCurrentItem(1);
                } else {
                    mViewPager.setCurrentItem(2);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_left:    //返回
                finish();
                break;
        }
    }

    private FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter(
            getSupportFragmentManager()) {

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }
    };

    private ViewPager.OnPageChangeListener mPageListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    mRadioGroup.check(R.id.btn_chat);
                    break;
                case 1:
                    mRadioGroup.check(R.id.btn_voice);
                    break;
                case 2:
                    mRadioGroup.check(R.id.btn_video);
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
}
