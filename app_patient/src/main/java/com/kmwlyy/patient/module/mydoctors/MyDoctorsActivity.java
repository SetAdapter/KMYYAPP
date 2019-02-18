package com.kmwlyy.patient.module.mydoctors;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.NewHttpClient;
import com.kmwlyy.patient.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljf on 2017/8/1.
 * 我的医生
 */

public class MyDoctorsActivity extends AppCompatActivity {

    TextView title;
    ListView orderListView;
    ListView watchListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydoctors);
        initView();
        initData();
        getMyDoctorInfo("1", "5");
    }

    private void getMyDoctorInfo(String currentPage, String pageSize) {
        HttpMyDoctor event = new HttpMyDoctor(currentPage, pageSize, new HttpListener<MyDoctorBean>() {
            @Override
            public void onError(int code, String msg) {

            }

            @Override
            public void onSuccess(MyDoctorBean myDoctorBean) {
                if (null != myDoctorBean) {
                    List<MyDoctorBean.ResultDataBean> resultData = myDoctorBean.getResultData();
                    orderListView.setAdapter(new DoctorAdapter(MyDoctorsActivity.this, resultData));

//                    List<MyDoctorBean.ResultDataBean.DataBean> data = myDoctorBean.getResultData().get(0).getData();
//                    watchListView.setAdapter(new DoctorAttentionAdapter(MyDoctorsActivity.this, data));
                }
            }
        });
        NewHttpClient httpClient = new NewHttpClient(MyDoctorsActivity.this, event);
        httpClient.start();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.tv_title_center);
        title.setText("我的医生");
        findViewById(R.id.iv_tools_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        orderListView = (ListView) findViewById(R.id.order_list);
        watchListView = (ListView) findViewById(R.id.watch_list);

    }

    private void initData() {
        List list = new ArrayList();
        for (int i = 0; i < 8; i++) {
            list.add("");
        }

//        watchListView.setAdapter(new DoctorAdapter(this, list));
    }

}
