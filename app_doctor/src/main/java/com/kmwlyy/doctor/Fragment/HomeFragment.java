package com.kmwlyy.doctor.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ImageUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.doctor.Activity.BannerActivity;
import com.kmwlyy.doctor.Activity.BaseActivity;
import com.kmwlyy.doctor.Activity.CalendarActivity;
import com.kmwlyy.doctor.Activity.PatientManageActivity;
import com.kmwlyy.doctor.Activity.ScheduleActivity;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.jpush.MyMessageActivity;
import com.kmwlyy.doctor.model.BannerItem;
import com.kmwlyy.doctor.model.DoctorService;
import com.kmwlyy.doctor.model.httpEvent.BannerGetList;
import com.kmwlyy.doctor.model.httpEvent.HttpDoctorService;
import com.networkbench.agent.impl.NBSAppAgent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.CircleViewPager;
import com.winson.ui.widget.PagerIndicator;
import com.kmwlyy.login.EventMessageClear;
import com.winson.ui.widget.RateLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Winson on 2017/6/27.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = HomeFragment.class.getSimpleName();

    RateLayout mBanner;
    View mMySchedule;
    View mMyPatientManage;
    ScrollView sv_home;
    TextView mVideoSubscribeTV, mIMSubscribeTV;
    String mVideoSubFormat, mIMSubFormat;
    CircleViewPager mBannerPager;
    PagerIndicator mBannerIndicator;
    HomePagerAdapter mHomePagerAdapter;
    Boolean updateFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mBanner = (RateLayout) root.findViewById(R.id.banner);
        mMySchedule = root.findViewById(R.id.my_schedule);
        mBannerPager = (CircleViewPager) root.findViewById(R.id.banner_pager);
        mBannerIndicator = (PagerIndicator) root.findViewById(R.id.banner_indicator);
        mMyPatientManage = root.findViewById(R.id.my_patient_manage);
        mVideoSubscribeTV = (TextView) root.findViewById(R.id.video_subscribe);
        mIMSubscribeTV = (TextView) root.findViewById(R.id.im_subscribe);

        root.findViewById(R.id.tv_message_all).setOnClickListener(this);
        mBanner.setOnClickListener(this);
        mMySchedule.setOnClickListener(this);
        mMyPatientManage.setOnClickListener(this);

        sv_home = (ScrollView) root.findViewById(R.id.sv_home);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mVideoSubFormat = getActivity().getResources().getString(R.string.video_subscribe_format);
        mIMSubFormat = getActivity().getResources().getString(R.string.im_subscribe_format);

        mHomePagerAdapter = new HomePagerAdapter();
        mBannerPager.setAdapter(mHomePagerAdapter);
        mBannerPager.setPagerIndicator(mBannerIndicator);
        mBannerPager.setDuration(3000);

        updateSubscribeCount(0, 0);
        getBannerList();

    }

    @Override
    public void onResume() {
        super.onResume();
        getTodayApptCount();
        if (mBannerPager != null) {
            mBannerPager.runImageSlide();
        }
        updateFlag = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(updateFlag && isVisibleToUser){
            getTodayApptCount();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBannerPager != null) {
            mBannerPager.stopImageSlide();
        }
    }

    private void updateSubscribeCount(int videoCount, int imCount) {
        if (mVideoSubscribeTV != null) {
            mVideoSubscribeTV.setText(Html.fromHtml(String.format(mVideoSubFormat, videoCount)));
        }
        if (mIMSubscribeTV != null) {
            mIMSubscribeTV.setText(Html.fromHtml(String.format(mIMSubFormat, imCount)));
        }
    }

    private void getTodayApptCount() {
        HttpDoctorService.GetTodayApptCount getTodayApptCount = new HttpDoctorService.GetTodayApptCount(new HttpListener<DoctorService>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("getTodayApptCount", code, msg));
                }

                updateSubscribeCount(0, 0);

            }

            @Override
            public void onSuccess(DoctorService doctorService) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("getTodayApptCount", DebugUtils.toJson(doctorService)));
                }

                updateSubscribeCount(doctorService.vidCount + doctorService.audCount, doctorService.picNoReplyCount);
            }
        });

        HttpClient httpClient = NetService.createClient(getActivity(), HttpClient.DOCTOR_API_URL, getTodayApptCount);
        httpClient.start();
    }

    private void getBannerList() {
        BannerGetList bannerGetList = new BannerGetList(new HttpListener<ArrayList<BannerItem>>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("getBannerList", code, msg));
                }
            }

            @Override
            public void onSuccess(ArrayList<BannerItem> bannerItems) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("getBannerList", DebugUtils.toJson(bannerItems)));
                }
//                if (bannerItems == null) {
//                    bannerItems = new ArrayList<>();
//                }
//
//                bannerItems.add(new BannerItem());
//                bannerItems.add(new BannerItem());
//                bannerItems.add(new BannerItem());
                mHomePagerAdapter.updateBannerItems(bannerItems);
                mBannerPager.notifyDataSetChanged();
            }
        });

        HttpClient client = NetService.createClient(getActivity(), HttpClient.KMYY_URL_2, bannerGetList);
        client.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_message_all://查看全部消息
                NBSAppAgent.onEvent("首页-查看全部");
                getActivity().startActivity(new Intent(getActivity(), MyMessageActivity.class));
                break;
            case R.id.my_schedule:
                NBSAppAgent.onEvent("首页-我的日程");
                getActivity().startActivity(new Intent(getActivity(), ScheduleActivity.class));
                break;
            case R.id.my_patient_manage:
                NBSAppAgent.onEvent("首页-患者管理");
                getActivity().startActivity(new Intent(getActivity(), PatientManageActivity.class));
                break;
        }
    }

    class HomePagerAdapter extends PagerAdapter {

        ArrayList<BannerItem> mBannerItems;

        public void updateBannerItems(ArrayList<BannerItem> datas) {
            mBannerItems = datas;
//            notifyDataSetChanged();
        }

        public BannerItem getItem(int position) {
            if (mBannerItems == null || mBannerItems.isEmpty() || position >= mBannerItems.size()) {
                return null;
            }
            return mBannerItems.get(position);
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {

//            RateLayout layout = new RateLayout(container.getContext());
//            layout.setRate(0.4537f);

            ImageView imageView = new ImageView(container.getContext());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(lp);
//            if (position == 0) {
//                imageView.setBackgroundResource(R.drawable.test_banner);
//            } else if (position == 1) {
//                imageView.setBackgroundResource(R.drawable.test_banner_2);
//            } else {
//                imageView.setBackgroundResource(R.drawable.test_banner_3);
//            }
//            layout.addView(imageView);

//            layout.setLayoutParams(lp);
            container.addView(imageView);

            final BannerItem bannerItem = mHomePagerAdapter.getItem(position);

            ImageLoader.getInstance().displayImage(
                    bannerItem.imgUrl == null ? null : HttpClient.IMAGE_URL + File.separator + bannerItem.imgUrl,
                    imageView, ImageUtils.getRectDisplayOptions(R.drawable.default_banner));

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!TextUtils.isEmpty(bannerItem.targetUrl)) {
                        NBSAppAgent.onEvent("首页-Banner-bannerItem.targetUrl");
                        BannerActivity.startSelf(v.getContext(), bannerItem.bannerName, bannerItem.targetUrl);
                    }
                }
            });

            return imageView;
        }

        @Override
        public int getCount() {
            return mBannerItems == null ? 0 : mBannerItems.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
