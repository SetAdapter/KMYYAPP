package com.kmwlyy.address.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.kmwlyy.address.R;
import com.kmwlyy.core.base.BaseFragment;


/**
 * Created by Administrator on 2016-8-14.
 */
public class ContainerActivity extends FragmentActivity {

    public static final String TAG_FRAGMENT = "TAG_FRAGMENT";

    public static final int ADDRESS = 0;
    public static final int ADDRESSLIST = 1;
    public static final int ADDRESSSELECTE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_fragment_container);

        int tag = getIntent().getIntExtra(TAG_FRAGMENT,0);
        BaseFragment fragment = null;
        switch (tag){
            case ADDRESS:
                fragment = new AddressFragment();
                break;
            case ADDRESSLIST:
                fragment = new AddressListFragment();
                break;
            case ADDRESSSELECTE:
                fragment = new AddressSelecteFragment();
        }
        if (fragment!=null){
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }
    }
}
