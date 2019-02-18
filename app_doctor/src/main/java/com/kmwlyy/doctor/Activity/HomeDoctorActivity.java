package com.kmwlyy.doctor.Activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.R;

/**
 * Created by fst on 2017/6/19.
 *
 */

public class HomeDoctorActivity extends BaseActivity {


    private Button service_active;
    private TextView tv_left;
    private TextView tv_center;
    private ImageView iv_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_doctor);

        initView();
        initListener();
    }



    private void initView() {
        service_active = (Button) findViewById(R.id.btn_service_activation);
        tv_left = (TextView) findViewById(R.id.tv_left);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        tv_center = (TextView) findViewById(R.id.tv_center);

        tv_center.setText(R.string.home_doctor);
        tv_left.setVisibility(View.GONE);
        iv_left.setVisibility(View.VISIBLE);

    }

    private void initListener() {
        service_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("IsOpened",false);
                intent.setClass(mContext,HomeDoctorSettingActivity.class);
                startActivity(intent);
                finish();
            }
        });

    /*    tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View arg0)  {
        super.onClick(arg0);
    }
}
