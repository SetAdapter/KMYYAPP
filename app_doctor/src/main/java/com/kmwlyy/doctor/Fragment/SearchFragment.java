package com.kmwlyy.doctor.Fragment;

import android.support.v4.app.Fragment;

/**
 * Created by Winson on 2017/7/9.
 */

public class SearchFragment extends Fragment {

    protected boolean mUseSearchMode;
    protected String mSearchKey;

    public void updateSearchMode(boolean useSearchMode) {
        mUseSearchMode = useSearchMode;
    }

    public void updateSearchKey(String searchKey) {
        mSearchKey = searchKey;
    }

}
