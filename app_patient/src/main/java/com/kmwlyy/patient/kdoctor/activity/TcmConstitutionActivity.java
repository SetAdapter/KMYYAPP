package com.kmwlyy.patient.kdoctor.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.UserMember;
import com.kmwlyy.core.net.event.HttpUserMember;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.myservice.ImageConsultFragment;
import com.kmwlyy.patient.myservice.MyConsultActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xcj on 2017/8/21.
 */

public class TcmConstitutionActivity extends BaseActivity {

    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    com.astuetz.PagerSlidingTabStrip tabs;

    private List<Fragment> fragmentList;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcm_constitution);
        butterknife.ButterKnife.bind(this);
        initToolBar();
        getList();
    }

    private void initToolBar(){
        tv_center.setText("中医体质");
    }

    private void initView(ArrayList<UserMember> userMembers){
        fragmentList = new ArrayList<Fragment>();
        ArrayList<String> titles = new ArrayList<>();
        for (UserMember userMember: userMembers){
            TcmConstitutionFragment imageConsultFragment = new TcmConstitutionFragment();
//            imageConsultFragment.setInfo(userMember.mMemberName,userMember.mIDNumber,userMember.mMobile,userMember.mGender+"");
            Bundle bundle = new Bundle();
            bundle.putSerializable("userMember",userMember);
            imageConsultFragment.setArguments(bundle);
            fragmentList.add(imageConsultFragment);
            titles.add(userMember.mMemberName);
        }

        manager = getSupportFragmentManager();
        viewPager.setAdapter(new PagerAdapter(titles,manager));
        viewPager.setOffscreenPageLimit(userMembers.size()-1);

//        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        tabs.setTextColor(getResources().getColor(R.color.primary_color));
        tabs.setTextSize((int) getResources().getDimension(R.dimen.big_font_size));
        tabs.setViewPager(viewPager);
        tabs.setTextColorStateListResource(R.color.tab_text_color);

    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<String> mTitles;

        public PagerAdapter(ArrayList<String> titles, FragmentManager fm) {
            super(fm);
            this.mTitles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }

    private void getList(){
        HttpUserMember.GetList getUserMemberList = new HttpUserMember.GetList(1, 20, new HttpListener<ArrayList<UserMember>>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(TcmConstitutionActivity.this,msg);
            }

            @Override
            public void onSuccess(ArrayList<UserMember> userMembers) {
                initView(userMembers);
            }
        });

        HttpClient mGetUserMemberListClient = new HttpClient(this, getUserMemberList);
        mGetUserMemberListClient.start();
    }
}
