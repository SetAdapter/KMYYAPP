package com.kmwlyy.core.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

import static com.kmwlyy.core.base.BaseFragment.doBack;

/**
 * Created by Administrator on 2016-8-6.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected ProgressDialog mLoadingDialog;
    protected Context mContext;

    //@BindView(R2.id.tv_left)
    public TextView mLeftText;
    //@BindView(R2.id.iv_left)
    public ImageView mLeftImage;
    //@BindView(R2.id.tv_center)
    public TextView mCenterText;
    //@BindView(R2.id.iv_center)
    public ImageView mCenterImage;
    //@BindView(R2.id.tv_right)
    public TextView mRightText;
    //@BindView(R2.id.iv_right)
    public ImageView mRightImage;

    /*******************************************************/

    protected abstract int getLayoutId();

    protected abstract void afterBindView();

    protected boolean isRegisterEventBus() {
        return false;
    }

    /*******************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(getLayoutId());
        if (isRegisterEventBus()) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        bindView();
        mContext = this;
        afterBindView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRegisterEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_left || i == R.id.iv_left) {
            onLeftClick(view);
        } else if (i == R.id.tv_center || i == R.id.iv_center) {
            onCenterClick(view);
        } else if (i == R.id.tv_right || i == R.id.iv_right) {
            onRightClick(view);
        }
    }

    /*******************************************************/
    protected void setTitle(String title) {
        if (mCenterText != null) {
            mCenterText.setText(title);
        }
    }

    private void bindView() {
        try {
            mLeftText = (TextView) findViewById(R.id.tv_left);
            mRightText = (TextView) findViewById(R.id.tv_right);
            mLeftImage = (ImageView) findViewById(R.id.iv_left);
            mRightImage = (ImageView) findViewById(R.id.iv_right);
            mCenterText = (TextView) findViewById(R.id.tv_center);
            mCenterImage = (ImageView) findViewById(R.id.iv_center);
            mLeftText.setOnClickListener(this);
            mRightText.setOnClickListener(this);
            mLeftImage.setOnClickListener(this);
            mRightImage.setOnClickListener(this);
            mCenterText.setOnClickListener(this);
            mCenterImage.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@OnClick({R2.id.tv_left, R2.id.iv_left})
    public void onLeftClick(View view) {
        doBack();
    }

    //@OnClick({R2.id.tv_center, R2.id.iv_center})
    public void onCenterClick(View view) {

    }

    //@OnClick({R2.id.tv_right, R2.id.iv_right})
    public void onRightClick(View view) {

    }

    /*******************************************************/
    public void hideDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    public void showDialog(String msg) {
        if (mLoadingDialog != null) {
            mLoadingDialog.setMessage(msg);
        } else {
            mLoadingDialog = ProgressDialog.show(mContext, "", msg);
        }
    }

    /*******************************************************/
    public void open(Class activity) {
        Intent intent = new Intent(mContext, activity);
        startActivity(intent);
    }
}
