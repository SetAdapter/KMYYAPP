package com.kmwlyy.patient.helper.base;

import android.support.v4.app.Fragment;

/**
 * Created by Winson on 2016/8/6.
 */
public class BaseFragment extends Fragment {

    private static final int NONE = 0x0;
    private static final int VISIBLE = 0x1;
    private static final int RESUME = 0x10;
    private static final int SHOW = 0x11;

    private int mShowState = 0;
    private boolean mShow;

    public void firstShow() {

    }

    public void clearShowState() {
        mShowState &= 0x10;
        mShow = false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mShowState |= VISIBLE;
            if (!mShow && mShowState == SHOW) {
                mShow = true;
                firstShow();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mShowState |= RESUME;
        if (!mShow && mShowState == SHOW) {
            mShow = true;
            firstShow();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mShowState = NONE;
        mShow = false;
    }
}
