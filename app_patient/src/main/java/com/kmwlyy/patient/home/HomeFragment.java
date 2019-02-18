package com.kmwlyy.patient.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ImageUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.login.EventMessageClear;
import com.kmwlyy.login.MessageApi;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseFragment;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.Doctor;
import com.kmwlyy.patient.helper.net.bean.HealthInformationBean;
import com.kmwlyy.patient.helper.net.event.ExpertRecommendEvent;
import com.kmwlyy.patient.helper.net.event.HealthInformationEvent;
import com.kmwlyy.patient.helper.net.event.HttpUserConsults;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.LibUtils;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.jpush.MyMessageActivity;
import com.kmwlyy.patient.main.PerfectInfoActivity;
import com.kmwlyy.patient.myplan.MembersPlanActivity;
import com.kmwlyy.patient.onlinediagnose.BuyIMConsultActivity;
import com.kmwlyy.patient.onlinediagnose.DoctorServiceActivity;
import com.kmwlyy.patient.onlinediagnose.OnlineDiagnoseActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.CircleViewPager;
import com.winson.ui.widget.InsideListView;
import com.winson.ui.widget.PagerIndicator;
import com.winson.ui.widget.RateLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Alois on 2016/12/9.
 */

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = HomeFragment.class.getSimpleName();

    private final int DEFAULT_PAGESIZE = 5;
    private final int DEFAULT_CURRENT_PAGE = 1;
    private final int DEFAULT_EXPERT_PAGESIZE = 4;
    private final int DEFAULT_EXPERT_PAGE = 1;

    EventApi.MainTabSelect mainTabSelect;

    private HttpClient getExpertRecommendClient;
    private HttpClient getHealthInfoClient;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    /* 顶部栏 */
    @BindView(R.id.tv_center)
    TextView tv_title_center;
    @BindView(R.id.tv_right)
    TextView tv_right;
    /* 轮播图 */
    @BindView(R.id.home_banner_pager)
    CircleViewPager home_banner_pager;
    @BindView(R.id.home_banner_indicator)
    PagerIndicator home_banner_indicator;

    @BindView(R.id.lv_health_information)
    InsideListView listView;


    /* 推荐专家 */
    @BindView(R.id.ll_clinic_all)
    LinearLayout ll_clinic_all;
    @BindView(R.id.tv_clinic_none)
    TextView tv_clinic_none;
    @BindView(R.id.iv_clinic_one)
    ImageView iv_clinic_one;
    @BindView(R.id.tv_clinic_one_name)
    TextView tv_clinic_one_name;
    @BindView(R.id.tv_clinic_one_title)
    TextView tv_clinic_one_title;
    @BindView(R.id.tv_clinic_one_hospital_name)
    TextView tv_clinic_one_hospital_name;
    @BindView(R.id.iv_clinic_two)
    ImageView iv_clinic_two;
    @BindView(R.id.tv_clinic_two_name)
    TextView tv_clinic_two_name;
    @BindView(R.id.tv_clinic_two_title)
    TextView tv_clinic_two_title;
    @BindView(R.id.tv_clinic_two_hospital_name)
    TextView tv_clinic_two_hospital_name;
    @BindView(R.id.iv_clinic_three)
    ImageView iv_clinic_three;
    @BindView(R.id.tv_clinic_three_name)
    TextView tv_clinic_three_name;
    @BindView(R.id.tv_clinic_three_title)
    TextView tv_clinic_three_title;
    @BindView(R.id.tv_clinic_three_hospital_name)
    TextView tv_clinic_three_hospital_name;
    @BindView(R.id.iv_clinic_four)
    ImageView iv_clinic_four;
    @BindView(R.id.tv_clinic_four_name)
    TextView tv_clinic_four_name;
    @BindView(R.id.tv_clinic_four_title)
    TextView tv_clinic_four_title;
    @BindView(R.id.tv_clinic_four_hospital_name)
    TextView tv_clinic_four_hospital_name;


    private HealthInfoAdapter healthInfoAdapter;
    private ArrayList<HealthInformationBean> healthInfoDataList = new ArrayList<HealthInformationBean>();
    private ArrayList<Doctor.ListItem> docrotList = new ArrayList<>();

    private final int DEFAULT_STATE = 0;//默认状态
    private final int FAIL_STATE = 1;//失败状态
    private final int SUCCESS_STATE = 2;//成功状态


    private int getHealthInfoState = DEFAULT_STATE;//获取资讯列表的结果
    private int getExpertState = DEFAULT_STATE;//获取专家推荐的结果


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        getMsgUnreadCount(new MessageApi.Message());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mainTabSelect = new EventApi.MainTabSelect();

        ButterKnife.bind(this, root);
        tv_title_center.setText("网络医院");

        initBannerModule();
        initSwipeRefreshLayout();
        healthInfoAdapter = new HealthInfoAdapter(getContext());
        listView.setAdapter(healthInfoAdapter);
        healthInfoAdapter.notifyDataSetChanged();
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (home_banner_pager != null) {
            home_banner_pager.runImageSlide();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (home_banner_pager != null) {
            home_banner_pager.stopImageSlide();
        }
    }

    private void initSwipeRefreshLayout() {
        swipeRefresh.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);


        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                if ((getExpertState == DEFAULT_STATE) || (getHealthInfoState == DEFAULT_STATE)){
                    swipeRefresh.setRefreshing(true);
                }
            }
        });
        onRefresh();
    }

    private void initBannerModule() {
        HomePagerAdapter adapter = new HomePagerAdapter();
        home_banner_pager.setAdapter(adapter);
        home_banner_pager.setPagerIndicator(home_banner_indicator);
        home_banner_pager.setDuration(3000);
    }

    @Override
    public void onRefresh() {
        getHealthInformation();
        getExpertRecommend();
    }


    //判断是否完成全部的刷新动作
    private void isRefreshFinish() {
        if ((getExpertState != DEFAULT_STATE) && (getHealthInfoState != DEFAULT_STATE)) {
            swipeRefresh.setRefreshing(false);
        }
    }

    @OnClick(R.id.tv_right)
    public void jumpMessagePage() {
        if (BaseApplication.instance.hasUserToken()) {
            startActivity(new Intent(getActivity(), MyMessageActivity.class));
        } else {
            BaseApplication.instance.logout();
        }
    }

    //右上角消息按钮,跳转消息界面
    @OnClick(R.id.iv_right)
    public void jumpMessage() {
        if (BaseApplication.instance.hasUserToken()) {
            startActivity(new Intent(getActivity(), MyMessageActivity.class));
        } else {
            BaseApplication.instance.logout();
        }
    }

    //在线问诊
    @OnClick(R.id.ll_online_diagnose)
    public void jumpOnlineDiagnose() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), OnlineDiagnoseActivity.class);
        startActivity(intent);
//        Intent intent = new Intent();
//        intent.setClass(getActivity(), PerfectInfoActivity.class);
//        startActivity(intent);
//        mainTabSelect.position = EventApi.MainTabSelect.ONLINE_DIAGNOSE;
//        EventBus.getDefault().post(mainTabSelect);
    }

    //预约挂号
    @OnClick(R.id.ll_register_order)
    public void jumpRegisterOrder() {
        mainTabSelect.position = EventApi.MainTabSelect.ONLINE_DIAGNOSE;
        EventBus.getDefault().post(mainTabSelect);
    }

    //今日义诊
    @OnClick(R.id.ll_duty_diagnose)
    public void jumpDutyDiagnose() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), DoctorDutyListActivity.class);
        startActivity(intent);
    }

    //免费咨询
    @OnClick(R.id.ll_free_consult)
    public void jumpFreeConsult() {
        if (PUtils.checkHaveUser(getActivity())) {
            BuyIMConsultActivity.startBuyIMConsultActivity(getActivity(), null, HttpUserConsults.FREE,0+"");
        }
    }

    //会员套餐
    @OnClick(R.id.iv_package)
    public void jumpMemberPackage() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), MembersPlanActivity.class);
        startActivity(intent);
    }

    //跳转健康咨询列表
    @OnClick(R.id.iv_more_healthInfo)
    public void jumpHealthInfoList() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), HealthInfoListActivity.class);
        startActivity(intent);
    }

    //跳转健康咨询列表
    @OnClick(R.id.tv_show_more_info)
    public void jumpHealthInfoList2() {
        jumpHealthInfoList();
    }

    //跳转专家推荐列表
    @OnClick(R.id.fl_expert_recommend)
    public void jumpExpertRecommend() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ExpertRecommendListActivity.class);
        startActivity(intent);
    }

    //跳转医生详情
    @OnClick(R.id.ll_clinic_one)
    public void jumpDoctorDetailOne() {
        try {
            jumpDoctorDetail(docrotList.get(0));
        } catch (Exception e) {
        }

    }

    //跳转医生详情
    @OnClick(R.id.ll_clinic_two)
    public void jumpDoctorDetailTwo() {
        try {
            jumpDoctorDetail(docrotList.get(1));
        } catch (Exception e) {
        }
    }

    //跳转医生详情
    @OnClick(R.id.ll_clinic_three)
    public void jumpDoctorDetailThree() {
        try {
            jumpDoctorDetail(docrotList.get(2));
        } catch (Exception e) {
        }

    }

    //跳转医生详情
    @OnClick(R.id.ll_clinic_four)
    public void jumpDoctorDetailFour() {
        try {
            jumpDoctorDetail(docrotList.get(3));
        } catch (Exception e) {
        }
    }

    //跳转医生详情
    private void jumpDoctorDetail(Doctor.ListItem data) throws Exception {
        DoctorServiceActivity.startDoctorServiceActivity(getContext(), data, true);
    }


    //获取健康资讯
    private void getHealthInformation() {
        getHealthInfoState = DEFAULT_STATE;
        HealthInformationEvent.GetList getHealthInformation = new HealthInformationEvent.GetList(
                DEFAULT_CURRENT_PAGE, DEFAULT_PAGESIZE,
                new HttpListener<ArrayList<HealthInformationBean>>() {
                    @Override
                    public void onError(int code, String msg) {
                        getHealthInfoState = FAIL_STATE;
                        isRefreshFinish();
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("getFamilyDoctorList", code, msg));
                        }
                    }

                    @Override
                    public void onSuccess(ArrayList<HealthInformationBean> healthInformation) {
                        getHealthInfoState = SUCCESS_STATE;
                        isRefreshFinish();
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("getDoctorClinicList", DebugUtils.toJson(healthInformation)));
                        }
                        healthInfoDataList.clear();
                        healthInfoDataList.addAll(healthInformation);
                        healthInfoAdapter.notifyDataSetChanged();
                    }
                }
        );

        getHealthInfoClient = NetService.createClient(getContext(), getHealthInformation);
        getHealthInfoClient.start();
    }


    //获取推荐专家
    private void getExpertRecommend() {
        getExpertState = DEFAULT_STATE;
        docrotList.clear();

        ExpertRecommendEvent.GetList getExpertRecommend = new ExpertRecommendEvent.GetList(
                DEFAULT_EXPERT_PAGE,
                DEFAULT_EXPERT_PAGESIZE,
                new HttpListener<ArrayList<Doctor.ListItem>>() {
                    @Override
                    public void onError(int code, String msg) {
                        getExpertState = FAIL_STATE;
                        isRefreshFinish();
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("getExpertRecommendList", code, msg));
                        }
                        ll_clinic_all.setVisibility(View.GONE);
                        tv_clinic_none.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onSuccess(ArrayList<Doctor.ListItem> expertRecommendDoctors) {
                        getExpertState = SUCCESS_STATE;
                        isRefreshFinish();
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("getExpertRecommendList", DebugUtils.toJson(expertRecommendDoctors)));
                        }
                        try {
                            ll_clinic_all.setVisibility(View.VISIBLE);
                            tv_clinic_none.setVisibility(View.GONE);
                            docrotList.addAll(expertRecommendDoctors);
                            initExpertRecommend();
                        } catch (Exception e) {

                        }


                    }
                }
        );

        getExpertRecommendClient = NetService.createClient(getContext(), getExpertRecommend);
        getExpertRecommendClient.start();
    }


    private void initExpertRecommend() throws Exception {
        if (docrotList.size() > 3) {
            Doctor.ListItem four = docrotList.get(3);
            tv_clinic_four_name.setText(four.mDoctorName);
            tv_clinic_four_title.setText(PUtils.getDoctorTitle(getContext(),four.mTitle));
            tv_clinic_four_hospital_name.setText(four.mHospitalName);
            ImageLoader.getInstance().displayImage(four.mUser.mPhotoUrl, iv_clinic_four, LibUtils.getCircleDisplayOptions(R.drawable.default_avatar_doctor));
        }
        else{

        }

        if (docrotList.size() > 2) {
            Doctor.ListItem three = docrotList.get(2);
            tv_clinic_three_name.setText(three.mDoctorName);
            tv_clinic_three_title.setText(PUtils.getDoctorTitle(getContext(),three.mTitle));
            tv_clinic_three_hospital_name.setText(three.mHospitalName);
            ImageLoader.getInstance().displayImage(three.mUser.mPhotoUrl, iv_clinic_three, LibUtils.getCircleDisplayOptions(R.drawable.default_avatar_doctor));
        }


        if (docrotList.size() > 1) {
            Doctor.ListItem two = docrotList.get(1);
            tv_clinic_two_name.setText(two.mDoctorName);
            tv_clinic_two_title.setText(PUtils.getDoctorTitle(getContext(),two.mTitle));
            tv_clinic_two_hospital_name.setText(two.mHospitalName);
            ImageLoader.getInstance().displayImage(two.mUser.mPhotoUrl, iv_clinic_two, LibUtils.getCircleDisplayOptions(R.drawable.default_avatar_doctor));
        }

        if (docrotList.size() > 0)
        {
            Doctor.ListItem one = docrotList.get(0);
            tv_clinic_one_name.setText(one.mDoctorName);
            tv_clinic_one_title.setText(PUtils.getDoctorTitle(getContext(),one.mTitle));
            tv_clinic_one_hospital_name.setText(one.mHospitalName);
            ImageLoader.getInstance().displayImage(one.mUser.mPhotoUrl, iv_clinic_one, LibUtils.getCircleDisplayOptions(R.drawable.default_avatar_doctor));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clearUnreadCount(EventMessageClear arg) {
        tv_right.setText("");
        tv_right.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMsgUnreadCount(MessageApi.Message message) {
        if (!BaseApplication.instance.hasUserToken()) {
            tv_right.setVisibility(View.GONE);
            return;
        }
        MessageApi.getMsgUnreadCount event = new MessageApi.getMsgUnreadCount(new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(getActivity(), msg);
            }

            @Override
            public void onSuccess(String count) {
                if (count.equals("0")) {
                    tv_right.setVisibility(View.GONE);
                } else {
                    if (count.length() > 2) {
                        tv_right.setText("99+");
                    } else {
                        tv_right.setText(count);
                    }
                    tv_right.setVisibility(View.VISIBLE);
                }
            }
        });
        new HttpClient(getActivity(), event).start();
    }




    class HomePagerAdapter extends PagerAdapter {

        @Override
        public View instantiateItem(ViewGroup container, int position) {

            RateLayout layout = new RateLayout(container.getContext());
            layout.setRate(0.4537f);

            ImageView imageView = new ImageView(container.getContext());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(lp);
            if (position == 0) {
                imageView.setBackgroundResource(R.drawable.test_banner);
            } else if (position == 1) {
                imageView.setBackgroundResource(R.drawable.test_banner_2);
            } else {
                imageView.setBackgroundResource(R.drawable.test_banner_3);
            }
            layout.addView(imageView);

            layout.setLayoutParams(lp);
            container.addView(layout);
            return layout;
        }

        @Override
        public int getCount() {
            return 3;
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


    class HealthInfoAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public HealthInfoAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return healthInfoDataList.size();
        }

        @Override
        public HealthInformationBean getItem(int position) {
            return healthInfoDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HealthViewHolder holder;
            if (convertView != null) {
                holder = (HealthViewHolder) convertView.getTag();
            } else {
                convertView = inflater.inflate(R.layout.item_health_info, parent, false);
                holder = new HealthViewHolder(convertView);
                convertView.setTag(holder);
            }
            HealthInformationBean bean = getItem(position);
            holder.tv_article_title.setText(bean.title.trim());

            final String url = bean.url;

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), HealthInfoDetailActivity.class);
                    intent.putExtra(HealthInfoDetailActivity.KEY_HEALTH_INFO_URL, url);
                    startActivity(intent);
                }
            });

            if (bean.formatDate == null) {
                bean.formatDate = CommonUtils.convertTime(getContext(), bean.lastModifiedTime);
            }
            holder.tv_article_date.setText(bean.formatDate);
            holder.tv_read_quantity.setText(bean.readingQuantity + "阅读");

            if (TextUtils.isEmpty(bean.imageUrl)) {
                holder.iv_article.setImageDrawable(getResources().getDrawable(R.drawable.default_img));
            } else {
                holder.iv_article.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(bean.imageUrl, holder.iv_article, ImageUtils.getCacheOptions());
            }
            return convertView;
        }
    }

    static class HealthViewHolder {
        @BindView(R.id.tv_article_title)
        public TextView tv_article_title;
        @BindView(R.id.tv_article_date)
        public TextView tv_article_date;
        @BindView(R.id.tv_read_quantity)
        public TextView tv_read_quantity;
        @BindView(R.id.iv_article)
        public ImageView iv_article;


        public HealthViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
