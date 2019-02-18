package com.kmwlyy.patient.weight;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment 基类
 * Created by fangs on 2017/4/26.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected AppCompatActivity mContext;
    protected View mRootView;
    protected Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mRootView) {
            mRootView = inflater.inflate(getContentLayout(), container, false);
            unbinder = ButterKnife.bind(this, mRootView);

            baseInitView();
            baseInit();
        } else {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (null != parent) {
                parent.removeView(mRootView);
            }
        }
        return mRootView;
    }

    @Override
    public void onClick(View view) {
    }

    protected void baseInitView() {
    }

    protected void baseInit() {
    }

    protected abstract int getContentLayout();

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
