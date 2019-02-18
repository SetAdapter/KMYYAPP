package com.kmwlyy.core.base;

import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.R;
import com.kmwlyy.core.R2;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016-8-6.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    @BindView(R2.id.tv_left)
    public TextView mLeftText;
    @BindView(R2.id.iv_left)
    public ImageView mLeftImage;
    @BindView(R2.id.tv_center)
    public TextView mCenterText;
    @BindView(R2.id.iv_center)
    public ImageView mCenterImage;
    @BindView(R2.id.tv_right)
    public TextView mRightText;
    @BindView(R2.id.iv_right)
    public ImageView mRightImage;

    protected Context mContext;
    protected ProgressDialog mLoadingDialog;

    /*******************************************************/
    protected abstract int getLayoutId();
    protected abstract void afterBindView();
    protected boolean isRegisterEventBus(){
        return false;
    }

    /*******************************************************/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isRegisterEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRegisterEventBus()){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        bindView(view);//暂换
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        afterBindView();
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
    private void bindView(View view) {
        try {
            mLeftText = (TextView) view.findViewById(R.id.tv_left);
            mRightText = (TextView) view.findViewById(R.id.tv_right);
            mLeftImage = (ImageView) view.findViewById(R.id.iv_left);
            mRightImage = (ImageView) view.findViewById(R.id.iv_right);
            mCenterText = (TextView) view.findViewById(R.id.tv_center);
            mCenterImage = (ImageView) view.findViewById(R.id.iv_center);
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

    @OnClick({R2.id.tv_left, R2.id.iv_left})
    public void onLeftClick(View view) {
        doBack();
    }

    @OnClick({R2.id.tv_center, R2.id.iv_center})
    public void onCenterClick(View view) {

    }

    @OnClick({R2.id.tv_right, R2.id.iv_right})
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
        if (getUserVisibleHint()) {//是否可见
            if (mLoadingDialog != null) {
                mLoadingDialog.setMessage(msg);
            } else {
                mLoadingDialog = ProgressDialog.show(mContext, "", msg);
            }
        }
    }

    public static void doBack() {//返回实现
        new Thread(new Runnable() {
            @Override
            public void run() {//这个方法是不能写在你的主线程里面的，所以你要自己开个线程用来执行
                Instrumentation inst = new Instrumentation();
                try {
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
