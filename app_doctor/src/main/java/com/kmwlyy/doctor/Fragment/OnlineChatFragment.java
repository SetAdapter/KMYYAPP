package com.kmwlyy.doctor.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.doctor.Activity.ConsultSearchActivity;
import com.kmwlyy.doctor.MyApplication;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.Constant;
import com.kmwlyy.login.MessageEvent;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.networkbench.agent.impl.NBSAppAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import doctor.kmwlyy.com.recipe.DrugListActivity;

public class OnlineChatFragment extends Fragment implements OnClickListener {
    private List<Fragment> fragmentList;
    private FragmentManager manager;
    private Adapter adapter;
    private int page = 0;
    private QueryChatFragment chatFragment;
    private QueryVoiceFragment videoFragment;
    private ElectRecipeFragment electRecipeFragment;

    @ViewInject(R.id.radiogroup)
    private RadioGroup group;
    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_online, null);
        ViewUtils.inject(this, view);
        ((TextView) view.findViewById(R.id.tv_center)).setText(getResources().getString(R.string.string_tab_room));
        view.findViewById(R.id.tv_left).setVisibility(View.GONE);

        ImageView ivRight = (ImageView) view.findViewById(R.id.iv_right);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.mipmap.icon_common_diag);
        ivRight.setOnClickListener(this);

        ImageView ivRight1 = (ImageView) view.findViewById(R.id.iv_right_second);
        ivRight1.setVisibility(View.VISIBLE);
        ivRight1.setImageResource(R.mipmap.icon_search);
        ivRight1.setOnClickListener(this);

        init();

        //要用到开处方 moudle,准备个广播。方便回调
        //注册广播
        registerBoradcastReceiver();

        return view;
    }

    private void init() {
        fragmentList = new ArrayList<Fragment>();
        chatFragment = new QueryChatFragment();
        videoFragment = new QueryVoiceFragment();
        electRecipeFragment = new ElectRecipeFragment();

        //图文咨询列表 参数
        Bundle bundle1 = new Bundle();
        bundle1.putString("OPDType", Constant.OPDTYPE_CHAT);
        videoFragment.setArguments(bundle1);

        //视频咨询列表 参数
        Bundle bundle3 = new Bundle();
        bundle3.putString("OPDType", Constant.OPDTYPE_VIDEO);
        videoFragment.setArguments(bundle3);

        fragmentList.add(chatFragment);
        fragmentList.add(videoFragment);
        fragmentList.add(electRecipeFragment);

        manager = getChildFragmentManager();
        adapter = new Adapter(manager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);

        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        group.check(R.id.btn_chat);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == R.id.btn_chat) {
                    page = 0;
                } else if (checkedId == R.id.btn_video) {
                    page = 1;
                } else if (checkedId == R.id.btn_elect_recipe) {
                    page = 2;
                }
                viewPager.setCurrentItem(page);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtils.i("OnlineChatFragment", "OnlineChatFragment" + "  isVisibleToUser:  " + isVisibleToUser);
        if (isVisibleToUser && page == 2) {
            electRecipeFragment.setUserVisibleHint(isVisibleToUser);
        } else {
            EventBus.getDefault().post(new ElectRecipeFragment.ShowSignWindow(false));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_right:
                NBSAppAgent.onEvent("诊室-打开常用处方");
                getActivity().startActivity(new Intent(getActivity(), DrugListActivity.class));
                break;
            case R.id.iv_right_second:
                NBSAppAgent.onEvent("诊室-进入搜索页面");
                ConsultSearchActivity.startItSelf(getActivity(), viewPager.getCurrentItem());
//                Intent intent = new Intent(getActivity(), ConsultSearchActivity.class);
//                getActivity().startActivity(intent);
                break;
        }
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

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            page = arg0;
            switch (arg0) {
                case 0:
                    group.check(R.id.btn_chat);
                    break;
                case 1:
                    group.check(R.id.btn_video);
                    break;
                case 2:
                    group.check(R.id.btn_elect_recipe);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constant.TOKEN_EXPIRE);
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constant.TOKEN_EXPIRE)) {
                BaseApplication.getInstance().updateAppToken();
            }
        }

    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    public void setCurrentPage(int position) {
        if (null != viewPager) {
            viewPager.setCurrentItem(position);
        }
    }
}
