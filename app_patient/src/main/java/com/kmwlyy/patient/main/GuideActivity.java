package com.kmwlyy.patient.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kmwlyy.core.util.SPUtils;
import com.kmwlyy.patient.MainActivity;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.viewpagerindicator.IconPageIndicator;
import com.viewpagerindicator.IconPagerAdapter;
import com.winson.ui.widget.ToastMananger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Winson on 2016/9/22.
 */
public class GuideActivity extends BaseActivity {

    public static final String TAG = GuideActivity.class.getSimpleName();

    @BindView(R.id.guide_page)
    ViewPager mGuidePage;
    @BindView(R.id.indicator)
    IconPageIndicator mIndicator;
    @BindView(R.id.guide_enter)
    Button mGuideEnter;

    private ViewPager.OnPageChangeListener mPagerChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        ButterKnife.bind(this);

        mGuideEnter.setVisibility(View.INVISIBLE);

        GuidePagerAdapter adapter = new GuidePagerAdapter(getSupportFragmentManager());
        mGuidePage.setAdapter(adapter);
        mIndicator.setViewPager(mGuidePage);
        mPagerChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 4) {
                    mGuideEnter.setVisibility(View.VISIBLE);
                } else {
                    mGuideEnter.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mGuidePage.addOnPageChangeListener(mPagerChangeListener);

        mGuideEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMain();
            }
        });

    }

    private void startMain() {
        SPUtils.put(this, "last_version", PUtils.getVersion(this));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGuidePage.removeOnPageChangeListener(mPagerChangeListener);
    }

    class GuidePagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

        final int RES[] = {R.drawable.guide_one, R.drawable.guide_two, R.drawable.guide_three, R.drawable.guide_four, R.drawable.guide_five};

        public GuidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return GuideFragment.newInstance(RES[position]);
        }

        @Override
        public int getIconResId(int index) {
            return R.drawable.guide_indicator;
        }

        @Override
        public int getCount() {
            return RES.length;
        }

    }


}
