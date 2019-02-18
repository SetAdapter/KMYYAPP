package com.kmwlyy.patient.myservice;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;
import android.content.Context;

import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Description描述: 我的咨询页面
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/20
 */
public class MyConsultActivity extends BaseActivity {
    private static final String TAG = MyConsultActivity.class.getSimpleName();
    public static final String POSITION = "POSITION";

    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    com.astuetz.PagerSlidingTabStrip tabs;

    private List<Fragment> fragmentList;
    private FragmentManager manager;

    private ImageConsultFragment imageConsultFragment;//图文咨询
    private VVConsultFragment voiceVideoConsultFragment;//语音咨询
    private MeetingConsultFragment meetingConsultFragment;//远程会诊

    private int mPosition = 0;

    public static void startMyConsultActivity(Context context, int position){
        Intent intent = new Intent(context, MyConsultActivity.class);
        intent.putExtra(POSITION, position);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctor);
        butterknife.ButterKnife.bind(this);
        mPosition = getIntent().getIntExtra(POSITION,0);
        initToolBar();
        initView();
    }

    private void initToolBar(){
        tv_center.setText(R.string.my_consult);
    }

    private void initView(){
        fragmentList = new ArrayList<Fragment>();
        imageConsultFragment = new ImageConsultFragment();
        voiceVideoConsultFragment = new VVConsultFragment();
        meetingConsultFragment = new MeetingConsultFragment();

        fragmentList.add(imageConsultFragment);
        fragmentList.add(voiceVideoConsultFragment);
        fragmentList.add(meetingConsultFragment);

        manager = getSupportFragmentManager();


        Resources res = getResources();
        String[] titles = new String[]{
                res.getString(R.string.image_word_consult),
                res.getString(R.string.voice_video),
                res.getString(R.string.meeting),
        };

        viewPager.setAdapter(new PagerAdapter(titles,manager));
        viewPager.setOffscreenPageLimit(2);

//        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        tabs.setTextColor(getResources().getColor(R.color.primary_color));
        tabs.setTextSize((int) getResources().getDimension(R.dimen.big_font_size));
        tabs.setViewPager(viewPager);
        tabs.setTextColorStateListResource(R.color.tab_text_color);

        viewPager.setCurrentItem(mPosition);

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
}
