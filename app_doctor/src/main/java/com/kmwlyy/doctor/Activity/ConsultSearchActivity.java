package com.kmwlyy.doctor.Activity;

import android.content.Context;
import android.content.Intent;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.Fragment.ElectRecipeFragment;
import com.kmwlyy.doctor.Fragment.QueryChatFragment;
import com.kmwlyy.doctor.Fragment.QueryVoiceFragment;
import com.kmwlyy.doctor.Fragment.SearchFragment;
import com.kmwlyy.doctor.R;
import com.networkbench.agent.impl.NBSAppAgent;

import org.greenrobot.eventbus.EventBus;

import cn.org.bjca.sdk.core.bean.ResultBean;
import cn.org.bjca.sdk.core.values.ConstantParams;

/**
 * Created by Winson on 2017/7/9.
 */

public class ConsultSearchActivity extends BaseActivity {

    public static final String TAG = ConsultSearchActivity.class.getSimpleName();

    static final String SEARCH_TYPE = "SEARCH_TYPE";
    public static final int TYPE_IM = 0;
    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_ELECT_RECIPE = 2;

    public static void startItSelf(Context context, int searchType) {
        Intent intent = new Intent(context, ConsultSearchActivity.class);
        intent.putExtra(SEARCH_TYPE, searchType);
        context.startActivity(intent);
    }

    EditText mSearchEdit;
    PopupWindow mSearchFlagPopupWindow;
    TextView mSearchFlagTV;
    SearchFragment mSearchFragment;
    int mSearchType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_search);

        findViewById(R.id.cancel).setOnClickListener(this);
        mSearchFlagTV = (TextView) findViewById(R.id.search_flag);
        mSearchFlagTV.setOnClickListener(this);
        mSearchEdit = (EditText) findViewById(R.id.edit_search);
        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mSearchFragment != null) {
                    mSearchFragment.updateSearchKey(s.toString());
                }
            }
        });

        mSearchType = getIntent().getIntExtra(SEARCH_TYPE, TYPE_IM);
        updateSearchType(mSearchType);
    }

    private void updateSearchType(int type) {
        mSearchType = type;
        switch (mSearchType) {
            case TYPE_IM:
                mSearchFlagTV.setText(R.string.string_online);
                NBSAppAgent.onEvent("诊室-搜索-选择图文咨询");
                updateSearchFragment(new QueryChatFragment());
                break;
            case TYPE_VIDEO:
                mSearchFlagTV.setText(R.string.string_video2);
                NBSAppAgent.onEvent("诊室-搜索-选择视频问诊");
                updateSearchFragment(new QueryVoiceFragment());
                break;
            case TYPE_ELECT_RECIPE:
                mSearchFlagTV.setText(R.string.string_elect_recipe);
                NBSAppAgent.onEvent("诊室-搜索-选择电子处方");
                updateSearchFragment(new ElectRecipeFragment());
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.cancel:
                onBackPressed();
                break;
            case R.id.search_flag:
                updateSearchFlag(v);
                break;
            case R.id.im:
                updateSearchType(TYPE_IM);
                break;
            case R.id.video:
                updateSearchType(TYPE_VIDEO);
                break;
            case R.id.elect_recipe:
                updateSearchType(TYPE_ELECT_RECIPE);
                break;
        }
    }

    private void updateSearchFragment(SearchFragment fragment) {
        if (mSearchFlagPopupWindow != null) {
            mSearchFlagPopupWindow.dismiss();
        }
        mSearchFragment = fragment;
        mSearchFragment.updateSearchMode(true);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.search_content, mSearchFragment)
                .commit();
    }

    private void updateSearchFlag(View v) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_consult_search, null);
        contentView.findViewById(R.id.im).setOnClickListener(this);
        contentView.findViewById(R.id.video).setOnClickListener(this);
        contentView.findViewById(R.id.elect_recipe).setOnClickListener(this);
        mSearchFlagPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mSearchFlagPopupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mSearchFlagPopupWindow.setBackgroundDrawable(dw);
        mSearchFlagPopupWindow.setOutsideTouchable(false);
        mSearchFlagPopupWindow.showAsDropDown(v);
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
            ToastUtils.showShort(mContext,"操作取消");
            return;
        }
        if (requestCode == ConstantParams.ACTIVITY_SIGN_DATA) {
            String result = data.getStringExtra(ConstantParams.KEY_SIGN_BACK);
            Gson gson = new Gson();
            ResultBean resultBean = gson.fromJson(result, ResultBean.class);
            if (resultBean != null && TextUtils.equals(resultBean.getStatus(), ConstantParams.SUCCESS)) {
                ToastUtils.showShort(mContext,"签名成功!");
                EventBus.getDefault().post(new ElectRecipeFragment.ShowSignWindow(false));
                EventBus.getDefault().post(new ElectRecipeFragment.SignOperation(ElectRecipeFragment.TYPE_REFRESH));
            } else {
                ToastUtils.showShort(mContext,"签名失败：" + resultBean.getMessage());
            }
        }
    }

}
