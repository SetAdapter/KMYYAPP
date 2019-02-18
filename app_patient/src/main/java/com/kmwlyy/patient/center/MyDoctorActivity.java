package com.kmwlyy.patient.center;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.kmwlyy.login.EventLoginApi;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Description描述: 我的医生（包括问诊医生和关注医生两个Fragment）
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/13
 */
public class MyDoctorActivity extends BaseActivity{
    private static final String TAG = MyDoctorActivity.class.getSimpleName();


    @BindView(R.id.tv_center)
    TextView tv_center;
//    @BindView(R.id.radioGroup)
//    RadioGroup radioGroup;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    com.astuetz.PagerSlidingTabStrip tabs;

    private List<Fragment> fragmentList;
    private FragmentManager manager;
    private DiagnoseDoctorFragment diagnoseDoctorFragment;
    private FollowDoctorFragment followDoctorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctor);
        butterknife.ButterKnife.bind(this);

        initToolBar();
        initView();
    }

    private void initToolBar(){
        tv_center.setText(R.string.my_doctor2);
    }

    private void initView(){
        fragmentList = new ArrayList<Fragment>();
        diagnoseDoctorFragment = new DiagnoseDoctorFragment();
        followDoctorFragment = new FollowDoctorFragment();

        fragmentList.add(diagnoseDoctorFragment);
        fragmentList.add(followDoctorFragment);

        manager = getSupportFragmentManager();
//        adapter = new Adapter(manager);


        Resources res = getResources();
        String[] titles = new String[]{
                res.getString(R.string.diagnose_doctor),
                res.getString(R.string.follow_doctor),
        };

        viewPager.setAdapter(new PagerAdapter(titles,manager));
        viewPager.setOffscreenPageLimit(2);

//        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        tabs.setTextColor(getResources().getColor(R.color.primary_color));
        tabs.setTextSize((int) getResources().getDimension(R.dimen.big_font_size));
        tabs.setViewPager(viewPager);
//        mServiceTabs.setTabBackground(R.drawable.app_btn);
        tabs.setTextColorStateListResource(R.color.tab_text_color);

//        radioGroup.check(R.id.btn_diagnose_doctor);
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                // TODO Auto-generated method stub
//                if (checkedId == R.id.btn_diagnose_doctor) {
//                    viewPager.setCurrentItem(0);
//                }  else {
//                    viewPager.setCurrentItem(1);
//                }
//            }
//        });
    }


    private class Adapter extends FragmentPagerAdapter {

        public Adapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
            manager = fm;
            // TODO Auto-generated constructor stub
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub

            return fragmentList.get(position);

        }
    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        private String[] mTitles;

        public PagerAdapter(String[] titles, FragmentManager fm) {
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
            return mTitles[position];
        }
    }



//    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
//        @Override
//        public void onPageSelected(int arg0) {
//            switch (arg0) {
//                case 0:
//                    radioGroup.check(R.id.btn_diagnose_doctor);
//                    break;
//                case 1:
//                    radioGroup.check(R.id.btn_follow_doctor);
//                    break;
//            }
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int arg0) {}
//
//        @Override
//        public void onPageScrolled(int arg0, float arg1, int arg2) {}
//    }

}
