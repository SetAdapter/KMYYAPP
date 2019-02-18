package com.kmwlyy.patient.module.myservice.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xcj on 2017/8/10.
 */

public class ContractServicePackageFragmentChildAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> listFragment = new ArrayList<>();
    private String[] mTitles;

    public ContractServicePackageFragmentChildAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles) {
        super(fm);
        this.listFragment = fragments;
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
