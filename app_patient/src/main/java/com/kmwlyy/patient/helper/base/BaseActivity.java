package com.kmwlyy.patient.helper.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.kmwlyy.login.EventLoginApi;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.utils.EventApi;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Winson on 2016/8/6.
 */
public class BaseActivity extends AppCompatActivity {

    private boolean donotSetClickBack = false;//不要设置左上角按钮动作
    protected boolean mSkipFinsih;
    private View.OnClickListener mBackPressListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    public ProgressDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        View nav = findViewById(R.id.tv_left);
        if (nav != null && !donotSetClickBack) {
            nav.setOnClickListener(mBackPressListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void setBarTitle(String titleStr) {
        TextView title = (TextView) findViewById(R.id.tv_center);
        if (title != null) {
            title.setText(titleStr);
        }
    }

    protected void setBarTitle(int titleRes) {
        TextView title = (TextView) findViewById(R.id.tv_center);
        if (title != null) {
            title.setText(titleRes);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogout(EventApi.Logout logout) {
        if (!mSkipFinsih) {
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(EventLoginApi.Login login) {

    }

    /* 设置是否允许左上角按键点击返回 */
    public void setDonotClickBack(boolean donotSetClickBack) {
        this.donotSetClickBack = donotSetClickBack;
    }

    /**
     * 显示 Dialog
     */
    public void showLoadDialog(int id) {
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setMessage(getResources().getString(id));
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.show();
    }

    /**
     * 取消 Dialog
     */
    public void dismissLoadDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

}
