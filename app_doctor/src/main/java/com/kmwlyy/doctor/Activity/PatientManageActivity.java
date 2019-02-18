package com.kmwlyy.doctor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.doctor.Fragment.PatientGroupFragment;
import com.kmwlyy.doctor.Fragment.PatientListFragment;
import com.kmwlyy.doctor.R;

/**
 * Created by Winson on 2017/7/4.
 */
public class PatientManageActivity extends BaseActivity {

    public static final String TAG = PatientManageActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_manage);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.divide_group).setOnClickListener(this);
        findViewById(R.id.search).setOnClickListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.group_content, new PatientGroupFragment()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.patient_content, new PatientListFragment()).commit();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.divide_group:
                startActivity(new Intent(this, PatientGroupListActivity.class));
                break;
            case R.id.search:
                startActivity(new Intent(this, PatientSearchActivity.class));
                break;
        }
    }

}
