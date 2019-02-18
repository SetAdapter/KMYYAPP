package com.kmwlyy.doctor.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.doctor.R;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    public TextView mTitleText; // 标题栏
    public TextView mLeftBtn; // 标题左边按钮
    public Button mRightBtn; // 标题右边按钮
    public HttpClient httpClient;

    public Context mContext;
    public ProgressDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mContext = this;
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onResume() {
        super.onResume();
        //隐藏键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
