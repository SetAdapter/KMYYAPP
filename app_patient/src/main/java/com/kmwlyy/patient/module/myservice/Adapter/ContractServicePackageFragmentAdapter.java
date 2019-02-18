package com.kmwlyy.patient.module.myservice.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xcj on 2017/8/8.
 */

public class ContractServicePackageFragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> listFragment = new ArrayList<>();
    private String[] mTitles;
    private Context context;

    public ContractServicePackageFragmentAdapter(AppCompatActivity context, List<Fragment> fragments, String[] titles) {
        super(context.getSupportFragmentManager());
        this.context = context;
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
