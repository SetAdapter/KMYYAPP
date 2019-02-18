package com.kmwlyy.patient.main;

import android.os.Bundle;

import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;

/**
 * Created by Winson on 2016/10/11.
 */
public class SoftIntroduceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soft_introduce);

        setBarTitle(R.string.soft_introduce);
    }

}
