package com.kmwlyy.patient.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.winson.ui.widget.RateLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Winson on 2016/10/11.
 */
public class AboutUsActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.soft_introduce)
    TextView mSoftIntroduce;
    @BindView(R.id.version_info)
    TextView mVersionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        ButterKnife.bind(this);

        tv_center.setText(R.string.about_us);

        mSoftIntroduce.setOnClickListener(this);
        mVersionInfo.setText("v" + PUtils.getVersion(this));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.soft_introduce:
                CommonUtils.startActivity(this, SoftIntroduceActivity.class);
                break;
        }

    }

}
