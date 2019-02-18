package com.kmwlyy.registry.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.kmwlyy.core.base.BaseFragment;
import com.kmwlyy.registry.R;


/**
 * Created by Administrator on 2016-8-14.
 */
public class ContainerActivity extends FragmentActivity {

    public static final String TAG_FRAGMENT = "TAG_FRAGMENT";

    public static final int AREA = 0;
    public static final int DOCLIST = 1;
    public static final int SCHEDULE = 2;
    public static final int REGISTRY = 3;
    public static final int REGISTRYLIST = 4;
    public static final int DEPARTMENT = 5;//选择部门页面
    public static final int DOCTORLIST = 6;//选择医生页面

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_fragment_container);

        int tag = getIntent().getIntExtra(TAG_FRAGMENT,0);
        BaseFragment fragment = null;
        switch (tag){
            case AREA:
                fragment = new SelectAreaFragment();
                break;
//            case DOCLIST:
//                fragment = new SelectDoctorFragment();
//                break;
            case SCHEDULE:
                fragment = new SchedulelFragment();
                break;
            case REGISTRY:
                fragment = new RegistryFragment();
                break;
            case REGISTRYLIST:
                fragment = new RegistryListFragment();
                break;
            case DEPARTMENT:
                fragment = new SelectDepartmentFragment();
                break;
            case DOCTORLIST:
                fragment = new SelectDoctorListFragment();
                break;
        }
        if (fragment!=null){
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }
    }
}
