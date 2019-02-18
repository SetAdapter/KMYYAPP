package com.kmwlyy.patient.module.signagreenment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kmwlyy.patient.R;


/**
 * Created by Administrator on 2017/8/8.
 */

public class SignAgreenmentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_agreenment);

        TextView tv_title_center = (TextView) findViewById(R.id.tv_title_center);
        tv_title_center.setText("签约须知");
        Button iv_tools_left = (Button) findViewById(R.id.iv_tools_left);
        iv_tools_left.setText("取消");
        iv_tools_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
